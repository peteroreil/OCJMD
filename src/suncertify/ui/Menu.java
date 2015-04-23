/**
 * 
 */
package suncertify.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Menu extends JMenuBar {
	
	private static final long serialVersionUID = 1L;
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem exitMenuItem = new JMenuItem("Exit");
	
	public Menu() {
		this.exitMenuItem.addActionListener(new Quit());
		this.fileMenu.add(this.exitMenuItem);
		this.add(fileMenu);
	}	
	
	private class Quit implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int answer = JOptionPane.showConfirmDialog(null,
					"are you sure you wish to quit?", "Exit",  
					JOptionPane.YES_NO_OPTION);
			if (answer == 0) {
				System.exit(0);		
			}
		}	
	}
}



