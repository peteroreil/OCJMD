package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import suncertify.main.Runner;

/**
 * ClientWindow.java
 * The main client window for the Bodgit & Scarper application
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class ClientWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private ConfigurationDialog config;
	private UIController controller;
	private ApplicationMode mode;
	private SubcontractorTableModel tableModel;
	private JTable subcontractorTable = new JTable();
	private JTextField searchNameField = new JTextField(20);
	private JTextField searchLocationField = new JTextField(20);
	private JLabel searchNameLabel = new JLabel("Name: ");
	private JLabel searchLocationLabel = new JLabel("Location: ");
	
	/**
	 * Creates an instance of <code>ConfigurationDialog</code> and then launches the main 
	 * window after retrieving the required configuraiton information.
	 * @param mode - the client mode that the client application will run in
	 */
	public ClientWindow(ApplicationMode mode) {
		super("Bodgit & Scarper");
		initializeFrame();
		this.mode = mode;
		this.config = new ConfigurationDialog(this, mode);		
		initializeController();
		initializeUI();						
	}
	
	/*
	 * Initializes and assigns the Clientwindow's instance of the controller
	 * of the MVC design pattern.
	 * Displays a JOptionPane to the user in event of exception
	 */
	private void initializeController() {
		try {
			this.controller = new UIController(mode,
					  config.getDBFilePath(),
					  config.getHostname(),
					  config.getServerPort());
		} catch (UIControllerException uice) {
			Runner.displayException(uice.getMessage());
		}
		
	}

	/*
	 * Sets location and default exit 
	 */
	private void initializeFrame() {
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}

	/*
	 * Creates all UI Elements
	 * Creates the JMenuBar
	 * Creates the AbstractTableModel
	 * Adds the AbstractTableModel to the JTable
	 * Creates all Panels and adds to the ClientWindow 
	 */
	private void initializeUI() {
		this.setSize(800, 400);
		this.setLocationRelativeTo(null);
		addMenuBar();
		getTableModel();
		setTableModel();
		this.add(new MainScreen());
		configureJTable();
		this.setVisible(true);
	}
	
	/*
	 * get an instance of AbstractTableModel from the controller
	 * which contains all Subcontractor Records
	 */
	private void getTableModel() {
		try {	
			this.tableModel = controller.getAllSubcontractors();
		} catch (UIControllerException uice) {
			Runner.displayException(uice.getMessage());
		}
	}

	/*
	 * Set JTable Properties
	 */
	private void configureJTable() {
		subcontractorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		subcontractorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);	
	}

	private void setTableModel() {
		this.subcontractorTable.setModel(tableModel);
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
	
	
	/**
	 * MainScreen.java
	 * Private inner class
     * The main panel for the <code>ClientWindow</code> class application
     * 
	 * @author Peter O'Reilly
	 * @version 1.0.0
	 */
	private class MainScreen extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private JPanel searchPanel;
		private JPanel bookingPanel;
		private JPanel containerPanel;
		
		/**
		 * Creates an instance ofthe main panel for the 
		 * client application. Creates the JScrollPane.
		 * Creates a wrapper panel that contains both
		 * a search fields panel and booking panel and adds 
		 * them to the MainScreen instance.
		 */
		public MainScreen() {
			this.setLayout(new BorderLayout());
			addScrollPane();
			createSearchPanel();
			createBookingPanel();
			createContainerPanel();
		}

		/**
		 * Creates a <code>JPanel</code> <b>containerPanel</b> and
		 * adds both the <b>searchPanel</b> and <b>bookingPanel</b>
		 * to it.
		 */
		private void createContainerPanel() {
			containerPanel = new JPanel(new BorderLayout());
			containerPanel.add(searchPanel, BorderLayout.NORTH);
			containerPanel.add(bookingPanel, BorderLayout.SOUTH);
			this.add(containerPanel, BorderLayout.SOUTH);
		}

		/**
		 * creates the <code>JPanel</code> <b>bookingPanel</b>.
		 * The bookingPanel contains a single <code>JButton</code>
		 * This method adds the button and <code>ActionListener</code>
		 * which launches a <code>JOptionPane</code> to prompt for
		 * the 8-digit customer ID. This customer ID is then passed to
		 * the controller which attempts to book the record.
		 */
		private void createBookingPanel() {
			JButton bookButton = new JButton("Book");
            bookingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bookingPanel.add(bookButton);
            
            bookButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					int selectedRecord = subcontractorTable.getSelectedRow();
					
					if (selectedRecord < 0) {
						JOptionPane.showMessageDialog(null, "Please select a record to book");
					} else {
						String customerID = JOptionPane.showInputDialog(null, "Please enter 8 digit customer ID",
									"Booking", JOptionPane.OK_CANCEL_OPTION);
					
						int columns = subcontractorTable.getColumnCount();
						String[] record = new String[columns];
						for (int i = 0; i < columns ; i++) {
							record[i] = (String) subcontractorTable.getValueAt(selectedRecord, i);
						}
						
						try {
							tableModel = controller.bookRecord(record, customerID);
							setTableModel();
						} catch (UIControllerException uice) {
							Runner.displayException(uice.getMessage());
						}
					}
				}
            	
            });
		}
		
		/**
		 * This creates a <code>JPanel</code> and adds two <code>JTextField</code>'s.
		 * One for name search field and one for the location search field.
		 * A <code>JButton</code> is added for the search button and adds an 
		 * <code>ActionListener</code> which calls the 
		 * <code>searchSubcontractors(String[] criteria)</code> method of the 
		 * <code>UIController</code> class.
		 */
		private void createSearchPanel() {			
            JButton searchButton = new JButton("Search");
            searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            searchPanel.add(searchNameLabel);
            searchPanel.add(searchNameField);
            searchPanel.add(searchLocationLabel);
            searchPanel.add(searchLocationField);
            searchPanel.add(searchButton);	
            
            searchButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					String name = searchNameField.getText();
					String location = searchLocationField.getText();
					tableModel = controller.searchSubcontractors(name, location);
					setTableModel();
					searchNameField.setText("");
					searchLocationField.setText("");
				}});
		}

		/*
		 * Creates a JScrollPane and adds the subcontractorTable
		 * (JTable) to it.
		 */
		private void addScrollPane() {
			JScrollPane scroll = new JScrollPane(subcontractorTable);
			scroll.setSize(600, 350);
			this.add(scroll, BorderLayout.CENTER);			
		}
	}
}
