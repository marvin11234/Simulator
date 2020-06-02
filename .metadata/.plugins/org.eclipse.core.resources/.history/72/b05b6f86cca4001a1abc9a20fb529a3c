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
	String currentLineSt;
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
				while ((currentLineSt = br.readLine()) != null)
				{
					inputStArray[arrayStelleEinlesenInt] = currentLineSt;
					
					arrayStelleEinlesenInt++;
				}

						while((temCurLnSt = inputStArray[i]) !=  null)
						{
											
							try 
							{
							
								temCurLnSt = inputStArray[i];
								if(temCurLnSt != null)
								{							
									tableStArray[i][1] = temCurLnSt.substring(0,4);
									tableStArray[i][2] = temCurLnSt.substring(5,9);
									tableStArray[i][3] = temCurLnSt.substring(20,25);
									tableStArray[i][4] = temCurLnSt.substring(27,32);
									tableStArray[i][5] = temCurLnSt.substring(36,inputStArray[i].length());						
								}
							}
							catch (NumberFormatException e)
							{
								System.out.println("Fehler: es Konnte keine Einlesezeile gefunden werden");
							}
							i++;
						}

			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
			
			befehlDezimalIntArray = memo.CodeSpeichern(tableStArray);

			Thread BefehlsThreat = new Thread(proc);
			BefehlsThreat.start();

			
		}
		else 
		{
			sb.append("No file was choosen");
		}
	}
	public int GetprogramCounter(){
		int aktuellerBefehlInt = memo.programCounterInt;
		return aktuellerBefehlInt;
	}
	public int GetBefehl(int pcr){
		int befehl = memo.programMemoryIntArray[pcr];
		return befehl;
	}
	
	public void addwf(int d, int f)throws Exception
	{
		System.out.println("addwf");
		memo.IncPc();
	}
	
	public void andwf(int d, int f)throws Exception
	{
		System.out.println("andwf");
		memo.IncPc();
	}
	
	public void clrw()throws Exception
	{
		System.out.println("clrw");
		memo.IncPc();
	}
	
	public void clrf(int f)throws Exception
	{
		System.out.println("clrf");
		memo.IncPc();
	}
	
	public void comf(int d, int f)throws Exception
	{
		System.out.println("comf");
		memo.IncPc();
	}
	
	public void decf(int d, int f)throws Exception
	{
		System.out.println("decf");
		memo.IncPc();
	}
	
	public void decfsz(int d, int f)throws Exception
	{
		System.out.println("decfsz");
		memo.IncPc();
	}
	
	public void incf(int d, int f)throws Exception
	{
		System.out.println("incf");
		memo.IncPc();
	}
	
	public void incfsz(int d, int f)throws Exception
	{
		System.out.println("incfsz");
		memo.IncPc();
	}
	
	public void iorwf(int d, int f)throws Exception
	{
		System.out.println("iorwf");
		memo.IncPc();
	}
	
	public void sleep()throws Exception
	{
		System.out.println("sleep");
		memo.IncPc();
	}
	
	public void Return()throws Exception
	{
		System.out.println("Return");
		memo.IncPc();
	}
	
	public void retfie()throws Exception
	{
		System.out.println("retfie");
		memo.IncPc();
	}
	
	public void clrwdt()throws Exception
	{
		System.out.println("addclrwdtwf");
		memo.IncPc();
	}
	
	public void nop()throws Exception
	{
		System.out.println("nop");
		memo.IncPc();
	}
	
	public void movwf(int f)throws Exception
	{
		System.out.println("movwf");
		memo.IncPc();
	}
	
	public void rlf(int d, int f)throws Exception
	{
		System.out.println("rlf");
		memo.IncPc();
	}
	
	public void rrf(int d, int f)throws Exception
	{
		System.out.println("rrf");
		memo.IncPc();
	}
	
	public void subwf(int d, int f)throws Exception
	{
		System.out.println("subwf");
		memo.IncPc();
	}
	
	public void swapf(int d, int f)throws Exception
	{
		System.out.println("swapf");
		memo.IncPc();
	}
	
	public void movf(int d, int f)throws Exception
	{
		System.out.println("movf");
		memo.IncPc();
	}
	
	public void xorwf(int d, int f)throws Exception
	{
		System.out.println("xorwf");
		memo.IncPc();
	}
	
	
	public void bcf(int b, int f)throws Exception
	{
		System.out.println("bcf");
		memo.IncPc();
	}
	public void bsf(int b, int f)throws Exception
	{
		System.out.println("bsf");
		memo.IncPc();
	}
	public void btfsc(int b, int f)throws Exception
	{
		System.out.println("btfsc");
		memo.IncPc();
	}
	public void btfss(int b, int f)throws Exception
	{
		System.out.println("btfss");
		memo.IncPc();
	}
	
	public void call(int k)throws Exception
	{
		memo.SetPC(k);
		System.out.println("call");

	}
	public void Goto(int k)throws Exception
	{
		System.out.println("Goto");
		memo.SetPC(k);
	}
	public void addlw(int k)throws Exception
	{
		System.out.println("addlw");
		memo.IncPc();
	}
	public void iorlw(int k)throws Exception
	{
		System.out.println("iorlw");
		memo.IncPc();
	}
	public void movlw(int k)throws Exception
	{
		System.out.println("movlw");
		System.out.println("*************************");
		System.out.println(k);
		System.out.println("*************************");
		memo.IncPc();
	}
	public void retlw(int k)throws Exception
	{
		System.out.println("retlw");
		memo.IncPc();
	}
	public void sublw(int k)throws Exception
	{
		System.out.println("sublw");
		memo.IncPc();
	}
	public void xorlw(int k)throws Exception
	{
		System.out.println("xorlw");
		memo.IncPc();
	}
	
	public void andlw(int k)throws Exception
	{
		System.out.println("andlw");
		System.out.println("*************************");
		System.out.println(k);
		System.out.println("*************************");
		memo.IncPc();
	}
}
