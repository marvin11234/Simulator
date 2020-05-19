import javax.swing.JFileChooser;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;




public class Controller {
	
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
			Memory memoryInst = new Memory();
			befehlDezimalIntArray = memoryInst.CodeSpeichern(tableStArray);
			
			Prozessor prozessorInst = new Prozessor();
			prozessorInst.Befehlsabarbeitung(befehlDezimalIntArray);
			
		}
		else 
		{
			sb.append("No file was choosen");
		}
	}
	
	public void addwf(int d, int f)throws Exception
	{
		System.out.println("addwf");
	}
	
	public void andwf(int d, int f)throws Exception
	{
		System.out.println("andwf");
	}
	
	public void clrw()throws Exception
	{
		System.out.println("clrw");
	}
	
	public void clrf(int f)throws Exception
	{
		System.out.println("clrf");
	}
	
	public void comf(int d, int f)throws Exception
	{
		System.out.println("comf");
	}
	
	public void decf(int d, int f)throws Exception
	{
		System.out.println("decf");
	}
	
	public void decfsz(int d, int f)throws Exception
	{
		System.out.println("decfsz");
	}
	
	public void incf(int d, int f)throws Exception
	{
		System.out.println("incf");
	}
	
	public void incfsz(int d, int f)throws Exception
	{
		System.out.println("incfsz");
	}
	
	public void iorwf(int d, int f)throws Exception
	{
		System.out.println("iorwf");
	}
	
	public void sleep()throws Exception
	{
		System.out.println("sleep");
	}
	
	public void Return()throws Exception
	{
		System.out.println("Return");
	}
	
	public void retfie()throws Exception
	{
		System.out.println("retfie");
	}
	
	public void clrwdt()throws Exception
	{
		System.out.println("addclrwdtwf");
	}
	
	public void nop()throws Exception
	{
		System.out.println("nop");
	}
	
	public void movwf(int f)throws Exception
	{
		System.out.println("movwf");
	}
	
	public void rlf(int d, int f)throws Exception
	{
		System.out.println("rlf");
	}
	
	public void rrf(int d, int f)throws Exception
	{
		System.out.println("rrf");
	}
	
	public void subwf(int d, int f)throws Exception
	{
		System.out.println("subwf");	
	}
	
	public void swapf(int d, int f)throws Exception
	{
		System.out.println("swapf");
	}
	
	public void movf(int d, int f)throws Exception
	{
		System.out.println("movf");
	}
	
	public void xorwf(int d, int f)throws Exception
	{
		System.out.println("xorwf");
	}
	
	
	public void bcf(int b, int f)throws Exception
	{
		System.out.println("bcf");
	}
	public void bsf(int b, int f)throws Exception
	{
		System.out.println("bsf");
	}
	public void btfsc(int b, int f)throws Exception
	{
		System.out.println("btfsc");
	}
	public void btfss(int b, int f)throws Exception
	{
		System.out.println("btfss");
	}
	
	public void call(int k)throws Exception
	{
		System.out.println("call");
	}
	public void Goto(int k)throws Exception
	{
		System.out.println("Goto");
	}
	public void addlw(int k)throws Exception
	{
		System.out.println("addlw");
	}
	public void iorlw(int k)throws Exception
	{
		System.out.println("iorlw");
	}
	public void movlw(int k)throws Exception
	{
		System.out.println("movlw");
	}
	public void retlw(int k)throws Exception
	{
		System.out.println("retlw");
	}
	public void sublw(int k)throws Exception
	{
		System.out.println("sublw");
	}
	public void xorlw(int k)throws Exception
	{
		System.out.println("xorlw");
	}
	
	public void andlw(int k)throws Exception
	{
		System.out.println("andlw");
	}
}
