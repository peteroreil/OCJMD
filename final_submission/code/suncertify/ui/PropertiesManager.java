package suncertify.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * PropertiesManager.java
 * The PropertiesManager reads and writes all provided configuration details
 * to the suncertify.properties file.
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class PropertiesManager {

	public static final String CLIENT_SERVERHOST_PROP = "client.serverhost";
	public static final String CLIENT_SERVERPORT_PROP = "client.serverport";		
	public static final String STANDALONE_DBFILE_PROP = "standalone.databasefile";		
	public static final String SERVER_SERVERPORT_PROP = "server.serverport";
	public static final String SERVER_DBFILE_PROP = "server.databasefile";
	private static final String PROP_FILENAME = "suncertify.properties";
	private File propFile = null;
	private Properties properties = null;	
	
	/**
	 * PropertiesManager no-arg constructor.
	 * Creates a new properties file if none already exists
	 * and loads the properties file into a Properties object. 
	 */
	public PropertiesManager() {
		initializeFile();
		readProperties();
	}
	
	/*
	 * creates a new properties file if none exists
	 */
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

	/*
	 * initializes the instance Properties object
	 * and loads the properties file into the Properties
	 * object.
	 */
	private void readProperties() {
		this.properties = new Properties();
		
		try (FileInputStream fis = new FileInputStream(propFile)) {
			properties.load(fis);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Getter for named property,
	 * if none found returns an empty String
	 * @param name - the property name
	 * @return the String value of the specified property
	 */
	public String getProperty(String name) {
		return this.properties.getProperty(name, "");
	}
	
	/**
	 * Setter for named property.
	 * Sets the String value of a given property.
	 * @param name - the named property to set
	 * @param value - the value to set the property to
	 */
	public void setProperty(String name, String value) {
		this.properties.setProperty(name, value);
	}
	
	/**
	 * Writes the properties to the suncertify.properties file
	 */
	public void saveProperties() {
		try (FileOutputStream fos = new FileOutputStream(this.propFile)){
			this.properties.store(fos, "Properties From: "+ this.getClass().getName());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
