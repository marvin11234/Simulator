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
	
	//gui und memo Instanz erzeugen
	public Controller(Simulator_Window simulator_Window) {
		gui = simulator_Window;
		memo = new Memory(this);
		tmr = new Timer(this);
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
						int pc =Integer.parseInt(pcSt ,16);
						int code = Integer.parseInt(codeSt ,16);
						this.getMemo().programMemoryIntArray[pc] = code;
					}
				}
				//initialisierung Speicher anzeige
				InitGprView();
				
				InitStackView();
						
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
		
		if (d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)	//ERGEBNIS IN F SPEICHERN ?
		{
			getMemo().set_SRAM(erg,f);
			PrintGPR();
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
			PrintGPR();
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
		PrintGPR();
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
				PrintGPR();
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
			PrintGPR();
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
			PrintGPR();
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
			PrintGPR();
		}
		getMemo().IncPc();
	}
	
	public void incfsz(int d, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("incfsz");
		int temp = getMemo().GetF(f);
		int erg = 0; 
		
		if(erg == 255) 
		{
			erg = 0;
			getMemo().IncPc();
			this.proc.setNop(true);

		}
		else 
		{
			erg++;
		}
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
			PrintGPR();
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
			PrintGPR();
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
		int k  = getMemo().GetStack();
		System.out.println("Return " + k + "1");
		PrintAfterPop();
		
		System.out.println("Return " + k + "2");
		
		getMemo().SetPC(k);
		System.out.println("Return " + k + "3" );
		getMemo().IncPc();
		this.proc.setNop(true);
	}
	
	public void retfie()throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("retfie");
		getMemo().IncPc();
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
		PrintGPR();
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
				PrintGPR();
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
				PrintGPR();
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
		
		if(d == 0 )
		{
			getMemo().WriteW(erg);
		}
		else if (d == 1)
		{
			getMemo().set_SRAM(erg, f);
			PrintGPR();
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
			PrintGPR();
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
			PrintGPR();
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
			PrintGPR();
		}
		getMemo().IncPc();
	}
	
	
	public void bcf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bcf");
		getMemo().set_SRAM_Bit(f, b,0);
		PrintGPR();
		getMemo().IncPc();
	}
	public void bsf(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("bsf");
		getMemo().set_SRAM_Bit(f, b,1);
		PrintGPR();
		getMemo().IncPc();
	}
	public void btfsc(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfsc");
		int[] tempF = getMemo().GetFBin(f);
		if(tempF[b] == 0)
		{
			getMemo().IncPc();
			this.proc.setNop(true);
		}
		getMemo().IncPc();
	}
	public void btfss(int b, int f)throws Exception //BEEINFLUSST KEINE STATI
	{
		System.out.println("btfss");
		int[] tempF = getMemo().GetFBin(f);
		if(tempF[b] == 1)
		{
			getMemo().IncPc();
			this.proc.setNop(true);
		}
		getMemo().IncPc();
	}
	
	public void call(int k)throws Exception //BEEINFLUSST KEINE STATI
	{	
		System.out.println("call" + k );
		getMemo().SetStack();
		PrintStack();
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
		
		getMemo().WriteW(erg);
		getMemo().IncPc();
	}
	public void iorlw(int k)throws Exception  //BEEINFLUSST  C, DC, Z
	{
		System.out.println("iorlw");
		int temp = getMemo().GetWInt();
		int erg  = (temp | k);
		
		
		Zeroflag(erg);
		
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
		k  = getMemo().GetStack();
		System.out.println("RetLW 2" + k);
		PrintAfterPop();
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
	
	/*############################################################################
	 * 
	 *
	 * 
	 * Initialisieren der GPR Ausgabe
	 * 
	 * 
	 * #########################################################################*/
	public void InitGprView()
	{

		for (int i = 12; (i < 48); i++)
		{
			int f = i;
			String hex = Integer.toHexString(i);
			int[] fRegIntArray = getMemo().GetFBin(f);
			gui.tblGprMdl.addRow(new Object[] {hex, fRegIntArray[7], fRegIntArray[6], fRegIntArray[5], fRegIntArray[4],fRegIntArray[3], fRegIntArray[2], fRegIntArray[1], fRegIntArray[0]} ); 
		}
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 * Beschrieben der GPR Ausgabe
	 * 
	 * 
	 * #########################################################################*/
	public void PrintGPR()
	{
		for (int i = gui.tblGprMdl.getRowCount() - 1; i >= 0; i--)
		{
			gui.tblGprMdl.removeRow(i);
		}

		for (int i = 12; (i < 48); i++)
		{
			int f = i;
			String hex = Integer.toHexString(i);
			int[] fRegIntArray = getMemo().GetFBin(f);
			gui.tblGprMdl.addRow(new Object[] {hex, fRegIntArray[7], fRegIntArray[6], fRegIntArray[5], fRegIntArray[4],fRegIntArray[3], fRegIntArray[2], fRegIntArray[1], fRegIntArray[0]} ); 
		}
	}


	public void InitStackView()
	{
		System.out.println("Init Stack");
		for(int i = 0; i <= 7; i++)
		{
			gui.tblStackMdl.addRow(new Object[] {i, 0});
			
		}
	}
	int stackAr[]; 
	public void PrintStack()
	{ 
		
		for(int i = 6; i >= 0; i--)
		{
			 Object test = gui.tblStackMdl.getValueAt(i, 1);
			 int stack = (int) test;
			 gui.tblStackMdl.setValueAt(stack, i+1, 1);
		}
		gui.tblStackMdl.setValueAt(getMemo().ShowStack(), 0, 1);
	}
	
	public void PrintAfterPop()
	{
		if(getMemo().cmdStack.isEmpty() == true)
		{
			for(int i = 0; i <= 7; i++)
			{
				gui.tblStackMdl.setValueAt(0, i, 1);
				
			}
		}
		else 
		{
			for(int i = 0; i <= 6; i++)
			{
				if(i == 0) 
				{
					gui.tblStackMdl.setValueAt(0, i, 1);
				}
				else
				{
					 Object test = gui.tblStackMdl.getValueAt(i, 1);
					 int stack = (int) test;
					 gui.tblStackMdl.setValueAt(stack, i-1, 1);
				}
			}
			 gui.tblStackMdl.setValueAt(0, 7, 1);
			
		}

	}
	

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
	
	public void start() 
	{
		if(! this.processorRunning)
		{
			proc = new Prozessor(this);
			proc.start();
			this.processorRunning = true;
		}
		
	}

	/*############################################################################
	 * 
	 *
	 * 
	 * Stop Thread
	 * 
	 * 
	 * #########################################################################*/
	public void stop()
	{
		if( this.processorRunning)
		{
			this.processorRunning = false;
			proc.stopThread();
		}
		
	}
	

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
	
	protected int getVorzaehler() 
	{

		return getMemo().getBitValue(0x81, 7);
	}

	protected Memory getMemo() {
		return memo;
	}

	public Simulator_Window getGui()
	{
		return gui;
	}
}
