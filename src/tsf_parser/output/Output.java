package tsf_parser.output;

import java.util.Scanner;

import javax.swing.JOptionPane;

public class Output {

	public static void print(boolean displayGUI, String text){
		if (displayGUI) {
			JOptionPane.showMessageDialog(null, text, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.out.println(text);
		}
	}
	
	public static boolean userDialog(boolean displayGUI, String text){
		if (displayGUI){
			int reply = JOptionPane.showConfirmDialog(null, text, "Info", JOptionPane.YES_NO_OPTION);
			System.out.println(reply == JOptionPane.YES_OPTION);
			return reply == JOptionPane.YES_OPTION;
		} else {
			System.out.println(text);
			Scanner scanner;
			scanner = new Scanner(System.in);
			String Answer = scanner.nextLine();
			// scanner.close();

			if (Answer.equals("y") || Answer.equals("Y") || Answer.isEmpty()) {
				return true;
			} else {
				return false;
			}
			//TODO No to all (capital N)
		}
	}
}
