
public class Timer {

	private Controller ctr;
	private int raEdge;
	private boolean clockout;
	private int ra0;
	private int Vorteiler;

	public Timer(Controller pCtr) {
		ctr = pCtr;
	}

	public void checkIncrement() {

		// pruefenen des T0CS bit (externer Takt �berber Ra4 == 0)
		if (ctr.getMemo().getBitValue(129, 5) == 1) {
			// Prue fen ob T0Se bit gesetzt == fallende Flanke oder nicht == steigenden Flanke
			if (ctr.getMemo().getBitValue(129, 4) == 0) {
				// pruefen on Steigende Flanke anliegt, diese kann von 0 zu 1 aber auch von 1 zu
				// 0gehen
				if (raEdge == 1) {
					incrementTimer(); 
				}
			} else {
				if (raEdge == 2) {
					incrementTimer();
				}
			}
		} else {
			if (clockout) {
				incrementTimer();
			}
		}
	}

	private void incrementTimer() {
		Vorteiler++;
		int preScalerActive = ctr.getMemo().getBitValue(0x81, 3);
		if ((preScalerActive == 0) && Vorteiler == (Math.pow(2.0, ctr.getVorteiler()) * 2) || preScalerActive == 1) {
			int in = ctr.getMemo().GetF(1);
			if (in == 255) {
				ctr.getMemo().set_SRAM(0, 1);

				ctr.getMemo().set_SRAM_Bit(0x0b, 2, 1);
				ctr.getMemo().SetzeroFlag();
			} else {
				in++;
				ctr.getMemo().set_SRAM(in, 1);
			}
			Vorteiler = 0;
		}
	}

	public void updateTimer(int pRA, boolean pCLOCKOUT) {
		
		if (pRA != ra0) {
			ra0 = pRA;
			if (pRA == 1) {
				raEdge = 1;
			} else {
				raEdge = 2;
			}
		} else {
			raEdge = 0;
		}
		clockout = pCLOCKOUT;
	}
	

	// Getter fuer Vorteiler
	public double getVorteiler() {
		return Vorteiler;
	}

	// Setter fuer Vorteiler
	public void setVorteiler(int VorteilerWert) {
		Vorteiler = VorteilerWert;
	}
}
