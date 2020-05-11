
public class Prozessor extends Thread{

	
	public void Befehlsabarbeitung(int[] befehlDezimalIntArray) {
		System.out.println(befehlDezimalIntArray[0] );
		int zeileInt = befehlDezimalIntArray[0];
		int precomandInt = (zeileInt >> 12) &0x0003;
		int commandInt = (zeileInt >> 8) &0x000F;
		int payloadInt = (zeileInt ) &0x00FF;
		
		if(precomandInt == 0)
		{
			int d = payloadInt >> 7;
			int f = payloadInt & 0b01111111 ;
			
			switch(commandInt)
			{
			case 0b0111:
				addwf(d,f);
				break;
			}
		}
		
	}
	
	private void addwf(int d,int f)
	{
		
	}
}

