import java.util.Stack;

public class Memory extends Thread{
	
	//Datenspeicher: erste Bank 00-7F zwe�te Bank 80-FF
	protected int[][] dataMemoryIntArray = new int [256][8];
	
	//Speicher f�r den programmcode; 0000 = Reset 0004 = interrupt
	protected int[] programMemoryIntArray = new int [1024];

	//enth�lt die Zeile des Befehl, default == 0
	protected int programCounterInt = 0;
	
	//W-Register 
	protected int[] wRegisterIntArray = new int [8]; 
	int wRegInt = 0;
	
	//status Register
	protected int[] statusRegisterIntArray = new int [8];
	//statusRegisterIntArray[3] = 1; !!!!
	//statusRegisterIntArray[4] = 1;
	
	protected Stack <Integer>  cmdStack = new Stack <Integer>();
	
	//Controller Instanz erzeugen
	Controller ctr;
	
	public Memory(Controller controller) {
		ctr = controller;
	}
	
	//Increment Programmcounter
	public void IncPc()
	{
	programCounterInt = programCounterInt +1;
	System.out.println("PC:" + programCounterInt);
	}
	//Set programmcounter (goto/Call)
	public void SetPC(int k)
	{ 
		programCounterInt = k;
		System.out.println("PC:" + programCounterInt);
	//programCounterInt = anyValue;
	}
	
	//gibt den wert des w-registers als int zur�ck
	public int GetWInt()
	{
		return wRegInt;
	}
	
	//funktion zum setzen des DigitCarryFlag
	public void SetDigitCarry() 
	{
		statusRegisterIntArray[1] = 1;
	}
	
	//funktion zum setzen des ZeroFlag
	public void SetzeroFlag() 
	{
		statusRegisterIntArray[2] = 1;
	}
	
	//funktion zum setzen des CarryFlag
	public void SetCarry() 
	{
		statusRegisterIntArray[0] = 1;
	}
	
	//beschrieben des W-registers mit dem literal k
	public void WriteW(int k) 
	{
		wRegInt = k;
	     int index = 0;
	     while(k > 0){
	       wRegisterIntArray[index++] = k%2;
	       k = k/2;
	     }
	     for(int i = wRegisterIntArray.length -1;i >= 0;i--){
	       System.out.println("W-Register:" + wRegisterIntArray[i]);
	     }
	}
	
	//beschreiben des code Sepichers
	public int[] CodeSpeichern(String[][] tableStArray)
	{
		int i = 0;

		while(tableStArray[i][3] != null)
		{
			if(! tableStArray[i][1].equals("    "))
			{
				programMemoryIntArray[Integer.parseInt(tableStArray[i][1],16)] = Integer.parseInt(tableStArray[i][2],16);
				//System.out.println("Stelle "+ Integer.parseInt(tableStArray[i][1],16) + ":"+ programMemoryIntArray[Integer.parseInt(tableStArray[i][1],16)]);				
			}

			i++;
		}
		return programMemoryIntArray;
	}
}
