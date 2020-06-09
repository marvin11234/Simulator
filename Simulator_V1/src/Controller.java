import javax.swing.JFileChooser;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;




public class Controller {
	
	private Simulator_Window gui; 
	private Prozessor proc; 
	private Memory memo;
	
	private boolean processorRunning = false; 
	
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	int arrayStelleEinlesenInt = 0;
	String[] inputStArray = new String[1024];
	String[] programmSpeicherStArray = new String[1024];
	String temCurLnSt;
	String[][] tableStArray = new String[1024][6];
	int zeileInt = 0;
	int i = 0;
	int[] befehlDezimalIntArray = new int[1024];
	
	
	public Controller(Simulator_Window simulator_Window) {
		gui = simulator_Window;
		memo = new Memory(this);
	}

	public void Einlesen() throws Exception{
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get the file
			java.io.File file = fileChooser.getSelectedFile();
			//Default buffer reader
			BufferedReader br = null;
			try 
			{ 
				br = new BufferedReader(new FileReader(file));
				
				String currentLineSt;
				while ((currentLineSt = br.readLine()) != null)
				{
					String pcSt = currentLineSt.substring(0,4);
					String codeSt = currentLineSt.substring(5,9);
					String linecountSt = currentLineSt.substring(20,25);
					String label = "";
					if(currentLineSt.charAt(27) != ' ')
					{
						int index = 27;
						while(currentLineSt.charAt(index) != ' ')
						{
							label = label + currentLineSt.charAt(index);
							index ++;
						}
					}
					if(label.isEmpty())
					{
						gui.tblCode.addRow(new Object[] {"",pcSt, codeSt, linecountSt, label,currentLineSt.substring(36)});
					}
					else
					{
						gui.tblCode.addRow(new Object[] {"",pcSt, codeSt, linecountSt, label,""});
					}
					if(! currentLineSt.substring(0,4).equals("    "))
					{
						int pc =Integer.parseInt(pcSt ,16);
						int code = Integer.parseInt(codeSt ,16);
						this.getMemo().programMemoryIntArray[pc] = code;
					}
				}

						
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else 
		{
			sb.append("No file was choosen");
		}
	}

	
	
	
	
	
	public void addwf(int d, int f)throws Exception	//BEEINFLUSST C; DC; Z
	{
		int temp = getMemo().GetWInt();
		int temp2 = getMemo().GetF(f);
		int erg = temp + temp2;
		int k = temp2;
		getMemo().Print(temp, erg, k);
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
/*		if(erg < 255)
		{
			memo.SetCarry();
			erg = ((temp + f) +255);
		}
*/		
		if (d == 0 )
		{
			System.out.println("Schreibe in F:" + "erg: " + erg + "f" + f);
			getMemo().WriteW(erg);
		}
		else if (d == 1)	//ERGEBNIS IN F SPEICHERN ?
		{
			System.out.println("Schreibe in F:" + "erg:" + erg + "f" + f);
			getMemo().WriteF(erg,f);
		}
		getMemo().IncPc();
	}
	
	public void andwf(int d, int f)throws Exception //BEEINFLUSST Z
	{
		int temp = getMemo().GetWInt();
		int erg = (temp & f);
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO
		}
		getMemo().IncPc();
	}
	
	public void clrw()throws Exception //BEEINFLUSST Z
	{
		int erg = 00;
		getMemo().WriteW(erg);
		getMemo().SetzeroFlag();
		getMemo().IncPc();
	}
	
	public void clrf(int f)throws Exception //BEEINFLUSST Z
	{
		int erg = 00;
		//memo.WriteW(erg); TODO: WRITEF()
		getMemo().SetzeroFlag();
		getMemo().IncPc();
	}
	
	public void comf(int d, int f)throws Exception //BEEINFLUSST Z
	{
		
			int temp = ~ f; 
			int erg = temp;
			
			if(erg == 0)
			{
				getMemo().SetzeroFlag();
			}
			
			if(d == 0 )
			{
				getMemo().WriteW(erg);
			}
			else if (d == 1)
			{
				// TODO WRITEF()
			}
		getMemo().IncPc();
	}
	
	public void decf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		int temp =  f;
		int erg = temp - 1; 
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		getMemo().IncPc();
	}
	
	public void decfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("decfsz");
		getMemo().IncPc();
	}
	
	public void incf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		int temp =  f;
		int erg = temp + 1; 
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		getMemo().IncPc();
	}
	
	public void incfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("incfsz");
		getMemo().IncPc();
	}
	
	public void iorwf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		int temp =  getMemo().GetWInt();
		int erg = temp | f ; 
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		getMemo().IncPc();
	}
	
	public void sleep()throws Exception  //BEEINFLUSST KEINE STATI
	{
		System.out.println("sleep");
		getMemo().IncPc();
	}
	
	public void Return()throws Exception //BEEINFLUSST KEINE STATI
	{
		int k  = getMemo().GetStack();
		getMemo().SetPC(k);
		System.out.println("Return");
		getMemo().IncPc();
	}
	
	public void retfie()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("retfie");
		getMemo().IncPc();
	}
	
	public void clrwdt()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("addclrwdtwf");
		getMemo().IncPc();
	}
	
	public void nop()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("nop");
		getMemo().IncPc();
	}
	
	public void movwf(int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("movwf");
		int erg = getMemo().GetWInt();
		getMemo().WriteF(erg,f);
		getMemo().IncPc();
	}
	
	public void rlf(int d, int f)throws Exception //BEEINFLUSST C
	{
		System.out.println("rlf");
		getMemo().IncPc();
	}
	
	public void rrf(int d, int f)throws Exception //BEEINFLUSST C
	{
		System.out.println("rrf");
		getMemo().IncPc();
	}
	
	public void subwf(int d, int f)throws Exception //BEEINFLUSST C, DC, Z
	{
		int temp =  getMemo().GetWInt();
		int erg = f - temp ; 
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		getMemo().IncPc();
	}
	
	public void swapf(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("swapf");
		getMemo().IncPc();
	}
	
	public void movf(int d, int f)throws Exception  //BEEINFLUSST  Z
	{
		int erg = f ; 
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		getMemo().IncPc();
	}
	
	public void xorwf(int d, int f)throws Exception //BEEINFLUSST  Z
	{
		int temp =  getMemo().GetWInt();
		//temp = f;
		
		int erg = ((temp ^ f)) ; 
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		getMemo().IncPc();
	}
	
	
	public void bcf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bcf");
		getMemo().IncPc();
	}
	public void bsf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bsf");
		getMemo().IncPc();
	}
	public void btfsc(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfsc");
		getMemo().IncPc();
	}
	public void btfss(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfss");
		getMemo().IncPc();
	}
	
	public void call(int k)throws Exception //BEEINFLUSST KEINE STATI
	{
		getMemo().SetStack();
		getMemo().SetPC(k);
		System.out.println("call");

	}
	public void Goto(int k)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("Goto");
		getMemo().SetPC(k);
	}
	public void addlw(int k)throws Exception //BEEINFLUSST  C, DC, Z
	{
		int temp = getMemo().GetWInt();
		int erg = temp + k;
		getMemo().Print(temp, erg, k);
		if (erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void iorlw(int k)throws Exception  //BEEINFLUSST  C, DC, Z
	{
		
		int temp = getMemo().GetWInt();
		int erg  = (temp | k);
		
		
		if(erg == 0) 
		{
			getMemo().SetzeroFlag();
		}
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void movlw(int k)throws Exception //BEEINFLUSST  KEINE STATI	
	{
		int erg = k;
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void retlw(int k)throws Exception //BEEINFLUSST  KEINE STATI	
	{	
		System.out.println("retlw");
		int erg = k;
		getMemo().WriteW(erg);
		k  = getMemo().GetStack();
		getMemo().SetPC(k);

		getMemo().IncPc();
	}
	public void sublw(int k)throws Exception //BEEINFLUSST  C, DC, Z
	{
		int temp = getMemo().GetWInt();
		int erg = k - temp;
		getMemo().Print(temp, erg, k); //DEBUG
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(erg < 0) {
			getMemo().SetCarry();
		}
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void xorlw(int k)throws Exception //BEEINFLUSST Z
	{
		int temp = getMemo().GetWInt();
		int erg = temp ^ k  ;
		getMemo().Print(temp, erg, k); //DEBUG
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	
	public void andlw(int k)throws Exception //BEEINFLUSST Z
	{
		int temp = getMemo().GetWInt();
		int erg = (temp & k);
		if(k == 0) 
		{
			getMemo().SetzeroFlag();
		}
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}

	public void start() 
	{
		if(! this.processorRunning)
		{
			proc = new Prozessor(this);
			proc.start();
			this.processorRunning = true;
		}
		
	}

	public void stop()
	{
		if( this.processorRunning)
		{
			this.processorRunning = false;
			proc.stopThread();
		}
		
	}

	public Memory getMemo() {
		return memo;
	}
}
