package tsf_parser.gui;

import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import tsf_parser.app.TSFParser;
import tsf_parser.output.Output;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	private JPanel contentPane;
	private JTextField databaseTextField;
	private TSFParser parser;
	private JButton browseDBButton;
	private JLabel monthLabel;
	private JComboBox<String> monthChooserComboBox;
	private static String versionString = "0.1.1";
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
						// TODO recognize month parameter
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
		btnStartDownload.setBounds(370, 38, 190, 23);
		btnStartDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// If the button "Start download" will be pressed, the website
				// will be parsed and saved to the database
				databasePath = databaseTextField.getText();

				if (!databasePath.endsWith(".db")) {
					Output.print(displayGUI, "Path to database must end with '.db'");
				} else {
					startParsing();
				}
			}
		});
		contentPane.add(btnStartDownload);

		JLabel databasePathLabel = new JLabel("Database path:");
		databasePathLabel.setBounds(5, 4, 94, 23);
		contentPane.add(databasePathLabel);
		databasePathLabel.setHorizontalAlignment(SwingConstants.LEFT);

		databaseTextField = new JTextField();
		databaseTextField.setBounds(109, 4, 347, 23);
		contentPane.add(databaseTextField);
		databaseTextField.setHorizontalAlignment(SwingConstants.LEFT);
		databaseTextField.setColumns(10);

		browseDBButton = new JButton("Browse");
		browseDBButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Please select your database file");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database File", "db");
				chooser.setFileFilter(filter);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					databaseTextField.setText(chooser.getSelectedFile().toString());
				} else {
					System.out.println("No Selection");
				}
			}
		});
		browseDBButton.setBounds(466, 4, 94, 23);
		contentPane.add(browseDBButton);

		monthLabel = new JLabel("Month:");
		monthLabel.setHorizontalAlignment(SwingConstants.LEFT);
		monthLabel.setBounds(5, 42, 94, 14);
		contentPane.add(monthLabel);

		monthChooserComboBox = new JComboBox<String>();
		monthChooserComboBox.setBounds(109, 39, 58, 20);
		contentPane.add(monthChooserComboBox);
		
		JButton btnExportToCSV = new JButton("Export (CSV)");
		btnExportToCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CSVExportDialog csvExport = new CSVExportDialog("Export database to CSV", MainGUI.this);
				csvExport.setModalityType(ModalityType.APPLICATION_MODAL);
				csvExport.setVisible(true);
			}
		});
		btnExportToCSV.setBounds(210, 38, 150, 23);
		contentPane.add(btnExportToCSV);

		// Start of parsing the months, so they can be displayed in the ComboBox
		parser = new TSFParser(displayGUI);
		parser.parseMonths();
		ArrayList<String> months = parser.getMonthsList();
		for (String month : months) {
			monthChooserComboBox.addItem(month);
		}
	}

	private void startParsing() {
		this.parser.setMonth(this.monthChooserComboBox.getSelectedIndex());
		System.out.println(this.monthChooserComboBox.getSelectedIndex());
		System.out.println(databasePath);
		this.parser.parseUsers(databasePath);
	}
}
