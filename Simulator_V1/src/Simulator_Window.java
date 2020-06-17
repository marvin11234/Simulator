

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
import javax.swing.JRadioButton;


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
	boolean cfStatus;
	boolean dcStatus;
	boolean zfStatus;
	JRadioButton rdbtnCF = new JRadioButton("Carry Flag");
	JRadioButton rdbtnDC = new JRadioButton("Digit Carry");
	JRadioButton rdbtnZ = new JRadioButton("Zero Flag");
	
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
		
		JPanel panelSFR = new JPanel();
		panelSFR.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelSFR.setBounds(1051, 11, 133, 252);
		frame.getContentPane().add(panelSFR);
		panelSFR.setLayout(null);
		
		
		
		rdbtnCF.setBounds(6, 7, 109, 23);
		panelSFR.add(rdbtnCF);
				

		rdbtnDC.setBounds(6, 33, 109, 23);
		panelSFR.add(rdbtnDC);

		rdbtnZ.setBounds(6, 59, 109, 23);
		panelSFR.add(rdbtnZ);
		
		JRadioButton rdbtnPD = new JRadioButton("Power-Down");
		rdbtnPD.setBounds(6, 85, 109, 23);
		panelSFR.add(rdbtnPD);
		
		JRadioButton rdbtnTO = new JRadioButton("Time-Out");
		rdbtnTO.setBounds(6, 111, 109, 23);
		panelSFR.add(rdbtnTO);
		
		JRadioButton rdbtnReg1 = new JRadioButton("RP0RP1");
		rdbtnReg1.setBounds(6, 137, 109, 23);
		panelSFR.add(rdbtnReg1);
		
		JRadioButton rdbtnReg2 = new JRadioButton("RP0RP2");
		rdbtnReg2.setBounds(6, 163, 109, 23);
		panelSFR.add(rdbtnReg2);
		
		JRadioButton rdbtnIPR = new JRadioButton("IPR");
		rdbtnIPR.setBounds(6, 189, 109, 23);
		panelSFR.add(rdbtnIPR);
		
		ActionListener alRdbtnC = new ActionListener() 
		{
		      public void actionPerformed(ActionEvent actionEvent) 
		      {
		    	  int value = 0;
			    	if(rdbtnCF.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("Carry = 1");
			    		ctr.SetCarry(value);
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("Carry = 0");
			    		ctr.SetCarry(value);
			    	}
		      }
		};
		
		ActionListener alRdbtnDC = new ActionListener() 
		{
		      public void actionPerformed(ActionEvent actionEvent) 
		      {
		    	  int value = 0;
		    	if(rdbtnDC.isSelected())
		    	{
		    		value = 1;
		    		System.out.println("DC = 1");
		    	}
		    	else
		    	{
		    		value = 0;
		    		System.out.println("DC = 0");
		    	}
		      }
		 };
		
		ActionListener alRdbtnZ = new ActionListener() 
		{
		      public void actionPerformed(ActionEvent actionEvent) 
		     {
		    	  int value = 0;
			    	if(rdbtnZ.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("Zero = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("Zero = 0");
			    	}
		     }
		};
					
		ActionListener alRdbtnPD = new ActionListener() 
		{
		      public void actionPerformed(ActionEvent actionEvent) 
		      {
		    	  int value = 0;
			    	if(rdbtnPD.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("Power Down = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("Power Down= 0");
			    	}
		      }
		};
		
		ActionListener alRdbtnTO = new ActionListener() 
		{
		      public void actionPerformed(ActionEvent actionEvent) 
		      {
		    	  int value = 0;
			    	if(rdbtnTO.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TO = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TO = 0");
			    	}
		      }
		};
							
		ActionListener alRdbtnReg1 = new ActionListener() 
		{
			int value = 0;
			public void actionPerformed(ActionEvent actionEvent) 
			{
		    	if(rdbtnReg1.isSelected())
		    	{
		    		value = 1;
		    		System.out.println("reg1 = 1");
		    	}
		    	else
		    	{
		    		value = 0;
		    		System.out.println("reg1 = 0");
		    	}
			}
		 };
		
		 ActionListener alRdbtnReg2 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnReg2.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("reg2= 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("reg2 = 0");
			    	}
		    }
		};
		
		 ActionListener alRdbtnIPR = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnIPR.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("IPR = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("IPR = 0");
			    	}
		    }
		};
		 
		 
		rdbtnCF.addActionListener(alRdbtnC); 
		rdbtnDC.addActionListener(alRdbtnDC);
		rdbtnZ.addActionListener(alRdbtnZ);
		rdbtnPD.addActionListener(alRdbtnPD);
		rdbtnTO.addActionListener(alRdbtnTO);
		rdbtnReg1.addActionListener(alRdbtnReg1);
		rdbtnReg2.addActionListener(alRdbtnReg2);
		rdbtnIPR.addActionListener(alRdbtnIPR);
		
		cfStatus = rdbtnCF.isSelected();
		dcStatus = rdbtnDC.isSelected();
		zfStatus = rdbtnZ.isSelected();
		
	}
	
	public void SetCFGui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnCF.setSelected(true);
		}
		else
		{
			rdbtnCF.setSelected(false);
		}
		
	}

	public void SetDCGui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnDC.setSelected(true);
		}
		else
		{
			rdbtnDC.setSelected(false);
		}
		
	}

	public void SetZFGui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnZ.setSelected(true);
		}
		else
		{
			rdbtnZ.setSelected(false);
		}
		
	}

	
}
