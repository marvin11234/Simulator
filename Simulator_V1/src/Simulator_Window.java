

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;

import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Stack;
import java.awt.event.ActionEvent;
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
	public JTable tblCodeAusgabe;
	protected DefaultTableModel tblCode;
	public JTable tblGPR;
	protected DefaultTableModel tblGprMdl;
	private JTable tblStack_1;
	protected DefaultTableModel tblStackMdl;
	public JTable tblStack;

	
	//SFR Pins die von aussen angesteuert werden
	JRadioButton rdbtnCF = new JRadioButton("Carry Flag");
	JRadioButton rdbtnDC = new JRadioButton("Digit Carry");
	JRadioButton rdbtnZ = new JRadioButton("Zero Flag");
	JRadioButton rdbtnReg1 = new JRadioButton("RP0/1");
	JRadioButton rdbtnPD = new JRadioButton("Power-Down");
	
	boolean cfStatus;
	boolean dcStatus;
	boolean zfStatus;
	boolean Reg1Status;
	
	boolean einzellschritt = false;
	
	boolean trisA0Status;
	boolean trisA1Status;
	boolean trisA2Status;
	boolean trisA3Status;
	boolean trisA4Status;
	

	
	boolean trisB0Status;
	boolean trisB1Status;
	boolean trisB2Status;
	boolean trisB3Status;
	boolean trisB4Status;
	boolean trisB5Status;
	boolean trisB6Status;
	boolean trisB7Status;
	
	
	//IO-Pins + Switch fuer In/Output RA
	JRadioButton rdbtnTRISA0 = new JRadioButton("TRIS A0");
	JRadioButton rdbtnTRISA4 = new JRadioButton("TRIS A1");
	JRadioButton rdbtnTRISA3 = new JRadioButton("TRIS A2");
	JRadioButton rdbtnTRISA2 = new JRadioButton("TRIS A3");
	JRadioButton rdbtnTRISA1 = new JRadioButton("TRIS A4");
	JRadioButton rdbtnRA4 = new JRadioButton("RA4");
	JRadioButton rdbtnRA3 = new JRadioButton("RA3");
	JRadioButton rdbtnRA2 = new JRadioButton("RA2");
	JRadioButton rdbtnRA1 = new JRadioButton("RA1");
	JRadioButton rdbtnRA0 = new JRadioButton("RA0");
	//IO-Pins + Switch fuer In/Output RB
	JRadioButton rdbtnTRISB0 = new JRadioButton("TRISB0");
	JRadioButton rdbtnTRISB1 = new JRadioButton("TRISB1");
	JRadioButton rdbtnTRISB2 = new JRadioButton("TRISB2");
	JRadioButton rdbtnTRISB3 = new JRadioButton("TRISB3");
	JRadioButton rdbtnTRISB4 = new JRadioButton("TRISB4");
	JRadioButton rdbtnTRISB5 = new JRadioButton("TRISB5");
	JRadioButton rdbtnTRISB6 = new JRadioButton("TRISB6");
	JRadioButton rdbtnTRISB7 = new JRadioButton("TRISB7");
	JRadioButton rdbtnRB0 = new JRadioButton("RB0");
	JRadioButton rdbtnRB1 = new JRadioButton("RB1");
	JRadioButton rdbtnRB2 = new JRadioButton("RB2");
	JRadioButton rdbtnRB3 = new JRadioButton("RB3");
	JRadioButton rdbtnRB4 = new JRadioButton("RB4");
	JRadioButton rdbtnRB5 = new JRadioButton("RB5");
	JRadioButton rdbtnRB6 = new JRadioButton("RB6");
	JRadioButton rdbtnRB7 = new JRadioButton("RB7");
	
	//Auswahl fÃ¼r die Laufzeitberechnung
	JRadioButton rdbtn500KHz = new JRadioButton("500 KHz");
	JRadioButton rdbtn1MHz = new JRadioButton("1 MHz");
	JRadioButton rdbtn2MHz = new JRadioButton("2 MHz");
	JRadioButton rdbtn3MHz = new JRadioButton("3 MHz");
	JRadioButton rdbtn4MHz = new JRadioButton("4 MHz");
	JLabel lblLfZt = new JLabel();
	JLabel lblWReg = new JLabel("W:");
	int takt = 4;
	
	
	//Konstruktor
	public Simulator_Window() {
		initialize();
		ctr = new Controller(this);
		ctr.initialize();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					Simulator_Window Simulator_WindowInst = new Simulator_Window();
					Simulator_WindowInst.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 1544, 861);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		
		//einfï¿½gen Menï¿½bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//einbinden Button fï¿½r File load in Menubar
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
		tblCodeAusgabe.addMouseListener(new java.awt.event.MouseAdapter()
				{
					@Override
					public void mouseClicked(java.awt.event.MouseEvent evt)
					{
					 int row = tblCodeAusgabe.rowAtPoint(evt.getPoint());
					 int coll = tblCodeAusgabe.columnAtPoint(evt.getPoint());
					
					 if(coll == 0)
					 {
						 ctr.SetBreakPoint(row);
						 //ctr.setBreakPoint(row);
					 }
					}
				});
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
		
		//Start & Stop Button
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
		
		JButton btnStep = new JButton("Step");
		btnStep.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnStep.setBounds(10, 110, 144, 42);
		panelControl.add(btnStep);
		
		btnStep.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent c) {
		if(einzellschritt == true)
		{
			einzellschritt = false;
			
		}
		else
		{
			einzellschritt = true;
		}
		}
		});
		
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
		tblGprMdl.setColumnIdentifiers(new Object[] {"", "0", "01", "02", "03", "04", "05", "06", "07" });
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
		
		//Anzeige SpecialFunctionRegister SFR
		JPanel panelSFR = new JPanel();
		panelSFR.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelSFR.setBounds(985, 13, 133, 217);
		frame.getContentPane().add(panelSFR);
		panelSFR.setLayout(null);
		
		rdbtnCF.setBounds(6, 7, 109, 23);
		panelSFR.add(rdbtnCF);
				

		rdbtnDC.setBounds(6, 33, 109, 23);
		panelSFR.add(rdbtnDC);

		rdbtnZ.setBounds(6, 59, 109, 23);
		panelSFR.add(rdbtnZ);
		
		
		rdbtnPD.setBounds(6, 85, 109, 23);
		panelSFR.add(rdbtnPD);
		
		JRadioButton rdbtnTO = new JRadioButton("Time-Out");
		rdbtnTO.setBounds(6, 111, 109, 23);
		panelSFR.add(rdbtnTO);
	
		
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
		Reg1Status = rdbtnReg1.isSelected();
		
		
		//Anzeige IO-Ports TRIS A
		JPanel panelTRISAIO = new JPanel();
		panelTRISAIO.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelTRISAIO.setBounds(10, 11, 253, 134);
		frame.getContentPane().add(panelTRISAIO);
		panelTRISAIO.setLayout(null);

		rdbtnRA0.setBounds(6, 7, 109, 23);
		panelTRISAIO.add(rdbtnRA0);

		rdbtnRA1.setBounds(6, 29, 109, 23);
		panelTRISAIO.add(rdbtnRA1);

		rdbtnRA2.setBounds(6, 53, 109, 23);
		panelTRISAIO.add(rdbtnRA2);

		rdbtnRA3.setBounds(6, 78, 109, 23);
		panelTRISAIO.add(rdbtnRA3);
		
		rdbtnRA4.setBounds(6, 104, 109, 23);
		panelTRISAIO.add(rdbtnRA4);
		
		rdbtnTRISA0.setBounds(136, 7, 109, 23);
		panelTRISAIO.add(rdbtnTRISA0);

		rdbtnTRISA1.setBounds(136, 104, 109, 23);
		panelTRISAIO.add(rdbtnTRISA1);

		rdbtnTRISA2.setBounds(136, 53, 109, 23);
		panelTRISAIO.add(rdbtnTRISA2);
		
		rdbtnTRISA3.setBounds(136, 78, 109, 23);
		panelTRISAIO.add(rdbtnTRISA3);
		
		rdbtnTRISA4.setBounds(136, 29, 109, 23);
		panelTRISAIO.add(rdbtnTRISA4);
		

		
		 ActionListener alRdbtnTRISA0 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISA0.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISA0 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISA0 = 0");
			    	}
		    }
		};
		
		 ActionListener alRdbtnTRISA1 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISA1.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISA1 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISA1 = 0");
			    	}
		    }
		};
		
		 ActionListener alRdbtnTRISA2 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISA2.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISA2 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISA2 = 0");
			    	}
		    }
		};
		
		 ActionListener alRdbtnTRISA3 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISA3.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISA3 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISA3 = 0");
			    	}
		    }
		};
		
		 ActionListener alRdbtnTRISA4 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISA4.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISA4 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISA4 = 0");
			    	}
		    }
		};
		
		 
		 ActionListener alRdbtnRA0 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRA0.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[5][0] = value;
			    		System.out.println("RA0 = 1");
			    	}
			    	else
			    	{
			    		value = 0;ctr.getMemo().dataMemoryIntArray[5][0] = value;
			    		System.out.println("RA0 = 0");
			    	}
		    }
		};
		
		 ActionListener alRdbtnRA1 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRA1.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[5][1] = value;
			    		System.out.println("RA1 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[5][1] = value;
			    		System.out.println("RA1 = 0");
			    	}
		    }
		};
		 
		 ActionListener alRdbtnRA2 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRA2.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[5][2] = value;
			    		System.out.println("RA2 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[5][2] = value;
			    		System.out.println("RA2 = 0");
			    	}
		    }
		};
		 
		 ActionListener alRdbtnRA3 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRA3.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[5][3] = value;
			    		System.out.println("RA3 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[5][3] = value;
			    		System.out.println("RA3 = 0");
			    	}
		    }
		};
		 
		 ActionListener alRdbtnRA4 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRA4.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[5][4] = value;
			    		System.out.println("RA4 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[5][4] = value;
			    		System.out.println("RA4 = 0");
			    	}
		    }
		};
		 
		rdbtnTRISA0.addActionListener(alRdbtnTRISA0);
		rdbtnTRISA1.addActionListener(alRdbtnTRISA1); 
		rdbtnTRISA2.addActionListener(alRdbtnTRISA2); 
		rdbtnTRISA3.addActionListener(alRdbtnTRISA3); 
		rdbtnTRISA4.addActionListener(alRdbtnTRISA4); 
		rdbtnRA0.addActionListener(alRdbtnRA0); 
		rdbtnRA1.addActionListener(alRdbtnRA1); 
		rdbtnRA2.addActionListener(alRdbtnRA2); 
		rdbtnRA3.addActionListener(alRdbtnRA3); 
		rdbtnRA4.addActionListener(alRdbtnRA4); 
		
		//Anzeige I/O-Ports TRIS B
		JPanel panelTRISBIO = new JPanel();
		panelTRISBIO.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelTRISBIO.setBounds(295, 11, 272, 252);
		frame.getContentPane().add(panelTRISBIO);
		panelTRISBIO.setLayout(null);
		
		
		rdbtnRB0.setBounds(6, 7, 109, 23);
		panelTRISBIO.add(rdbtnRB0);

		rdbtnRB1.setBounds(6, 33, 109, 23);
		panelTRISBIO.add(rdbtnRB1);

		rdbtnRB2.setBounds(6, 59, 109, 23);
		panelTRISBIO.add(rdbtnRB2);

		rdbtnRB3.setBounds(6, 85, 109, 23);
		panelTRISBIO.add(rdbtnRB3);

		rdbtnRB4.setBounds(6, 111, 109, 23);
		panelTRISBIO.add(rdbtnRB4);

		rdbtnRB5.setBounds(6, 137, 109, 23);
		panelTRISBIO.add(rdbtnRB5);

		rdbtnRB6.setBounds(6, 163, 109, 23);
		panelTRISBIO.add(rdbtnRB6);

		rdbtnRB7.setBounds(6, 189, 109, 23);
		panelTRISBIO.add(rdbtnRB7);
		
		
		rdbtnTRISB0.setBounds(157, 7, 109, 23);
		panelTRISBIO.add(rdbtnTRISB0);

		rdbtnTRISB1.setBounds(157, 33, 109, 23);
		panelTRISBIO.add(rdbtnTRISB1);

		rdbtnTRISB2.setBounds(157, 59, 109, 23);
		panelTRISBIO.add(rdbtnTRISB2);

		rdbtnTRISB3.setBounds(157, 85, 109, 23);
		panelTRISBIO.add(rdbtnTRISB3);

		rdbtnTRISB4.setBounds(157, 111, 109, 23);
		panelTRISBIO.add(rdbtnTRISB4);

		rdbtnTRISB5.setBounds(157, 137, 109, 23);
		panelTRISBIO.add(rdbtnTRISB5);

		rdbtnTRISB6.setBounds(157, 163, 109, 23);
		panelTRISBIO.add(rdbtnTRISB6);

		rdbtnTRISB7.setBounds(157, 196, 109, 23);
		panelTRISBIO.add(rdbtnTRISB7);
		
		
		 ActionListener alrdbtnRB0 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB0.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][0] = value;
			    		System.out.println("RB0 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][0] = value;
			    		System.out.println("RB0 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB1 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB1.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][1] = value;
			    		System.out.println("RB1 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][1] = value;
			    		System.out.println("RB1 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB2 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB2.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][2] = value;
			    		System.out.println("RB2 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][2] = value;
			    		System.out.println("RB2 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB3 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB3.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][3] = value;
			    		System.out.println("RB3 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][3] = value;
			    		System.out.println("RB3 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB4 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB4.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][4] = value;
			    		System.out.println("RB4 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][4] = value;
			    		System.out.println("RB4 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB5 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB5.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][5] = value;
			    		System.out.println("RB5 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][5] = value;
			    		System.out.println("RB5 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB6 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB6.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][6] = value;
			    		System.out.println("RB6 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][6] = value;
			    		System.out.println("RB6 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnRB7 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnRB7.isSelected())
			    	{
			    		value = 1;
			    		ctr.getMemo().dataMemoryIntArray[6][7] = value;
			    		System.out.println("RB7 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		ctr.getMemo().dataMemoryIntArray[6][7] = value;
			    		System.out.println("RB7 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB0 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB0.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB0 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB0 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB1 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB1.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB1 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB1 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB2 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB2.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB2 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB2 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB3 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB3.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB3 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB3 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB4 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB4.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB4 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB4 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB5 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB5.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB5 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB5 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB6 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB6.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB6 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB6 = 0");
			    	}
		    }
		};
		 ActionListener alrdbtnTRISB7 = new ActionListener() 
		 {
			 int value = 0;
			 public void actionPerformed(ActionEvent actionEvent) 
			{
			    	if(rdbtnTRISB7.isSelected())
			    	{
			    		value = 1;
			    		System.out.println("TRISB7 = 1");
			    	}
			    	else
			    	{
			    		value = 0;
			    		System.out.println("TRISB7 = 0");
			    	}
		    }
		};
		
		rdbtnTRISB0.addActionListener(alrdbtnTRISB0); 
		rdbtnTRISB1.addActionListener(alrdbtnTRISB1); 
		rdbtnTRISB2.addActionListener(alrdbtnTRISB2); 
		rdbtnTRISB3.addActionListener(alrdbtnTRISB3); 
		rdbtnTRISB4.addActionListener(alrdbtnTRISB4); 
		rdbtnTRISB5.addActionListener(alrdbtnTRISB5); 
		rdbtnTRISB6.addActionListener(alrdbtnTRISB6); 
		rdbtnTRISB7.addActionListener(alrdbtnTRISB7); 
		rdbtnRB0.addActionListener(alrdbtnRB0);
		rdbtnRB1.addActionListener(alrdbtnRB1);
		rdbtnRB2.addActionListener(alrdbtnRB2);
		rdbtnRB3.addActionListener(alrdbtnRB3);
		rdbtnRB4.addActionListener(alrdbtnRB4);
		rdbtnRB5.addActionListener(alrdbtnRB5);
		rdbtnRB6.addActionListener(alrdbtnRB6);
		rdbtnRB7.addActionListener(alrdbtnRB7);

		trisA0Status = rdbtnRA0.isSelected();
		trisA1Status = rdbtnRA1.isSelected();
		trisA2Status = rdbtnRA2.isSelected();
		trisA3Status = rdbtnRA3.isSelected();
		trisA4Status = rdbtnRA4.isSelected();
		
		trisB0Status = rdbtnRB0.isSelected();
		trisB1Status = rdbtnRB1.isSelected();
		trisB2Status = rdbtnRB2.isSelected();
		trisB3Status = rdbtnRB3.isSelected();
		trisB4Status = rdbtnRB4.isSelected();
		trisB5Status = rdbtnRB5.isSelected();
		trisB6Status = rdbtnRB6.isSelected();
		trisB7Status = rdbtnRB7.isSelected();
		
		//Anzeige Laufzeit
		JPanel panelLfZt = new JPanel();
		panelLfZt.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLfZt.setBounds(696, 11, 168, 219);
		frame.getContentPane().add(panelLfZt);
		panelLfZt.setLayout(null);


		lblLfZt.setBounds(23, 124, 118, 32);
		panelLfZt.add(lblLfZt);


		rdbtn500KHz.setBounds(26, 7, 109, 23);
		panelLfZt.add(rdbtn500KHz);

		rdbtn1MHz.setBounds(26, 27, 109, 23);
		panelLfZt.add(rdbtn1MHz);

		rdbtn2MHz.setBounds(26, 47, 109, 23);
		panelLfZt.add(rdbtn2MHz);

		rdbtn3MHz.setBounds(26, 67, 109, 23);
		panelLfZt.add(rdbtn3MHz);

		rdbtn4MHz.setBounds(26, 87, 109, 23);
		rdbtn4MHz.setSelected(true);
		panelLfZt.add(rdbtn4MHz);

		//Nur einer klickbar
		ButtonGroup laufZeitBg = new ButtonGroup();
		laufZeitBg.add(rdbtn500KHz);
		laufZeitBg.add(rdbtn1MHz);
		laufZeitBg.add(rdbtn2MHz);
		laufZeitBg.add(rdbtn3MHz);
		laufZeitBg.add(rdbtn4MHz);
		

		lblWReg.setBounds(57, 190, 56, 16);
		panelLfZt.add(lblWReg);
		lblWReg.setVisible(false);
		 ActionListener alRdbtn500KHz = new ActionListener() 
		 {
			 public void actionPerformed(ActionEvent actionEvent) 
			{
				 takt = 500;
				 System.out.println("500KHz");
		    }
		};
		 ActionListener alRdbtn1MHz = new ActionListener() 
		 {
			 public void actionPerformed(ActionEvent actionEvent) 
			{
				 takt = 1;
				 System.out.println("1mhz");
		    }
		};
		 ActionListener alRdbtn2MHz = new ActionListener() 
		 {
			 public void actionPerformed(ActionEvent actionEvent) 
			{
				 takt = 2;
				 System.out.println("2mhz");
		    } 
		};
		 ActionListener alRdbtn3MHz = new ActionListener() 
		 {
			 public void actionPerformed(ActionEvent actionEvent) 
			{
				 takt = 3;
				 System.out.println("3Mhz");
		    }
		};
		 ActionListener alRdbtn4MHz = new ActionListener() 
		 {
			 public void actionPerformed(ActionEvent actionEvent) 
			{
				 takt = 4;
				 System.out.println("4 Mhz");
		    }
		};

		rdbtn500KHz.addActionListener(alRdbtn500KHz);	
		rdbtn1MHz.addActionListener(alRdbtn1MHz);	
		rdbtn2MHz.addActionListener(alRdbtn2MHz);	
		rdbtn3MHz.addActionListener(alRdbtn3MHz);	
		rdbtn4MHz.addActionListener(alRdbtn4MHz);
		
	}
	
	
	
	public void SetCFGui(boolean flagValue)
	{
		if(flagValue == true)
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
	
	public void SetRP0Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnReg1.setSelected(true);
		}
		else
		{
			rdbtnReg1.setSelected(false);
		}
		
	}
	
	
	//TRIS A
	public void SetTRISABit0Gui(int flagValue)
	{
		if( flagValue == 1)
		{
			rdbtnTRISA0.setSelected(true);
		}
		else
		{
			rdbtnTRISA0.setSelected(false);
		}
		
	}
	public void SetTRISABit1Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISA1.setSelected(true);
		}
		else
		{
			rdbtnTRISA1.setSelected(false);
		}
		
	}
	public void SetTRISABit2Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISA2.setSelected(true);
		}
		else
		{
			rdbtnTRISA2.setSelected(false);
		}
		
	}
	
	public void SetTRISABit3Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISA3.setSelected(true);
		}
		else
		{
			rdbtnTRISA3.setSelected(false);
		}
		
	}
	public void SetTRISABit4Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISA4.setSelected(true);
		}
		else
		{
			rdbtnTRISA4.setSelected(false);
		}
		
	}
	
	//TRIS B
	public void SetTRISBBit0Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB0.setSelected(true);
		}
		else
		{
			rdbtnTRISB0.setSelected(false);
		}
		
	}
	public void SetTRISBBit1Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB1.setSelected(true);
		}
		else
		{
			rdbtnTRISB1.setSelected(false);
		}
		
	}
	public void SetTRISBBit2Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB2.setSelected(true);
		}
		else
		{
			rdbtnTRISB2.setSelected(false);
		}
		
	}
	public void SetTRISBBit3Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB3.setSelected(true);
		}
		else
		{
			rdbtnTRISB3.setSelected(false);
		}
		
	}
	public void SetTRISBBit4Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB4.setSelected(true);
		}
		else
		{
			rdbtnTRISB4.setSelected(false);
		}
		
	}
	public void SetTRISBBit5Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB5.setSelected(true);
		}
		else
		{
			rdbtnTRISB5.setSelected(false);
		}
		
	}
	public void SetTRISBBit6Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB6.setSelected(true);
		}
		else
		{
			rdbtnTRISB6.setSelected(false);
		}
		
	}
	public void SetTRISBBit7Gui(int flagValue)
	{
		if(flagValue == 1)
		{
			rdbtnTRISB7.setSelected(true);
		}
		else
		{
			rdbtnTRISB7.setSelected(false);
		}	
	}
	

	public void Befehlsmarkierung(int programmCounterInt)
	{
		String cellValue;
		int cellValueInt;
	
		for(int i = 0 ; i <= tblCodeAusgabe.getRowCount() -1; i++ )
		{
			if(tblCodeAusgabe.getValueAt(i, 1).equals("    "))
			{
			}
			else 
			{
				cellValue = String.valueOf((String) tblCodeAusgabe.getValueAt(i, 1));
				cellValueInt = Integer.parseInt(cellValue, 16);
				if( cellValueInt == programmCounterInt)
				{	
					tblCodeAusgabe.setRowSelectionInterval(i, i);
					tblCodeAusgabe.setSelectionBackground(Color.GREEN);
				}
				//System.out.println("cellValueInt: " + cellValueInt);
			}
		}
	}
	
	public void InitGprView()
	{
		for(int i = 0; i < 32; i++)
		{
			this.tblGprMdl.addRow(new Object[] {Integer.toHexString(i*8),"00","00","00","00","00","00","00","00"});
		}
	}
	
	public void updateGPR(int y, int x, int value)
	{
		this.tblGPR.getModel().setValueAt(Integer.toHexString(value), y , x+1);
	}
	

	//Check RB und sperre Ports
	public void updateTrisB(int [] GetTRISB)
	{
		if(GetTRISB[0] == 1)
		{

			ctr.getGui().rdbtnRB0.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB0.setEnabled(false);
		}
		
		if(GetTRISB[1] == 1)
		{

			ctr.getGui().rdbtnRB1.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB1.setEnabled(false);
		}
		
		if(GetTRISB[2] == 1)
		{

			ctr.getGui().rdbtnRB2.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB2.setEnabled(false);
		}
		if(GetTRISB[3] == 1)
		{

			ctr.getGui().rdbtnRB3.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB3.setEnabled(false);
		}
		if(GetTRISB[4] == 1)
		{

			ctr.getGui().rdbtnRB4.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB4.setEnabled(false);
		}
		if(GetTRISB[5] == 1)
		{

			ctr.getGui().rdbtnRB5.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB5.setEnabled(false);
		}
		if(GetTRISB[6] == 1)
		{

			ctr.getGui().rdbtnRB6.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB6.setEnabled(false);
		}
		if(GetTRISB[7] == 1)
		{

			ctr.getGui().rdbtnRB7.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRB7.setEnabled(false);
		}
	}

	//Check TRISA und sperre Ports
	public void updateTrisA(int [] GetTRISA)
	{
		if(GetTRISA[0] == 1)
		{

			ctr.getGui().rdbtnRA0.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRA0.setEnabled(false);
		}
		
		if(GetTRISA[1] == 1)
		{

			ctr.getGui().rdbtnRA1.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRA1.setEnabled(false);
		}
		
		if(GetTRISA[2] == 1)
		{

			ctr.getGui().rdbtnRA2.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRA2.setEnabled(false);
		}
		if(GetTRISA[3] == 1)
		{

			ctr.getGui().rdbtnRA3.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRA3.setEnabled(false);
		}
		if(GetTRISA[4] == 1)
		{

			ctr.getGui().rdbtnRA4.setEnabled(true);
		}
		else
		{
			ctr.getGui().rdbtnRA4.setEnabled(false);
		}
	}
	
	public void InitStackView()
	{
		System.out.println("Init Stack");
		for(int i = 0; i <= 7; i++)
		{
			tblStackMdl.addRow(new Object[] {i, 0});
		}
	}
	
	public void updateStack(int[] stack)
	{ 
		
		for(int i = 6; i >= 0; i--)
		{
			tblStackMdl.setValueAt(stack[i], 0, 1);
			
		}
	}
	
	public void updateFlags(int[] GetFlags)
	{
		if(GetFlags[0] == 1)
		{

			ctr.getGui().rdbtnCF.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnCF.setSelected(false);
		}
		
		if(GetFlags[1] == 1)
		{

			ctr.getGui().rdbtnDC.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnDC.setSelected(false);
		}
		
		if(GetFlags[2] == 1)
		{

			ctr.getGui().rdbtnZ.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnZ.setSelected(false);
		}
		if(GetFlags[5] == 1)
		{

			ctr.getGui().rdbtnReg1.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnReg1.setSelected(false);
		}
		
	}
	
	public void printLaufzeit(double laufzeit)
	{
		DecimalFormat dfLz = new DecimalFormat("###.##");
		dfLz.setRoundingMode(RoundingMode.HALF_UP);
		lblLfZt.setText(dfLz.format(laufzeit) + " µS");
	}
	public void printWReg(int wreg)
	{
		String test = Integer.toHexString(wreg);
		lblWReg.setText(test);
	}
	
	  protected DefaultTableModel getTblCodeModel() 
	  {
		  return tblCode;
	  }
}


