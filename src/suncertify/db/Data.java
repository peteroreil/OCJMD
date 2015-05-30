package suncertify.db;

/**
 * Data class. Facade.
 * Hiding logic required to lock and perform crud and search
 * operations on the database.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0  
 */

public class Data implements DBMain {

	private Database database = null;
	private LockManager lockManager = null;
	
	public Data() throws DatabaseException {
		this(System.getProperty("user.dir"));
	}
	
	public Data(String databaseFilePath) throws DatabaseException {
		this.database = new Database(databaseFilePath);
		this.lockManager = new LockManager();
	}
	
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		return database.read(recNo);
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		database.update(recNo, data);
	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		database.delete(recNo);
	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		return database.find(criteria);
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		return database.create(data);
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		lockManager.lock(recNo, this);
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		lockManager.unlock(recNo, this);
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		return lockManager.isLocked(recNo);
	}	
}
