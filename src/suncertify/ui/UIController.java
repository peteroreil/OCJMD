package suncertify.ui;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

import suncertify.db.DBMain;
import suncertify.db.LocalDBConnector;
import suncertify.db.RecordNotFoundException;
import suncertify.db.Subcontractor;
import suncertify.rmi.RemoteDBConnector;
import suncertify.rmi.RemoteDBMain;


/**
 * UIController.java
 * The controller class in the MVC pattern.
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class UIController {

	private DBMain standaloneConnection;
	private RemoteDBMain remoteConnection; 
	private ApplicationMode mode;
	
	/**
	 * UIController constructor
	 * @param mode - <code>suncertify.ui.ApplicationMode</code> application is running in
	 * @param dbFile - the <code>String</code> location of database file
	 * @param host - the <code>String</code> hostname of server
	 * @param port - the <code>int</code> port of server
	 * @throws UIControllerException
	 */
	public UIController(ApplicationMode mode, String dbFile,
			String host, int port) throws UIControllerException{
		this.mode = mode;
		
		try {
			if (mode == ApplicationMode.CLIENT) {
				remoteConnection = RemoteDBConnector.getConnection(host, port);
			} else {
				standaloneConnection = LocalDBConnector.getConnection(dbFile);
			}
		} catch (ConnectException ce) {
			throw new UIControllerException("Unable to connect to server: "+host+" on port: " +
					port+".\nPlease Verify Server is Running");		
		} catch (Exception e) {
			throw new UIControllerException(e.getMessage());
		}
	}
	
	/**
	 * returns a SubcontractorTableModel of all records 
	 * @return <code>suncertify.ui.SubcontractorTableModel</code> 
	 */
	public SubcontractorTableModel getAllSubcontractors() {
		return searchSubcontractors("", "");
	}
	
	/**
	 * Returns a list of all matching records which match the search criteria.
	 * @see suncertify.db.Database#find(String[])
	 * @param name - <code>String</code> name of subcontractor to search
	 * @param location - <code>String</code> location to search
	 * @return <code>suncertify.ui.SubcontractorTableModel</code> of search results
	 */
	public SubcontractorTableModel searchSubcontractors(String name, String location) {
		SubcontractorTableModel resultsModel = new SubcontractorTableModel();
		if (name.length() == 0 && location.length() == 0) {
			name = null;
			location = null;
		}		
		
		String[] criteria = new String[] {name, location, "", "", "", ""};
		
		try {
			if (mode == ApplicationMode.CLIENT) {
				int[] recordNums = remoteConnection.find(criteria);
				
				for (Integer i : recordNums) {
					String[] subcontractor = remoteConnection.read(i);
					resultsModel.addSubcontractorRecord(subcontractor);
				}
			}
			
			if (mode == ApplicationMode.STANDALONE) {
				int[] recordNums = standaloneConnection.find(criteria);
				
				for (Integer i : recordNums) {
					String[] subcontractor = standaloneConnection.read(i);
					resultsModel.addSubcontractorRecord(subcontractor);
				}
			}
		} catch (Exception e) {
			throw new UIControllerException("Failed to obtain contractors" +
					e.getMessage());
		}
		
		return resultsModel;
	}

	/**
	 * Books the specified record and applies the provided customerID to
	 * the record. This method acquires a lock on the specified record, updates the
	 * customer field and releases the lock on the specified record, then returns a SubscriberTableModel
	 * of all records.
	 * @param record - <code>String[]</code> the record to book 
	 * @param customerID - the <code>String</code> customerID to apply to booking 
	 * @return <code>suncertify.ui.SubcontractorTableModel</code> all records
	 */
	public SubcontractorTableModel bookRecord(String[] record,
			String customerID) {
		String[] updatedRecord = new String[6];
		
		for (int i=0; i<record.length; i++) {
			updatedRecord[i] = record[i];
		}
		
		updatedRecord[5] = null;
		Subcontractor subcontractor = new Subcontractor(updatedRecord);
		
		try {			
			subcontractor.setCustomerId(customerID);
		} catch (IllegalArgumentException iae) {
			throw new UIControllerException("Error setting customer id.\nEnsure customer ID is numeric \nand 8 digits in length");
		}
		
		int recNo = (subcontractor.hashCode());
		
		if (mode == ApplicationMode.CLIENT) {
			try {
				remoteConnection.lock(recNo);
				remoteConnection.update(recNo, subcontractor.toArray());
			} catch (RecordNotFoundException e) {
				throw new UIControllerException("Record not found\n " +
						e.getMessage());
			} catch (RemoteException e) {
				throw new UIControllerException(e.getMessage());
			} finally {
				try {
					remoteConnection.unlock(recNo);
				} catch (RecordNotFoundException e) {
					throw new UIControllerException("Record not found\n " +
							e.getMessage());
				} catch (RemoteException e) {
					throw new UIControllerException(e.getMessage());
				}
			}
		} else if (mode == ApplicationMode.STANDALONE) {
			try {
				standaloneConnection.lock(recNo);
				standaloneConnection.update(recNo, subcontractor.toArray());
			} catch (RecordNotFoundException e) {
				throw new UIControllerException("Record not found\n " +
						e.getMessage());
			} finally {
				try {
					standaloneConnection.unlock(recNo);
				} catch (RecordNotFoundException e) {
					throw new UIControllerException("Record not found\n " +
							e.getMessage());
				}
			}
		}
				
		return getAllSubcontractors();
	}
	
}
