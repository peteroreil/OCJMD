package suncertify.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DBRemoteFactory extends Remote {
	public static final String REMOTE_FACTORY = "REMOTE_FACTORY";
	public DBRemote getDataConnection() throws RemoteException;
}
