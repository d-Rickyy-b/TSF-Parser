package tsf_parser.gui;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import tsf_parser.app.TSFParser;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private TSFParser parser;
	private JButton btnNewButton;
	private JLabel label;
	private JComboBox<String> comboBox;
	private static String versionString = "0.1";
	private static String databasePath = "";
	private static boolean displayGUI = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				displayGUI = true;

				if (args.length == 1 && args[0].toLowerCase().equals("nogui")) {
					System.err.println(String.format(
							"You fucked up. Usage:\n\njava -jar TSF_Parser_v%s.jar nogui AbsolutePathToDatabase\n\nnogui:\tThat way no gui will be launched (optional\npath:\tYou need to specify a path to the database file",
							versionString));
					System.exit(-1);
				} else if (args.length == 1) {
					displayGUI = false;
				}
				
				for (String element : args) {
					System.out.println(element);
					if (element.toLowerCase().equals("nogui")) {
						// Don't display a GUI
						displayGUI = false;
					} else {
						//TODO recognize month parameter
						databasePath = element;
					}
				}

				if (displayGUI) {
					try {
						MainGUI frame = new MainGUI();
						frame.setVisible(true);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// TODO Do stuff without the GUI.
					TSFParser parser = new TSFParser(displayGUI);
					parser.parseMonths();
					parser.setMonth(1);
					System.out.println(databasePath);
					parser.parseUsers(databasePath);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		// Declarations for the appearance of the JFrame
		// Java Swing is bullshit ._. Sorry for bad coding here ... I don't
		// need/want a gui anyways.

		setDefaultLookAndFeelDecorated(true);
		setTitle("TSF Parser");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 581, 105);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnStartDownload = new JButton("Start download");
		btnStartDownload.setBounds(312, 38, 248, 23);
		btnStartDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// If the button "Start download" will be pressed, the website
				// will be parsed and saved to the database
				databasePath = textField.getText();
				startParsing();
			}
		});
		contentPane.add(btnStartDownload);

		JLabel lblDatabasePath = new JLabel("Database path:");
		lblDatabasePath.setBounds(5, 4, 94, 23);
		contentPane.add(lblDatabasePath);
		lblDatabasePath.setHorizontalAlignment(SwingConstants.LEFT);

		textField = new JTextField();
		textField.setBounds(109, 4, 347, 23);
		contentPane.add(textField);
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setColumns(10);

		btnNewButton = new JButton("Browse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(textField.getText());
			}
		});
		btnNewButton.setBounds(466, 4, 94, 23);
		contentPane.add(btnNewButton);

		label = new JLabel("Month:");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setBounds(5, 42, 94, 14);
		contentPane.add(label);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(109, 39, 58, 20);
		contentPane.add(comboBox);

		// Start of parsing the months, so they can be displayed in the ComboBox
		parser = new TSFParser(displayGUI);
		parser.parseMonths();
		ArrayList<String> months = parser.getMonthsList();
		for (String month : months) {
			comboBox.addItem(month);
		}
	}

	private void startParsing() {
		this.parser.setMonth(this.comboBox.getSelectedIndex());
		System.out.println(this.comboBox.getSelectedIndex());
		System.out.println(databasePath);
		this.parser.parseUsers(databasePath);
		String dir = System.getProperty("user.dir");
		System.out.println(dir);
	}
}
