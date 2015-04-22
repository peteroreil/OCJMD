package suncertify.network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DatabaseConnectionFactory extends UnicastRemoteObject 
											implements RemoteDatabaseConnectionFactory{

	private static final long serialVersionUID = 1L;

	protected DatabaseConnectionFactory() throws RemoteException {}

	@Override
	public DatabaseConnection getConnection() throws RemoteException {
		return new DatabaseConnection();
	}

}
