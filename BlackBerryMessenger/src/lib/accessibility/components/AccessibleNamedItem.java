package lib.accessibility.components;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Font;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.accessibility.AccessibleRole;
import net.rim.device.api.ui.accessibility.AccessibleTable;
import net.rim.device.api.ui.accessibility.AccessibleText;
import net.rim.device.api.ui.accessibility.AccessibleValue;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Color;

public class AccessibleNamedItem extends Field implements AccessibleContext {

	private final String name;
	private final String imageResourceName;
	private final int width;
	private final int height;
	private int textColor;
	private int buttonColor;
	
	public AccessibleNamedItem(String name, String imageResourceName, int width, int height){
		super(Field.FOCUSABLE);
		this.name = name;
		this.imageResourceName = imageResourceName;
		this.width = width;
		this.height = height;
		textColor = Color.BLACK;
		buttonColor = Color.WHITE;
	}
	
	protected void paint(Graphics graphics){
		Font oldFont = graphics.getFont();
		graphics.setFont(oldFont.derive(Font.BOLD,40));
		graphics.setColor(buttonColor);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(textColor);
		graphics.drawText(name, 1, height/2-20);
		graphics.setFont(oldFont);
	}
	
	protected int switchColor(int color){
		if (color == Color.BLACK) color = Color.WHITE;
		else color = Color.BLACK;
		return color;
	}
	
	public int getPrefferedWidth() {
		return width;
	}
	
	public int getTextColor(){
		return textColor;
	}
	
	public int getButtonColor(){
		return buttonColor;
	}
	
	public String getAccessibleName() {
		return name;
	}
	
	
	public AccessibleContext getAccessibleChildAt(int index) {
		return null;
	}

	public int getAccessibleChildCount() {
		return 0;
	}

	public AccessibleContext getAccessibleParent() {
		return null;
	}

	public int getAccessibleRole() {
		//FIXME co s tim? dat moznost nastavit konstruktorem? nebo nechat na potomkovi?
		return AccessibleRole.MENU_ITEM;
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
		return null;
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
	
	public String getName() {
		return name; 
	}
	
	public Bitmap getBitmap() {
		return Bitmap.getBitmapResource(imageResourceName);
	}

/**
	public void setButtonId(int id)
    {
        this.id = id;
    }
    public int getButtonId()
    {
        return this.id;
    }
    */    
    
    public int getPreferredHeight()
    {
        return height;
    }
    
    
    public int getPreferredWidth()
    {
        return width;
    }
    
    protected void onFocus(int direction)
    {
    	textColor = Color.WHITE;
    	buttonColor = Color.BLACK;
        invalidate();
    }
    
    protected void onUnfocus()
    {
    	textColor = Color.BLACK;
    	buttonColor = Color.WHITE;
        invalidate();
    }
    
    protected void drawFocus(Graphics g, boolean on)
    {        

    }
    
    protected void layout(int width, int height) 
    {
        setExtent(Math.min( width, getPreferredWidth()), Math.min( 
                            height, getPreferredHeight()));
    }

    protected boolean navigationClick(int status, int time)
    {
        fieldChangeNotify(0);
        return true;
    }
    

}
