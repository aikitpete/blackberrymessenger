package lib.utils;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Utils {

	public static void notifyUser(final String message) {
		if(UiApplication.isEventDispatchThread()){
			Dialog.alert(message);
		}else{
			UiApplication.getUiApplication().invokeLater(new Runnable(){
				public void run() {
					Dialog.alert(message);
				}
			});
		}
	}
	
}
