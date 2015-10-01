package lib.accessibility.components;

import net.rim.device.api.i18n.ResourceBundleFamily;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleState;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;
import net.rim.device.api.ui.accessibility.AccessibleValue;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.util.StringUtilities;

public class ALabel extends LabelField implements AccessibleContext,AccessibleText {
	
	public ALabel() {
		super();
	}

	public ALabel(Object text, int offset, int length, long style) {
		super(text, offset, length, style);
	}

	public ALabel(Object text, long style) {
		super(text, style);
	}

	public ALabel(Object text) {
		super(text);
	}

	public ALabel(ResourceBundleFamily rb, int key) {
		super(rb, key);
	}

	public AccessibleContext getAccessibleChildAt(int index) {
		return null;
	}

	public int getAccessibleChildCount() {
		return 0;
	}

	public String getAccessibleName() {
		return getText();
	}

	public AccessibleContext getAccessibleParent() {
		return getManager().getAccessibleContext();
	}

	public int getAccessibleRole() {
		return AccessibleRole.LABEL;
	}

	public AccessibleContext getAccessibleSelectionAt(int index) {
		return null;
	}

	public int getAccessibleSelectionCount() {
		return 0;
	}

	public int getAccessibleStateSet() {
		int state = 0;
		if(isFocusable())	state |= AccessibleState.FOCUSABLE;
		if(isFocus())		state |= AccessibleState.FOCUSED;
		return state;
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
		return (getAccessibleStateSet() & state) > 0;
	}

	public String getAtIndex(int type, int index) {
		switch(type){
			case AccessibleText.CHAR:
				return String.valueOf(getText().charAt(index));
			case AccessibleText.LINE:
				return getText();
			case AccessibleText.WORD:
				return StringUtilities.stringToWords(getText())[index];
			default:
				return null;
		}
	}

	public int getCaretPosition() {
		return 0;
	}

	public int getCharCount() {
		return getText().length();
	}

	public int getLineCount() {
		return 1;
	}

	public int getSelectionEnd() {
		return 0;
	}

	public int getSelectionStart() {
		return 0;
	}

	public String getSelectionText() {
		return getText();
	}

	public String getWholeText() {
		return getText();
	}

}
