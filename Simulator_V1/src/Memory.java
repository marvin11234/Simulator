import java.util.Arrays;
import java.util.Stack;

public class Memory extends Thread{
	
	//Datenspeicher: erste Bank 00-7F zwe�te Bank 80-FF
	protected int[][] dataMemoryIntArray = new int [256][8];

	public void WriteF(int erg, int f)
	{
		System.out.println("### "+ "WriteF " + erg + " ###" );
		 int index = 0;
	     for(int i = 0; i <= (dataMemoryIntArray[f].length)-1; i++)//ggf. �ber Zeroflag l�sen ???
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
	//funktion liefert den wert einer stelle f des Filoeregisters als int zur�ck
	public int GetF(int f) 
	{
		int fDezimal;
		fDezimal= (dataMemoryIntArray[f][7] * 128) + (dataMemoryIntArray[f][6] * 64) + (dataMemoryIntArray[f][5] * 32) + (dataMemoryIntArray[f][4] * 16) + (dataMemoryIntArray[f][3] * 8) + (dataMemoryIntArray[f][2] * 4) + (dataMemoryIntArray[f][1] * 2) + (dataMemoryIntArray[f][0] * 1);
		return fDezimal;
	}
	//Funktion liefert den Wert einer bestimmten Stelle des File Registers als 8 bit int Array zur�ck
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
	
	//Funktion liefert das W-Register als 8bit int Array zur�ck
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
	//Speicher f�r den programmcode; 0000 = Reset 0004 = interrupt
	protected int[] programMemoryIntArray = new int [1024];

	//enth�lt die Zeile des Befehl, default == 0
	protected int programCounterInt = 0;
	
	//W-Register 
	protected int[] wRegisterIntArray = new int [8]; 
	int wRegInt = 0;
	
	
	protected Stack <Integer>  cmdStack = new Stack <Integer>();
	
	public void SetStack()
	{
		cmdStack.push(programCounterInt +1 );
		System.out.println("Stack" + cmdStack.peek());
	}
	
	public int GetStack()
	{
		int stackInt = cmdStack.peek();
		cmdStack.pop();
		return stackInt;
		
	}
	//Controller Instanz erzeugen
	Controller ctr;
	
	public Memory(Controller controller) {
		ctr = controller;
	}
	
	//Increment Programmcounter
	public void IncPc()
	{
	programCounterInt = programCounterInt +1;
	System.out.println("PC: " + Integer.toHexString(programCounterInt));
	}
	//Set programmcounter (goto/Call)
	public void SetPC(int k)
	{ 
		programCounterInt = k;
		System.out.println("PC:" + programCounterInt);
	}
	
	//gibt den wert des w-registers als int zur�ck
	public int GetWInt()
	{
		return wRegInt;
	}
	
	
	//funktion zum setzen des DigitCarryFlag
	public void SetDigitCarry() 
	{
		dataMemoryIntArray[3][1] = 1;

	}
	
	//funktion zum R�cksetzen des DigitCarryFlag
	public void ResetDigitCarry() 
	{
		dataMemoryIntArray[3][1] = 0;

	}
	
	//funktion zum setzen des ZeroFlag
	public void SetzeroFlag() 
	{
		dataMemoryIntArray[3][2] = 1;

	}
	//funktion zum R�cksetzen des ZeroFlag
	public void ResetetzeroFlag() 
	{
		dataMemoryIntArray[3][2] = 0;
	}
	
	//funktion zum setzen des CarryFlag
	public void SetCarry() 
	{
		dataMemoryIntArray[3][0] = 1;

	}
	//funktion zum R�cksetzen des CarryFlag
	public void ResetCarry() 
	{
		dataMemoryIntArray[3][0] = 0;
	}
	
	public int GetCarry()
	{
		return 		dataMemoryIntArray[3][0];
	}
	
	//beschrieben des W-registers mit dem literal k
	public void WriteW(int erg) 
	{
		System.out.println("###" + "WriteW "+ erg + " ###" );
		wRegInt = erg;
	     int index = 0;
	     for(int i = 0; i <= (wRegisterIntArray.length)-1; i++)//ggf. �ber Zeroflag l�sen ???
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

	//beschreiben des code Sepichers
	public void CodeSpeichern(String pcSt, String codeSt)
	{
		
		System.out.println("pcST" + pcSt + " codeSt" + codeSt);
		int pcInt = Integer.parseInt(pcSt, 16);
		programMemoryIntArray[pcInt] = Integer.parseInt(codeSt, 16);
		System.out.println("CODESpeicherARRAY" + programMemoryIntArray[pcInt]);

	}
	
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
}
