package lib.accessibility.entities;

import java.util.Vector;

import lib.accessibility.interfaces.AccessibleTextContext;
import lib.utils.StringTokenizer;
import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.MessagingException;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;
import net.rim.device.api.ui.accessibility.AccessibleValue;

class AMessageHeader implements AccessibleTextContext {
	
	private String contactInfo;
	private String receivedInfo;
	private String wholeText;
	
	private Vector lines = new Vector();
	private Vector words = new Vector();
	
	protected AMessageHeader(Message message) {
		try {
			switch(message.getFolder().getType()){
				case Folder.INBOX:
				case Folder.DELETED:
					contactInfo = (message.getFrom().getName() != null) ? message.getFrom().getName() : message.getFrom().getAddr();
					break;
				case Folder.DRAFT:
				case Folder.SENT:
					Address[] recipients = message.getRecipients(Message.RecipientType.TO);
					if(recipients.length == 0)
						contactInfo = "No recipients";
					else if(recipients.length > 1)
						contactInfo = "Multiple recipients";
					else
						contactInfo = (recipients[0].getName() != null) ? recipients[0].getName() : recipients[0].getAddr();
					break;
				default:
					contactInfo = "";
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			contactInfo = "";
		}
		receivedInfo = getDate(message.getReceivedDate().toString());
		wholeText = "Contact: "+contactInfo+" Date: "+receivedInfo;
		
		lines.addElement("Contact: "+contactInfo);
		lines.addElement("Date: "+receivedInfo);
		
		StringTokenizer t = new StringTokenizer(wholeText);
		while(t.hasMoreTokens())
			words.addElement(t.nextToken());
	}
	
	public AccessibleContext getAccessibleChildAt(int index) {
		return null;
	}

	public int getAccessibleChildCount() {
		return 0;
	}
	
	protected String getDate (String string) {
		return string.substring(string.indexOf(" ")+1, string.indexOf(":")+3);
	}

	public String getAccessibleName() {
		return "From "+contactInfo+" Date: "+receivedInfo;
	}

	public AccessibleContext getAccessibleParent() {
		return null;
	}

	public int getAccessibleRole() {
		return AccessibleRole.PUSH_BUTTON;
	}

	public AccessibleContext getAccessibleSelectionAt(int index) {
		return null;
	}

	public int getAccessibleSelectionCount() {
		return 0;
	}

	public int getAccessibleStateSet() {
		return 0;
	}

	public AccessibleTable getAccessibleTable() {
		return null;
	}

	public AccessibleText getAccessibleText() {
		return this;
	}

	public AccessibleValue getAccessibleValue() {
		return null;
	}

	public boolean isAccessibleChildSelected(int index) {
		return false;
	}

	public boolean isAccessibleStateSet(int state) {
		return false;
	}

	public String getAtIndex(int part, int index) {
		switch(part){
			case AccessibleText.CHAR:
				return wholeText.substring(index, index+1);
			case AccessibleText.LINE:
				return (String) lines.elementAt(index);
			case AccessibleText.WORD:
				return (String) words.elementAt(index);
		}
		return null;
	}

	public int getCaretPosition() {
		return 0;
	}

	public int getCharCount() {
		return wholeText.length();
	}

	public int getLineCount() {
		return 2;
	}

	public int getSelectionEnd() {
		return 0;
	}

	public int getSelectionStart() {
		return 0;
	}

	public String getSelectionText() {
		return wholeText;
	}

	public String getWholeText() {
		return wholeText;
	}
}