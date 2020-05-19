
public class Prozessor extends Thread{

	public void Befehlsabarbeitung(int[] befehlDezimalIntArray) throws Exception {
		int x = 0;
		while(x <= 100)
		{
			System.out.println(befehlDezimalIntArray[x] );
			
		int zeileInt = befehlDezimalIntArray[x];
		int precommandInt = (zeileInt >> 12) & 0x0003;
		int commandInt = (zeileInt >> 8) & 0x000F;
		Controller controllerInst  = new Controller();

		if(precommandInt == 0) //Byte orientierte Fileregister Operationen
		{
			int d = ((zeileInt) & 0x00FF) >> 7;
			int f = ((zeileInt) & 0x00FF) & 0b01111111;
			int destination = ((zeileInt) & 0b00000010000000);
			int bofo = ((zeileInt) & 0b00000000001111);
			
			switch(commandInt)
			{
			case 0b0111:
				controllerInst.addwf(d,f);
				break;
			case 0b0101:
				controllerInst.andwf(d,f);
				break;
			case 0b0001:
				if(destination == 0) {
					controllerInst.clrw();
				}
				else
				{
					controllerInst.clrf(f);
				}
					
				break;
			case 0b1001:
			controllerInst.comf(d,f);
				break;
			case 0b0011:
				controllerInst.decf(d,f);
				break;
			case 0b1011:
				controllerInst.decfsz(d,f);
				break;
			case 0b1010:
				controllerInst.incf(d,f);
				break;
			case 0b1111:
				controllerInst.incfsz(d,f);
				break;
			case 0b0100:
				controllerInst.iorwf(d,f);
				break;
			case 0b1000:
				controllerInst.movf(d,f);
				break;
			case 0b0000:
				if(destination == 0) {
					if(bofo == 3)
					{
						controllerInst.sleep();
					}
					else if(bofo == 8)
					{
						controllerInst.Return();
					}
					else if (bofo == 9)
					{
						controllerInst.retfie();
					}
					else if(bofo == 4)
					{
						controllerInst.clrwdt();
					}
					else
					{
						controllerInst.nop();	
					}
				}
				else
				{
					controllerInst.movwf(f);						
				}
				break;
			case 0b1101:
				controllerInst.rlf(d,f);
				break;
			case 0b1100:
				controllerInst.rrf(d,f);
				break;
			case 0b0010:
				controllerInst.subwf(d,f);
				break;
			case 0b1110:
				controllerInst.swapf(d,f);
				break;
			case 0b0110:
				controllerInst.xorwf(d,f);
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
				controllerInst.bcf(b,f);
				break;
			case (0b01):
				controllerInst.bsf(b,f);
				break;
			case (0b10):
				controllerInst.btfsc(b,f);
				break;
			case (0b11):
				controllerInst.btfss(b,f);
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
				controllerInst.call(k);//???
				break;
			case 0b1:
				controllerInst.Goto(k);//???
				break;
				
			}
		}
		else if(precommandInt == 3)
		{
			int k = (zeileInt)& 0b00011111111111;
			switch(commandInt)
			{
			case 0b1110:
				controllerInst.addlw(k);
				break;
			case 0b1111:
				controllerInst.addlw(k);
				break;
				
			case 0b1001:
				controllerInst.andlw(k);
				break;
				
			case 0b1000:
				controllerInst.iorlw(k);
				break;
				
			case 0b0000:
				controllerInst.movlw(k);
				break;
			case 0b0001:
				controllerInst.movlw(k);
				break;
			case 0b0010:
				controllerInst.movlw(k);
				break;
			case 0b0011:
				controllerInst.movlw(k);
				break;
				
			case 0b0100:
				controllerInst.retlw(k);
				break;
			case 0b0101:
				controllerInst.retlw(k);
				break;
			case 0b0110:
				controllerInst.retlw(k);
				break;
			case 0b0111:
				controllerInst.retlw(k);
				break;
				
			case 0b1100:
				controllerInst.sublw(k);
				break;
			case 0b1101:
				controllerInst.sublw(k);
				break;
				
			case 0b1010:
				controllerInst.xorlw(k);
				break;
			}
		}
		x++;
	}
		
}
}

