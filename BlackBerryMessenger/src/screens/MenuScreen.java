package screens;

import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.FolderNotFoundException;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import java.io.IOException;
import core.Application;
import net.rim.device.api.ui.accessibility.AccessibilityManager;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.UiApplication;
import lib.accessibility.components.AScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import lib.accessibility.components.AccessibleNamedItem;
import lib.accessibility.components.AccessibleMailFolder;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.FieldChangeListener;
import lib.utils.Util;
import config.Config;
import lib.utils.Utils;
import screens.FolderScreen;



public class MenuScreen extends AScreen {

	private HorizontalFieldManager hManager;
	private VerticalFieldManager vManager;
	private AccessibleNamedItem wSection;
	private AccessibleMailFolder iSection, oSection;
	private BitmapField picture;
	private int index;

	/**
	 * Default constructor of the main screen
	 * @param name name of screen
	 */
	public MenuScreen(String name) {
		
		super(name);
		hManager = new HorizontalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_HORIZONTAL_SCROLLBAR);
		vManager = new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR);
		picture = new BitmapField(Bitmap.getBitmapResource("write.png"));
		wSection = new AccessibleNamedItem("Write", "write.png",Display.getWidth()/2,Display.getHeight()/4);
		iSection = new AccessibleMailFolder("Inbox", "inbox.png",Display.getWidth()/2,Display.getHeight()/4,Folder.INBOX);
		oSection = new AccessibleMailFolder("Outbox", "outbox.png",Display.getWidth()/2,Display.getHeight()/4,Folder.OUTBOX);
		index = 4;
		FocusChangeListener wListener = new FocusChangeListener(){
			public void focusChanged(Field field, int arg1) {
				if (index!=0){
			        index = 0;
			        Util.speak(wSection.getAccessibleName());
			        picture.setBitmap(wSection.getBitmap());
				}
			}
		};
		FocusChangeListener iListener = new FocusChangeListener(){
			public void focusChanged(Field field, int arg1) {
				if (index!=1){
					index = 1;
					Util.speak(iSection.getAccessibleName()+" contains "+iSection.getMessageCount()+" new messages, "+iSection.getMessageCount()+" total messages.");
					picture.setBitmap(iSection.getBitmap());
				}
			}
		};
		FocusChangeListener oListener = new FocusChangeListener(){
			public void focusChanged(Field field, int arg1) {
				if (index!=2){
			        index = 2;
			        Util.speak(oSection.getAccessibleName()+" contains "+oSection.getMessageCount()+" new messages, "+oSection.getMessageCount()+" total messages.");
			        picture.setBitmap(oSection.getBitmap());
				}
			}
		};

		FieldChangeListener wClickListener = new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
			Util.speak("Selected "+iSection.getAccessibleName());
			try {
			Message message = new Message();
			message.setContent("");
			UiApplication.getUiApplication().pushScreen(new ComposeScreen("Compose new message", message));
			} catch (MessagingException ex) {
				ex.printStackTrace();
				Utils.notifyUser("Could not edit message");
			}
			}
			
		};
		
		FieldChangeListener iClickListener = new FieldChangeListener(){
			public void fieldChanged(Field field, int arg1) {
				Util.speak("Selected "+iSection.getAccessibleName());
				if (iSection.getMessageCount()!=0) {
					try {UiApplication.getUiApplication().pushScreen(new FolderScreen("Select action",Folder.INBOX));}
					catch (FolderNotFoundException e) {
						e.printStackTrace();
						Utils.notifyUser("Folder does not exist");
					} catch (MessagingException e) {
						e.printStackTrace();
						Utils.notifyUser("Could initialize empty message");
					}
				}
				else {
					Utils.notifyUser("Inbox is empty.");
					Util.speak("Unable to open inbox, folder is empty.");
				}
			}
		};
		FieldChangeListener oClickListener = new FieldChangeListener(){
			public void fieldChanged(Field field, int arg1) {
				Util.speak("Selected "+oSection.getAccessibleName());
				if (oSection.getMessageCount()!=0) {
					try {UiApplication.getUiApplication().pushScreen(new FolderScreen("Select action",Folder.OUTBOX));}
					catch (FolderNotFoundException e) {
						e.printStackTrace();
						Utils.notifyUser("Draft folder does not exist");
					} catch (MessagingException e) {
						e.printStackTrace();
						Utils.notifyUser("Could initialize empty message");
					}
				}
				else {
					Utils.notifyUser("Outbox is empty.");
					Util.speak("Unable to open outbox, folder is empty.");
				}
			}
		};
		
		wSection.setFocusListener(wListener);
		wSection.setChangeListener(wClickListener);
		iSection.setFocusListener(iListener);
		iSection.setChangeListener(iClickListener);
		oSection.setFocusListener(oListener);
		oSection.setChangeListener(oClickListener);
		
		add(hManager);
		vManager.add(wSection);
		vManager.add(iSection);
		vManager.add(oSection);
		hManager.add(vManager);
		
		hManager.add(new SeparatorField(Field.USE_ALL_HEIGHT){
			protected void layout (int width, int height){
				
			    setExtent(1,Display.getHeight());

			}
		});
		
		hManager.add(picture);
		

	}
	
	/**
	 * When closed
	 */
	public boolean onClose() {
		if (Application.screenReader != null
				 && AccessibilityManager.isAccessibleEventListenerRegistered()) {
					AccessibilityManager.removeAccessibleEventListener(Application.screenReader);
				}
				try {
					Config.print();
					Config.save();
				} catch (IOException e) {
					e.printStackTrace();
					UiApplication.getUiApplication().invokeAndWait(new Runnable(){
						public void run() {
							Dialog.alert("Failed to save config file");
						}
					});
				}
				System.exit(2);
				return super.onClose();
	}
	
	/**
	 * Help activated with the convenience key
	 */
	public String getAccessibleHelp() {
		return "You are in main screen of messenger application, select one of 4 options: write message, message inbox, message outbox, " +
				"or exit application. Navigation buttons are keypad, backcspace and help button. You can access this help in each menu.";
	}

}
