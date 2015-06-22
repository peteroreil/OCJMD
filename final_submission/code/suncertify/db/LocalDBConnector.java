package suncertify.db;

/**
 * LocalDBConnector.java
 * Contains a single static method which creates a 
 * local, non-networked instance of the implementing
 * class of DBMain.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class LocalDBConnector {
	
	/**
	 * Creates an instance of type DBMain
	 * @param dbFile - Absolute path to the database file to use
	 * @return DBMain
	 */
	public static DBMain getConnection(String dbFile) {
		return new Data(dbFile);
	}
}
