package lib.accessibility.components;

import net.rim.device.api.ui.AccessibleEventDispatcher;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleState;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;
import net.rim.device.api.ui.accessibility.AccessibleValue;
import net.rim.device.api.ui.component.ObjectListField;

public class AObjectList extends ObjectListField implements AccessibleContext {

	private String listAccessibleName;

	public AObjectList(String listAccessibleName) {
		this.listAccessibleName = listAccessibleName;
		
		setFocusListener(new FocusChangeListener(){
			public void focusChanged(Field arg0, int arg1) {
				AccessibleEventDispatcher.dispatchAccessibleEvent(AccessibleContext.ACCESSIBLE_SELECTION_CHANGED, null, null, AObjectList.this);
			}
		});
	}
	
	public AccessibleContext getAccessibleContext() {
		return this;
	}
	
	public AccessibleContext getAccessibleChildAt(int index) {
		return (AccessibleContext) get(this, index);
	}

	public int getAccessibleChildCount() {
		return getSize();
	}

	public String getAccessibleName() {
		return listAccessibleName;
	}

	public AccessibleContext getAccessibleParent() {
		Manager manager = getManager();
		return (manager != null) ? manager.getAccessibleContext() : null;
	}

	public int getAccessibleRole() {
		return AccessibleRole.LIST;
	}

	public AccessibleContext getAccessibleSelectionAt(int index) {
		return getAccessibleChildAt(getSelection()[index]);
	}

	public int getAccessibleSelectionCount() {
		return getSelection().length;
	}

	public int getAccessibleStateSet() {
		int state = 0;
		if (isFocus())
			state |= AccessibleState.FOCUSED;
		if (isFocusable())
			state |= AccessibleState.FOCUSABLE;
		return state;
	}

	public AccessibleTable getAccessibleTable() {
		return null;
	}

	public AccessibleText getAccessibleText() {
		return null;
	}

	public AccessibleValue getAccessibleValue() {
		return null;
	}

	public boolean isAccessibleChildSelected(int index) {
		int[] s = getSelection();
		for (int i = 0, l = s.length; i < l; i++)
			if (s[i] == index)
				return true;
		return false;
	}

	public boolean isAccessibleStateSet(int state) {
		return (getAccessibleStateSet() & state) != 0;
	}

}
