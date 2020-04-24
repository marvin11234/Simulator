import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;


public class Controller {
	

	JFileChooser fileChooser = new JFileChooser();
	StringBuilder sb = new StringBuilder();
	
	String[] inputStArray = new String[1024];
	
	public void PickMe() throws Exception{
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			//get the file
			java.io.File file = fileChooser.getSelectedFile();
			
			BufferedReader br = null;

			try 
			{ 

			br = new BufferedReader(new FileReader(file));

			String sCurrentLine;
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null){
				inputStArray[i] = sCurrentLine;
				i++;

			}
			for (int b = 0; b <= i; b++) {
			System.out.println(inputStArray[b]);
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
