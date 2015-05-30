/**
 * 
 */
package suncertify.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteDBConnector {

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
