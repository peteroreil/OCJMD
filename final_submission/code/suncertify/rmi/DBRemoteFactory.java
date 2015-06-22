package suncertify.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * DBRemoteFactory.java
 * DBRemoteFactory Interface defines method to implement the 
 * factory pattern to return an instance of the DBRemote.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public interface DBRemoteFactory extends Remote {
	
	public static final String REMOTE_FACTORY = "REMOTE_FACTORY";
	
	public DBRemote getDataConnection() throws RemoteException;
}
