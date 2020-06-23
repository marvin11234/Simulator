import java.util.Arrays;
import java.util.Stack;

public class Memory extends Thread {

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Controller Instanz erzeugen
	 * 
	 * 
	 * #########################################################################
	 */
	Controller ctr;

	public Memory(Controller controller) {
		ctr = controller;
	}
	
	public void run() {
		while(true) {
			for(int i = 0; i <256; i++)
			{
				ctr.getGui().updateGPR(i/8, i%8, this.GetF(i));
				ctr.getGui().updateTrisB(GetTRISB());
				ctr.getGui().updateTrisA(GetTRISA());
				ctr.getGui().updateStack(intStack);
				ctr.getGui().updateFlags(GetFlags());
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	/*
	 * ############################################################################
	 * 
	 * Datenspeicher: erste Bank 00-7F zweï¿½te Bank 80-FF
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	protected int[][] dataMemoryIntArray = new int[256][8];

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Speicher fï¿½r den programmcode; 0000 = Reset 0004 = interrupt
	 * 
	 * 
	 * #########################################################################
	 */
	protected int[] programMemoryIntArray = new int[1024];

	/*
	 * ############################################################################
	 * 
	 * enthï¿½lt die Zeile des Befehl, default == 0 beschrieben ï¿½ber die funktion
	 * incpc
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	protected int programCounterInt = 0;
	

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * W-Register
	 * 
	 * 
	 * #########################################################################
	 */
	protected int[] wRegisterIntArray = new int[8];
	int wRegInt = 0;

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Stack
	 * 
	 * 
	 * #########################################################################
	 */
	protected int[] intStack = new int[8];
	private int intstackSize = 0;

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	private int dataLatchA = 0;
	private int dataLatchB = 0;

	/*
	 * ############################################################################
	 * 
	 * Beschreibt das F Register an der Stelle an der stelle f mit dem Inhalt erg
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	public void set_SRAM(int erg, int f)
	{
		
		int RP0 = this.dataMemoryIntArray[3][5];
		f = (RP0 << 7) | f;
		this.set_SRAM_Direct(erg,  f);
	}
	
	public void set_SRAM_Direct(int erg, int f)
	{
	
		String c = Integer.toBinaryString(erg);
		for(int l = c.length(); l < 8; l++) 
		{
		c = "0" + c;
		}
		for(int i = 7; i >= 0; i-- ) 
		{
		this.set_SRAM_Bit(f, 7-i, Integer.parseInt(""+c.charAt(i)));
		}
		System.out.println("F: " + Integer.toHexString(f) + " V: " + c);
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * Methode zum beschreiben des SRAM
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	public void set_SRAM_Bit(int f, int bit, int Wert) {

		switch (f) {
		// INDF
		case 0:
			WriteSRAM(0, bit, Wert);
			WriteSRAM(128, bit, Wert);
			break;
		// PCL
		case 2:
			WriteSRAM(2, bit, Wert);
			WriteSRAM(130, bit, Wert);
			int PCLATH = GetF(10);
			int PCL = GetF(2);
			programCounterInt = ((PCLATH & 0x1F) << 8) | PCL;
			break;
		// Status
		case 3:
			WriteSRAM(3, bit, Wert);
			WriteSRAM(131, bit, Wert);
			break;
		// FSR
		case 4:
			WriteSRAM(4, bit, Wert);
			WriteSRAM(132, bit, Wert);
			break;

		// PCLATH
		case 10:
			WriteSRAM(10, bit, Wert);
			WriteSRAM(138, bit, Wert);
			break;

		// INTCON
		case 11:
			WriteSRAM(11, bit, Wert);
			WriteSRAM(139, bit, Wert);
			break;
		default:
			if (dataMemoryIntArray[3][5] == 0) {
				// if TMR0 und Vorteiler
				if (f == 1 && getBitValue(129, 3) == 0) {
					ctr.getTimer().setVorteiler(0);
				} else if (f == 5) {
					if (Wert == 1) {
						dataLatchA = dataLatchA | (Wert << bit);
					} else {
						int mask = 0xff;
						mask = mask ^ (0x01 << bit);
						dataLatchA = dataLatchA & mask;
					}
				}
				// PortA in Datalatch
				else if (f == 6) {
					if (Wert == 1) {
						dataLatchB = dataLatchB | (Wert << bit);
					} else {
						int mask = 0xff;
						mask = mask ^ (0x01 << bit);
						dataLatchB = dataLatchB & mask;
					}
				}

				WriteSRAM(f, bit, Wert);
			} else if ((dataMemoryIntArray[3][5] == 1) && (f < 128)) {
				WriteSRAM(f + 128, bit, Wert);

			} else {
				WriteSRAM(f, bit, Wert);
			}
			break;
		}

	}

	/*
	 * ############################################################################
	 * 
	 * direktes beschrieben einer stelle des SRAM
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	private void WriteSRAM(int sAdresse, int sStelle, int sWert) {;
		dataMemoryIntArray[sAdresse][sStelle] = sWert;

	}

	/*
	 * ############################################################################
	 * 
	 * funktion liefert den wert einer stelle f des Filoeregisters als int zurï¿½ck
	 * 
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	public int GetF(int f) {
		String c = "";
		for(int i = 0; i < 8; i++) 
		{
		c = c + dataMemoryIntArray[f][7-i];
		}
		return Integer.parseInt(c,2);
	}
//bit direkt kriegen
	protected int get_Memory(int fileaddress, int bit) 
	{
		if(dataMemoryIntArray[3][5] == 0) 
		{
			return dataMemoryIntArray[fileaddress][bit];
		}else  if((dataMemoryIntArray[3][5] == 1) && (fileaddress < 128))
		{
			return dataMemoryIntArray[fileaddress+128][bit];
		}else 
		{
			return 0;
		}
	}
	/*
	 * ############################################################################
	 * 
	 * 
	 * 
	 * Funktion liefert den Wert einer bestimmten Stelle des File Registers als 8
	 * bit int Array zurï¿½ck
	 * 
	 * 
	 * #########################################################################
	 */
	public int[] GetFBin(int f) {
		int[] testArray = new int[8];
		int i = 0;
		while (i <= 7) {
			testArray[i] = dataMemoryIntArray[f][i];

			i++;
		}
		return testArray;
	}

	/*
	 * ############################################################################
	 * 
	 * 
	 * Funktion liefert das W-Register als 8bit int Array zurï¿½ck
	 * 
	 * 
	 * 
	 * #########################################################################
	 */
	public int[] GetWBin() {
		int[] testArray = new int[8];
		int i = 0;
		while (i <= 7) {
			testArray[i] = wRegisterIntArray[i];

			i++;
		}
		return testArray;
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * beschrieben des Stack
	 * 
	 * 
	 * #########################################################################
	 */
/*	public void SetStack() {
		cmdStack.push(programCounterInt + 1);
		System.out.println("Stack: " + cmdStack.peek());
	}
*/
	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Beschreiben des W Registers mit dem Literal K
	 * 
	 * 
	 * #########################################################################
	 */
	public void WriteW(int erg) {
		System.out.println("###" + "WriteW " + erg + " ###");
		wRegInt = erg;
		int index = 0;
		for (int i = 0; i <= (wRegisterIntArray.length) - 1; i++)// ggf. ï¿½ber Zeroflag lï¿½sen ???
		{
			wRegisterIntArray[i] = 0;
		}
		while (erg > 0) {
			wRegisterIntArray[index++] = erg % 2;
			erg = erg / 2;
		}
		for (int i = wRegisterIntArray.length - 1; i >= 0; i--) {
			System.out.println("W-Register:" + wRegisterIntArray[i]);
		}
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * rï¿½ckgabe des W-Register Wertes als Integer Wert
	 * 
	 * 
	 * #########################################################################
	 */
	public int GetWInt() {
		return wRegInt;
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * gibt Wert des Stack als int variable stackInt zurï¿½ck
	 * 
	 * 
	 * #########################################################################
	 */
	/*public int GetStack() {
		int stackInt = cmdStack.peek();
		cmdStack.pop();
		return stackInt;
	}

	public int ShowStack() {
		int stackInt = 0;
		if (cmdStack.isEmpty() == true) {
			stackInt = 0;
		} else {
			stackInt = cmdStack.peek();
		}

		return stackInt;
	}

	public Stack<Integer> getStack()
	{
		return cmdStack;
	}*/
	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * erhï¿½ht den programmcounter
	 * 
	 * 
	 * #########################################################################
	 */
	public void IncPc() {
		programCounterInt = programCounterInt + 1;
		System.out.println("PC: " + Integer.toHexString(programCounterInt));
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Setzt den Programmcounter auf einen bestimmten Wert wird fï¿½r Call und Goto
	 * bennï¿½tigt
	 * 
	 * #########################################################################
	 */
	public void SetPC(int k) {
		programCounterInt = k;
		System.out.println("PC:" + programCounterInt);
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion setzt das DigitCarryFlag bei ï¿½berlauf des 4 bits im Hauptspeicher
	 * (Status register bit 0)
	 * 
	 * #########################################################################
	 */
	public void SetDigitCarry() {
		dataMemoryIntArray[3][1] = 1;

	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion setzt das DigitCarryFlag zurï¿½ck (auf 0) Hauptspeicher (Status
	 * register bit 1)
	 * 
	 * #########################################################################
	 */
	public void ResetDigitCarry() {
		dataMemoryIntArray[3][1] = 0;

	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion setzt das Zeroflag wenn ergebniss einer operation == 0 im
	 * Hauptspeicher (Status register bit 2)
	 * 
	 * #########################################################################
	 */
	public void SetzeroFlag() {
		dataMemoryIntArray[3][2] = 1;

	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion setzt das Zeroflag auf 0 Hauptspeicher (Status register bit 2)
	 * 
	 * #########################################################################
	 */
	public void ResetetzeroFlag() {
		dataMemoryIntArray[3][2] = 0;
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion setzt das CarryFlag bei ï¿½berlauf im Hauptspeicher (Status register
	 * bit 0)
	 * 
	 * #########################################################################
	 */
	public void SetCarry() {
		dataMemoryIntArray[3][0] = 1;

	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion setzt das CarryFlag zurï¿½ck im Hauptspeicher (Status register bit 0)
	 * 
	 * #########################################################################
	 */
	public void ResetCarry() {
		dataMemoryIntArray[3][0] = 0;
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * Funktion liefert den aktuellen Wert der Im Carry steht Hauptspeicher (Status
	 * register bit 0)
	 * 
	 * #########################################################################
	 */
	public int getBitValue(int Adresse, int Stelle) {
		return dataMemoryIntArray[Adresse][Stelle];
	}

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * funktion zum direkten speicherzugriff (FSR)
	 * 
	 * 
	 * #########################################################################
	 */
	protected int WriteDirect(int f) {
		String c = "";
		for (int i = 0; i < 8; i++) {
			c = c + dataMemoryIntArray[f][7 - i];
		}
		return Integer.parseInt(c, 2);
	}

	public void InitMemoryPWROn() {
		// Status Register

		// PD
		dataMemoryIntArray[3][3] = 1;

		dataMemoryIntArray[131][3] = 1;

		// TO
		dataMemoryIntArray[3][4] = 1;
		dataMemoryIntArray[131][3] = 1;

		// TrisA auf Eingang
		dataMemoryIntArray[133][0] = 1;
		dataMemoryIntArray[133][1] = 1;
		dataMemoryIntArray[133][2] = 1;
		dataMemoryIntArray[133][3] = 1;
		dataMemoryIntArray[133][4] = 1;
		ctr.getGui().rdbtnTRISA0.setSelected(true);
		ctr.getGui().rdbtnTRISA1.setSelected(true);
		ctr.getGui().rdbtnTRISA2.setSelected(true);
		ctr.getGui().rdbtnTRISA3.setSelected(true);
		ctr.getGui().rdbtnTRISA4.setSelected(true);

		// TrisB auf Eingang
		dataMemoryIntArray[134][0] = 1;
		dataMemoryIntArray[134][1] = 1;
		dataMemoryIntArray[134][2] = 1;
		dataMemoryIntArray[134][3] = 1;
		dataMemoryIntArray[134][4] = 1;
		dataMemoryIntArray[134][5] = 1;
		dataMemoryIntArray[134][6] = 1;
		dataMemoryIntArray[134][7] = 1;
		ctr.getGui().rdbtnTRISB0.setSelected(true);
		ctr.getGui().rdbtnTRISB1.setSelected(true);
		ctr.getGui().rdbtnTRISB2.setSelected(true);
		ctr.getGui().rdbtnTRISB3.setSelected(true);
		ctr.getGui().rdbtnTRISB4.setSelected(true);
		ctr.getGui().rdbtnTRISB5.setSelected(true);
		ctr.getGui().rdbtnTRISB6.setSelected(true);
		ctr.getGui().rdbtnTRISB7.setSelected(true);


		// Option Register
		// PS0
		dataMemoryIntArray[129][0] = 1;
		// PS1
		dataMemoryIntArray[129][1] = 1;
		// PS2
		dataMemoryIntArray[129][2] = 1;
		// PSA
		dataMemoryIntArray[129][3] = 1;
		// TOSE
		dataMemoryIntArray[129][4] = 1;
		// TOCS
		dataMemoryIntArray[129][5] = 1;
		// INTEDG
		dataMemoryIntArray[129][6] = 1;
		// RBPU
		dataMemoryIntArray[129][7] = 1;
		
		//System.out.println("Option Register: " + dataMemoryIntArray[129][4]);
	}

	public void resetRam()
	{
		//löschen des SRam inhalts
		for(int i = 12; i <= 47; i++)
		{
			set_SRAM(0,i);
		}
		//löschen des Stack
		clearStack();
	}
	protected int get_Memory(int fileaddress) {
		String c = "";
		for (int i = 0; i < 8; i++) {
			if (dataMemoryIntArray[3][5] == 0) {
				c = c + dataMemoryIntArray[fileaddress][7 - i];
			} else if ((dataMemoryIntArray[3][5] == 1) && (fileaddress < 128)) {
				c = c + dataMemoryIntArray[fileaddress + 128][7 - i];
			} else {
				c = c + dataMemoryIntArray[fileaddress][7 - i];
			}
		}
		return Integer.parseInt(c, 2);
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
	
	
	public void checkSFR()
	{
		//carry Flag
		if(dataMemoryIntArray[3][0] == 1)
		{

			ctr.getGui().rdbtnCF.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnCF.setSelected(false);
		}
		
		//Dc Flag Gui
		if(dataMemoryIntArray[3][1] == 1)
		{

			ctr.getGui().rdbtnDC.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnDC.setSelected(false);
		}
		//ZF
		if(dataMemoryIntArray[3][2] == 1)
		{

			ctr.getGui().rdbtnZ.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnZ.setSelected(false);
		}
		//RP0
		if(dataMemoryIntArray[3][5] == 1)
		{

			ctr.getGui().rdbtnReg1.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnReg1.setSelected(false);
		}
	}
	public void checkRA()
	{
		if(dataMemoryIntArray[5][4] == 1)
		{

			ctr.getGui().rdbtnRA4.setSelected(true);
		}
		else
		{
			ctr.getGui().rdbtnRA4.setSelected(false);
		}
	}
	

	protected void pushToStack(int adr)
	{
		for(int i = 7; i> 0; i--)
		{
			intStack[i] = intStack[i-1];
		}
		intStack[0] = adr;
		if(this.intstackSize <8)
		{
			this.intstackSize +=1;
		}
	}
	
	protected int popFromStack() 
	{
		this.intstackSize -= 1;
		int retVal = intStack[0];
		for(int i = 0; i < 7; i++) {
			intStack[i] = intStack[i + 1];
		}
		return retVal;
	}
	
	protected void clearStack() {
		this.intstackSize = 0;
		for (int i = 0; i < 8; i++) {
			intStack[i] = 0;
		}
	}
	protected int[] getStack()
	{
		return intStack;
	}
	
	
	
	//getter for TrisB
	public int[] GetTRISB()
	{
		return this.GetFBin(0x86);
	}
	//Getter for TrisB
	private int[] GetTRISA() {
		return this.GetFBin(0x85);
	}
	private int[] GetFlags()
	{
		return this.GetFBin(0x03);
	}
	protected void checkDCFlag(int in_1, int in_2) 
	{
		if (((in_1 & 0x0F) + (in_2 & 0x0F)) > 0x0F) {
			SetDigitCarry();
		}
		else {
			ResetDigitCarry();
		}
	}
}
