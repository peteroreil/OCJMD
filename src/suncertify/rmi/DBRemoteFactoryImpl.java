package suncertify.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class DBRemoteFactoryImpl extends UnicastRemoteObject implements DBRemoteFactory {

	private static final long serialVersionUID = 1L;
	private static String dbFile = null;
	
	public DBRemoteFactoryImpl(String dbFile) throws RemoteException {
		DBRemoteFactoryImpl.dbFile = dbFile;
	}

	@Override
	public DBRemote getDataConnection() throws RemoteException {
		return new DBRemoteImpl(dbFile);
	}

}
