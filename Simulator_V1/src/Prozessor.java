
public class Prozessor extends Thread{

	
	public void Befehlsabarbeitung(int[] befehlDezimalIntArray) {
		System.out.println(befehlDezimalIntArray[0] );
		int zeileInt = befehlDezimalIntArray[0];
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
				addwf(d,f);
				break;
			case 0b0101:
				andwf(d,f);
				break;
			case 0b0001;
				if(destination == 0) {
					clrw();
				}
				else
				{
					clrf(f);
				}
					
				break;
			case 0b1001
				comf(d,f);
				break;
			case 0b0011:
				decf(d,f);
				break;
			case 0b1011:
				decfsz(d,f);
				break;
			case 0b1010:
				incf(d,f);
				break;
			case 0b1111:
				incfsz(d,f);
				break;
			case 0b0100:
				iorwf(d,f);
				break;
			case 0b1000:
				movf(d,f);
				break;
			case 0b0000:
				if(destination == 0) {
					if(bofo == 3)
					{
						sleep();
					}
					else if(bofo == 8)
					{
						Return();
					}
					else if (bofo == 9)
					{
						retfie()
					}
					else if(bofo == 4)
					{
						clrwdt();
					}
					else
					{
						nop();	
					}
				}
				else
				{
					movwf(f);						
				}
				break;
			case 0b1101:
				rlf(d,f);
				break;
			case 0b1100:
				rrf(d,f);
				break;
			case 0b0010:
				subwf(d,f);
				break;
			case 0b1110:
				swapf(d,f);
				break;
			case 0b0110:
				xorwf(d,f);
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
				bcf(b,f);
				break;
			case (0b01):
				bsf(b,f);
				break;
			case (0b10):
				btfsc(b,f);
				break;
			case (0b11):
				btfss(b,f);
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
				call(k);//???
				break;
			case 0b1:
				Goto(k);//???
				break;
				
			}
		}
		else if(precommandInt == 3)
		{
			int k = (zeileInt)& 0b00011111111111;
			switch(commandInt)
			{
			case 0b1110:
				addlw(k);
				break;
			case 0b1111:
				addlw(k);
				break;
				
			case 0b1001:
				andlw(k);
				break;
				
			case 0b1000:
				iorlw(k);
				break;
				
			case 0b0000:
				movlw(k);
				break;
			case 0b0001:
				movlw(k);
				break;
			case 0b0010:
				movlw(k);
				break;
			case 0b0011:
				movlw(k);
				break;
				
			case 0b0100:
				retlw(k);
				break;
			case 0b0101:
				retlw(k);
				break;
			case 0b0110:
				retlw(k);
				break;
			case 0b0111:
				retlw(k);
				break;
				
			case 0b1100:
				sublw(k);
				break;
			case 0b1101:
				sublw(k);
				break;
				
			case 0b1010:
				xorlw(k);
				break;
			}
		}
		
	}
	
	private void addwf(int d,int f)
	{
		
	}
}

