import java.util.Stack;

public class Memory extends Thread{
	
	//Datenspeicher: erste Bank 00-7F zweíte Bank 80-FF
	protected int[][] dataMemoryIntArray = new int [256][8];
	
	public void WriteF(int erg, int f)
	{
		System.out.println("### "+ erg + " ###" );
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
	       System.out.println("F-Register:" + dataMemoryIntArray[f][i]);
	     }
	}
	
	public int GetF(int f) 
	{
		int fDezimal;
		fDezimal= (dataMemoryIntArray[f][7] * 128) + (dataMemoryIntArray[f][6] * 64) + (dataMemoryIntArray[f][5] * 32) + (dataMemoryIntArray[f][4] * 16) + (dataMemoryIntArray[f][3] * 8) + (dataMemoryIntArray[f][2] * 4) + (dataMemoryIntArray[f][1] * 2) + (dataMemoryIntArray[f][0] * 1);
		System.out.println("Get FileRegsiter" + fDezimal);
		
		return fDezimal;
	}
	//Speicher für den programmcode; 0000 = Reset 0004 = interrupt
	protected int[] programMemoryIntArray = new int [1024];

	//enthält die Zeile des Befehl, default == 0
	protected int programCounterInt = 0;
	
	//W-Register 
	protected int[] wRegisterIntArray = new int [8]; 
	int wRegInt = 0;
	
	//status Register
	protected int[] statusRegisterIntArray = new int [8];
	//statusRegisterIntArray[3] = 1; !!!!
	//statusRegisterIntArray[4] = 1;
	
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
	System.out.println("PC:" + programCounterInt);
	}
	//Set programmcounter (goto/Call)
	public void SetPC(int k)
	{ 
		programCounterInt = k;
		System.out.println("PC:" + programCounterInt);
	//programCounterInt = anyValue;
	}
	
	//gibt den wert des w-registers als int zurück
	public int GetWInt()
	{
		return wRegInt;
	}
	
	
	//funktion zum setzen des DigitCarryFlag
	public void SetDigitCarry() 
	{
		statusRegisterIntArray[1] = 1;
	}
	
	//funktion zum Rücksetzen des DigitCarryFlag
	public void ResetDigitCarry() 
	{
		statusRegisterIntArray[1] = 0;
	}
	
	//funktion zum setzen des ZeroFlag
	public void SetzeroFlag() 
	{
		statusRegisterIntArray[2] = 1;
	}
	//funktion zum Rücksetzen des ZeroFlag
	public void ResetetzeroFlag() 
	{
		statusRegisterIntArray[2] = 0;
	}
	
	//funktion zum setzen des CarryFlag
	public void SetCarry() 
	{
		statusRegisterIntArray[0] = 1;
	}
	//funktion zum Rücksetzen des CarryFlag
	public void ResetCarry() 
	{
		statusRegisterIntArray[0] = 0;
	}
	
	//beschrieben des W-registers mit dem literal k
	public void WriteW(int erg) 
	{
		System.out.println("WriteW "+ erg + " ###" );
		wRegInt = erg;
	     int index = 0;
	     for(int i = 0; i <= (wRegisterIntArray.length)-1; i++)//ggf. über Zeroflag lösen ???
	     {
	    	 wRegisterIntArray[i] = 0;
	     }
	     while(erg > 0){
	       wRegisterIntArray[index++] = erg % 2;
	       erg = erg/2;
	     }
	     for(int i = wRegisterIntArray.length -1;i >= 0;i--){
	       System.out.println("W-Register:" + wRegisterIntArray[i]);
	     }
	}
	public void Print(int temp,int erg,int k) //DEBUG => CONTROLLER => SUBLW
	{
		System.out.println("temp: " + temp + " k: "  + k + " erg: " + erg);
	}
	//beschreiben des code Sepichers
	public void CodeSpeichern(String pcSt, String codeSt)
	{
		
		System.out.println("pcST" + pcSt + " codeSt" + codeSt);
		int pcInt = Integer.parseInt(pcSt, 16);
		programMemoryIntArray[pcInt] = Integer.parseInt(codeSt, 16);
		System.out.println("CODESpeicherARRAY" + programMemoryIntArray[pcInt]);
		//return programMemoryIntArray;
	}
}
