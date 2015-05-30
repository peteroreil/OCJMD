package suncertify.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

	private DBRemoteFactory factory;
	
	public Server(int port, String dbFile) throws RemoteException, AlreadyBoundException {
		Registry registry = LocateRegistry.createRegistry(port);		
		factory = new DBRemoteFactoryImpl(dbFile);
		registry.bind(DBRemoteFactory.REMOTE_FACTORY, factory);
	}
}
