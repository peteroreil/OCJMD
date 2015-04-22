package suncertify.main;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import suncertify.network.Server;

public class Runner {
	public static void main(String ... args) throws RemoteException, AlreadyBoundException {
		new Server(2223);
	}
}
