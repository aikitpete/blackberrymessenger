package lib.accessibility.components;

import java.util.Vector;

import lib.utils.StringTokenizer;
import net.rim.device.api.ui.AccessibleEventDispatcher;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleState;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;
import net.rim.device.api.ui.accessibility.AccessibleValue;
import net.rim.device.api.ui.component.ButtonField;

public class AButton extends ButtonField implements AccessibleContext, AccessibleText {

	private Vector words = new Vector();
	
	public AButton() {
		super();
		init();
	}

	public AButton(long style) {
		super(style);
		init();
	}

	public AButton(String label, long style) {
		super(label, style);
		init();
	}

	public AButton(String label) {
		super(label);
		init();
	}

	private void init() {
		StringTokenizer t = new StringTokenizer(getLabel());
		while(t.hasMoreTokens())
			words.addElement(t.nextToken());
	}

	public AccessibleContext getAccessibleContext() {
		return this;
	}

	private int focusState = AccessibleState.FOCUSABLE;

	protected void onFocus(int direction) {
		// Update accessible state and notify screen reader
		int oldState = focusState;
		focusState = focusState | AccessibleState.FOCUSED;
		AccessibleEventDispatcher.dispatchAccessibleEvent(AccessibleContext.ACCESSIBLE_STATE_CHANGED, new Integer(
				oldState), new Integer(focusState), this);
		super.onFocus(direction);
	}

	protected void onUnfocus() {
		super.onUnfocus();

		// Update accessible state and notify screen reader
		int oldState = focusState;
		focusState = focusState & ~AccessibleState.FOCUSED;
		AccessibleEventDispatcher.dispatchAccessibleEvent(AccessibleContext.ACCESSIBLE_STATE_CHANGED, new Integer(
				oldState), new Integer(focusState), this);
	}

	public AccessibleContext getAccessibleChildAt(int index) {
		// Text field doesn't have any children
		return null;
	}

	public int getAccessibleChildCount() {
		// Text field doesn't have any children
		return 0;
	}

	public String getAccessibleName() {
		return "Generic accessible button";
	}

	public AccessibleContext getAccessibleParent() {
		// Return manager where text component is added
		Manager manager = getManager();
		return (manager != null) ? manager.getAccessibleContext() : null;
	}

	public int getAccessibleRole() {
		// Component serves as a text field.
		return AccessibleRole.TEXT_FIELD;
	}

	public AccessibleContext getAccessibleSelectionAt(int index) {
		// Text field doesn't have any children
		return null;
	}

	public int getAccessibleSelectionCount() {
		// Text field doesn't have any children
		return 0;
	}

	public int getAccessibleStateSet() {
		// Text component can be focused but not edited
		boolean focused = isFocus();

		if (focused) {
			return AccessibleState.FOCUSED | AccessibleState.FOCUSABLE;
		} else {
			return AccessibleState.FOCUSABLE;
		}
	}

	public AccessibleTable getAccessibleTable() {
		// This is a text component, not a table
		return null;
	}

	public AccessibleText getAccessibleText() {
		return this;
	}

	public AccessibleValue getAccessibleValue() {
		// This is a text component, no numerical values
		return null;
	}

	public boolean isAccessibleChildSelected(int index) {
		// Text field doesn't have any children
		return false;
	}

	public boolean isAccessibleStateSet(int state) {
		return (state & getAccessibleStateSet()) != 0;
	}

	// --------------------------------------------------

	public String getAtIndex(int part, int index) {
		// Return character, line or word at the given index
		switch (part) {
		case AccessibleText.CHAR:
			return String.valueOf(getLabel().charAt(index));

		case AccessibleText.LINE:
			return getLabel();

		case AccessibleText.WORD:
			return (String) words.elementAt(index);
		}

		return null;
	}

	public int getCaretPosition() {
		// Our text component is not editable and doesn't support caret
		// navigation
		return 0;
	}

	public int getCharCount() {
		// Number of characters
		return getLabel().length();
	}

	public int getLineCount() {
		// only one line
		return 1;
	}

	public int getSelectionEnd() {
		// Text component doesn't have text selection feature
		return 0;
	}

	public int getSelectionStart() {
		// Text component doesn't have text selection feature
		return 0;
	}

	public String getSelectionText() {
		return getLabel();
	}

	public String getWholeText() {
		return getLabel();
	}

}
