package suncertify.db;

import java.io.File;


/**
 * Data.java
 * Implements the DBMain Interface.
 * Implements the facade pattern delegating to the Database 
 * class for CRUD operations and to LockManager for logical locking of 
 * records.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0  
 */
public class Data implements DBMain {

	private Database database = null;
	private LockManager lockManager = null;
	
	/**
	 * Default constructor. Uses the User working directory
	 * using the default db-2x1.db file as the source datafile.
	 * @throws DatabaseException
	 */
	public Data() throws DatabaseException {
		this(System.getProperty("user.dir") + File.separator +"db-2x1.db");
	}
	
	/**
	 * Single arg constructor which take the string absolute path to the
	 * database file.
	 * @param databaseFilePath - absolute path to database file
	 * @throws DatabaseException
	 */
	public Data(String databaseFilePath) throws DatabaseException {
		this.database = new Database(databaseFilePath);
		this.lockManager = new LockManager();
	}
	
	/**
	 *Reads a record from the file. Returns an array where each
	 *element is a record value.
	 *@param recNo - the record number to read
	 *@return array of record values
	 *@throws RecordNotFoundException
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		return database.read(recNo);
	}

	/**
	 *Modifies the fields of a record. The new value for field n 
	 *appears in data[n].
	 *@param recNo - the record number to update
	 *@param data - array of values to update record
	 *@throws RecordNotFoundException
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		database.update(recNo, data);
	}

	/**
	 *Deletes a record, making the record number and associated disk
	 *storage available for reuse. 
	 *@param recNo - the record number to delete
	 *@throws RecordNotFoundException
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		database.delete(recNo);
	}

	/**
	 * Returns an array of record numbers that match the specified
	 *criteria. Field n in the database file is described by
	 *criteria[n]. A null value in criteria[n] matches any field
	 *value. A non-null  value in criteria[n] matches any field
	 *value that begins with criteria[n]. (For example, "Fred"
	 *matches "Fred" or "Freddy".)
	 *@param criteria - string array of search criteria. Requires a string
	 *array of length 6. 
	 *@throws RecordNotFoundException
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		return database.find(criteria);
	}

	/**
	 *Creates a new record in the database (possibly reusing a
	 *deleted entry). Inserts the given data, and returns the record
	 *number of the new record.
	 *@param data - string array of the new record to create
	 *@return the record number of the newly created record
	 *@throws DuplicateKeyException
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException {
		return database.create(data);
	}

	/**
	 *Locks a record so that it can only be updated or deleted by this client.
	 *If the specified record is already locked, the current thread gives up
	 *the CPU and consumes no CPU cycles until the record is unlocked.
	 *@param recNo - the record number to lock
	 *@throws RecordNotFoundException
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		lockManager.lock(recNo, this);
	}

	/**
	 * Releases the lock on a record.
	 * @param recNo - the record number to unlock
	 * @throw RecordNotFoundException
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		lockManager.unlock(recNo, this);
	}

	/**
	 *Determines if a record is currently locked. Returns true if the
	 *record is locked, false otherwise.
	 *@param recNo - the record number to check
	 *@return boolean true if is locked, false otherwise.
	 *@throws RecordNotFoundException
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		return lockManager.isLocked(recNo);
	}	
}
