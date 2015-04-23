package suncertify.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel searchLabel = new JLabel("Contractor Name");
	private JTextField searchField = new JTextField(30);
	private JButton searchButton = new JButton("Search");
	
	public SearchPanel() {
		super(new FlowLayout(FlowLayout.CENTER));
		this.searchButton.addActionListener(new Search());
		this.add(this.searchLabel);
		this.add(this.searchField);
		this.add(this.searchButton);
	}
	
	private class Search implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showInputDialog("Implement Search Buttom");
		}		
	}
}
