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
	int zeileInt = 0;
	
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
					if(currentLineSt.startsWith(" "))
					{	
						arrayStelleEinlesenInt++;
					}
					else
					{	
						try 
						{
							temCurLnSt = currentLineSt;
							zeileInt = Integer.parseInt(temCurLnSt.substring(0,4));
							programmSpeicherStArray[zeileInt] = temCurLnSt.substring(5,9);
						}
						catch (NumberFormatException e)
						{
							System.out.println("Fehler: es Konnte keine Einlesezeile gefunden werden");
						}
					}
				}
				for (int b = 0; b <= arrayStelleEinlesenInt; b++) 
				{
					System.out.println(programmSpeicherStArray[b]);
				}
			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
			Memory memoryInst = new Memory();
			memoryInst.CodeSpeichern(programmSpeicherStArray);
			
		}
		else 
		{
			sb.append("No file was choosen");
		}
	}

}
