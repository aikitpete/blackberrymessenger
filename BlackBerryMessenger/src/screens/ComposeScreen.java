package screens;


import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import lib.utils.Util;
import lib.utils.Utils;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.UiApplication;
import lib.accessibility.components.AScreen;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.MessagingException;



public class ComposeScreen extends AScreen {
	
	private Message message;
	private TextField subject;
	private RichTextField body;
	private VerticalFieldManager manager;
	private Font messageFont;
	private Font bodyFont;

	/**
	 * Constructor of screen for composing messages
	 * @param name name of screen
	 * @param message message being composed
	 */
	public ComposeScreen(String name, Message message){
		super(name);
		this.message = message;
		messageFont = getFont().derive(Font.BOLD,40);
		bodyFont = getFont().derive(Font.BOLD,60);
		
		add(manager = new VerticalFieldManager());
		manager.add(subject = new TextField("Sub: ",message.getSubject(),64,Field.USE_ALL_WIDTH|Field.VISUAL_STATE_DISABLED));
		subject.setFont(messageFont);
		manager.add(new SeparatorField(Field.USE_ALL_WIDTH));
		body = new RichTextField(message.getBodyText(),Field.USE_ALL_WIDTH);
		body.setEditable(true);
		body.setFont(bodyFont);
		manager.add(body);
		
		
		
	}
	
	/**
	 * Sets up menu contents
	 */
	protected void makeMenu(Menu menu, int instance) {
		menu.deleteAll();
		ContextMenu cm = ContextMenu.getInstance();
		cm.addItem(new MenuItem("Continue",0,0){

			public String getAccessibleName() {
				return "Continue";
			}
			
			public void run() {
				Util.speak("Selected "+getAccessibleName());
				updateMessage();
				
				UiApplication.getUiApplication().pushScreen(new RecipientScreen("Select recipients", message));
			}
			
		});
		
		cm.addItem(new MenuItem("Proofread",0,0){

			public String getAccessibleName() {
				return "Proofread";
			}
			
			public void run() {
				updateMessage();
				Util.speak("Proofreading: "+"Message subject: "+message.getSubject()+" Message body:"+message.getBodyText());
				
			}
			
		});
		cm.addItem(MenuItem.separator(0));
		menu.add(cm);
		menu.add(MenuItem.getPrefab(MenuItem.CLOSE));
	}
	
	/**
	 * Updates data in message
	 */
	public void updateMessage() {
		try {
			message.setContent(body.getText());
			message.setSubject(subject.getText());
		} catch (MessagingException e) {
			Utils.notifyUser("Unable to update message content.");
			e.printStackTrace();
		}
	}
	
	/**
	 * When application is being closed
	 */
	public boolean onClose() {
		updateMessage();
		Folder folder = message.getFolder();
		if (folder!=null) {
			folder.deleteMessage(message);
			folder.appendMessage(message);
		}
		UiApplication.getUiApplication().popScreen(getScreen());
		return true;
	}
	
	/**
	 * Help accessible with convenience key
	 */
	public String getAccessibleHelp() {
		return "You are in message editing screen, in first field write your message subject"+
		" in second field write your message text, for menu press middle button, where you can"+
		" proofread, continue or cancel your message.";
	}

}
