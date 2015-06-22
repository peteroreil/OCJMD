package suncertify.rmi;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RemoteDBConnector.java
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class RemoteDBConnector {

	/**
	 * This method obtains a stub of the DBRemoteFactory and 
	 * calls the remote getDataConnection method which returns
	 * an instance of the DBRemote Stub. 
	 * @param host - the host ip or fqdn of remote Registry
	 * @param port - the port the remote Registry is listening on 
	 * @return DBRemote - Stub of the <code>DBRemoteImpl</code>
	 * @throws ConnectException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static DBRemote getConnection(String host, int port) throws ConnectException, RemoteException, NotBoundException {
		Registry reg = LocateRegistry.getRegistry(host, port);
		DBRemoteFactory factory = (DBRemoteFactory) reg.lookup(DBRemoteFactory.REMOTE_FACTORY);
		DBRemote remoteData = (DBRemote) factory.getDataConnection();
		return remoteData;
	}
}
