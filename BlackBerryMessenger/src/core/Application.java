package core;

import java.io.IOException;

import lib.message.SMSConnection;
import net.rim.device.api.ui.UiApplication;
import screens.MenuScreen;
import java.util.Vector;
import lib.accessibility.components.AScreen;
import lib.accessibility.reader.ScreenReader;
import lib.adapter.KeyAdapter;
import lib.utils.Utils;
import config.Config;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.event.FolderEvent;
import net.rim.blackberry.api.mail.event.FolderListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.accessibility.AccessibilityManager;
import net.rim.device.api.ui.component.Dialog;

public class Application extends UiApplication implements FolderListener {

	public static void main(String[] args) {
		Application app = new Application();
		app.enterEventDispatcher();
	}
	
	public static ScreenReader screenReader;

	public Application() {
		try {
			Config.load();
			Config.print();
			//new SMSConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			new SMSConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Only one listener can be registered
			if (!AccessibilityManager.isAccessibleEventListenerRegistered()) {
				screenReader = new ScreenReader();
				AccessibilityManager.setAccessibleEventListener(screenReader);
			} else {
				System.out.println("An AccessibleEventListener is already registered");
			}
		} catch (net.rim.device.api.system.UnsupportedOperationException uoe) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert("Accessibilty not supported on this device");
				}
			});
		}
		
		addKeyListener(new KeyAdapter(){
			public boolean keyDown(int keycode, int time) {
				if(Keypad.key(keycode) == Keypad.KEY_CONVENIENCE_1
				|| Keypad.key(keycode) == Keypad.KEY_CONVENIENCE_2)
					if(getActiveScreen() instanceof AScreen){
						Utils.notifyUser(((AScreen)getActiveScreen()).getAccessibleHelp());

					return true;
				}
				return false;
			}
		});
		
		Session.getDefaultInstance().getStore().addFolderListener(this);
		pushScreen(new MenuScreen("SelectAction"));
		
	}
	public void messagesAdded(final FolderEvent e) {
		final Message m = e.getMessage();
		invokeLater(new Runnable(){
			public void run() {
				synchronized(secondaryFolderListeners){
					for(int i=0;i<secondaryFolderListeners.size();i++)
						((FolderListener)secondaryFolderListeners.elementAt(i)).messagesAdded(e);
				}
				
				try {
					if(!m.isSet(Message.Flag.OPENED)){
						if(Dialog.ask(Dialog.D_YES_NO,
								"New e-mail from: "+m.getFrom().getName()+" " +
								"("+m.getFrom().getAddr()+"), " +
								"do you want to read it?") == Dialog.YES){
							//pushScreen(new MailReadMessageScreen(m));
						}
					}
				} catch (MessagingException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public void messagesRemoved(FolderEvent e) {
		synchronized(secondaryFolderListeners){
			for(int i=0;i<secondaryFolderListeners.size();i++)
				((FolderListener)secondaryFolderListeners.elementAt(i)).messagesRemoved(e);
		}
	}
	
	public static Vector secondaryFolderListeners = new Vector();
	
	public static void addFolderListener(FolderListener fl) {
		synchronized(secondaryFolderListeners){
			secondaryFolderListeners.addElement(fl);
		}
	}
	
	public static void removeFolderListener(FolderListener fl) {
		synchronized(secondaryFolderListeners){
			secondaryFolderListeners.removeElement(fl);
		}
	}
}
