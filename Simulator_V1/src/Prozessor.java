
public class Prozessor extends Thread {
	
	private boolean exit = false;
	private boolean isNop = false;
	protected void setNop(boolean isNop) {
		this.isNop = isNop;
	}
	private Controller ctr;
	private boolean clockout = false;
	public Prozessor(Controller controller) {
		ctr = controller;
	}
	@Override public void run(){ 
		while(! exit) {
			try {
    			/*if(ctr.getBreakPointList()[ctr.getMemo().programCounterInt]) 
    			{
    				ctr.stop();
    			}*/
				ctr.CheckBreakPoint();

				if(ctr.getGui().einzellschritt == true)
				{
					ctr.stop();
				}
				//ctr.getMemo().checkRA();
				/*ctr.getMemo().checkSFR();
				ctr.getMemo().CheckIO();*/
				ctr.getGui().Befehlsmarkierung(ctr.getMemo().programCounterInt);
				ctr.laufZeitBerechnung();
				

				if(isNop)
				{
					this.befehlsAbarabeitung(0x00);
					ctr.getMemo().programCounterInt--;
					isNop = false;
				}
				else
				{
					
					this.befehlsAbarabeitung(ctr.getMemo().programMemoryIntArray[ctr.getMemo().programCounterInt]);
				}
				
				//check Timer
				clockout = true;
				ctr.getTimer().updateTimer((ctr.getMemo().getBitValue(0x05, 4)), clockout);
				ctr.getTimer().checkIncrement();
				
				//Check interrupts
				ctr.getInterrupt().updateSources(ctr.getMemo().GetF(0x06));
    			ctr.getInterrupt().checkRBISR();
    			ctr.getInterrupt().checkInterrupt();

    			//UpdateGui
				/*ctr.getMemo().checkSFR();
				ctr.getMemo().checkRA();
				ctr.getMemo().checkTRIS();
				ctr.getMemo().CheckIO();
				ctr.getMemo().checkEdit();
				ctr.PrintStack();
				ctr.getGui().printWReg(ctr.getMemo().GetWInt());*/
				clockout = false;
				
				if(exit)
				{
					break;
				}
				//Thread.sleep(2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*############################################################################
	 * 
	 *
	 * 
	 * switch case fuer das Erkennen des Befehls und das 
	 * Aufrufen der enstprechnenden Funktion
	 * #########################################################################*/
	public void befehlsAbarabeitung(int codeLine) throws Exception {

		int precommandInt = (codeLine >> 12) & 0x0003;
		int commandInt = (codeLine >> 8) & 0x000F;
		int payload = (codeLine & 0x00FF);
	

		if(precommandInt == 0) //Byte orientierte Fileregister Operationen
		{
			int d = ((payload >> 7) & 0x01); //destination
			int f = ((payload) & 0x007F);
	
			//erkennung der  Adressierung �ber FSR
			if (f == 0x00 || f == 0x80)
			{
				f = ctr.getMemo().WriteDirect(0x04);
			}
			
			switch(commandInt)
			{
			case 0b0111:
				ctr.addwf(d,f);
				break;
			case 0b0101:
				ctr.andwf(d,f);
				break;
			case 0b0001:
				if(d == 0) {
					ctr.clrw();
				}
				else
				{
					ctr.clrf(f);
				}
					
				break;
			case 0b1001:
				ctr.comf(d,f);
				break;
			case 0b0011:
				ctr.decf(d,f);
				break;
			case 0b1011:
				ctr.decfsz(d,f);
				break;
			case 0b1010:
				ctr.incf(d,f);
				break;
			case 0b1111:
				ctr.incfsz(d,f);
				break;
			case 0b0100:
				ctr.iorwf(d,f);
				break;
			case 0b1000:
				ctr.movf(d,f);
				break;
			case 0b0000:
				if(d == 1)
				{
					ctr.movwf(f);
				}
				else if(payload == 0b01100100)
				{
					ctr.clrwdt();
				}
				else if(payload == 0b00001001)
				{
					ctr.retfie();
				}
				else if(payload == 0b00001000)
				{
					ctr.Return();	
				}
				else if (payload == 0b01100011)
				{
					ctr.sleep();
				}
				else
				{
					ctr.nop();
				}
				break;
			case 0b1101:
				ctr.rlf(d,f);
				break;
			case 0b1100:
				ctr.rrf(d,f);
				break;
			case 0b0010:
				ctr.subwf(d,f);
				break;
			case 0b1110:
				ctr.swapf(d,f);
				break;
			case 0b0110:
				ctr.xorwf(d,f);
				break;
			}
		}
		else if(precommandInt == 1)//Bit orientierte fileregister operationen
		{
			int b = ((codeLine) >> 7) & 0x0007;
			int f = (codeLine)& 0x007F;
			
			if (f == 0x00 || f == 0x80)
			{
				f = ctr.getMemo().WriteDirect(0x04);
			}
			
			switch(commandInt >> 2)
			{
			case (0b00):
				ctr.bcf(b,f);
				break;
			case (0b01):
				ctr.bsf(b,f);
				break;
			case (0b10):
				ctr.btfsc(b,f);
				break;
			case (0b11):
				ctr.btfss(b,f);
				break;

			}
		}
		else if(precommandInt == 2)//Literal and Controll Operations
		{
			int k = codeLine & 0x07FF;
			switch(commandInt >> 3)
			{	
			case 0b0:
				ctr.call(k);//???
				break;
			case 0b1:
				ctr.Goto(k);//???
				break;
				
			}
		}
		else if(precommandInt == 3)
		{
			int k = (codeLine) & 0x00FF;
			
			if((commandInt >> 1) == 7)
			{
				ctr.addlw(k);
			}
			else if(commandInt == 0b1001)
			{
				ctr.andlw(k);
			}
			else if(commandInt == 0b1000)
			{
				ctr.iorlw(k);
			}
			else if((commandInt >>2) == 0 )
			{
				ctr.movlw(k);
			}
			else if((commandInt >>2) == 1)
			{
				ctr.retlw(k);
			}
			else if((commandInt >> 1) == 6)
			{
				ctr.sublw(k);
			}
			else if(commandInt == 0b1010)
			{
				ctr.xorlw(k);
			}
		}		
}
	public void stopThread()
	{
		exit = true;
	}
}

