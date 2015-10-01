package config;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import lib.utils.StringUtils;
import net.rim.device.api.io.LineReader;

public class Config {

	private static Vector configAttributes = new Vector();
	
	public static void print() {
		System.out.println("--- CFG DEBUG START ---");
		for(int i = 0;i<configAttributes.size();i++){
			ConfigAttribute attr = (ConfigAttribute)configAttributes.elementAt(i);
			System.out.println(attr.getAttribute()+" = "+attr.getValue());
		}
		System.out.println("--- CFG DEBUG END -----");
	}
	
	public static String get(String key) {
		for(int i = 0;i<configAttributes.size();i++){
			ConfigAttribute attr = (ConfigAttribute)configAttributes.elementAt(i);
			if(attr.getAttribute().equals(key))
				return attr.getValue();
		}
		return null;
	}
	
	public static void set(String key,String value) {
		for(int i = 0;i<configAttributes.size();i++){
			ConfigAttribute attr = (ConfigAttribute)configAttributes.elementAt(i);
			if(attr.getAttribute().equals(key)){
				if(!attr.getValue().equals(value))
					configAttributes.setElementAt(new ConfigAttribute(key,value), i);
				return;
			}
		}
		configAttributes.addElement(new ConfigAttribute(key,value));
	}
	
	public static void load() throws IOException {
		FileConnection config = (FileConnection)Connector.open("file:///store/home/user/config",  Connector.READ_WRITE);
		if(!config.exists())
			config.create();
		
		LineReader lr = new LineReader(config.openInputStream());
		configAttributes.removeAllElements();
		while(lr.lengthUnreadData() > 0){
			String[] parts = StringUtils.split(ConfigConstant.SEPARATOR, new String(lr.readLine()));
			configAttributes.addElement(new ConfigAttribute(parts[0],parts[1]));
		}
		config.close();
	}
	
	/**
	 * FIXME nechce fungovat, otestovat, vyresit
	 * @throws IOException
	 */
	public static void save() throws IOException {
		FileConnection config = (FileConnection)Connector.open("file:///store/home/user/config", Connector.READ_WRITE);
		config.truncate(0);
		PrintStream los = new PrintStream(config.openOutputStream());
		for(int i = 0;i<configAttributes.size();i++){
			ConfigAttribute attr = (ConfigAttribute)configAttributes.elementAt(i);
			los.println(attr.getAttribute()+ConfigConstant.SEPARATOR+attr.getValue());
		}
		los.flush();
		los.close();
	}
	
	public static interface ConfigConstant {
		public static final String SEPARATOR = "=";
		
		public static final String KEY_TUTORIAL = "show_tutorial";
		
		public static final String VALUE_TRUE = "true";
		public static final String VALUE_FALSE = "false";
	}
	
	public static class ConfigAttribute {
		private final String attribute;
		private final String value;
		
		public ConfigAttribute(String attribute, String value) {
			this.attribute = attribute;
			this.value = value;
		}
		
		public String getAttribute() {
			return attribute;
		}
		
		public String getValue() {
			return value;
		}
	}
}
