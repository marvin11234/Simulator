import java.util.Arrays;
import java.util.Stack;

public class Memory extends Thread{
	
	/*############################################################################
	 * 
	 *
	 * 
	 * 	Controller Instanz erzeugen
	 * 
	 * 
	 * #########################################################################*/
	Controller ctr;
	public Memory(Controller controller) {
		ctr = controller;
	}
	
	/*############################################################################
	 * 
	 * Datenspeicher: erste Bank 00-7F zweíte Bank 80-FF
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################*/
	protected int[][] dataMemoryIntArray = new int [256][8];
	
	/*############################################################################
	 * 
	 *
	 * 
	 * Speicher für den programmcode; 0000 = Reset 0004 = interrupt
	 * 
	 * 
	 * #########################################################################*/
	protected int[] programMemoryIntArray = new int [1024];

	/*############################################################################
	 * 
	 * enthält die Zeile des Befehl, default == 0
	 *  beschrieben über die funktion incpc
	 * 
	 * 
	 * 
	 * #########################################################################*/
	protected int programCounterInt = 0;
	
	/*############################################################################
	 * 
	 *
	 * 
	 * W-Register
	 * 
	 * 
	 * #########################################################################*/
	protected int[] wRegisterIntArray = new int [8]; 
	int wRegInt = 0;
	
	/*############################################################################
	 * 
	 *
	 * 
	 * Stack
	 * 
	 * 
	 * #########################################################################*/
	protected Stack <Integer>  cmdStack = new Stack <Integer>();
	
	/*############################################################################
	 * 
	 *
	 * 
	 * legt den aktuellen Porgrammcounter auf den Stack, 
	 * wird für call befehl als rücksprung bennötigt
	 * 
	 * 
	 * #########################################################################*/
	
	
	

	/*############################################################################
	 * 
	 * Beschreibt das F Register an der Stelle an der stelle f mit dem Inhalt erg
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################*/
	public void WriteF(int erg, int f)
	{
		System.out.println("### "+ "WriteF " + erg + " ###" );
		 int index = 0;
	     for(int i = 0; i <= (dataMemoryIntArray[f].length)-1; i++)//ggf. über Zeroflag lösen ???
	     {
	    	 dataMemoryIntArray[f][i] = 0;
	     }
	     while(erg > 0){
	    	 dataMemoryIntArray[f][index++] = erg % 2;
	       erg = erg/2;
	     }
	     for(int i = dataMemoryIntArray[f].length -1;i >= 0;i--){
	       System.out.println("F-Register: " +  Integer.toHexString(f) + ": " + dataMemoryIntArray[f][i]);
	     }
	}
	
	/*############################################################################
	 * 
	 * funktion liefert den wert einer stelle f des Filoeregisters als int zurück
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################*/
	public int GetF(int f) 
	{
		int fDezimal;
		fDezimal= (dataMemoryIntArray[f][7] * 128) + (dataMemoryIntArray[f][6] * 64) + (dataMemoryIntArray[f][5] * 32) + (dataMemoryIntArray[f][4] * 16) + (dataMemoryIntArray[f][3] * 8) + (dataMemoryIntArray[f][2] * 4) + (dataMemoryIntArray[f][1] * 2) + (dataMemoryIntArray[f][0] * 1);
		return fDezimal;
	}
	
	/*############################################################################
	 * 
	 * 
	 * 
	 *Funktion liefert den Wert einer bestimmten Stelle des File Registers als 8 bit int Array zurück
	 * 
	 * 
	 * #########################################################################*/
	public int[] GetFBin(int f)
	{
		int[] testArray = new int [8];
		int i = 0;
		while(i <= 7)
		{
			testArray [i] = dataMemoryIntArray[f][i];

			i++;
		}
		return testArray;
	}
	
	/*############################################################################
	 * 
	 * 
	 * Funktion liefert das W-Register als 8bit int Array zurück
	 * 
	 * 
	 * 
	 * #########################################################################*/
	public int[] GetFWin()
	{
		int[] testArray = new int [8];
		int i = 0;
		while(i <= 7)
		{
			testArray [i] = wRegisterIntArray[i];

			i++;
		}
		return testArray;
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 * beschrieben des Stack
	 * 
	 * 
	 * #########################################################################*/
	public void SetStack()
	{
		cmdStack.push(programCounterInt +1 );
		System.out.println("Stack: " + cmdStack.peek());
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 * Beschreiben des W Registers mit dem Literal K
	 * 
	 * 
	 * #########################################################################*/
	public void WriteW(int erg) 
	{
		System.out.println("###" + "WriteW "+ erg + " ###" );
		wRegInt = erg;
	     int index = 0;
	     for(int i = 0; i <= (wRegisterIntArray.length)-1; i++)//ggf. über Zeroflag lösen ???
	     {
	    	 wRegisterIntArray[i] = 0;
	     }
	    	 while(erg > 0)
	   	     {
	    		 wRegisterIntArray[index++] = erg % 2;
	    		 erg = erg/2;
	   	     }
	     for(int i = wRegisterIntArray.length -1;i >= 0;i--){
	       System.out.println("W-Register:" + wRegisterIntArray[i]);
	     }
	}

	/*############################################################################
	 * 
	 *
	 * 
	 * rückgabe des W-Register Wertes als Integer Wert
	 * 
	 * 
	 * #########################################################################*/
	public int GetWInt()
	{
		return wRegInt;
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 * gibt Wert des Stack als int variable stackInt zurück
	 * 
	 * 
	 * #########################################################################*/
	public int GetStack()
	{
		int stackInt = cmdStack.peek();
		cmdStack.pop();
		return stackInt;	
	}
	
	public int ShowStack()
	{
		int stackInt = 0;
		if(cmdStack.isEmpty() == true )
		{
			stackInt = 0;
		}
		else
		{
			stackInt = cmdStack.peek();
		}
		
		return stackInt;
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 * erhöht den programmcounter
	 * 
	 * 
	 * #########################################################################*/
	public void IncPc()
	{
	programCounterInt = programCounterInt +1;
	System.out.println("PC: " + Integer.toHexString(programCounterInt));
	}


	/*############################################################################
	 * 
	 *
	 * 
	 * Setzt den Programmcounter auf einen bestimmten Wert
	 * wird für Call und Goto bennötigt
	 * 
	 * #########################################################################*/
	public void SetPC(int k)
	{ 
		programCounterInt = k;
		System.out.println("PC:" + programCounterInt);
	}
	
	
	
	
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion setzt das DigitCarryFlag bei Überlauf des 4 bits im 
	 * Hauptspeicher (Status register bit 0)
	 * 
	 * #########################################################################*/
	public void SetDigitCarry() 
	{
		dataMemoryIntArray[3][1] = 1;

	}
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion setzt das DigitCarryFlag zurück (auf 0)
	 * Hauptspeicher (Status register bit 1)
	 * 
	 * #########################################################################*/
	public void ResetDigitCarry() 
	{
		dataMemoryIntArray[3][1] = 0;

	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion setzt das Zeroflag wenn ergebniss einer operation == 0 im 
	 * Hauptspeicher (Status register bit 2)
	 * 
	 * #########################################################################*/
	public void SetzeroFlag() 
	{
		dataMemoryIntArray[3][2] = 1;

	}
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion setzt das Zeroflag auf 0
	 * Hauptspeicher (Status register bit 2)
	 * 
	 * #########################################################################*/
	public void ResetetzeroFlag() 
	{
		dataMemoryIntArray[3][2] = 0;
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion setzt das CarryFlag bei Überlauf im 
	 * Hauptspeicher (Status register bit 0)
	 * 
	 * #########################################################################*/
	public void SetCarry() 
	{
		dataMemoryIntArray[3][0] = 1;

	}
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion setzt das CarryFlag zurück im 
	 * Hauptspeicher (Status register bit 0)
	 * 
	 * #########################################################################*/
	public void ResetCarry() 
	{
		dataMemoryIntArray[3][0] = 0;
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 *Funktion liefert den aktuellen Wert der Im Carry steht
	 * Hauptspeicher (Status register bit 0)
	 * 
	 * #########################################################################*/
	public int GetBitValue(int Adresse, int Stelle)
	{
		return 		dataMemoryIntArray[Adresse][Stelle];
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 *funktion zum direkten speicherzugriff (FSR) 
	 * 
	 * 
	 * #########################################################################*/
	protected int WriteDirect(int f) 
	{
		String c = "";
		for(int i = 0; i < 8; i++) 
		{
				c = c + dataMemoryIntArray[f][7-i];
		}
		System.out.print("Direct Memory Access: " + f);
		return Integer.parseInt(c,2);
	}
	
	public void SetCarryBit(int value)
	{
		dataMemoryIntArray[3][0] = value;
	}
	
	public void SetDigitCarryBit(int value)
	{
		dataMemoryIntArray[3][1] = value;
	}
	
	public void SetZeroBit(int value)
	{
		dataMemoryIntArray[3][2] = value;
	}
	
	public void SetPowerDownBit(int value)
	{
		dataMemoryIntArray[3][3] = value;
	}
	
	public void SetToBit(int value)
	{
		dataMemoryIntArray[3][4] = value;
	}
	
	public void SetReg1Bit(int value)
	{
		dataMemoryIntArray[3][5] = value;
	}
	
	public void SetReg2Bit(int value)
	{
		dataMemoryIntArray[3][6] = value;
	}
	
	public void SetIPRBit(int value)
	{
		dataMemoryIntArray[3][7] = value;
	}
}
