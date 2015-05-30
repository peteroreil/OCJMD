package suncertify.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.DBMain;
import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;


public class DBRemoteImpl extends UnicastRemoteObject implements DBRemote  {

	private static final long serialVersionUID = 1L;

	private DBMain db = null;
	
	protected DBRemoteImpl(String dbFile) 
			throws RemoteException, RemoteException {
		super();
		this.db = new Data(dbFile);
	}

	@Override
	public String[] read(int recNo) 
			throws RecordNotFoundException, RemoteException {
		return db.read(recNo);
	}

	@Override
	public void update(int recNo, String[] data) 
			throws RecordNotFoundException, RemoteException {
		db.update(recNo, data);		
	}

	@Override
	public void delete(int recNo) 
			throws RecordNotFoundException, RemoteException {
		db.delete(recNo);
	}

	@Override
	public int[] find(String[] criteria) 
			throws RecordNotFoundException, RemoteException {
		return db.find(criteria);
	}

	@Override
	public int create(String[] data) 
			throws DuplicateKeyException, RemoteException {
		return db.create(data);
	}

	@Override
	public void lock(int recNo) 
			throws RecordNotFoundException, RemoteException {
		db.lock(recNo);
	}

	@Override
	public void unlock(int recNo) 
			throws RecordNotFoundException, RemoteException {
		db.unlock(recNo);
	}

	@Override
	public boolean isLocked(int recNo) 
			throws RecordNotFoundException, RemoteException {
		return db.isLocked(recNo);
	}

}
