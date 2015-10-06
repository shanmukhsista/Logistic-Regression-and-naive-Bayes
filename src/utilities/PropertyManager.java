package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
	public Properties properties ;
	public PropertyManager(String fileName){
		try{
			properties = new Properties(); 
			//load the property file.
			InputStream inputProperties = new  FileInputStream(fileName + ".properties"); 
			properties.load(inputProperties);
		}
		catch(Exception e){
			System.out.println("Unable to load the config file. Please try again.");
			System.exit(-1);
		}
		
	}
	public String GetPropertyValue(String property){
		return properties.getProperty(property, ""); 
	}
}
