package lib.message;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

import net.rim.blackberry.api.sms.SMS;
import net.rim.blackberry.api.sms.SendListener;

public class SMSConnection {

	private MessageConnection messageConnection;
	
	private Vector receiveListeners = new Vector();
	private Vector sendListeners = new Vector();
	
	private SendListener sl = new SendListener() {
		public boolean sendMessage(Message message) {
			for(int i = 0;i<sendListeners.size();i++)
				((SendListener)sendListeners.elementAt(i)).sendMessage(message);
			return true;
		}
	};
	private MessageListener ml = new MessageListener() {
		public void notifyIncomingMessage(MessageConnection conn) {
			try {
				Message m = messageConnection.receive();
				String address = m.getAddress();
				String msg = null;
				if (m instanceof TextMessage) {
					TextMessage tm = (TextMessage) m;
					msg = tm.getPayloadText();
				} else if (m instanceof BinaryMessage) {
					byte[] data = ((BinaryMessage) m).getPayloadData();
					// convert Binary Data to Text
					msg = new String(data, "UTF-8");
				} else {
					System.out.println("Invalid Message Format");
					return;
				}
				System.out.println("Received SMS text from " + address + " : " + msg);
				for(int i = 0;i<receiveListeners.size();i++)
					((ReceiveListener)receiveListeners.elementAt(i)).receiveMessage(m);
			} catch (InterruptedIOException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public SMSConnection() throws IOException {
		messageConnection = (MessageConnection) Connector.open("sms://:0");
		messageConnection.setMessageListener(ml);
		SMS.addSendListener(sl);
	}
	
	public void release() throws IOException {
		SMS.removeSendListener(sl);
		messageConnection.close();
	}
	
	public void addSendListener(SendListener l) {
		sendListeners.addElement(l);
	}
	
	public void removeSendListener(SendListener l) {
		sendListeners.removeElement(l);
	}
	
	public void addReceiveListener(ReceiveListener l) {
		receiveListeners.addElement(l);
	}
	
	public void removeReceiveListener(ReceiveListener l) {
		receiveListeners.removeElement(l);
	}
}
