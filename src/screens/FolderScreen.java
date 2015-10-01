package screens;

import core.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import lib.accessibility.components.AScreen;
import net.rim.blackberry.api.mail.event.FolderEvent;
import net.rim.blackberry.api.mail.event.FolderListener;
import net.rim.device.api.ui.container.VerticalFieldManager;
import lib.accessibility.components.AFolderList;
import lib.accessibility.entities.AMessage;
import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Message;


public class FolderScreen extends AScreen {

	private VerticalFieldManager manager;
	private int folder;
	private AFolderList messageList;
	private Store store;
	private Folder[] folders;
	private FolderListener folderListener = new FolderListener(){
		
		/**
		 * Updates content of displayed folder
		 */
		public void messagesAdded(FolderEvent e) {
			if(e.getMessage().getFolder().getType() == FolderScreen.this.folder){
				synchronized(Application.getEventLock()){
					int selectedIndex = messageList.getSelectedIndex();
					messageList.insert(0, new AMessage(e.getMessage()));
					messageList.setSelectedIndex(selectedIndex+1);
					messageList.invalidate();
					messageList.getManager().invalidate();
				}
			}
		}
		public void messagesRemoved(FolderEvent e) {
			if(e.getMessage().getFolder().getType() == FolderScreen.this.folder){
				synchronized(Application.getEventLock()){
					for(int i=0;i<messageList.getSize();i++)
						if(((AMessage)messageList.get(messageList, i)).getMessage().getMessageId() == e.getMessage().getMessageId())
							messageList.delete(i);
				};
			}
		}
	};
		
	
	
	/**
	 * Screen for displaying folder contents
	 * @param FolderName name of displayed folder
	 * @param folder type of folder
	 * @throws MessagingException message could not be read
	 */
	public FolderScreen(String FolderName, int folder) throws MessagingException {
		super(FolderName+"folder");
		add(manager = new VerticalFieldManager(Manager.NO_VERTICAL_SCROLLBAR));
		manager.add(messageList = new AFolderList(FolderName){

		});
		messageList.setRowHeight(Display.getHeight());
		store = Session.getDefaultInstance().getStore();
		folders = store.list(folder);
		refresh();
		store.addFolderListener(folderListener);
		
	}
	
	/**
	 * Update 
	 * @throws MessagingException
	 */
	public void refresh() throws MessagingException {
		for (int i = 0; i < messageList.getSize(); i++){
			messageList.delete(messageList.getSize()-1);
		}
		
		for(int fIndex = 0;fIndex < folders.length;fIndex++){
			if(folder == Folder.SENT){
				Message[] messages = folders[fIndex].getMessages();
				Message[] draftMessages = folders[fIndex].getDraftMessages(messages);
				msg:for(int j=0;j<messages.length;j++){
					for(int k=0;k<draftMessages.length;k++)
						if(draftMessages[k].equals(messages[j]))
							continue msg;
					messageList.insert(0, new AMessage(messages[j]));
				}
			}else{
				Message[] messages = folders[fIndex].getMessages();
				for(int i = 0;i<messages.length;i++){
					messageList.insert(0, new AMessage(messages[i]));
				}
			}
		}
	}
	
	/**
	 * Called when screen is being closed
	 */
	public boolean onClose() {
		UiApplication.getUiApplication().popScreen(getScreen());
		return true;
	}
	
	public String getAccessibleHelp() {
		return "You are now in folder screen, where you can browse your messages ordered by date. For each message "+
		" you will hear it's details. For more options press middle button.";
	}
	

	}

