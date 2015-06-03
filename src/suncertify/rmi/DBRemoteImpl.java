package suncertify.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.DBMain;
import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;


/**
 * DBRemoteImpl.java
 * This class implements the <code>DBRemote</code> interface an is of type 
 * <code>Remote</code> and <code>RemoteDBMain</code>. This class delegates the 
 * remote method invocations to the it's instance of <code>DBMain</code>
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class DBRemoteImpl extends UnicastRemoteObject implements DBRemote  {

	private static final long serialVersionUID = 1L;

	private DBMain db = null;
	
	/**
	 * 
	 * @param dbFile - the absolute path to the database file
	 * @throws RemoteException
	 * @throws DatabaseException
	 */
	protected DBRemoteImpl(String dbFile) 
			throws DatabaseException, RemoteException {
		super();
		this.db = new Data(dbFile);
	}
    
	/**
	 * @throws RemoteException
	 * @throws RecordNotFoundException
	 * @see suncertify.db.Data#read(int)
	 */
	@Override
	public String[] read(int recNo) 
			throws RecordNotFoundException, RemoteException {
		return db.read(recNo);
	}
    
	/**
	 * @throws RemoteException
	 * @throw RecordNotFoundException
	 * @see suncertify.db.Data#update(int, String[])
	 */
	@Override
	public void update(int recNo, String[] data) 
			throws RecordNotFoundException, RemoteException {
		db.update(recNo, data);		
	}

	/**
	 * @throws RemoteException
	 * @throw RecordNotFoundException
	 * @see suncertify.db.Data#delete(int)
	 */
	@Override
	public void delete(int recNo) 
			throws RecordNotFoundException, RemoteException {
		db.delete(recNo);
	}

	/**
	 * @throws RemoteException
	 * @throw RecordNotFoundException
	 * @see suncertify.db.Data#find(String[])
	 */
	@Override
	public int[] find(String[] criteria) 
			throws RecordNotFoundException, RemoteException {
		return db.find(criteria);
	}

	/**
	 * @throws DuplicateKeyException
	 * @throws RemoteException
	 * @see suncertify.db.Data#create(String[])
	 */
	@Override
	public int create(String[] data) 
			throws DuplicateKeyException, RemoteException {
		return db.create(data);
	}

	/**
	 * @throws RemoteException
	 * @throw RecordNotFoundException
	 * @see suncertify.db.Data#lock(int)
	 */
	@Override
	public void lock(int recNo) 
			throws RecordNotFoundException, RemoteException {
		db.lock(recNo);
	}

	/**
	 * @throws RemoteException
	 * @throw RecordNotFoundException
	 * @see suncertify.db.Data#unlock(int)
	 */
	@Override
	public void unlock(int recNo) 
			throws RecordNotFoundException, RemoteException {
		db.unlock(recNo);
	}

	/**
	 * @throws RemoteException
	 * @throw RecordNotFoundException
	 * @see suncertify.db.Data#isLocked(int)
	 */
	@Override
	public boolean isLocked(int recNo) 
			throws RecordNotFoundException, RemoteException {
		return db.isLocked(recNo);
	}

}
