package suncertify.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import suncertify.db.Subcontractor;


/**
 * SubcontractorTableModel.java
 * Implementation of the AbstractTableModel
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
public class SubcontractorTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Subscontractor Name", "Location",
			"Specialities", "Employees", "Rate", "Booked By"};		
	private List<String[]> subscontractorRecords = new ArrayList<String[]>();
	
	@Override
	public int getRowCount() {
		return subscontractorRecords.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String[] record = subscontractorRecords.get(rowIndex);
		return record[columnIndex];
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	/**
	 * adds a <code>suncertify.db.Subcontractor</code> to the subcontractorRecords
	 * ArrayList
	 * @param sub - the <code>suncertify.db.Subcontractor</code> to add
	 */
	public void addSubcontractorRecord(Subcontractor sub) {
		addSubcontractorRecord(sub.toArray());
	}
	
	/**
	 * adds a String[] to the subContractorRecords ArrayList
	 * @param sub - the String[] array to add
	 */
	public void addSubcontractorRecord(String[] sub) {
		this.subscontractorRecords.add(sub);
	}

}
