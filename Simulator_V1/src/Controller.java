import javax.swing.JFileChooser;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;




public class Controller {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					Simulator_Window Simulator_WindowInst = new Simulator_Window();
					Simulator_WindowInst.initialize(Simulator_WindowInst);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	String currentLineSt;
	int arrayStelleEinlesenInt = 0;
	String[] inputStArray = new String[1024];
	String[] programmSpeicherStArray = new String[1024];
	String temCurLnSt;
	String[][] tableStArray = new String[1024][6];
	int zeileInt = 0;
	int i = 0;
	int[] befehlDezimalIntArray = new int[1024];
	
	public void Einlesen() throws Exception{
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get the file
			java.io.File file = fileChooser.getSelectedFile();
			//Default buffer reader
			BufferedReader br = null;
			try 
			{ 
				br = new BufferedReader(new FileReader(file));	
				while ((currentLineSt = br.readLine()) != null)
				{
					inputStArray[arrayStelleEinlesenInt] = currentLineSt;
					
					arrayStelleEinlesenInt++;
				}

						while((temCurLnSt = inputStArray[i]) !=  null)
						{
											
							try 
							{
							
								temCurLnSt = inputStArray[i];
								if(temCurLnSt != null)
								{							
									tableStArray[i][1] = temCurLnSt.substring(0,4);
									tableStArray[i][2] = temCurLnSt.substring(5,9);
									tableStArray[i][3] = temCurLnSt.substring(20,25);
									tableStArray[i][4] = temCurLnSt.substring(27,32);
									tableStArray[i][5] = temCurLnSt.substring(36,inputStArray[i].length());						
								}
							}
							catch (NumberFormatException e)
							{
								System.out.println("Fehler: es Konnte keine Einlesezeile gefunden werden");
							}
							i++;
						}

			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
			Memory memoryInst = new Memory();
			befehlDezimalIntArray = memoryInst.CodeSpeichern(tableStArray);
			
			Prozessor prozessorInst = new Prozessor();
			prozessorInst.Befehlsabarbeitung(befehlDezimalIntArray);
			
		}
		else 
		{
			sb.append("No file was choosen");
		}
	}

}
