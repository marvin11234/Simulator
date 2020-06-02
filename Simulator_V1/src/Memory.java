import java.util.Stack;

public class Memory extends Thread{
	
	//Datenspeicher: erste Bank 00-7F zweíte Bank 80-FF
	protected int[][] dataMemoryIntArray = new int [256][8];
	
	//Speicher für den programmcode; 0000 = Reset 0004 = interrupt
	protected int[] programMemoryIntArray = new int [1024];

	//enthält die Zeile des Befehl, default == 0
	protected int programCounterInt = 0;
	
	//W-Register 
	protected int[] wRegisterIntArray = new int [8]; 
	
	protected Stack <Integer>  cmdStack = new Stack <Integer>();
	
	int i = 0;
	
	Controller ctr;
	
	public Memory(Controller controller) {
		ctr = controller;
	}
	
	public void IncPc()
	{
	programCounterInt = programCounterInt +1;
	}
	public void SetPC(int k)
	{ 
		programCounterInt = k;
	//programCounterInt = anyValue;
	}

	public int[] CodeSpeichern(String[][] tableStArray)
	{

		while(tableStArray[i][3] != null)
		{
			if(! tableStArray[i][1].equals("    "))
			{
				programMemoryIntArray[Integer.parseInt(tableStArray[i][1],16)] = Integer.parseInt(tableStArray[i][2],16);
				System.out.println("Stelle "+ Integer.parseInt(tableStArray[i][1],16) + ":"+ programMemoryIntArray[Integer.parseInt(tableStArray[i][1],16)]);				
			}

			i++;
		}
		return programMemoryIntArray;
	}
}
