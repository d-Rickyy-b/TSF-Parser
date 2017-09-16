package tsf_parser.output;

import java.util.Scanner;

import javax.swing.JOptionPane;

public class Output {

	public static final Object[] OPTIONS = { "Yes", "No", "Yes To All", "No To All" };

	public static void print(boolean displayGUI, String text) {
		if (displayGUI) {
			JOptionPane.showMessageDialog(null, text, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.out.println(text);
		}
	}

	public static int userDialog(boolean displayGUI, String text) {
		if (displayGUI) {
			int reply = JOptionPane.showOptionDialog(null, text, "Info", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, OPTIONS, OPTIONS[2]);
			return reply;
		} else {
			System.out.println(text + " (y/Y/n/N): ");
			Scanner scanner;
			scanner = new Scanner(System.in);
			String Answer = scanner.nextLine();
			scanner.close();

			if (Answer.equals("y") || Answer.isEmpty()) {
				return 0;
			} else if (Answer.equals("Y")) {
				return 2;
			} else if (Answer.equals("n")) {
				return 1;
			} else if (Answer.equals("Y")) {
				return 2;
			} else if (Answer.equals("N")) {
				return 3;
			} else {
				return 0;
			}
			// TODO No to all (capital N)
		}
	}
}
