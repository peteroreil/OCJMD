package suncertify.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel searchPanel;
	private JPanel bookingPanel;
	
	public ControlPanel() {
		super(new BorderLayout());
		this.searchPanel = new SearchPanel();
		this.bookingPanel = new BookingPanel();
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(bookingPanel, BorderLayout.SOUTH);
	}
	
}
