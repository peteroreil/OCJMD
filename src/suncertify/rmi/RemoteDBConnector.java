package suncertify.rmi;

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
	 */
	public static DBRemote getConnection(String host, int port) {
		DBRemote remoteData = null;
		try {
			Registry reg = LocateRegistry.getRegistry(host, port);
			DBRemoteFactory factory = (DBRemoteFactory) reg.lookup(DBRemoteFactory.REMOTE_FACTORY);
			remoteData = (DBRemote) factory.getDataConnection();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return remoteData;
	}
}
