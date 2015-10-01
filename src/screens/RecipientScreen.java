package screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import lib.utils.Util;
import lib.utils.Utils;
import lib.utils.StringUtils;
import lib.utils.StringUtils.StringConvertor;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.UiApplication;
import lib.accessibility.components.AScreen;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Transport;
import net.rim.blackberry.api.mail.MessagingException;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import net.rim.blackberry.api.pdap.BlackBerryContactList;
import net.rim.blackberry.api.mail.AddressException;



public class RecipientScreen extends AScreen {
	
	private Message message;
	private LabelField toField;
	private LabelField ccField;
	private LabelField bccField;
	private VerticalFieldManager manager;
	private Font recipientFont;
	
	public RecipientScreen(String name, Message message){
		super(name);
		this.message = message;
		recipientFont = getFont().derive(Font.BOLD,40);
		
		add(manager = new VerticalFieldManager());
		manager.add(toField = new LabelField("",Field.USE_ALL_WIDTH|Field.FOCUSABLE));
		toField.setFont(recipientFont);
		manager.add(new SeparatorField(Field.USE_ALL_WIDTH));
		
		manager.add(ccField = new LabelField("",Field.USE_ALL_WIDTH|Field.FOCUSABLE));
		ccField.setFont(recipientFont);
		manager.add(new SeparatorField(Field.USE_ALL_WIDTH));
		
		manager.add(bccField = new LabelField("",Field.USE_ALL_WIDTH|Field.FOCUSABLE));
		bccField.setFont(recipientFont);
		manager.add(new SeparatorField(Field.USE_ALL_WIDTH));
		
		updateAddressField(toField,Message.RecipientType.TO);
		updateAddressField(ccField,Message.RecipientType.CC);
		updateAddressField(bccField,Message.RecipientType.BCC);
		
	}
	
	protected void makeMenu(Menu menu, int instance) {
		menu.deleteAll();
		{
		ContextMenu cm = ContextMenu.getInstance();
		cm.setTarget(getScreen());
		cm.addItem(new MenuItem("Send",0,0){
			public void run() {
				try {
					if(message.getRecipients(Message.RecipientType.TO).length > 0
					|| message.getRecipients(Message.RecipientType.CC).length > 0
					|| message.getRecipients(Message.RecipientType.BCC).length > 0){
						Transport.send(message);
						if (message.getFolder()!=null)message.getFolder().deleteMessage(message);
						Session.getDefaultInstance().getStore().list(Folder.SENT)[0].appendMessage(message);
						Utils.notifyUser("Message sent successfully");
						UiApplication.getUiApplication().popScreen(getScreenBelow());
						close();
					}else{
						Utils.notifyUser("No recipients");
					}
				} catch (MessagingException e) {
					e.printStackTrace();
					Utils.notifyUser("Failed to send message");
				}
			}
		});
		if(toField.isFocus()){
			cm.addItem(new MenuItem("Add recipient: TO",0,0){
				public void run() {
					addRecipient(toField, Message.RecipientType.TO);
				}
			});
			cm.addItem(new MenuItem("Clear field",0,0){
				public void run() {
					clearRecipients(toField, Message.RecipientType.TO);
				}
			});
		}else if(ccField.isFocus()){
			cm.addItem(new MenuItem("Add recipient: CC",0,0){
				public void run() {
					addRecipient(ccField, Message.RecipientType.CC);
				}
			});
			cm.addItem(new MenuItem("Clear field",0,0){
				public void run() {
					clearRecipients(ccField, Message.RecipientType.CC);
				}
			});
		}else if(bccField.isFocus()){
			cm.addItem(new MenuItem("Add recipient: BCC",0,0){
				public void run() {
					addRecipient(bccField, Message.RecipientType.BCC);
				}
			});
			cm.addItem(new MenuItem("Clear field",0,0){
				public void run() {
					clearRecipients(bccField, Message.RecipientType.BCC);
				}
			});
		}
		
		cm.addItem(MenuItem.separator(0));
		
		cm.addItem(new MenuItem("Proofread",0,0){
			
			public String getAccessibleName() {
				return "Proofread";
			}

			public void run() {
				Util.speak("Reading recipients: ");
				try {
					Util.speak("Direct recipients: ");
					for (int i = 0; i < message.getRecipients(Message.RecipientType.TO).length; i++)
						Util.speak(message.getRecipients(Message.RecipientType.TO)[i].getName()+" ");
					Util.speak("Copy recipients: ");
					for (int i = 0; i < message.getRecipients(Message.RecipientType.CC).length; i++)
						Util.speak(message.getRecipients(Message.RecipientType.CC)[i].getName()+" ");
					Util.speak("Hidden copy recipients: ");
					for (int i = 0; i < message.getRecipients(Message.RecipientType.BCC).length; i++)
						Util.speak(message.getRecipients(Message.RecipientType.BCC)[i].getName()+" ");
				} catch (MessagingException e) {
					System.out.println("Unable to read recipients.");
					e.printStackTrace();
				}
				
			}
			
		});
		
		cm.addItem(MenuItem.separator(0));
		menu.add(cm);
		}
		menu.add(MenuItem.getPrefab(MenuItem.CLOSE));
	}
	
	private StringConvertor addressToStringConvertor = new StringConvertor(){
		public String getChunk(Object o) {
			if(o instanceof Address)
				return (((Address) o).getName() != null) ? ((Address) o).getName() : ((Address) o).getAddr();
			return null;
		}
	};
	
	private void updateAddressField(final LabelField field, final int type) {
		if(UiApplication.isEventDispatchThread()){
			try {
				field.setText(getLabelPrefix(type)+StringUtils.join("; ", message.getRecipients(type), addressToStringConvertor));
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}else{
			UiApplication.getUiApplication().invokeLater(new Runnable(){
				public void run() {
					try {
						field.setText(getLabelPrefix(type)+StringUtils.join("; ", message.getRecipients(type), addressToStringConvertor));
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	
	/**
	 * Sets prefix for different recipient types
	 * @param messageType type of message
	 * @return
	 */
	private static String getLabelPrefix(int messageType) {
		switch(messageType){
			case Message.RecipientType.TO: 		return "To: ";
			case Message.RecipientType.CC: 		return "Cc: ";
			case Message.RecipientType.BCC: 	return "BCc: ";
			case Message.RecipientType.FROM:	return "From: ";
			default:
				return "";
		}
	}
	
	/**
	 * Adds recipient to message
	 * @param field which field is to be used
	 * @param type type of recipient
	 */
	private void addRecipient(LabelField field,int type){
		try {
			BlackBerryContactList contactList = (BlackBerryContactList) PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
			final PIMItem contact = contactList.choose(); //contactList.choose(null,BlackBerryContactList.AddressTypes.EMAIL,false);
			if(contact != null){
				String addr = contact.getString(Contact.EMAIL, 0);
				String[] name = contact.getStringArray(Contact.NAME, 0);
		        message.addRecipient(type, new Address(addr,name[Contact.NAME_GIVEN]+" "+name[Contact.NAME_FAMILY]));
				updateAddressField(field, type);
			}
		} catch (IndexOutOfBoundsException e){
			Utils.notifyUser("Selected contact does not have an e-mail address");
		} catch (PIMException e) {
			e.printStackTrace();
			Utils.notifyUser("Failed to open contact list");
		} catch (AddressException e) {
			e.printStackTrace();
			Utils.notifyUser("E-mail address invalid");
		} catch (MessagingException e) {
			e.printStackTrace();
			Utils.notifyUser("Unknown failure");
		}
	}
	
	/**
	 * Clears message recipients
	 * @param field field to be used
	 * @param type type of recipient
	 */
	private void clearRecipients(LabelField field,int type){
		try {
			message.removeAllRecipients(type);
			updateAddressField(field, type);
		} catch (MessagingException e) {
			e.printStackTrace();
			Utils.notifyUser("Unknown failure");
		}
	}
	
	/**
	 * When screen is closed
	 */
	public boolean onClose() {
		Folder folder = message.getFolder();
		if (folder!=null) {
			folder.deleteMessage(message);
			folder.appendMessage(message);
		}
		UiApplication.getUiApplication().popScreen(getScreen());
		return true;
	}
	
	/**
	 * Help content accessible with convenience key
	 */
	public String getAccessibleHelp() {
		return "You are in the message recepient screen, you can insert one recepient in each of these 3 cathegories, "+
		"which are main recipient, copy recipient and hidden coppy recipient. If you are done select send from the menu.";
	}
	
}
