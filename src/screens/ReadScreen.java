package screens;

import lib.accessibility.components.AScreen;
import lib.accessibility.components.ALabel;
import lib.utils.StringUtils;
import lib.utils.Util;
import lib.utils.Utils;
import screens.FolderScreen;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Folder;
import lib.utils.StringUtils.StringConvertor;
import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.blackberry.api.mail.FolderNotFoundException;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ReadScreen extends AScreen {
	
	private final Message message;
	private VerticalFieldManager manager;
	private Font messageFont;
	private RichTextField textField;
	private LabelField field;
	
	public ReadScreen(String name, Message message) {
		super(name);
		this.message = message;
		messageFont = getFont().derive(Font.BOLD,40);
		add(manager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR | Manager.NO_HORIZONTAL_SCROLL));
		manager.add(field = new LabelField("S: "+message.getSubject(),Field.FOCUSABLE | Field.USE_ALL_WIDTH | Field.VISUAL_STATE_DISABLED));
		field.setFont(messageFont);
		StringConvertor convertor = new StringConvertor(){
			public String getChunk(Object o) {
				if(o instanceof Address)
					return (((Address) o).getName() != null) ? ((Address) o).getName() : ((Address) o).getAddr();
				return null;
			}
		};
		
		int[] recipientTypes = new int[]{
			Message.RecipientType.FROM,
			Message.RecipientType.TO,
			Message.RecipientType.CC,
			Message.RecipientType.BCC
		};
		try {
		for(int i=0;i<4;i++){
			Address[] addresses;
			
				addresses = message.getRecipients(recipientTypes[i]);
			
			if(addresses.length > 0){
				LabelField field = new ALabel(getLabelPrefix(recipientTypes[i])+StringUtils.join("; ", addresses, convertor),Field.USE_ALL_WIDTH | Field.FOCUSABLE | Field.VISUAL_STATE_DISABLED);
				field.setFont(messageFont);
				manager.add(field);
			}
		}
		} catch (MessagingException e) {
			System.out.println("Messaging error.");
			e.printStackTrace();
		}
		manager.add(new SeparatorField(Field.USE_ALL_WIDTH));
		textField = new RichTextField(message.getBodyText(),Field.USE_ALL_WIDTH);
		textField.setEditable(false);
		textField.setFont(messageFont);
		manager.add(textField);
		
		FocusChangeListener fieldListener = new FocusChangeListener(){
			public void focusChanged(Field field, int arg1) {
				Util.speak(field.toString());
			}
		};
		field.setFocusListener(fieldListener);
	}
	
	/**
	 * Makes menu accessible with middle button
	 */
	protected void makeMenu(Menu menu, int instance) {
		menu.deleteAll();
		ContextMenu cm = ContextMenu.getInstance();
		cm.addItem(new MenuItem("Read",0,0){
			public String getAccessibleName() {
				return "Voiceread";
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
				Util.speak("Reading subject: ");
				if (message.getSubject()!=null)Util.speak(message.getSubject());
				else Util.speak("No subject.");
				Util.speak("Reading body: ");
				if (message.getBodyText()!=null && message.getBodyText()!="")
				Util.speak(message.getBodyText());
				else Util.speak("No body.");
			}
		});
		if(message.getFolder().getType() == Folder.INBOX){
		cm.addItem(MenuItem.separator(0));
		cm.addItem(new MenuItem("Reply",0,0){
			public String getAccessibleName() {
				return "Reply";
			}
			
			public void run() {
				try {
					Folder[] draftFolders = Session.getDefaultInstance().getStore().list(Folder.DRAFT);
					final Message newMessage = new Message(draftFolders[0]);
					newMessage.addRecipient(Message.RecipientType.TO, message.getFrom());
					newMessage.setContent("");
					UiApplication.getUiApplication().invokeLater(new Runnable(){
						public void run() {
								UiApplication.getUiApplication().pushScreen(new ComposeScreen("Reply to message screen",newMessage));
								close();
						}
					});
				} catch (FolderNotFoundException e) {
					e.printStackTrace();
					Utils.notifyUser("Draft folder does not exist");
				} catch (MessagingException e) {
					e.printStackTrace();
					Utils.notifyUser("Could initialize reply message");
				}
			}
		});
		cm.addItem(new MenuItem("Forward",0,0){
			public String getAccessibleName() {
				return "Forward";
			}
			
			public void run() {
				try {
					Folder[] draftFolders = Session.getDefaultInstance().getStore().list(Folder.DRAFT);
					final Message newMessage = new Message(draftFolders[0]);
					newMessage.addRecipient(Message.RecipientType.TO, message.getFrom());
					newMessage.setSubject("Fw: "+message.getSubject());
					newMessage.setContent("-- Original message --\n\n"+message.getBodyText());
					UiApplication.getUiApplication().invokeLater(new Runnable(){
						public void run() {
								UiApplication.getUiApplication().pushScreen(new ComposeScreen("Forward message screen",newMessage));
								close();
						}
					});
				} catch (FolderNotFoundException e) {
					e.printStackTrace();
					Utils.notifyUser("Draft folder does not exist");
				} catch (MessagingException e) {
					e.printStackTrace();
					Utils.notifyUser("Could initialize forwarded message");
				}
			}
		});
		}
		cm.addItem(new MenuItem("Delete",0,0){
			public String getAccessibleName() {
				return "Delete";
			}
			
			public void run() {
				UiApplication.getUiApplication().invokeLater(new Runnable(){
					public void run() {
					if(message.getFolder().deleteMessage(message)){
						Utils.notifyUser("Message deleted");
						UiApplication.getUiApplication().popScreen(getScreenBelow());
						close();
					}else{
						Utils.notifyUser("Failed to delete message");
					}
					}
				});
			}
			
		});
		
		cm.addItem(MenuItem.separator(0));
		menu.add(cm);
		menu.add(MenuItem.getPrefab(MenuItem.CLOSE));
	}

	/**
	 * Gets help content, which is accessible by convenience key
	 */
	public String getAccessibleHelp() {
		return "This is the screen, where you can read the message, to read it by built-in reader access the menu activated by middle button and select Read";
	}
	
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
	
	public boolean onClose() {
		if((message.getFlags() & Message.Flag.OPENED) == 0){
			message.setFlags(message.getFlags() | Message.Flag.OPENED);
			message.getFolder().deleteMessage(message);
			message.getFolder().appendMessage(message);
		}
		return super.onClose();
	}
}
