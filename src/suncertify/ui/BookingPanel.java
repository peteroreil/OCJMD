package suncertify.ui;


import java.awt.FlowLayout;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class BookingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton reserveButton = new JButton("Book Contractor");
	
	public BookingPanel() {
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.reserveButton.addActionListener(new Reserve());
		this.add(reserveButton);
	}
	
	private class Reserve implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showInputDialog("Implement Booking Buttom");
		}		
	}

}
