package suncertify.db;

import java.util.List;

/**
 * Data class. Facade.
 * Hiding logic required to lock and perform crud and search
 * operations on the database.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0  
 */

public class Data implements DBMain{

	private Database database = null;
	private BookingManager bookingManager = null;
	
	public Data() throws DatabaseException {
		this(System.getProperty("user.dir"));
	}
	
	public Data(String databaseFilePath) throws DatabaseException {
		this.database = new Database(databaseFilePath);
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
		database.lock(recNo);
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		database.unlock(recNo);
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		return database.isLocked(recNo);
	}
	
	public boolean reserveSubcontractor(String contractor) {
		return bookingManager.reserveSubcontractor(contractor, this);
	}
	
	public void releaseSubcontractor(String contractor) {
		bookingManager.releaseSubcontractor(contractor, this);
	}
	
	public List<Subcontractor> getSubcontractors() {
		return database.getSubcontractors();
	}
		
}
