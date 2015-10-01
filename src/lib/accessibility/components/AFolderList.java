package lib.accessibility.components;

import lib.accessibility.entities.AMessage;
import net.rim.device.api.ui.Font;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.accessibility.AccessibleContext;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import lib.utils.Util;
import net.rim.blackberry.api.mail.MessagingException;
import screens.ReadScreen;
import net.rim.device.api.ui.UiApplication;
import net.rim.blackberry.api.mail.Message;


public class AFolderList extends AObjectList {
	
	private Bitmap bitmapUp, bitmapDown;
	private int index;
	
	public AFolderList(String name) {
		super(name);
		bitmapUp = Bitmap.getBitmapResource("up.png");
	    bitmapDown = Bitmap.getBitmapResource("down.png");
	    index = 0;
	}
	
	public AccessibleContext getAccessibleChildAt(int index) {
		return ((AMessage) get(this, index)).getAccessibleHeader();
	}
	
	
	public void drawListRow (ListField listField, Graphics g, int index, int y, int width){
		
		g.setColor(Color.WHITE);
		g.fillRect(0, y, Display.getWidth(), Display.getHeight());
		g.setColor(Color.BLACK);
		Font old = g.getFont();
		g.setFont(getFont().derive(Font.BOLD,30));
		Message selected = ((AMessage)get(this,getSelectedIndex())).getMessage();
		Util.speak("Message "+(getSelectedIndex()+1)+" of "+getSize());
		if (getSelectedIndex()!=0) g.drawBitmap(0, y, bitmapUp.getWidth(), bitmapUp.getHeight(), bitmapUp, 0, 0);
		if (getSelectedIndex()!= this.getSize()-1) g.drawBitmap(0, y+Display.getHeight()-bitmapDown.getHeight(), bitmapDown.getWidth(), bitmapDown.getHeight(), bitmapDown, 0, 0);
		g.drawLine(0, y+bitmapUp.getHeight()+1, width, y+bitmapUp.getHeight()+1);
		g.drawLine(0, y+Display.getHeight()-bitmapDown.getHeight()-2, width, y+Display.getHeight()-bitmapDown.getHeight()-2);
		g.drawText((getSelectedIndex()+1)+"/"+(getSize()),Display.getWidth()-90,y+5);
		if (selected.getSubject()!=null) {
			g.drawText("S: "+selected.getSubject(), 5, y+105);
		}
		else {
			g.drawText("S: <no subject>", 5, y+105);
		}
		try {
		if (selected.getReceivedDate()!=null && selected.getFrom()!=null) {
			g.drawText("F: "+selected.getFrom().getName(), 5, y+55);
			g.drawText("D: "+selected.getSentDate().toString(), 5, y+155);
		
		} else if (selected.getSentDate()!=null){
			if (selected.getRecipients(Message.RecipientType.TO).length>0) {
				g.drawText("R: "+selected.getRecipients(Message.RecipientType.TO)[0].getName(), 5, y+55);
			}
			else {
				g.drawText("R: <no recipient>", 5, y+55);
			}
			g.drawText("D: "+getDate(selected.getReceivedDate().toString()), 5, y+155);
			if (index!=getIndex())Util.speak("Message has no subject.");
		}
		} catch (MessagingException e) {
			System.out.println("Unable to read message.");
			e.printStackTrace();
		}
		index=getIndex();
		g.setFont(old);
	}
	
	protected String getDate (String string) {
		return string.substring(string.indexOf(" ")+1, string.indexOf(":")+3);
	}
	
	protected boolean navigationClick(int status, int time) {
		if(getSize() > 0)
		{
				UiApplication.getUiApplication().pushScreen(new ReadScreen("Read message",((AMessage) get(this, getSelectedIndex())).getMessage()));
				invalidate();
		}
			
	
		return super.navigationClick(status, time);
	}
	
}