package suncertify.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import suncertify.db.Subcontractor;

public interface RemoteDatabaseConnection extends Remote {
	
	public Subcontractor read(int recNo) throws RemoteException;
	
	public void update(int recNo, Subcontractor sub) throws RemoteException;
	
	public void delete(int recNo) throws RemoteException;
	
	public List<Subcontractor> find(String[] criteria) throws RemoteException;
	
	public int create(Subcontractor subcontractor) throws RemoteException;
	
	public void lock(int recNo) throws RemoteException;
	
	public void unlock(int recNo) throws RemoteException;
	
	public boolean isLocked(int recNo) throws RemoteException;
}
