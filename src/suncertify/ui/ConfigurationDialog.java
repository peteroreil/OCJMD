package suncertify.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.ui.ApplicationMode;


public class ConfigurationDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private JButton browseButton = new JButton("Browse");
	private JTextField dbFileTextField = new JTextField();
	private JTextField serverIPTextField = new JTextField();
	private JTextField serverPortTextField = new JTextField();
	private JLabel dbFileLabel = new JLabel("Database File");
	private JLabel serverIPLabel = new JLabel("Server Host");
	private JLabel serverPortLabel = new JLabel("Server port");
	private JLabel dbErrorLabel = new JLabel("* please select a valid database file");
	private JLabel portErrorLabel = new JLabel("* port must be integer");
	private JLabel hostnameErrorLabel = new JLabel("* must supply hostname");
	private PropertiesManager propManager;
	private ApplicationMode mode = null;
	
	public ConfigurationDialog(JFrame frame, ApplicationMode mode) {
		super(frame, "Configure Datasource");
		this.mode = mode;
		this.propManager = new PropertiesManager();
		this.setModal(true);
		this.setLocationRelativeTo(null);
		disableUIElements();
		loadConfiguration();
		initializeGUI();	
		addActionListeners();
		this.setVisible(true);
	}
	
	private void closeDialog() {
		this.setVisible(false);
		this.dispose();
	}

	private void loadConfiguration() {		
		if (mode == ApplicationMode.CLIENT) {
			String serverIPAddress = propManager
					.getProperty(PropertiesManager.CLIENT_SERVERHOST_PROP);
			String serverPortNumber = propManager
					.getProperty(PropertiesManager.CLIENT_SERVERPORT_PROP);
			
			this.serverIPTextField.setText(serverIPAddress);
			this.serverPortTextField.setText(serverPortNumber);
			
		} else if (mode == ApplicationMode.SERVER) {
			String dbFilePath = propManager
					.getProperty(PropertiesManager.SERVER_DBFILE_PROP);
			String hostPortNumber = propManager
					.getProperty(PropertiesManager.SERVER_SERVERPORT_PROP);
			
			this.dbFileTextField.setText(dbFilePath);
			this.serverPortTextField.setText(hostPortNumber);
		
		} else {
			// is standalone
			String dbFilePath = propManager
					.getProperty(PropertiesManager.STANDALONE_DBFILE_PROP);
			this.dbFileTextField.setText(dbFilePath);
		}
	}

	private void disableUIElements() {	
		portErrorLabel.setForeground(Color.RED);
		portErrorLabel.setVisible(false);
		
		dbErrorLabel.setForeground(Color.RED);
		dbErrorLabel.setVisible(false);
		
		hostnameErrorLabel.setForeground(Color.RED);
		hostnameErrorLabel.setVisible(false);
		
		if(mode == ApplicationMode.CLIENT) {
			browseButton.setEnabled(false);
			dbFileTextField.setEnabled(false);
			dbFileLabel.setEnabled(false);
			
		} else if (mode == ApplicationMode.SERVER) {
			serverIPTextField.setEnabled(false);
			serverIPLabel.setEnabled(false);
		} else {
			// is standalone mode
			serverIPTextField.setEnabled(false);
			serverIPLabel.setEnabled(false);
			serverPortTextField.setEnabled(false);
			serverPortLabel.setEnabled(false);
		}
	}


	private void initializeGUI() {
		this.setSize(400, 220);
		
		GridBagLayout layout = new GridBagLayout();
		this.getContentPane().setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(5, 5, 5, 5);
        
        String dialogMessage = "Please configure or confirm the following data source properties";
        this.getContentPane().add(new JLabel(dialogMessage), constraints);
		
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.insets = new Insets(5, 5, 5, 5);
        this.getContentPane().add(dbErrorLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 0, 0);
        this.getContentPane().add(dbFileLabel,  constraints);
        
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(5, 5, 0, 0);
        this.getContentPane().add(dbFileTextField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 0, 15);
        this.getContentPane().add(browseButton, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 0, 0);
        this.getContentPane().add(serverIPLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(5, 5, 0, 0);
        this.getContentPane().add(serverIPTextField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 0, 15);
        this.getContentPane().add(hostnameErrorLabel, constraints);
        
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 0, 0);
        this.getContentPane().add(serverPortLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(5, 5, 0, 0);
        this.getContentPane().add(serverPortTextField, constraints);
        
        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 0, 15);
        this.getContentPane().add(portErrorLabel, constraints);

        JPanel buttonPanel = new JPanel();
        GridLayout gl = new GridLayout(1, 2);
        gl.setHgap(20);
        buttonPanel.setLayout(gl);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(5, 25, 15, 25);
        this.getContentPane().add(buttonPanel, constraints);		
	}
	
	private boolean isFieldEmpty(JTextField field) {
		return (field.getText().length() == 0);
	}
	
	private boolean validateInputs() {
		if (mode == ApplicationMode.STANDALONE) {
			if (isFieldEmpty(dbFileTextField)) {
				dbErrorLabel.setVisible(true);
				return false;
			} 
			
		} else if (mode == ApplicationMode.CLIENT) {
			boolean isValid = true;
			if (isFieldEmpty(serverIPTextField)) {
				hostnameErrorLabel.setVisible(true);
				isValid = false;
			}
			
			if (isFieldEmpty(serverPortTextField)) {
				portErrorLabel.setVisible(true);
				isValid = false;
			}
			
			return isValid;
			
		} else if (mode == ApplicationMode.SERVER) {
			boolean isValid = true;
			
			if (isFieldEmpty(dbFileTextField)) {
				dbErrorLabel.setVisible(true);
				isValid = false;
			}
			
			if (isFieldEmpty(serverPortTextField)) {
				portErrorLabel.setVisible(true);
				isValid = false;
			}
			
			return isValid;
		}
		
		return true;
	}


	private void addActionListeners() {
		
		this.okButton.addActionListener(new ActionListener() {		
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetErrorMessages();
				boolean hasRequiredInputs = validateInputs();
				
				if (hasRequiredInputs) {
					if (mode == ApplicationMode.STANDALONE) {
						propManager.setProperty(PropertiesManager.STANDALONE_DBFILE_PROP,
								dbFileTextField.getText());
					} else {
						
						if (mode == ApplicationMode.CLIENT) {
							propManager.setProperty(PropertiesManager.CLIENT_SERVERHOST_PROP,
									serverIPTextField.getText());
							propManager.setProperty(PropertiesManager.CLIENT_SERVERPORT_PROP,
									serverPortTextField.getText());
						} else if (mode == ApplicationMode.SERVER) {
							propManager.setProperty(PropertiesManager.SERVER_DBFILE_PROP,
									dbFileTextField.getText());
							propManager.setProperty(PropertiesManager.SERVER_SERVERPORT_PROP,
									serverPortTextField.getText());
						}
					}				
					propManager.saveProperties();
					closeDialog();	
				}
			}		
		});
		
		this.cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);				
			}
			
		});
		
		this.browseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(new File("."));
				int exitCode = fc.showOpenDialog(null);
				
				if (exitCode == JFileChooser.APPROVE_OPTION) {
					dbFileTextField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
			
		});
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}


	protected void resetErrorMessages() {
		dbErrorLabel.setVisible(false);
		hostnameErrorLabel.setVisible(false);
		portErrorLabel.setVisible(false);
	}

	public int getServerPort() {
		String portNumber = serverPortTextField.getText();
		
		if (portNumber.length() > 0) {
			return Integer.parseInt(serverPortTextField.getText());
		}
		return 0;
	}
	
	public String getDBFilePath() {
		return dbFileTextField.getText();
	}
	
	public String getHostname() {
		return serverIPTextField.getText();
	}
}
