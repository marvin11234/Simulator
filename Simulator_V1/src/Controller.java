import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;


public class Controller {
	

	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	String sCurrentLine;
	int iArrayStelleEinlesen = 0;
	String[] inputStArray = new String[1024];
	String[] ProgrammspeicherStArray = new String[1024];
	int iZeile = 0;
	
	public void Einlesen() throws Exception{
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get the file
			java.io.File file = fileChooser.getSelectedFile();
			
			//Default buffer reader
			BufferedReader br = null;

			try 
			{ 
				
			br = new BufferedReader(new FileReader(file));	
			
			while ((sCurrentLine = br.readLine()) != null){
				inputStArray[iArrayStelleEinlesen] = sCurrentLine;
				if(sCurrentLine.startsWith(" "))
				{	
					iArrayStelleEinlesen++;

				}
				else
				{	
					try {
						iZeile = Integer.parseInt(sCurrentLine.substring(0,4));
						ProgrammspeicherStArray[iZeile] = sCurrentLine.substring(5,9);
					}
					catch (NumberFormatException e)
					{
						System.out.println("Fehler: es Konnte keine Einlesezeile gefunden werden");
					}
				}


			}
			for (int b = 0; b <= iArrayStelleEinlesen; b++) {
			System.out.println(ProgrammspeicherStArray[b]);
				}
			}
			catch (IOException e){
			e.printStackTrace();
			}
			
		}
		else {
			sb.append("No file was choosen");
		}
	}

}
