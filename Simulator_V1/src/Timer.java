
public class Timer {
 
	private Controller ctr;
	private int steigendeFlanke;
	private boolean clockout;
	private int Vorteiler;
	
	public Timer(Controller pCtr) {
		this.ctr = pCtr;
	}
	
	public void checkSource()
	{
		int Adresse = 0x81;
		int Stelle = 5;
		//prüfen des T0CS bit (externer Takt über Ra4 == 0)
		if(ctr.getMemo().GetBitValue(Adresse, Stelle) == 1)
		{
			//Prüfen ob T0Se bit gesetzt == fallende Flanke oder nicht == steigenden Flanke
			Stelle = 4;
			if(ctr.getMemo().GetBitValue(Adresse, Stelle) == 0)
			{
				//prüfen on Steigende Flanke anliegt, diese kann von 0 zu 1 aber auch von 0 zu -1 gehen 
				if(steigendeFlanke == 2)
				{
					incrementTimer(); //TODO incrementTimer funktion schreiben
				}
				else if (steigendeFlanke == 1)
				{
					incrementTimer();
				}
				else if(clockout)
				{
					incrementTimer();
				}
			}
		}
	}
	
	public void incrementTimer()
	{
		Vorteiler ++;
		//0x81,  = PSA Bit = checken ob Vortei8ler gesetzt (0== eingeschaltet 1== ausgeschaltet)
		int Adresse = 0x81; 
		int Stelle = 3;
		//Math.pow == hoch -> 2^(getVorzähler*2)
		if(ctr.getMemo().GetBitValue(Adresse, Stelle) == 0) && Vorteiler == (Math.pow(2,ctr.getVorzähler())*2))
		{
			
		}
	}
	
}
