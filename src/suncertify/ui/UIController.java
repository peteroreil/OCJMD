package suncertify.ui;

import java.rmi.RemoteException;

import suncertify.db.DBMain;
import suncertify.db.LocalDBConnector;
import suncertify.db.RecordNotFoundException;
import suncertify.db.Subcontractor;
import suncertify.rmi.RemoteDBConnector;
import suncertify.rmi.RemoteDBMain;


public class UIController {

	private DBMain standaloneConnection;
	private RemoteDBMain remoteConnection; 
	private ApplicationMode mode;
	
	public UIController(ApplicationMode mode, String dbFile,
			String host, int port) throws UIControllerException{
		this.mode = mode;
		
		try {
			if (mode == ApplicationMode.CLIENT) {
				remoteConnection = RemoteDBConnector.getConnection(host, port);
			} else {
				standaloneConnection = LocalDBConnector.getConnection(dbFile);
			}
		} catch (Exception e) {
			throw new UIControllerException(e.getMessage());
		}
	}
	
	public SubcontractorTableModel getAllSubcontractors() {
		return searchSubcontractors("", "");
	}
	
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
