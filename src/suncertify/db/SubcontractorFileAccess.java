package suncertify.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SubcontractorFileAccess {

	private static final String DATABASE_NAME = "db-2x1.db";
	private static RandomAccessFile database = null;
	private static Map<String, Long> recordNumbers = new HashMap<String, Long>();
	private static ReadWriteLock recordNumbersLock = new ReentrantReadWriteLock();
	private static String recordPlaceHolder = null;
	private static String dbPath = null;
	
	static {
		recordPlaceHolder = new String(new byte[Subcontractor.RECORD_LENGTH]);
	}
	
	public SubcontractorFileAccess(String databasePath) throws DatabaseConnectionException {
		if(dbPath == null) {
			try {
				database = new RandomAccessFile(dbPath, "rw");
				dbPath = databasePath+File.separator+DATABASE_NAME;
				this.getSubcontractorList(true);
			} catch (FileNotFoundException e) {
				throw new DatabaseConnectionException("Database File cannot be found");
			}			
		}		
	}

	/**
	 * @param b
	 */
	private void getSubcontractorList(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public String[] read(int recNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(int recNo, String[] data) {
		// TODO Auto-generated method stub
		
	}

	public void delete(int recNo) {
		// TODO Auto-generated method stub
		
	}

	public int[] find(String[] criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	public int create(String[] data) {
		// TODO Auto-generated method stub
		return 0;
	}

}
