package suncertify.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel tablePanel;
	private JPanel controlPanel;
	private JMenuBar menu;
	
	public MainFrame() {
		super("Bodgit & Scarper");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.tablePanel = new TablePanel();
		this.controlPanel = new ControlPanel();
		this.menu = new Menu();
		
		this.add(this.tablePanel, BorderLayout.CENTER);
		this.add(this.controlPanel, BorderLayout.SOUTH);
		this.setJMenuBar(menu);
		
		this.setSize(800, 400);
		this.setVisible(true);		
	}
}
