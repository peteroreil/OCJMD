/*
 * Runner.java
 * Main starting point for application
 * 
 */
package suncertify.main;

import javax.swing.JOptionPane;

import suncertify.ui.ApplicationMode;
import suncertify.ui.ClientWindow;
import suncertify.ui.ServerWindow;

/**
 * Runner.java
 * A facade for the three modes the application can be run in.
 * This class validates the CLI arguments required to start
 * the application and launches the application in either the Server 
 * or the specified Client mode.
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class Runner {
	
	private static ApplicationMode mode = null;
	
	/**
	 * Main method that launches the Bodgit & Scarper application
	 * @param args - the command line arguments. 
	 */
	public static void main(String ... args) {
		setApplicationMode(args);
		
		if (mode == ApplicationMode.SERVER) {
			new ServerWindow(mode);
		} else {
			new ClientWindow(mode);
		}		
	}

	/**
	 * Assigns the ApplicaitonMode enum after validating the
	 * the command line arguments. Sets the enum to either
	 * <ol>
	 * 	<li>ApplicationMode.SERVER</li>
	 * <li>ApplicationMode.STANDALONE</li>
	 * <li>ApplicationMode.CLIENT</li>
	 * </ol>
	 * 
	 * <b>ApplicationMode.SERVER</b> - will launch the RMI Server application.</br>
	 * <b>ApplicationMode.CLIENT</b> - will launch the Networked Client application.</br>
	 * <b>ApplicationMode.STANDALONE</b> - will launch the Standalone application.</br>
	 * </br>
	 * If incorrect arguments. It will print to console the correct usage and exit.
	 * @param args - the command line arguments to start the server.
	 */
	private static void setApplicationMode(String[] args) {
		String usage = "\nUSAGE: java -jar <filename> [mode]\n\n requires a single " +
				"or no (default) parameter.\n\nmode: \n\tServer: " + 
				"Runs in server mode\n\tAlone: Runs in standalone mode" + 
				"\n\tdefault: Runs as client in networked mode";
		
		if (args.length > 1) {
			System.out.println("Incorrect number of parameters passed");
			System.out.println(args.length + " parameters passed.");
			System.out.println(usage);
			System.exit(-1);
		}
		
		mode = null;
		
		if (args.length == 1) {
			String modeName = args[0].toLowerCase().trim();
			
			switch (modeName) {
				case "server" : mode = ApplicationMode.SERVER ; break;
				case "alone" : mode = ApplicationMode.STANDALONE ; break;
			}
			
			if (mode == null) {
				System.out.println("Incorrect argument passed. Must be 'Server'" +
						" or 'Alone'\n" + usage);
				System.exit(-1);
			}
		}
		
		if (args.length == 0) {
			mode = ApplicationMode.CLIENT;
		}
		
	}
	
	/**
	 * A Message dialog to display error messages to the user.
	 * @param message - the string error message to display to the user
	 */
	public static void displayException(String message) {
		JOptionPane.showMessageDialog(null, message,
				"Application Exception",
				JOptionPane.ERROR_MESSAGE);
	}
}
