package suncertify.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * DBRemoteFactoryImpl.java
 * Implementation of the <code>DBRemoteFactory</code> interface
 * This class extends the <code>UnicastRemoteObject</code>
 *
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class DBRemoteFactoryImpl extends UnicastRemoteObject implements DBRemoteFactory {

	private static final long serialVersionUID = 1L;
	private static String dbFile = null;
	
	/**
	 * Factory constructor
	 * @param dbFile - the absolute path to the database file.
	 * @throws RemoteException
	 */
	public DBRemoteFactoryImpl(String dbFile) throws RemoteException {
		DBRemoteFactoryImpl.dbFile = dbFile;
	}

	/**
	 * Returns an instance of <code>DBRemoteImpl</code> 
	 * @return DBRemote 
	 * @throws RemoteException
	 */
	@Override
	public DBRemote getDataConnection() throws RemoteException {
		return new DBRemoteImpl(dbFile);
	}

}
