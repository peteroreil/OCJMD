package suncertify.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.main.Runner;
import suncertify.rmi.Server;


public class ServerWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private ConfigurationDialog config;
	private JButton exitButton;
	private JTextField databaseTextField;
	private JTextField portNumberTextField;
	
	public ServerWindow(ApplicationMode mode) {
		super("Bodgit & Scarper Server");
		this.config = new ConfigurationDialog(this, mode);
		startServer();
	}
	
	private void startServer() {
		int port = config.getServerPort();
		String dbFile = config.getDBFilePath();
		
		try {
			new Server(port, dbFile);
			initializeFrame();
			initializeUI(dbFile, String.valueOf(port));	
		} catch (RemoteException e) {
			Runner.displayException(e.getMessage());
		} catch (AlreadyBoundException e) {
			Runner.displayException(e.getMessage());
		}
	}
	
	private void initializeFrame() {
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
	private void initializeUI(String dbFile, String port) {
		this.setSize(500, 200);
		this.setLocationRelativeTo(null);
		addMenuBar();
		this.add(new ServerScreen(dbFile, port));
		this.setVisible(true);
	}
	
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}			
		});
		
		this.setJMenuBar(menuBar);
	}

	private class ServerScreen extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public ServerScreen(String dbFile, String port) {

			initializeUIElements(dbFile, port);        
			layoutElements();
			addActionListeners();
		}

		private void layoutElements() {
			GridBagLayout layout = new GridBagLayout();
			this.setLayout(layout);
			
			GridBagConstraints constraints = new GridBagConstraints();
	        constraints.gridx = 0;
	        constraints.gridy = 0;
	        constraints.gridwidth = 2;
	        constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.BOTH;
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.weightx = 1.0;
	        constraints.weighty = 1.0;
	        constraints.insets = new Insets(5, 5, 5, 5);	        
	        this.add(new JLabel("Server Running"), constraints);
	        
	        constraints = new GridBagConstraints();
	        constraints.gridx = 0;
	        constraints.gridy = 2;
	        constraints.gridwidth = 1;
	        constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.NONE;
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(5, 5, 0, 0);
	        this.add(new JLabel("Server Host"),  constraints);
	        
	        constraints = new GridBagConstraints();
	        constraints.gridx = 1;
	        constraints.gridy = 2;
	        constraints.gridwidth = 1;
	        constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.BOTH;
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.weightx = 1.0;
	        constraints.insets = new Insets(5, 5, 0, 10);
	        this.add(databaseTextField, constraints);
	        
	        constraints = new GridBagConstraints();
	        constraints.gridx = 0;
	        constraints.gridy = 3;
	        constraints.gridwidth = 1;
	        constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.NONE;
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(5, 5, 10, 0);
	        this.add(new JLabel("Port Number"),  constraints);
	        
	        constraints = new GridBagConstraints();
	        constraints.gridx = 1;
	        constraints.gridy = 3;
	        constraints.gridwidth = 1;
	        constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.BOTH;
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.weightx = 1.0;
	        constraints.insets = new Insets(5, 5, 10, 10);
	        this.add(portNumberTextField, constraints);
	        
	        constraints = new GridBagConstraints();
	        constraints.gridx = 0;
	        constraints.gridy = 4;
	        constraints.gridwidth = 3;
	        constraints.gridheight = 1;
	        constraints.fill = GridBagConstraints.NONE;
	        constraints.anchor = GridBagConstraints.EAST;
	        constraints.weightx = 1.0;
	        constraints.insets = new Insets(10, 5, 10, 10);
	        this.add(exitButton, constraints);
		}

		private void initializeUIElements(String dbFile, String port) {
			exitButton = new JButton("Exit");
			databaseTextField = new JTextField(dbFile);
	        portNumberTextField = new JTextField(port);
		}
	}

	public void addActionListeners() {
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}});		
	}
}
