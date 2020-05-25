
public class Prozessor extends Thread {
	
	Controller ctr;
	public Prozessor(Controller controller) {
		ctr = controller;
	}
	@Override public void run(){ 
		while(true) {
			try {
				this.befehlsAbarabeitung();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void befehlsAbarabeitung() throws Exception {
		
			int pcr = ctr.GetprogramCounter();
			int befehl = ctr.GetBefehl(pcr);
			System.out.println("test"+ befehl);
			
		int zeileInt = befehl;
		int precommandInt = (zeileInt >> 12) & 0x0003;
		int commandInt = (zeileInt >> 8) & 0x000F;
	

		if(precommandInt == 0) //Byte orientierte Fileregister Operationen
		{
			int d = ((zeileInt) & 0x00FF) >> 7;
			int f = ((zeileInt) & 0x00FF) & 0b01111111;
			int destination = ((zeileInt) & 0b00000010000000);
			int bofo = ((zeileInt) & 0b00000000001111);
			
			switch(commandInt)
			{
			case 0b0111:
				ctr.addwf(d,f);
				break;
			case 0b0101:
				ctr.andwf(d,f);
				break;
			case 0b0001:
				if(destination == 0) {
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
				if(destination == 0) {
					if(bofo == 3)
					{
						ctr.sleep();
					}
					else if(bofo == 8)
					{
						ctr.Return();
					}
					else if (bofo == 9)
					{
						ctr.retfie();
					}
					else if(bofo == 4)
					{
						ctr.clrwdt();
					}
					else
					{
						ctr.nop();	
					}
				}
				else
				{
					ctr.movwf(f);						
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
			int b = ((zeileInt) & 0b00001110000000) >> 8;;
			int f = (zeileInt)& 0b00000001111111;
			int bofro = (commandInt >> 2);
			
			switch(bofro)
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
			int k = (zeileInt)& 0b00000011111111;
			int lac = (commandInt >> 3);
			switch(lac)
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
			int k = (zeileInt)& 0b00011111111111;
			switch(commandInt)
			{
			case 0b1110:
				ctr.addlw(k);
				break;
			case 0b1111:
				ctr.addlw(k);
				break;
				
			case 0b1001:
				ctr.andlw(k);
				break;
				
			case 0b1000:
				ctr.iorlw(k);
				break;
				
			case 0b0000:
				ctr.movlw(k);
				break;
			case 0b0001:
				ctr.movlw(k);
				break;
			case 0b0010:
				ctr.movlw(k);
				break;
			case 0b0011:
				ctr.movlw(k);
				break;
				
			case 0b0100:
				ctr.retlw(k);
				break;
			case 0b0101:
				ctr.retlw(k);
				break;
			case 0b0110:
				ctr.retlw(k);
				break;
			case 0b0111:
				ctr.retlw(k);
				break;
				
			case 0b1100:
				ctr.sublw(k);
				break;
			case 0b1101:
				ctr.sublw(k);
				break;
				
			case 0b1010:
				ctr.xorlw(k);
				break;
			}
		}		
}
}

