package suncertify.main;

import javax.swing.JOptionPane;

import suncertify.ui.ApplicationMode;
import suncertify.ui.MainWindow;

public class Runner {
	
	private static ApplicationMode mode = null;
	
	public static void main(String ... args) {
		setApplicationMode(args);
		new MainWindow(mode);
	}

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
	
	public static void displayException(String message) {
		JOptionPane.showMessageDialog(null, message,
				"Application Exception",
				JOptionPane.ERROR_MESSAGE);
	}
}
