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
	
	protected Stack <Integer>  cmdStack = new Stack <Integer>();
	
	public void CodeSpeichern(String[] programmSpeicherStArray)
	{
			int test = Integer.parseInt(programmSpeicherStArray[2],16);
			System.out.println("test" + test);
		
	}
}
