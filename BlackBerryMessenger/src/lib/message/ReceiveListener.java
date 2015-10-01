package lib.message;

import javax.wireless.messaging.Message;

public interface ReceiveListener {
	
	public boolean receiveMessage(Message message);
	
}
