/*
 * ScreenReader.java
 *
 * Copyright � 1998-2009 Research In Motion Ltd.
 * 
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

package lib.accessibility.reader;

import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleEventListener;
import net.rim.device.api.ui.accessibility.AccessibleRole;


/**
 * The ScreenReader class implements the AccessibleEventListener interface to allow
 * the BlackBerry UI application to retrieve accessibility information.
 */
public final class ScreenReader implements AccessibleEventListener 
{
    /**     
     * @see AccessibleEventListener#accessibleEventOccurred(int, Object, Object, AccessibleContext)
     */
    public synchronized void accessibleEventOccurred(int event, Object oldValue, Object newValue, AccessibleContext context)
    {
        if(context == null)
        {
            return;
        }
        
        System.out.println("ScreenReader Context: " + context.toString());
                
        int oldState = (oldValue instanceof Integer) ? ((Integer) oldValue).intValue() : 0;
        int newState = (newValue instanceof Integer) ? ((Integer) newValue).intValue() : 0;  
        
        // Handle each accessible event by role
        switch(context.getAccessibleRole()) 
        {
            case AccessibleRole.APP_ICON: 
                ScreenReaderHandler.handleAppIcon( event, oldState, newState, context );
                break;
                
            case AccessibleRole.ICON: 
                ScreenReaderHandler.handleIcon(event, oldState, newState, context);
                break;
                
            case AccessibleRole.CHECKBOX: 
                ScreenReaderHandler.handleCheckBox(event, oldState, newState, context);
                break;
                
            case AccessibleRole.CHOICE: 
                ScreenReaderHandler.handleChoice(event, oldState, newState, context);
                break;
            
            case AccessibleRole.COMBO: 
                ScreenReaderHandler.handleCombo(event, oldState, newState, context);
                break;
               
            case AccessibleRole.DATE: 
                ScreenReaderHandler.handleDate(event, oldState, newState, context);
                break;
            
            case AccessibleRole.DATE_FIELD: 
                ScreenReaderHandler.handleDateField(event, oldState, newState, context);
                break;
            
            case AccessibleRole.DIALOG: 
                ScreenReaderHandler.handleDialog(event, oldState, newState, context);
                break;
            
            case AccessibleRole.LABEL: 
                ScreenReaderHandler.handleLabel(event, oldState, newState, context);
                break;
            
            case AccessibleRole.LIST: 
                ScreenReaderHandler.handleList(event, oldState, newState, context);
                break;
             
            case AccessibleRole.MENU: 
                ScreenReaderHandler.handleMenu(event, oldState, newState, context);
                break;
            
            case AccessibleRole.MENU_ITEM: 
                ScreenReaderHandler.handleMenuItem(event, oldState, newState, context);
                break;
            
            case AccessibleRole.PUSH_BUTTON: 
                ScreenReaderHandler.handlePushButton(event, oldState, newState, context);
                break;
            
            case AccessibleRole.RADIO_BUTTON: 
                ScreenReaderHandler.handleRadioButton(event, oldState, newState, context);
                break;
            
            case AccessibleRole.TEXT_FIELD: 
                ScreenReaderHandler.handleTextField(event, oldValue, newValue, context);
                break;
            
            case AccessibleRole.SCREEN: 
                // Check that screen really changed.
                ScreenReaderHandler.handleScreen(event, oldState, newState, context);
                break;
            
            case AccessibleRole.TREE_FIELD: 
                ScreenReaderHandler.handleTreeField(event, oldState, newState, context);
                break;
            
            case AccessibleRole.SYMBOL: 
                ScreenReaderHandler.handleSymbol(event, oldState, newState, context);
                break;
            
            case AccessibleRole.HYPERLINK: 
                ScreenReaderHandler.handleHyperLink(event, oldState, newState, context);
                break;
            
            case AccessibleRole.TABLE: 
                ScreenReaderHandler.handleTable(event, oldState, newState, context);
                break;
            
            case AccessibleRole.PANEL: 
                ScreenReaderHandler.handlePanel(event, oldState, newState, context);
                break;
            
            case AccessibleRole.BITMAP: 
                ScreenReaderHandler.handleBitmap(event, oldState, newState, context);
                break;
                
            case AccessibleRole.GAUGE: 
                ScreenReaderHandler.handleGauge(event, oldState, newState, context);
                break;
            
            case AccessibleRole.SEPARATOR: 
                // Do nothing.
                break;
            
            default:
                System.out.println("Unsupported accessible role: " + context.getAccessibleRole());
                break;
        }
    }    
}
