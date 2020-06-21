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

	/*
	 * ############################################################################
	 * 
	 * Datenspeicher: erste Bank 00-7F zwe�te Bank 80-FF
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
	 * Speicher f�r den programmcode; 0000 = Reset 0004 = interrupt
	 * 
	 * 
	 * #########################################################################
	 */
	protected int[] programMemoryIntArray = new int[1024];

	/*
	 * ############################################################################
	 * 
	 * enth�lt die Zeile des Befehl, default == 0 beschrieben �ber die funktion
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
	protected Stack<Integer> cmdStack = new Stack<Integer>();

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
	public void WriteSRAM(int sAdresse, int sStelle, int sWert) {
		dataMemoryIntArray[sAdresse][sStelle] = sWert;

	}

	/*
	 * ############################################################################
	 * 
	 * funktion liefert den wert einer stelle f des Filoeregisters als int zur�ck
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

	/*
	 * ############################################################################
	 * 
	 * 
	 * 
	 * Funktion liefert den Wert einer bestimmten Stelle des File Registers als 8
	 * bit int Array zur�ck
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
	 * Funktion liefert das W-Register als 8bit int Array zur�ck
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
	public void SetStack() {
		cmdStack.push(programCounterInt + 1);
		System.out.println("Stack: " + cmdStack.peek());
	}

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
		for (int i = 0; i <= (wRegisterIntArray.length) - 1; i++)// ggf. �ber Zeroflag l�sen ???
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
	 * r�ckgabe des W-Register Wertes als Integer Wert
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
	 * gibt Wert des Stack als int variable stackInt zur�ck
	 * 
	 * 
	 * #########################################################################
	 */
	public int GetStack() {
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

	/*
	 * ############################################################################
	 * 
	 *
	 * 
	 * erh�ht den programmcounter
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
	 * Setzt den Programmcounter auf einen bestimmten Wert wird f�r Call und Goto
	 * benn�tigt
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
	 * Funktion setzt das DigitCarryFlag bei �berlauf des 4 bits im Hauptspeicher
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
	 * Funktion setzt das DigitCarryFlag zur�ck (auf 0) Hauptspeicher (Status
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
	 * Funktion setzt das CarryFlag bei �berlauf im Hauptspeicher (Status register
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
	 * Funktion setzt das CarryFlag zur�ck im Hauptspeicher (Status register bit 0)
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
		
		System.out.println("Option Register: " + dataMemoryIntArray[129][4]);
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
		if(((dataMemoryIntArray[3][5] == 0) & (ctr.getGui().Reg1Status == false)) | (dataMemoryIntArray[3][5] == 1) & (ctr.getGui().Reg1Status == true) )
		{
		}
		else if(((dataMemoryIntArray[3][5] == 0) & (ctr.getGui().Reg1Status == true)) | (dataMemoryIntArray[3][5] == 1) & (ctr.getGui().Reg1Status == false))
		{
			ctr.getGui().SetRP0Gui((dataMemoryIntArray[3][5]));
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
