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
	
	//SET SFR AUS GUI
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
	
	//SET IO PORTS
	//TRIS A => 85hex
	public void SetTRISA0Bit(int value)
	{
		dataMemoryIntArray[133][0] = value;
	}
	public void SetTRISA1Bit(int value)
	{
		dataMemoryIntArray[133][1] = value;
	}
	public void SetTRISA2Bit(int value)
	{
		dataMemoryIntArray[133][2] = value;
	}
	public void SetTRISA3Bit(int value)
	{
		dataMemoryIntArray[133][3] = value;
	}
	public void SetTRISA4Bit(int value)
	{
		dataMemoryIntArray[133][4] = value;
	}
	//TRIS A IO-Pins => 5hex
	public void SetTRISAIO0Bit(int value)
	{
		dataMemoryIntArray[5][0] = value;
	}
	public void SetTRISAIO1Bit(int value)
	{
		dataMemoryIntArray[5][1] = value;
	}
	public void SetTRISAIO2Bit(int value)
	{
		dataMemoryIntArray[5][2] = value;
	}
	public void SetTRISAIO3Bit(int value)
	{
		dataMemoryIntArray[5][3] = value;
	}
	public void SetTRISAIO4Bit(int value)
	{
		dataMemoryIntArray[5][4] = value;
	}
	//Datenblatt 22
	
	//TRIS B =86hex
	public void SetTRISB0Bit(int value)
	{
		dataMemoryIntArray[134][0] = value;
	}
	public void SetTRISB1Bit(int value)
	{
		dataMemoryIntArray[134][1] = value;
	}
	public void SetTRISB2Bit(int value)
	{
		dataMemoryIntArray[134][2] = value;
	}
	public void SetTRISB3Bit(int value)
	{
		dataMemoryIntArray[134][3] = value;
	}
	public void SetTRISB4Bit(int value)
	{
		dataMemoryIntArray[134][4] = value;
	}
	public void SetTRISB5Bit(int value)
	{
		dataMemoryIntArray[134][5] = value;
	}
	public void SetTRISB6Bit(int value)
	{
		dataMemoryIntArray[134][6] = value;
	}
	public void SetTRISB7Bit(int value)
	{
		dataMemoryIntArray[134][7] = value;
	}
	//TRIS B IO-Pins => 6hex
	public void SetTRISBIO0Bit(int value)
	{
		dataMemoryIntArray[6][0] = value;
	}
	public void SetTRISBIO1Bit(int value)
	{
		dataMemoryIntArray[6][1] = value;
	}
	public void SetTRISBIO2Bit(int value)
	{
		dataMemoryIntArray[6][2] = value;
	}
	public void SetTRISBIO3Bit(int value)
	{
		dataMemoryIntArray[6][3] = value;
	}
	public void SetTRISBIO4Bit(int value)
	{
		dataMemoryIntArray[6][4] = value;
	}
	public void SetTRISBIO5Bit(int value)
	{
		dataMemoryIntArray[6][5] = value;
	}
	public void SetTRISBIO6Bit(int value)
	{
		dataMemoryIntArray[6][6] = value;
	}
	public void SetTRISBIO7Bit(int value)
	{
		dataMemoryIntArray[6][7] = value;
	}
	//Datenblatt Seite 24
	
	public void CheckSFR()
	{
		if(((dataMemoryIntArray[3][0] == 0) & (ctr.getGui().cfStatus == false)) | (dataMemoryIntArray[3][0] == 1) & (ctr.getGui().cfStatus == true) )
		{
		}
		else if(((dataMemoryIntArray[3][0] == 0) & (ctr.getGui().cfStatus == true)) | (dataMemoryIntArray[3][0] == 1) & (ctr.getGui().cfStatus == false))
		{
			ctr.getGui().SetCFGui((dataMemoryIntArray[3][0]));
		}
		
		if(((dataMemoryIntArray[3][1] == 0) & (ctr.getGui().dcStatus == false)) | (dataMemoryIntArray[3][1] == 1) & (ctr.getGui().dcStatus == true) )
		{
		}
		else if(((dataMemoryIntArray[3][1] == 0) & (ctr.getGui().dcStatus == true)) | (dataMemoryIntArray[3][1] == 1) & (ctr.getGui().dcStatus == false))
		{
			ctr.getGui().SetDCGui((dataMemoryIntArray[3][1]));
		}
		
		if(((dataMemoryIntArray[3][2] == 0) & (ctr.getGui().zfStatus == false)) | (dataMemoryIntArray[3][2] == 1) & (ctr.getGui().zfStatus == true) )
		{
		}
		else if(((dataMemoryIntArray[3][2] == 0) & (ctr.getGui().zfStatus == true)) | (dataMemoryIntArray[3][2] == 1) & (ctr.getGui().zfStatus == false))
		{
			ctr.getGui().SetZFGui((dataMemoryIntArray[3][2]));
		}
	}
	
	public void CheckIO()
	{		
		//Tris A ports
		if(((dataMemoryIntArray[5][0] == 0) & (ctr.getGui().trisA0Status == false)) | (dataMemoryIntArray[5][0] == 1) & (ctr.getGui().trisA0Status == true) )
		{
		}
		else if(((dataMemoryIntArray[5][0] == 0) & (ctr.getGui().trisA0Status == true)) | (dataMemoryIntArray[5][0] == 1) & (ctr.getGui().trisA0Status == false))
		{
			ctr.getGui().SetTRISABit0Gui((dataMemoryIntArray[5][0]));
		}
		
		if(((dataMemoryIntArray[5][1] == 0) & (ctr.getGui().trisA1Status == false)) | (dataMemoryIntArray[5][1] == 1) & (ctr.getGui().trisA1Status == true) )
		{
		}
		else if(((dataMemoryIntArray[5][1] == 0) & (ctr.getGui().trisA1Status == true)) | (dataMemoryIntArray[5][1] == 1) & (ctr.getGui().trisA1Status == false))
		{
			ctr.getGui().SetTRISABit1Gui((dataMemoryIntArray[5][1]));
		}
		
		if(((dataMemoryIntArray[5][2] == 0) & (ctr.getGui().trisA2Status == false)) | (dataMemoryIntArray[5][2] == 1) & (ctr.getGui().trisA2Status == true) )
		{
		}
		else if(((dataMemoryIntArray[5][2] == 0) & (ctr.getGui().trisA2Status == true)) | (dataMemoryIntArray[5][2] == 1) & (ctr.getGui().trisA2Status == false))
		{
			ctr.getGui().SetTRISABit2Gui((dataMemoryIntArray[5][2]));
		}
		
		if(((dataMemoryIntArray[5][3] == 0) & (ctr.getGui().trisA3Status == false)) | (dataMemoryIntArray[5][3] == 1) & (ctr.getGui().trisA3Status == true) )
		{
		}
		else if(((dataMemoryIntArray[5][3] == 0) & (ctr.getGui().trisA3Status == true)) | (dataMemoryIntArray[5][3] == 1) & (ctr.getGui().trisA3Status == false))
		{
			ctr.getGui().SetTRISABit3Gui((dataMemoryIntArray[5][3]));
		}
		
		if(((dataMemoryIntArray[5][4] == 0) & (ctr.getGui().trisA4Status == false)) | (dataMemoryIntArray[5][4] == 1) & (ctr.getGui().trisA4Status == true) )
		{
		}
		else if(((dataMemoryIntArray[5][4] == 0) & (ctr.getGui().trisA4Status == true)) | (dataMemoryIntArray[5][4] == 1) & (ctr.getGui().trisA4Status == false))
		{
			ctr.getGui().SetTRISABit4Gui((dataMemoryIntArray[5][4]));
		}
		//Tris B Ports
		if(((dataMemoryIntArray[6][0] == 0) & (ctr.getGui().trisB0Status == false)) | (dataMemoryIntArray[6][0] == 1) & (ctr.getGui().trisB0Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][0] == 0) & (ctr.getGui().trisB0Status == true)) | (dataMemoryIntArray[6][0] == 1) & (ctr.getGui().trisB0Status == false))
		{
			ctr.getGui().SetTRISBBit0Gui(dataMemoryIntArray[6][0]);
		}
		
		if(((dataMemoryIntArray[6][1] == 0) & (ctr.getGui().trisB1Status == false)) | (dataMemoryIntArray[6][1] == 1) & (ctr.getGui().trisB1Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][1] == 0) & (ctr.getGui().trisB1Status == true)) | (dataMemoryIntArray[6][1] == 1) & (ctr.getGui().trisB1Status == false))
		{
			ctr.getGui().SetTRISBBit1Gui((dataMemoryIntArray[6][1]));
		}
		
		if(((dataMemoryIntArray[6][2] == 0) & (ctr.getGui().trisB2Status == false)) | (dataMemoryIntArray[6][2] == 1) & (ctr.getGui().trisB2Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][2] == 0) & (ctr.getGui().trisB2Status == true)) | (dataMemoryIntArray[6][2] == 1) & (ctr.getGui().trisB2Status == false))
		{
			ctr.getGui().SetTRISBBit2Gui((dataMemoryIntArray[6][2]));
		}
		
		if(((dataMemoryIntArray[6][3] == 0) & (ctr.getGui().trisB3Status == false)) | (dataMemoryIntArray[6][3] == 1) & (ctr.getGui().trisB3Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][3] == 0) & (ctr.getGui().trisB3Status == true)) | (dataMemoryIntArray[6][3] == 1) & (ctr.getGui().trisB3Status == false))
		{
			ctr.getGui().SetTRISBBit3Gui((dataMemoryIntArray[6][3]));
		}
		
		if(((dataMemoryIntArray[6][4] == 0) & (ctr.getGui().trisB4Status == false)) | (dataMemoryIntArray[6][4] == 1) & (ctr.getGui().trisB4Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][4] == 0) & (ctr.getGui().trisB4Status == true)) | (dataMemoryIntArray[6][4] == 1) & (ctr.getGui().trisB4Status == false))
		{
			ctr.getGui().SetTRISBBit4Gui((dataMemoryIntArray[6][4]));
		}
		if(((dataMemoryIntArray[6][5] == 0) & (ctr.getGui().trisB5Status == false)) | (dataMemoryIntArray[6][5] == 1) & (ctr.getGui().trisB5Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][5] == 0) & (ctr.getGui().trisB5Status == true)) | (dataMemoryIntArray[6][5] == 1) & (ctr.getGui().trisB5Status == false))
		{
			ctr.getGui().SetTRISBBit5Gui((dataMemoryIntArray[6][5]));
		}
		
		if(((dataMemoryIntArray[6][6] == 0) & (ctr.getGui().trisB6Status == false)) | (dataMemoryIntArray[6][6] == 1) & (ctr.getGui().trisB6Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][6] == 0) & (ctr.getGui().trisB6Status == true)) | (dataMemoryIntArray[6][6] == 1) & (ctr.getGui().trisB6Status == false))
		{
			ctr.getGui().SetTRISBBit6Gui((dataMemoryIntArray[6][6]));
		}
		
		if(((dataMemoryIntArray[6][7] == 0) & (ctr.getGui().trisB7Status == false)) | (dataMemoryIntArray[6][7] == 1) & (ctr.getGui().trisB7Status == true) )
		{
		}
		else if(((dataMemoryIntArray[6][7] == 0) & (ctr.getGui().trisB7Status == true)) | (dataMemoryIntArray[6][7] == 1) & (ctr.getGui().trisB7Status == false))
		{
			ctr.getGui().SetTRISBBit7Gui((dataMemoryIntArray[6][7]));
		}
		
		
	
		
	}
}
