

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;

import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;


public class Simulator_Window {

	private Controller ctr;
	private JFrame frame;
	
	public Simulator_Window() {
		ctr = new Controller(this);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					Simulator_Window Simulator_WindowInst = new Simulator_Window();
					Simulator_WindowInst.initialize(Simulator_WindowInst);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(Simulator_Window Simulator_WindowInst) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Simulator_WindowInst.frame.setVisible(true);
		
		//text Feld für Testausgabe ausgewähltes programm
		JTextPane txtPane_Auswahl = new JTextPane();
		frame.getContentPane().add(txtPane_Auswahl, BorderLayout.NORTH);
		
		//einfügen Menübar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//einbinden Button für File load in Menubar
		JButton btnLoad_File = new JButton("Load File");
		btnLoad_File.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					ctr.Einlesen();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
		});
		menuBar.add(btnLoad_File);
		

	}
	

}
