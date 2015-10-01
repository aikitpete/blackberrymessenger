package lib.accessibility.entities;

import lib.accessibility.interfaces.AccessibleTextContext;
import net.rim.blackberry.api.mail.Message;

public class AMessage {

	private final Message message;
	private AMessageHeader header;
	
	public AMessage(Message message) {
		this.message = message;
		this.header = new AMessageHeader(message);
	}
	
	public Message getMessage() {
		return message;
	}
	
	//--------------------------
	
	public AccessibleTextContext getAccessibleHeader(){
		return header;
	}
	
}
