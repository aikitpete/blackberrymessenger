package lib.accessibility.components;

import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

public class AccessibleMailFolder extends AccessibleNamedItem {

	private final int folder;
	private Store store = Session.getDefaultInstance().getStore();
	private String caption;
	
	public AccessibleMailFolder(String name, String imageResourceName, int width, int height, int folder){
		super(name, imageResourceName, width, height);
		this.folder = folder;
		if (folder == Folder.INBOX){
			caption = "IN";
		}
		else caption = "OUT";
		
	}
	
	public int getFolder() {
		return folder;
	}
	
	public int getMessageCount() {
		try{
			if(folder == Folder.SENT){
				int count = 0;
				Folder[] folders = store.list(folder);
				for(int i=0;i<folders.length;i++){
					Message[] messages = folders[i].getMessages();
					Message[] draftMessages = folders[i].getDraftMessages(messages);
					msg:for(int j=0;j<messages.length;j++){
						for(int k=0;k<draftMessages.length;k++)
							if(draftMessages[k].equals(messages[j]))
								continue msg;
						count++;
					}
				}
				return count;
			}else{
				int count = 0;
				Folder[] folders = store.list(folder);
				for(int i=0;i<folders.length;i++)
					count += folders[i].getMessages().length;
				return count;
			}
		}catch(MessagingException e){
			return -1;
		}
	}
	
	protected void paint(Graphics graphics){
		Font oldFont = graphics.getFont();
		graphics.setFont(oldFont.derive(Font.BOLD,40));
		graphics.setColor(this.getButtonColor());
		graphics.fillRect(0, 0, this.getPreferredWidth(), this.getPreferredHeight());
		graphics.setColor(this.getTextColor());
		graphics.drawText(caption+"("+getMessageCount()+")", 1, this.getPreferredHeight()/2-20);
		graphics.setFont(oldFont);
	}
	
	public int getUnreadMessageCount() {
		try{
			int count = 0;
			Folder[] folders = store.list(folder);
			for(int i=0;i<folders.length;i++){
				Message[] messages = folders[i].getMessages();
				for(int j=0;j<messages.length;j++)
					if(!messages[j].isSet(Message.Flag.OPENED))
						count ++;
			}
			return count;
		}catch(MessagingException e){
			return 0;
		}
	}
}
