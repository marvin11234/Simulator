import javax.swing.JFileChooser;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;




public class Controller {
	
	private Simulator_Window gui; 
	private Prozessor proc; 
	private Memory memo;
	private Timer tmr;
	private Interrupt intp;
	
	private boolean processorRunning = false; 
	
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	int arrayStelleEinlesenInt = 0;
	String[] inputStArray = new String[1024];
	String[] programmSpeicherStArray = new String[1024];
	String temCurLnSt;
	String[][] tableStArray = new String[1024][6];
	int zeileInt = 0;
	int i = 0;
	int[] befehlDezimalIntArray = new int[1024];

	//Array für PC an denen ein BreakPoint liegt
	int bP[] = new int[20];
	int helper = 0;
	double laufzeit = 0;
	
	//gui und memo Instanz erzeugen
	public Controller(Simulator_Window simulator_Window) {
		gui = simulator_Window;
		memo = new Memory(this);
		tmr = new Timer(this);
		intp = new Interrupt(this);
	}
	

	public void initialize() {
		gui.InitGprView();
		gui.InitStackView();
		memo.InitMemoryPWROn();
		memo.start();
	}



	/*############################################################################
	 * 
	 *
	 * 
	 * Einlesen des File und beschrieben des Speichers mit dem Programmcode
	 * 
	 * 
	 * #########################################################################*/
	public void Einlesen() throws Exception{
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get the file
			java.io.File file = fileChooser.getSelectedFile();
			//Default buffer reader
			BufferedReader br = null;
			try 
			{ 
				br = new BufferedReader(new FileReader(file));
				
				String currentLineSt;
				this.initBreakpointSpeicher();
				//clearBreakPointList();
				clearCodeTable();
				
				while ((currentLineSt = br.readLine()) != null)
				{
					String pcSt = currentLineSt.substring(0,4);
					String codeSt = currentLineSt.substring(5,9);
					String linecountSt = currentLineSt.substring(20,25);
					String label = "";
					if(currentLineSt.charAt(27) != ' ')
					{
						int index = 27;
						while(currentLineSt.charAt(index) != ' ')
						{
							label = label + currentLineSt.charAt(index);
							index ++;
						}
					}
					if(label.isEmpty())
					{
						gui.tblCode.addRow(new Object[] {"",pcSt, codeSt, linecountSt, label,currentLineSt.substring(36)});
					}
					else
					{
						gui.tblCode.addRow(new Object[] {"",pcSt, codeSt, linecountSt, label,""});
					}
					if(! currentLineSt.substring(0,4).equals("    "))
					{
						int CodeLenght = Integer.parseInt(currentLineSt.substring(0,4),16);
						int pc =Integer.parseInt(pcSt ,16);
						int code = Integer.parseInt(codeSt ,16);
						this.getMemo().programMemoryIntArray[pc] = code;
						

					}
					
			
				}
				laufzeit = 0;
				getMemo().SetPC(0);
						
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else 
		{
			sb.append("No file was choosen");
		}
	}

	
	public void clearCodeTable() 
	{
		int rows = getGui().getTblCodeModel().getRowCount();
    	for(int i = 0; i<rows; i++) 
    	{
        	getGui().getTblCodeModel().removeRow(0);  		
    	}
	}
	
	
	//#########################################################################
	//
	//
	//
	//
	//
	//
	//Befehlsimplementierung
	//
	//
	//
	//
	//
	//#########################################################################	
	public void addwf(int d, int f)throws Exception	//BEEINFLUSST C; DC; Z
	{
		System.out.println("addwf");
		int temp = getMemo().GetWInt();
		int temp2 = getMemo().GetF(f);
		int erg = temp + temp2;


		if(erg > 255)
		{
			memo.SetCarry();
			erg = erg -256;
		}
		else
		{
			getMemo().ResetCarry();
		}
		
		Zeroflag(erg);
		getMemo().checkDCFlag(temp,temp2);
		
		if (d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)	//ERGEBNIS IN F SPEICHERN ?
		{
			getMemo().set_SRAM(erg,f);
		}
		getMemo().IncPc();
	}
	
	public void andwf(int d, int f)throws Exception //BEEINFLUSST Z
	{
		System.out.println("andwf");
		int temp = getMemo().GetWInt();
		int temp2 = getMemo().GetF(f);
		int erg = (temp & temp2);

		Zeroflag(erg);
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void clrw()throws Exception //BEEINFLUSST Z
	{
		System.out.println("clrw");
		int erg = 00;
		getMemo().WriteW(erg);
		Zeroflag(erg);
		getMemo().IncPc();
	}
	
	public void clrf(int f)throws Exception //BEEINFLUSST Z
	{
		System.out.println("clrf");
		int erg = 0;
		memo.set_SRAM(erg, f);
		Zeroflag(erg);
		getMemo().IncPc();
	}
	
	public void comf(int d, int f)throws Exception //BEEINFLUSST Z
	{
		System.out.println("comf");
		int[] ergInt = getMemo().GetFBin(f);
		int i = 0;
		while( i <= 7)
		{
			if(ergInt[i] == 0)
			{
				ergInt[i] = 1;
			}
			else if (ergInt[i] == 1)
			{
				ergInt[i] = 0;
			}
			i++;
		}
			//r�ckumwandlung zu int !!!ggf.funktion zum schreiben als array
			int erg = (ergInt[7] * 128) + (ergInt[6] * 64) + (ergInt[5] * 32) + (ergInt[4] * 16) + (ergInt[3] * 8) + (ergInt[2] * 4) + (ergInt[1] * 2) + (ergInt[0] * 1);
			
			Zeroflag(erg);

			
			if(d == 0 )
			{
				getMemo().WriteW(erg);
			}
			else if (d == 1)
			{
				memo.set_SRAM(erg, f);
			}
		getMemo().IncPc();
	}
	
	public void decf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		System.out.println("decf");
		int temp = getMemo().GetF(f);
		int erg = 0; 
		
		if(temp != 0)
		{
			erg = temp -1; //TODO Testen ob funkton f�r temp != 0 funktioniert!!!
		}
		else if( temp == 0)
		{
			erg = 255;
		}
		
		Zeroflag(erg);
	
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void decfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("decfsz");
		int erg = getMemo().GetF(f);
	
		if(erg == 0)
		{
			erg = 255;
		}
		else
		{
			erg--;
			if(erg == 0)
			{
				getMemo().IncPc();
				this.proc.setNop(true);
			}
		}
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void incf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		System.out.println("incf");
		int temp = getMemo().GetF(f);
		
		if(temp == 255) 
		{
			temp = 0;
			getMemo().SetzeroFlag();
		}else {
			temp++;
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(temp);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(temp, f);
		}
		getMemo().IncPc();
	}
	
	public void incfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("incfsz");
		int temp = getMemo().GetF(f);
		int erg = 0; 
		
		if(temp == 255) 
		{
			temp = 0;
			getMemo().IncPc();
			this.proc.setNop(true);

		}
		else 
		{
			temp++;
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(temp);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(temp, f);
		}
		getMemo().IncPc();
	}
	
	public void iorwf(int d, int f)throws Exception // BEEINFLUSST Z	
	{
		System.out.println("iorwf");
		int temp =  getMemo().GetWInt();
		int temp2 = getMemo().GetWInt();
		int erg = temp | temp2 ; 
		
		Zeroflag(erg);
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void sleep()throws Exception  //BEEINFLUSST KEINE STATI
	{
		System.out.println("sleep");
		getMemo().IncPc();
	}
	
	public void Return()throws Exception //BEEINFLUSST KEINE STATI
	{
		int k  = getMemo().popFromStack();
		System.out.println("Return " + k + "1");
		//PrintAfterPop();
		
		System.out.println("Return " + k + "2");
		
		getMemo().SetPC(k);
		System.out.println("Return " + k + "3" );
		getMemo().IncPc();
		this.proc.setNop(true);
	}
	
	public void retfie()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("retfie");
		getMemo().dataMemoryIntArray[0x0b][7] = 1;
		getMemo().SetPC(getMemo().popFromStack());
		this.proc.setNop(true);
	}
	
	public void clrwdt()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("addclrwdtwf");
		getMemo().IncPc();
	}
	
	public void nop()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("nop");
		getMemo().IncPc();
	}
	
	public void movwf(int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("movwf");
		int erg = getMemo().GetWInt();
		getMemo().set_SRAM(erg,f);
		getMemo().IncPc();
	}
	
	public void rlf(int d, int f)throws Exception //BEEINFLUSST C
	{
		System.out.println("rlf");
		int temp = getMemo().GetF(f);
		int carry = getMemo().getBitValue(3, 0);
	
		if ((temp & 128) == 128) {
			getMemo().SetCarry();
		}
		else {
			getMemo().ResetCarry();
		}
		int erg = ((temp << 1) & 0xFF) | (carry&1);
			if(d == 0 )
			{
				getMemo().WriteW(erg);
			}
			else if (d == 1)
			{
				getMemo().set_SRAM(erg, f);
			}
		getMemo().IncPc();
	}
	
	public void rrf(int d, int f)throws Exception //BEEINFLUSST C
	{
		System.out.println("rrf");
		int temp = getMemo().GetF(f); 
		int carry = getMemo().getBitValue(3, 0);
		
		System.out.println("rrf carry: " + carry + " temp f: " + temp);
	
		if ((temp & 1) == 1) {
			getMemo().SetCarry();
		}
		else {
			getMemo().ResetCarry();
		}
		int erg = (temp >> 1) | (carry << 7);
		
		System.out.println("rrf erg: " + erg);
			
		if(d == 0 )
			{
				getMemo().WriteW(erg);
			}
			else if (d == 1)
			{
				getMemo().set_SRAM(erg, f);
			}
		getMemo().IncPc();
	}
	
	public void subwf(int d, int f)throws Exception //BEEINFLUSST C, DC, Z
	{
		System.out.println("subwf");
		int temp =  getMemo().GetWInt();
		int temp2 = getMemo().GetF(f);
		int erg = 0;
		
		if(temp > temp2) 
		{
			erg = 256 - (temp-temp2);
		}else {
			erg = temp2 - temp;
		}
		if( temp2 -temp < 0 )
		{
			getMemo().ResetCarry();
		}
		else
		{
			getMemo().SetCarry();
		}
		
		Zeroflag(erg);
		getMemo().checkDCFlag(temp,temp2);
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void swapf(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("swapf");
		int temp = getMemo().GetF(f);
		int erg = ((temp & 0x0F) << 4) | ((temp & 0xF0) >> 4);
		
		if(erg == 0)
		{
			getMemo().SetzeroFlag();
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void movf(int d, int f)throws Exception  //BEEINFLUSST  Z
	{
		System.out.println("movf");
		int erg = getMemo().GetF(f); 
		
		Zeroflag(erg);
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	public void xorwf(int d, int f)throws Exception //BEEINFLUSST  Z
	{
		System.out.println("xorwf");
		int temp =  getMemo().GetWInt();
		int temp2 = getMemo().GetF(f);
		
		int erg = (temp ^ temp2); 
		
		Zeroflag(erg);
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
		}
		getMemo().IncPc();
	}
	
	
	public void bcf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bcf");
		getMemo().set_SRAM_Bit(f, b,0);
		getMemo().IncPc();
	}
	public void bsf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bsf");
		getMemo().set_SRAM_Bit(f, b,1);
		getMemo().IncPc();
	}
	public void btfsc(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfsc");
		int in = getMemo().get_Memory(f, b);
		if(in == 0) 
		{
			getMemo().IncPc();
			this.proc.setNop(true);
		}
		getMemo().IncPc();
	}
	public void btfss(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfss");
		int in = getMemo().get_Memory(f, b);
		if(in == 1) 
		{
			getMemo().IncPc();
			this.proc.setNop(true);
		}
		getMemo().IncPc();;
	}
	
	public void call(int k)throws Exception //BEEINFLUSST KEINE STATI
	{	
		System.out.println("call" + k );
		getMemo().pushToStack(k);
		getMemo().SetPC(k);
		this.proc.setNop(true);


	}
	public void Goto(int k)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("Goto");
		this.proc.setNop(true);
		getMemo().SetPC(k);
	}
	public void addlw(int k)throws Exception //BEEINFLUSST  C, DC, Z
	{
		System.out.println("addlw");
		int temp = getMemo().GetWInt();
		int erg = temp + k;
		
		if(erg > 255)
		{
			getMemo().SetCarry();
			erg = erg - 256;
		}
		else
		{
			getMemo().ResetCarry();
		}
		
		Zeroflag(erg);
		getMemo().checkDCFlag(temp,k);
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void iorlw(int k)throws Exception  //BEEINFLUSST  C, DC, Z
	{
		System.out.println("iorlw");
		int temp = getMemo().GetWInt();
		int erg  = (temp | k);
		
		
		Zeroflag(erg);
		getMemo().checkDCFlag(temp,k);
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void movlw(int k)throws Exception //BEEINFLUSST  KEINE STATI	
	{
		System.out.println("movlw");
		int erg = k;
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void retlw(int k)throws Exception //BEEINFLUSST  KEINE STATI	
	{	
		System.out.println("retlw");
		int erg = k;
		System.out.println("RetLW 1 " + erg );
		getMemo().WriteW(erg);
		k  = getMemo().popFromStack();
		System.out.println("RetLW 2" + k);
		//PrintAfterPop();
		getMemo().SetPC(k);

		getMemo().IncPc();
		this.proc.setNop(true);
	}
	public void sublw(int k)throws Exception //BEEINFLUSST  C, DC, Z
	{
		System.out.println("sublw");
		int temp = getMemo().GetWInt();
		int erg = 0;
		
		if(temp > k)
		{
			erg = 256 -(temp-k);
			getMemo().ResetCarry();
		}
		else
		{
			erg = k-temp;
			getMemo().SetCarry();
		}
		getMemo().checkDCFlag(temp,k);
		Zeroflag(erg);
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void xorlw(int k)throws Exception //BEEINFLUSST Z
	{
		System.out.println("xorlw");
		int temp = getMemo().GetWInt();
		int erg = temp ^ k  ;
		
		Zeroflag(erg);
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	
	public void andlw(int k)throws Exception //BEEINFLUSST Z
	{
		System.out.println("andlw");
		int temp = getMemo().GetWInt();
		int erg = (temp & k);
		
		Zeroflag(erg);
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	


	
	//#########################################################################
	//
	//
	//
	//
	//
	//
	//Getter und Setter Methoden
	//
	//
	//
	//
	//
	//#########################################################################
	protected int getVorteiler() 
	{	
		return (getMemo().get_Memory(0x81) & 0x07);
	}
	

	public void SetCarry(int value)
	{
		getMemo().SetCarryBit(value);
	}
	
	public void SetDigitCarry(int value)
	{
		getMemo().SetDigitCarryBit(value);
	}
	
	public void SetZero(int value)
	{
		getMemo().SetZeroBit(value);
	}
	
	public void SetPowerDown(int value)
	{
		getMemo().SetPowerDownBit(value);
	}
	
	public void SetTo(int value)
	{
		getMemo().SetToBit(value);
	}
	
	public void SetReg1(int value)
	{
		getMemo().SetReg1Bit(value);
	}


	public void SetReg2(int value)
	{
		getMemo().SetReg2Bit(value);
	}
	
	// TRIS A
	public void SetTRISA0(int value)
	{
		getMemo().SetTRISA0Bit(value);
	}
	
	public void SetTRISA1(int value)
	{
		getMemo().SetTRISA1Bit(value);
	}
	public void SetTRISA2(int value)
	{
		getMemo().SetTRISA2Bit(value);
	}
	public void SetTRISA3(int value)
	{
		getMemo().SetTRISA3Bit(value);
	}
	public void SetTRISA4(int value)
	{
		getMemo().SetTRISA4Bit(value);
	}
	//Anzeige IO-Ports TRIS A
	public void SetTRISAIO0(int value)
	{
		getMemo().SetTRISAIO0Bit(value);
	}
	public void SetTRISAIO1(int value)
	{
		getMemo().SetTRISAIO1Bit(value);
	}
	public void SetTRISAIO2(int value)
	{
		getMemo().SetTRISAIO2Bit(value);
	}
	public void SetTRISAIO3(int value)
	{
		getMemo().SetTRISAIO3Bit(value);
	}
	public void SetTRISAIO4(int value)
	{
		getMemo().SetTRISAIO4Bit(value);
	}
	
	// TRIS B
	public void SetTRISB0(int value)
	{
		getMemo().SetTRISB0Bit(value);
	}
	public void SetTRISB1(int value)
	{
		getMemo().SetTRISB1Bit(value);
	}
	public void SetTRISB2(int value)
	{
		getMemo().SetTRISB2Bit(value);
	}
	public void SetTRISB03(int value)
	{
		getMemo().SetTRISB3Bit(value);
	}
	public void SetTRISB4(int value)
	{
		getMemo().SetTRISB4Bit(value);
	}
	public void SetTRISB5(int value)
	{
		getMemo().SetTRISB5Bit(value);
	}
	public void SetTRISB6(int value)
	{
		getMemo().SetTRISB6Bit(value);
	}
	public void SetTRISB7(int value)
	{
		getMemo().SetTRISB7Bit(value);
	}
	

	//TRISB IO
	public void SetTRISBIO0(int value)
	{
		getMemo().SetTRISBIO0Bit(value);
	}
	public void SetTRISBIO1(int value)
	{
		getMemo().SetTRISBIO1Bit(value);
	}
	public void SetTRISBIO2(int value)
	{
		getMemo().SetTRISBIO2Bit(value);
	}
	public void SetTRISBIO3(int value)
	{
		getMemo().SetTRISBIO3Bit(value);
	}
	public void SetTRISBIO4(int value)
	{
	
		getMemo().SetTRISBIO4Bit(value);
	}
	public void SetTRISBIO5(int value)
	{
		getMemo().SetTRISBIO5Bit(value);
	}
	public void SetTRISBIO6(int value)
	{
		getMemo().SetTRISBIO6Bit(value);
	}
	public void SetTRISBIO7(int value)
	{
		getMemo().SetTRISBIO7Bit(value);
	}
	
	public void initBreakpointSpeicher()
	{
	for(int i = 0; i <= bP.length-1; i++)
	{
	bP[i] = 400;
	}
	}
	
	
	
	//#########################################################################
	//
	//
	//
	//
	//
	//
	//Breakpoint
	//
	//
	//
	//
	//
	//#########################################################################
	
	public void CheckBreakPoint()
	{
		for (int i = 0; i<= bP.length -1 ;i ++)
		{
			if(bP[i]  == getMemo().programCounterInt)
			{
				//System.out.println("BP TEST AUSGABE " + bP[i] +" "+ getMemo().programCounterInt);
				stop();
			}
		}
	}
	

	public void SetBreakPoint(int row)
	{
	String breakPoint = "BP";
	String test;
	String test2;
	int testInt;
	int bPpc;

	if( ! gui.tblCode.getValueAt(row, 1).equals(" ")) //BREAKPOINT IN ZEILE MIT CODE ?
	{
		if( gui.tblCode.getValueAt(row, 0).equals(breakPoint)) //BREAKPOINT ENTFERNEN
		{
			gui.tblCode.setValueAt(" ", row, 0);
			for(int i = 0; i <= bP.length-1; i++)
			{
				test2 = String.valueOf((String) gui.tblCode.getValueAt(row, 1));
				testInt = Integer.parseInt(test2, 16);
				if(bP[i] == testInt)
			{
					bP[i]=400;
			}
		}
	}
	else //BREAKPOINT SETZEN
	{
	test = String.valueOf((String) gui.tblCode.getValueAt(row, 1));

	bPpc = Integer.parseInt(test, 16);
	bP[helper]= (bPpc);
	gui.tblCode.setValueAt(breakPoint, row, 0);
	helper++;
	}
	}
	}
	
	//#########################################################################
	//
	//
	//
	//
	//
	//
	//Laufzeit
	//
	//
	//
	//
	//
	//#########################################################################
	public void laufZeitBerechnung()
	{
		 
		if(getGui().takt == 500)
		{
			laufzeit = laufzeit + ((4*Math.pow(10, 6)/500000));
		}
		else if(getGui().takt == 1)
		{
			laufzeit = laufzeit + ((4*Math.pow(10, 6)/1000000));
		}
		else if(getGui().takt == 2)
		{
			laufzeit = laufzeit + ((4*Math.pow(10, 6)/2000000));
		}
		else if(getGui().takt == 3)
		{
			laufzeit = laufzeit + ((4*Math.pow(10, 6)/3000000));
		}	
		else if(getGui().takt == 4)
		{
			laufzeit = laufzeit + ((4*Math.pow(10, 6))/4000000);
		}
		gui.printLaufzeit(laufzeit);
		
		//System.out.println("LaufZeit:" + laufzeit);
	}
	
	//#########################################################################
	//
	//
	//
	//
	//
	//
	//Stop/Start Thread
	//
	//
	//
	//
	//
	//#########################################################################
	public void start() 
	{
		if(! this.processorRunning)
		{
			proc = new Prozessor(this);
			proc.start();
			this.processorRunning = true;
		}
		
	}

	public void stop()
	{
		if( this.processorRunning)
		{
			this.processorRunning = false;
			proc.stopThread();
		}
		
	}
	

	//#########################################################################
	//
	//
	//
	//
	//
	//
	//Zeroflag
	//
	//
	//
	//
	//#########################################################################
	protected void Zeroflag(int zeroflag)
	{
		if(zeroflag == 0)
		{
			getMemo().SetzeroFlag();
		}
		else
		{
			getMemo().ResetetzeroFlag();
		}
	}
	
	
	protected Timer getTimer()
	{
		return tmr;
	}
	

	protected Memory getMemo() {
		return memo;
	}

	protected Simulator_Window getGui()
	{
		return gui;
	}
	
	protected Interrupt getInterrupt()
	{
		return intp;
	}



}
