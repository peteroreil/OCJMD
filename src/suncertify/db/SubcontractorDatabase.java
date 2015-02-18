package suncertify.db;

/**
 * SubcontractorDatabase class. Facade to access data.
 * Hiding logic required to lock and perform crud and search
 * operations on the database.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0  
 */

public class SubcontractorDatabase implements DBMain{

	private static SubcontractorFileAccess database = null;
	private static DatabaseLocker dbLocker = new DatabaseLocker();
	
	/**
	 * No Arg SubcontractorDatabase. Calls one-arg constructor passing 
	 * current working directory.
	 * @throws DatabaseConnectionException 
	 */
	public SubcontractorDatabase() throws DatabaseConnectionException {
		this(System.getProperty("user.dir"));
	}
	
	/** 
	 * @param <b>String</b> dbPath: the absolute path to directory where the 
	 * db file is located.
	 * @throws DatabaseConnectionException
	 */
	public SubcontractorDatabase(String dbPath) throws DatabaseConnectionException {
		database = new SubcontractorFileAccess(dbPath);
	}

	/** 
	 * reads a record from the database.
	 *@return <b>String[]</b> containing fields of a record.
	 *@param <b>int</b> the record number to read.
	 *@throws RecordNotFoundException
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		return database.read(recNo);
	}
	

	/** 
	 * updates a record from the database.
	 *@param <b>int</b> the record number to update.
	 *@param <b>String[]</b> the updated record.
	 *@throws RecordNotFoundException
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		database.update(recNo, data);		
	}

	
	/**
	 * deletes a record
	 * @param <b>int</b> recNo the record number to delete
	 * @throws RecordNotFoundException
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		database.delete(recNo);		
	}

	/**Returns an array of record numbers that match the specified
	 *criteria. Field n in the database file is described by
	 *criteria[n]. A null value in criteria[n] matches any field
	 *value. A non-null  value in criteria[n] matches any field
	 *value that begins with criteria[n]. (For example, "Fred"
	 *matches "Fred" or "Freddy".)searches for a record based on criteria
	 *@param <b>String[]</b> criteria the search criteria
	 *@return <b>int[]</b> array of record numbers of matching records
	 *@throws RecordNotFoundException if no matching record
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		return database.find(criteria);
	}

	/**
	 * creates a new record in the database
	 * @param <b>String[]</b> data, the record to create.
	 * @throws DuplicateKeyException if a record of the same key already 
	 * exists in the database
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException {
		return database.create(data);
	}

	/**
	 * locks the record number specified by the record number
	 * @param <b>int</b> recNo the record number to lock
	 * @throws RecordNotFoundException
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		dbLocker.lock(recNo);		
	}

	/**
	 * unlocks the specified record by record number
	 * @param <b>int</b> recNo the record number to unlock
	 * @throws RecordNotFoundException
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		dbLocker.unlock(recNo);
		
	}

	/**
	 * @param <b>int</b> the index to check if locked
	 * @return returns true if the record is locked, false otherwise
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		return dbLocker.isLocked(recNo);
	}
	
	
}
