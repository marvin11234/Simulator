
public class Prozessor extends Thread {
	
	private boolean exit = false;
	private Controller ctr;
	public Prozessor(Controller controller) {
		ctr = controller;
	}
	@Override public void run(){ 
		while(! exit) {
			try {
				this.befehlsAbarabeitung(ctr.getMemo().programMemoryIntArray[ctr.getMemo().programCounterInt]);
				if(exit)
				{
					break;
				}
				Thread.sleep(2000);
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
	 * Switch Case für das erkennen des Befehls und den Aufruf der
	 * entsprechenden Funktion
	 * 
	 * #########################################################################*/
	public void befehlsAbarabeitung(int codeLine) throws Exception {

		int precommandInt = (codeLine >> 12) & 0x0003;
		int commandInt = (codeLine >> 8) & 0x000F;
		int payload = (codeLine & 0x00FF);
	

		if(precommandInt == 0) //Byte orientierte Fileregister Operationen
		{
			int d = ((payload >> 7) & 0x01); //destination
			int f = ((payload) & 0x007F);
	
			//erkennung der  Adressierung über FSR
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

