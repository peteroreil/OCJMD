package suncertify.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import suncertify.db.Subcontractor;


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
	
	public void addSubcontractorRecord(Subcontractor sub) {
		addSubcontractorRecord(sub.toArray());
	}
	
	public void addSubcontractorRecord(String[] sub) {
		this.subscontractorRecords.add(sub);
	}

}
