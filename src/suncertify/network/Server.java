package suncertify.network;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	public static final String REMOTE_FACTORY = "REMOTE_FACTORY";
	private RemoteDatabaseConnectionFactory factory;
	
	public Server(int port) throws RemoteException, AlreadyBoundException {
		Registry registry = LocateRegistry.createRegistry(port);
		factory = new DatabaseConnectionFactory();
		registry.bind(REMOTE_FACTORY, factory);
	}
}
