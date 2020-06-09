

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;

import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class Simulator_Window {

	private Controller ctr;
	private JFrame frame;
	private JTable tblCodeAusgabe;
	protected DefaultTableModel tblCode;
	
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
		frame.setBounds(100, 100, 1130, 677);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Simulator_WindowInst.frame.setVisible(true);

		
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
		frame.getContentPane().setLayout(null);
		
		JPanel panelCodeAusgabe = new JPanel();
		panelCodeAusgabe.setBorder(new LineBorder(new Color(0,0,0),2));
		panelCodeAusgabe.setBounds(263, 199, 837, 372);
		panelCodeAusgabe.setLayout(null);
		frame.getContentPane().add(panelCodeAusgabe);
		
		JScrollPane spCodeAusgabe = new JScrollPane();
		spCodeAusgabe.setBounds(0, 0, 837, 372);
		panelCodeAusgabe.add(spCodeAusgabe);		
		
		
		tblCode = new DefaultTableModel();
		tblCode.setColumnIdentifiers(new Object[] {"", "PC", "Code", "Linecount","Label", "Mnem Code" });
		tblCodeAusgabe = new JTable();
		tblCodeAusgabe.setBounds(0, 0, 837, 372);
		tblCodeAusgabe.setEnabled(false);
		tblCodeAusgabe.setModel(tblCode);
		spCodeAusgabe.setViewportView(tblCodeAusgabe);
		for(int i = 0; i < 5; i++)
		{
		 TableColumn column = tblCodeAusgabe.getColumnModel().getColumn(i);
		 if(i == 0)
		 {
			 column.setPreferredWidth(30);
			 column.setMaxWidth(30);
			 column.setMinWidth(30);
			 column.setResizable(false);
		 } 
		 else if(i > 0 && i <4)
		 {
			 column.setPreferredWidth(80);
			 column.setMaxWidth(80);
			 column.setMinWidth(80);
			 
		 } 
		 else if(i == 4)
		 {
			 column.setPreferredWidth(110);
			 column.setMaxWidth(110);
			 column.setResizable(false);
		 }
		 }
		

	}
}
