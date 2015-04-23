package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class TablePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JTable table = new JTable();
	private JScrollPane scroll = new JScrollPane(table);
	
	public TablePanel() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.RED);
		scroll.setSize(500, 250);
		
		this.add(scroll, BorderLayout.CENTER);
		addToTable();
	}
	
	private void addToTable() {
		DefaultTableModel model = new DefaultTableModel();	
		model.addColumn("one");
		model.addColumn("two");
		model.addColumn("three");
		model.addColumn("four");
		model.addColumn("five");
		model.addColumn("six");
		model.addColumn("seven");
		int x = 0;
		while(x < 50) {
			model.addRow(new Object[]{"val one", "val two", "val three", 
					"val four", "val five", "val six", "val seven"});
			x++;
		}
		table.setModel(model);
	}
	
}
