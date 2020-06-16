

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
import java.awt.Font;


public class Simulator_Window {

	private Controller ctr;
	private JFrame frame;
	private JTable tblCodeAusgabe;
	protected DefaultTableModel tblCode;
	private JTable tblGPR;
	protected DefaultTableModel tblGprMdl;
	private JTable tblStack_1;
	protected DefaultTableModel tblStackMdl;
	private JTable tblStack;
	
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
	public void initialize(Simulator_Window Simulator_WindowInst) 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 1644, 1006);
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
		
		//Code Anzeige
		frame.getContentPane().setLayout(null);
		
		JPanel panelCodeAusgabe = new JPanel();
		panelCodeAusgabe.setBorder(new LineBorder(new Color(0,0,0),2));
		panelCodeAusgabe.setBounds(696, 274, 837, 372);
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

		JPanel panelControl = new JPanel();
		panelControl.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelControl.setBounds(1365, 11, 168, 157);
		frame.getContentPane().add(panelControl);
		panelControl.setLayout(null);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		ctr.start();
		}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnStart.setBounds(10, 10, 144, 42);
		panelControl.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		ctr.stop();
		}
		});
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnStop.setBounds(10, 60, 144, 42);
		panelControl.add(btnStop);
		
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
		
		//GPR Anzeige
		JPanel panelGPR = new JPanel();
		panelGPR.setBorder(new LineBorder(new Color(0,0,0),2));
		panelGPR.setBounds(10, 274, 557, 372);
		frame.getContentPane().add(panelGPR);
		panelGPR.setLayout(null);
		frame.getContentPane().add(panelGPR);

		JScrollPane spGPR = new JScrollPane();
		spGPR.setBounds(0, 0, 557, 372);
		panelGPR.add(spGPR);

		tblGprMdl = new DefaultTableModel();
		tblGprMdl.setColumnIdentifiers(new Object[] {"", "07", "06", "05", "04", "03", "02", "01", "00" });
		tblGPR = new JTable();
		tblGPR.setBounds(0, 0, 837, 372);
		tblGPR.setEnabled(false);
		tblGPR.setModel(tblGprMdl);
		spGPR.setViewportView(tblGPR);
		
		//Anzeige Stack
		JPanel panelStack = new JPanel();
		panelStack.setBorder(new LineBorder(new Color(0, 0, 0),2));
		panelStack.setBounds(1230, 11, 107, 157);
		frame.getContentPane().add(panelStack);
		panelStack.setLayout(null);
		frame.getContentPane().add(panelStack);
		
		JScrollPane spStack = new JScrollPane();
		spStack.setBounds(0, 0, 107, 157);
		panelStack.add(spStack);
				
		tblStackMdl = new DefaultTableModel();
		tblStackMdl.setColumnIdentifiers(new Object[] {"", "Inhalt" });
		tblStack = new JTable();
		tblStack.setBounds(0, 0, 107, 157);
		tblStack.setEnabled(false);
		tblStack.setModel(tblStackMdl);
		spStack.setViewportView(tblStack);	
		

	}
}
