package suncertify.network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import suncertify.db.Data;
import suncertify.db.DatabaseException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.Subcontractor;

public class DatabaseConnection extends UnicastRemoteObject implements RemoteDatabaseConnection {
	
	private static final long serialVersionUID = 1L;
	private Data data;

	public DatabaseConnection() throws RemoteException {
		System.out.println("New Database being created ...");
		try {
			this.data = new Data();
		} catch (DatabaseException dbe){
			throw new RemoteException(dbe.getMessage());
		}
	}
	
	@Override
	public Subcontractor read(int recNo) throws RemoteException {
		String[] record = null;
		try {
			record = this.data.read(recNo);
			return new Subcontractor(record);
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException(rnfe.getMessage());
		}
	}

	@Override
	public void update(int recNo, Subcontractor subcontractor) 
			throws RemoteException {			
		try {
			this.data.update(recNo, subcontractor.toArray());
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException(rnfe.getMessage());
		}
	}

	@Override
	public void delete(int recNo) throws RemoteException  {
		try {
			this.data.delete(recNo);
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException();
		}		
	}

	@Override
	public List<Subcontractor> find(String[] criteria) throws RemoteException  {
		int[] records = null;
		List<Subcontractor> subcontractors = new ArrayList<Subcontractor>();
		try {
			records = this.data.find(criteria);
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException();
		}
		
		for (Integer i : records) {
			Subcontractor sub = read(i);
			subcontractors.add(sub);
		}
		return subcontractors;
	}

	@Override
	public int create(Subcontractor subcontractor) throws RemoteException  {
		try {
			return this.data.create(subcontractor.toArray());
		} catch (DuplicateKeyException dke) {
			throw new RemoteException(dke.getMessage());
		}		
	}

	@Override
	public void lock(int recNo) throws RemoteException  {
		try {
			this.data.lock(recNo);
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException(rnfe.getMessage());
		}
	}

	@Override
	public void unlock(int recNo) throws RemoteException  {
		try {
			this.data.unlock(recNo);
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException(rnfe.getMessage());
		}
	}

	@Override
	public boolean isLocked(int recNo) throws RemoteException {
		try {
			return this.data.isLocked(recNo);
		} catch (RecordNotFoundException rnfe) {
			throw new RemoteException(rnfe.getMessage());
		}
	}

}
