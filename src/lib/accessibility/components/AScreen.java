package lib.accessibility.components;

import net.rim.device.api.ui.AccessibleEventDispatcher;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleState;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;
import net.rim.device.api.ui.accessibility.AccessibleValue;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.StringUtilities;

public abstract class AScreen extends MainScreen {
	
	private AccessibleContext context = new AccessibleContext() {
		private AccessibleText helpText;

		public AccessibleContext getAccessibleChildAt(int index) {
			return getAccessibleChildAt(index);
		}

		public int getAccessibleChildCount() {
			int count = 0;
			for(int i=0;i<getFieldCount();i++){
				if(getField(i).getAccessibleContext() != null)
					count++;
			}
			return count;
		}

		public String getAccessibleName() {
			return name;
		}

		public AccessibleContext getAccessibleParent() {
			return getManager().getAccessibleContext();
		}

		public int getAccessibleRole() {
			return AccessibleRole.SCREEN;
		}

		public AccessibleContext getAccessibleSelectionAt(int index) {
			return null;
		}

		public int getAccessibleSelectionCount() {
			return 0;
		}

		public int getAccessibleStateSet() {
			int state = 0;
			if(isFocus())		state |= AccessibleState.FOCUSED;
			if(isFocusable())	state |= AccessibleState.FOCUSABLE;
			return state;
		}

		public AccessibleTable getAccessibleTable() {
			return null;
		}

		public AccessibleText getAccessibleText() {
			if(helpText == null)
				helpText = new AccessibleText() {
					private String text = getAccessibleHelp();
					private String[] words = 

StringUtilities.stringToWords(text);

					public String getAtIndex(int part, int index) {
						switch(part){
							case AccessibleText.CHAR:
								return 

String.valueOf(text.charAt(index));
							case AccessibleText.LINE:
								return text;
							case AccessibleText.WORD:
								return words[index];
							default:
								return null;
						}
					}

					public int getCaretPosition() {
						return 0;
					}

					public int getCharCount() {
						return text.length();
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
						return text;
					}

					public String getWholeText() {
						return text;
					}
					
				};
			return helpText;
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
	};
	
	private String name;
	
	public AScreen(String name) {
		super();
		this.name = name;
	}

	public AScreen(String name,long style) {
		super(style);
		this.name = name;
	}

	public AccessibleContext getAccessibleContext() {
		return context;
	}

	public abstract String getAccessibleHelp();
	
	protected void onFocus(int direction) {
		

AccessibleEventDispatcher.dispatchAccessibleEvent(AccessibleContext.ACCESSIBLE_SELECTION_CHANGED, null, null, context);
		super.onFocus(direction);
	}
	
	protected void onUnfocus() {
		

AccessibleEventDispatcher.dispatchAccessibleEvent(AccessibleContext.ACCESSIBLE_SELECTION_CHANGED, null, null, context);
		super.onUnfocus();
	}

}
