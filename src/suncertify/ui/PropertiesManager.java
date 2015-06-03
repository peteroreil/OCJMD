package suncertify.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertiesManager {

	public static final String CLIENT_SERVERHOST_PROP = "client.serverhost";
	public static final String CLIENT_SERVERPORT_PROP = "client.serverport";		
	public static final String STANDALONE_DBFILE_PROP = "standalone.databasefile";		
	public static final String SERVER_SERVERPORT_PROP = "server.serverport";
	public static final String SERVER_DBFILE_PROP = "server.databasefile";
	private static final String PROP_FILENAME = "suncertify.properties";
	private File propFile = null;
	private Properties properties = null;	
	
	public PropertiesManager() {
		initializeFile();
		readProperties();
	}
	
	private void initializeFile() {
		this.propFile = new File(PROP_FILENAME);
		
		if (!this.propFile.exists()) {
			try {
				this.propFile.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	private void readProperties() {
		this.properties = new Properties();
		
		try (FileInputStream fis = new FileInputStream(propFile)) {
			properties.load(fis);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public String getProperty(String name) {
		return this.properties.getProperty(name, "");
	}
	
	public void setProperty(String name, String value) {
		this.properties.setProperty(name, value);
	}
	
	public void saveProperties() {
		try (FileOutputStream fos = new FileOutputStream(this.propFile)){
			this.properties.store(fos, "Properties From: "+ this.getClass().getName());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
