package suncertify.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteDatabaseConnectionFactory extends Remote {
	public RemoteDatabaseConnection getConnection() throws RemoteException;
}

