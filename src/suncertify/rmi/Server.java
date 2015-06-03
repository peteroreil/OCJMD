package suncertify.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server.java
 * @author Peter O'Reilly
 * @version 1.0.0 
 */
public class Server {

	private DBRemoteFactory factory;
	
	/**
	 * Server constructor.
	 * Creates and instance of <code>java.rmi.registry.Registry</code>
	 * on the given port number and exports and binds an instance of the 
	 * <code>DBRemoteFactoryImpl</code>.
	 * @param port - the port the Registry will listen on.
	 * @param dbFile - the absolute path to the database file
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public Server(int port, String dbFile) throws RemoteException, AlreadyBoundException {
		Registry registry = LocateRegistry.createRegistry(port);		
		factory = new DBRemoteFactoryImpl(dbFile);
		registry.bind(DBRemoteFactory.REMOTE_FACTORY, factory);
	}
}
