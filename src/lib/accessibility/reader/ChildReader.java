/*
 * ChildReader.java
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

import lib.utils.Util;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleState;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;


/**
 * This class contains methods to read accessible child elements
 */
public final class ChildReader
{  
    /**
     * Reads an accessible child element
     * @param context The accessible child element to read
     */
    static void readChildElement(AccessibleContext context) 
    {
        if(context == null)
        {
            return;
        }
        
        String name = context.getAccessibleName();
        if(name == null)
        {
            name = "";
        }
        
        // Evaluate the states set for the accessible element
        int statesSet = context.getAccessibleStateSet();                  
        boolean focused = (statesSet & AccessibleState.FOCUSED) != 0;
        boolean expanded = (statesSet & AccessibleState.EXPANDED) != 0;
        boolean collapsed = (statesSet & AccessibleState.COLLAPSED) != 0;
        boolean selected = (statesSet & AccessibleState.SELECTED) != 0;
        boolean selectable = (statesSet & AccessibleState.SELECTABLE) != 0;
        boolean multiSelectable = (statesSet & AccessibleState.MULTI_SELECTABLE) != 0;
        boolean editable = (statesSet & AccessibleState.EDITABLE) != 0;
        boolean checked = (statesSet & AccessibleState.CHECKED) != 0;
        boolean busy = (statesSet & AccessibleState.BUSY) != 0;
        boolean expandable = (statesSet & AccessibleState.EXPANDABLE) != 0;
        boolean focusable = (statesSet & AccessibleState.FOCUSABLE) != 0;
        
        // Create strings representing the accessible element states set
        String focusedText = focused ? " focused" : "";
        String expandedText = expanded ? " expanded" : "";
        String collapsedText = collapsed ? " collapsed" : "";
        String expandableText = expandable ? " expandable" : "";
        String selectedText = selected ? " selected" : "";
        String editableText = editable ? " editable" : "";
        String checkedText = checked ? " checked" : " unchecked";
        String multiSelectableText = multiSelectable ? " multi selectable" : "";
        String focusableText = focusable ? " focusable" : "";
        
        // This buffer will contain text to be spoken
        StringBuffer toSpeak = new StringBuffer(); 
        
        // Evaluate the navagational orientation state(s) set for the accessible element
        String orientation = Util.getOrientation(statesSet);
        
        AccessibleText text = context.getAccessibleText();
        
        int childCount = context.getAccessibleChildCount();
        
        // In cases where there are many components on a screen/dialog/menu...most of 
        // which are not frequently used, it's more practical to set a limit on the 
        // number of components to be read:
        // int maxCount = Math.min( childCount, 10);
        // and then use maxCount instead of childCount.
              
        switch(context.getAccessibleRole()) 
        {
            case AccessibleRole.SCREEN:
                if(busy) 
                {
                    Util.speak("Screen " + name + " loading");
                } 
                else 
                {
                    Util.speak("Screen " + name);
                    for( int i = 0; i < childCount; i++ ) 
                    {
                        AccessibleContext child = context.getAccessibleChildAt(i);
                        readChildElement(child);
                    }
                }
                break;
                
            case AccessibleRole.TEXT_FIELD:
                if(text != null) 
                {
                    String currentText = text.getWholeText();
                    currentText = (currentText != null) ? currentText.trim() : "";
                    String textToSpeak = (currentText.length() > 0) ? " with text " + currentText : " empty";
                    toSpeak.append(name + " text field " + textToSpeak);
                    toSpeak.append(focusedText);
                    toSpeak.append(editableText);
                }
                break;
                
            case AccessibleRole.LABEL:
                toSpeak.append(name);
                toSpeak.append(focusableText);
                toSpeak.append(focusedText);
                toSpeak.append(selectedText);
                toSpeak.append(expandedText);
                break;
                
            case AccessibleRole.APP_ICON:
                toSpeak.append(name + "application icon");
                toSpeak.append(focusedText);
                break;
                
            case AccessibleRole.ICON:
                toSpeak.append( name + " icon ");
                toSpeak.append(focusedText);
                break;
                
            case AccessibleRole.DATE:
                toSpeak.append(name + " date field ");
                toSpeak.append(selectedText);
                if(text != null)
                {
                    toSpeak.append(" with current value " + text.getWholeText());
                }
                break;
                
            case AccessibleRole.LIST:
                if(busy) 
                {
                    Util.speak(orientation + "list " + name + " loading");
                } 
                else 
                {
                    Util.speak( orientation + name + "list with " + childCount + " elements"  + focusedText + multiSelectableText);
                    for( int i = 0; i < childCount; i++ ) 
                    {
                        AccessibleContext child = context.getAccessibleChildAt(i);
                        readChildElement(child);
                    }
                }
                break;
                
            case AccessibleRole.PANEL:
                if(busy) 
                {
                    Util.speak("panel " + name + " loading");
                } 
                else 
                {
                    Util.speak(name + " panel with " + childCount + " items");
                    for(int i = 0; i < childCount; i++) 
                    {
                        AccessibleContext child = context.getAccessibleChildAt(i);
                        readChildElement(child);
                    }
                }
                break;
                
            case AccessibleRole.GAUGE:
                if(busy) 
                {
                    Util.speak("gauge " + name + " loading");
                } 
                else 
                {
                    Util.speak(name + " gauge with value " + context.getAccessibleValue().getCurrentAccessibleValue());                    
                }
                break;
                
            case AccessibleRole.PUSH_BUTTON:
                toSpeak.append(name + " button");
                toSpeak.append(focusedText);
                break;
                
            case AccessibleRole.MENU_ITEM:
                toSpeak.append(name + " menu item ");
                toSpeak.append(selectedText);
                break;
                
            case AccessibleRole.CHECKBOX:
                toSpeak.append(name);
                toSpeak.append( " check box " );                
                toSpeak.append(focusedText);
                toSpeak.append(checkedText);
                break;
                
            case AccessibleRole.TABLE:
                if(busy) 
                {
                    Util.speak("table " + name + " loading");
                } 
                else 
                {
                    AccessibleTable table = context.getAccessibleTable();
                    if(table != null) 
                    {
                        readTableElement(context);
                        if(selectable)
                        {
                            ScreenReaderHandler.handleTableSelection(table);
                        }
                    }
                }
                break;
                
            case AccessibleRole.BITMAP:
                Util.speak(name + " image");
                break;
                
            case AccessibleRole.COMBO:
                toSpeak.append(name + " combobox ");
                toSpeak.append(expandedText);
                if(expanded) 
                {
                    Util.speak(toSpeak.toString());
                    toSpeak.setLength(0);
                    for( int i = 0; i < childCount; i++ ) 
                    {
                        AccessibleContext child = context.getAccessibleChildAt(i);
                        readChildElement(child);
                    }
                } 
                else 
                {
                    String value = context.getAccessibleText().getWholeText();
                    if(value != null && value.length() > 0)
                    {
                        toSpeak.append(" current value " + value);
                    }
                    else
                    {
                        toSpeak.append(" empty");
                    }
                }
                break;
                
            case AccessibleRole.HYPERLINK:
                toSpeak.append(name + " hyperlink ");
                break;
                
            case AccessibleRole.SEPARATOR:
                toSpeak.append("separator");
                break;
                
            case AccessibleRole.TREE_FIELD:
                Util.speak(name + " tree field" + expandableText + collapsedText + expandedText + selectedText);
                for( int i = 0; i < childCount; i++ ) 
                {
                    AccessibleContext child = context.getAccessibleChildAt(i);
                    readChildElement(child);
                }
                break;
                
            case AccessibleRole.CHOICE:
                toSpeak.append(name + " choice with " + childCount + " elements");
                toSpeak.append(focusedText);
                toSpeak.append(expandedText);
                if(expanded) 
                {
                    Util.speak(toSpeak.toString());
                    toSpeak.setLength( 0 );
                    for( int i = 0; i < childCount; i++ ) 
                    {
                        AccessibleContext child = context.getAccessibleChildAt(i);
                        readChildElement(child);
                    }
                } 
                else 
                {
                    toSpeak.append(" current value " + context.getAccessibleSelectionAt(0).getAccessibleName());
                }
                break;
        }
        
        Util.speak(toSpeak.toString());
    }   
    
    /**
     * Reads an accessible table element
     * @param context The accessible element to read
     */
    static void readTableElement( AccessibleContext context )
    {
        if( !(context instanceof AccessibleTable ))
        {
            return;
        }

        AccessibleTable table = (AccessibleTable) context;
        
        // Evaluate number of columns and rows in the accessible table
        int tableColCount = table.getAccessibleColumnCount();
        int tableRowCount = table.getAccessibleRowCount();       

        String name = context.getAccessibleName();
        
        if(name == null)
        {
            name = "";
        }

        // Describe the table
        StringBuffer tableHeader = new StringBuffer();
        tableHeader.append(name);
        tableHeader.append(" table with ");
        tableHeader.append(tableColCount);
        tableHeader.append(" columns and ");
        tableHeader.append(tableRowCount);
        tableHeader.append(" rows");
        Util.speak(tableHeader.toString());
        
        // Get the column headers
        AccessibleContext[] tableColumnsNames = table.getAccessibleColumnHeader();
        
        if(tableColumnsNames != null)
        {
            // Read cells, column by column
            for(int i = 0; i < tableColumnsNames.length; i++)
            {
                AccessibleContext column = tableColumnsNames[i];
                Util.speak( "column " + (i + 1));
                if(column == null)
                {
                    Util.speak("empty");
                }
                else
                {
                    readChildElement(column);
                }
                
                // Read cells in the column
                for(int row = 0; row < tableRowCount; row++)
                {
                    AccessibleContext accessibleCell = table.getAccessibleAt(row, i);
                    if(accessibleCell == null)
                    {
                        Util.speak("empty");
                    }
                    else
                    {
                        readChildElement(accessibleCell);
                    }
                }
            }
        }
        else
        {
            // Read cells, row by row
            Util.speak("table data");
            for(int row = 0; row < tableRowCount; row++)
            {
                for(int col = 0; col < tableColCount; col++)
                {
                    AccessibleContext accessibleCell = table.getAccessibleAt(row, col);
                    if(accessibleCell == null)
                    {
                        Util.speak("empty");
                    }
                    else
                    {
                        readChildElement(accessibleCell);
                    }
                }
            }
        }
    }   
}
