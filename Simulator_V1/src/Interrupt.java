
public class Interrupt {


	private int rb0;
	private int rb4;
	private int rb5;
	private int rb6;
	private int rb7;
	private int rb0Edge;
	private boolean rbChanged;

	Controller ctr;

	public Interrupt(Controller pCtr) 

	{

		this.ctr = pCtr;

	}
	
	
	public boolean checkInterruptFlags() {

 	   // check T0IF and T0IE

 	   if(ctr.getMemo().dataMemoryIntArray[0x0b][2] == 1 && ctr.getMemo().dataMemoryIntArray[0x0b][5] == 1) 

 	   {

 		   System.out.println("Timer0 Interrupt");

 		   return true;

 	   }else

 	   // check INTF and INTE

 	   if(ctr.getMemo().dataMemoryIntArray[0x0b][1] == 1 && ctr.getMemo().dataMemoryIntArray[0x0b][4] == 1) 

 	   {

 		   System.out.println("RB0 Interrupt");
 		   ctr.getMemo().dataMemoryIntArray[0x0b][2] = 0;
 		   return true;

 	   }else

 	   // check RBIF and RBIE

 	   if(ctr.getMemo().dataMemoryIntArray[0x0b][0] == 1 && ctr.getMemo().dataMemoryIntArray[0x0b][3] == 1) 

 	   {

 		   System.out.println("RB Interrupt");
 		   ctr.getMemo().dataMemoryIntArray[0x0b][2] = 0;
 		   return true;

 	   }else
    	   // check EEIF and EEIE
    	   if(ctr.getMemo().dataMemoryIntArray[0x88][4] == 1 && ctr.getMemo().dataMemoryIntArray[0x0b][6] == 1) 
    	   {
    		   System.out.println("INTF Interrupt occured");
    		   return true;
    	   }

 	   // No Interrupt

 	   return false;

	}
	

	protected void updateSources(int rb) 
	{
		if(this.rb0 != (rb & 0x01)) 
		{
			this.rb0 = (rb & 0x01);
			if((rb & 0x01) == 1) 
			{
				this.rb0Edge = 1;
			}else 
			{
				this.rb0Edge = 2;
			}
		}else 
		{
			this.rb0Edge = 0;
		}
		
		if(this.rb4 != (rb & 0x10)) {
			this.rbChanged = true;
			this.rb4 = (rb & 0x10);
		}
		if(this.rb5 != (rb & 0x20)) {
			this.rbChanged = true;
			this.rb5 = (rb & 0x20);
		}
		if(this.rb6 != (rb & 0x40)) {
			this.rbChanged = true;
			this.rb6 = (rb & 0x40);
		}
		if(this.rb7 != (rb & 0x80)) {
			this.rbChanged = true;
			this.rb7 = (rb & 0x80);
		}
	}

	protected void checkRBISR() 

	{

		// INTEDG is 1 = pos edge 

		if(ctr.getMemo().dataMemoryIntArray[0x81][6] == 1) 

		{

			if(this.rb0Edge == 1) 

			{

				ctr.getMemo().set_SRAM_Bit(0x0b, 1, 1);

			}

		}else 

		{

			if(this.rb0Edge == 2) 

			{

				ctr.getMemo().set_SRAM_Bit(0x0b, 1, 1);

			}

		}
		if(this.rbChanged) 
		{
			ctr.getMemo().set_SRAM_Bit(0x0b, 0, 1);
			this.rbChanged = false;
		}
	}

	

	public void checkInterrupt() 

	{

		// If Global interrupts are enabled and an interrupt occured

		if (ctr.getMemo().dataMemoryIntArray[0x0b][7] == 1 && checkInterruptFlags()) {

			//ctr.memory.pushToStack(ctr.memory.programmcounter);

			ctr.getMemo().pushToStack(ctr.getMemo().programCounterInt +1);

			// clearing GIE bit to disable other interrupts

			ctr.getMemo().set_SRAM_Bit(0x0b, 7, 0);

			// Subtract 1 because the programcounter is incremented again at the end of the run loop in processor



			ctr.getMemo().SetPC(0x04 - 1);

		}

	}
}
