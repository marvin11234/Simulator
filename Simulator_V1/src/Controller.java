import javax.swing.JFileChooser;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;




public class Controller {
	
	Simulator_Window gui; 
	Prozessor proc; 
	Memory memo;
	
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
		proc = new Prozessor(this);
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
						this.memo.programMemoryIntArray[pc] = code;
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
		int temp = memo.GetWInt();
		int temp2 = memo.GetF(f);
		int erg = temp + temp2;
		int k = temp2;
		memo.Print(temp, erg, k);
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
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
			memo.WriteW(erg);
		}
		else if (d == 1)	//ERGEBNIS IN F SPEICHERN ?
		{
			System.out.println("Schreibe in F:" + "erg:" + erg + "f" + f);
			memo.WriteF(erg,f);
		}
		memo.IncPc();
	}
	
	public void andwf(int d, int f)throws Exception //BEEINFLUSST Z
	{
		int temp = memo.GetWInt();
		int erg = (temp & f);
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO
		}
		memo.IncPc();
	}
	
	public void clrw()throws Exception //BEEINFLUSST Z
	{
		int erg = 00;
		memo.WriteW(erg);
		memo.SetzeroFlag();
		memo.IncPc();
	}
	
	public void clrf(int f)throws Exception //BEEINFLUSST Z
	{
		int erg = 00;
		//memo.WriteW(erg); TODO: WRITEF()
		memo.SetzeroFlag();
		memo.IncPc();
	}
	
	public void comf(int d, int f)throws Exception //BEEINFLUSST Z
	{
		
			int temp = ~ f; 
			int erg = temp;
			
			if(erg == 0)
			{
				memo.SetzeroFlag();
			}
			
			if(d == 0 )
			{
				memo.WriteW(erg);
			}
			else if (d == 1)
			{
				// TODO WRITEF()
			}
		memo.IncPc();
	}
	
	public void decf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		int temp =  f;
		int erg = temp - 1; 
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		memo.IncPc();
	}
	
	public void decfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("decfsz");
		memo.IncPc();
	}
	
	public void incf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		int temp =  f;
		int erg = temp + 1; 
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		memo.IncPc();
	}
	
	public void incfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("incfsz");
		memo.IncPc();
	}
	
	public void iorwf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		int temp =  memo.GetWInt();
		int erg = temp | f ; 
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		memo.IncPc();
	}
	
	public void sleep()throws Exception  //BEEINFLUSST KEINE STATI
	{
		System.out.println("sleep");
		memo.IncPc();
	}
	
	public void Return()throws Exception //BEEINFLUSST KEINE STATI
	{
		int k  = memo.GetStack();
		memo.SetPC(k);
		System.out.println("Return");
		memo.IncPc();
	}
	
	public void retfie()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("retfie");
		memo.IncPc();
	}
	
	public void clrwdt()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("addclrwdtwf");
		memo.IncPc();
	}
	
	public void nop()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("nop");
		memo.IncPc();
	}
	
	public void movwf(int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("movwf");
		int erg = memo.GetWInt();
		memo.WriteF(erg,f);
		memo.IncPc();
	}
	
	public void rlf(int d, int f)throws Exception //BEEINFLUSST C
	{
		System.out.println("rlf");
		memo.IncPc();
	}
	
	public void rrf(int d, int f)throws Exception //BEEINFLUSST C
	{
		System.out.println("rrf");
		memo.IncPc();
	}
	
	public void subwf(int d, int f)throws Exception //BEEINFLUSST C, DC, Z
	{
		int temp =  memo.GetWInt();
		int erg = f - temp ; 
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		memo.IncPc();
	}
	
	public void swapf(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("swapf");
		memo.IncPc();
	}
	
	public void movf(int d, int f)throws Exception  //BEEINFLUSST  Z
	{
		int erg = f ; 
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		memo.IncPc();
	}
	
	public void xorwf(int d, int f)throws Exception //BEEINFLUSST  Z
	{
		int temp =  memo.GetWInt();
		//temp = f;
		
		int erg = ((temp ^ f)) ; 
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(d == 0 )
		{
			memo.WriteW(erg);
		}
		else if (d == 1)
		{
			// TODO WRITEF()
		}
		memo.IncPc();
	}
	
	
	public void bcf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bcf");
		memo.IncPc();
	}
	public void bsf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bsf");
		memo.IncPc();
	}
	public void btfsc(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfsc");
		memo.IncPc();
	}
	public void btfss(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfss");
		memo.IncPc();
	}
	
	public void call(int k)throws Exception //BEEINFLUSST KEINE STATI
	{
		memo.SetStack();
		memo.SetPC(k);
		System.out.println("call");

	}
	public void Goto(int k)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("Goto");
		memo.SetPC(k);
	}
	public void addlw(int k)throws Exception //BEEINFLUSST  C, DC, Z
	{
		int temp = memo.GetWInt();
		int erg = temp + k;
		memo.Print(temp, erg, k);
		if (erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		memo.WriteW(erg);
		memo.IncPc();
	}
	public void iorlw(int k)throws Exception  //BEEINFLUSST  C, DC, Z
	{
		
		int temp = memo.GetWInt();
		int erg  = (temp | k);
		
		
		if(erg == 0) 
		{
			memo.SetzeroFlag();
		}
		memo.WriteW(erg);
		memo.IncPc();
	}
	public void movlw(int k)throws Exception //BEEINFLUSST  KEINE STATI	
	{
		int erg = k;
		
		memo.WriteW(erg);
		memo.IncPc();
	}
	public void retlw(int k)throws Exception //BEEINFLUSST  KEINE STATI	
	{	
		System.out.println("retlw");
		int erg = k;
		memo.WriteW(erg);
		k  = memo.GetStack();
		memo.SetPC(k);

		memo.IncPc();
	}
	public void sublw(int k)throws Exception //BEEINFLUSST  C, DC, Z
	{
		int temp = memo.GetWInt();
		int erg = k - temp;
		memo.Print(temp, erg, k); //DEBUG
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		if(erg < 0) {
			memo.SetCarry();
		}
		
		memo.WriteW(erg);
		memo.IncPc();
	}
	public void xorlw(int k)throws Exception //BEEINFLUSST Z
	{
		int temp = memo.GetWInt();
		int erg = temp ^ k  ;
		memo.Print(temp, erg, k); //DEBUG
		
		if(erg == 0)
		{
			memo.SetzeroFlag();
		}
		
		memo.WriteW(erg);
		memo.IncPc();
	}
	
	public void andlw(int k)throws Exception //BEEINFLUSST Z
	{
		int temp = memo.GetWInt();
		int erg = (temp & k);
		if(k == 0) 
		{
			memo.SetzeroFlag();
		}
		memo.WriteW(erg);
		memo.IncPc();
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
