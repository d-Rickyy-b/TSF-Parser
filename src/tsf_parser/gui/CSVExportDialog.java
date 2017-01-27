package tsf_parser.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import tsf_parser.database.Database;
import tsf_parser.output.Output;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class CSVExportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField exportDatabaseField;
	private JButton okButton;
	private JTextField exportFileField;
	private JDialog csvExportDialog;

	public CSVExportDialog(String title, JFrame frame) {
		this.csvExportDialog = this;
		setTitle(title);
		setBounds(100, 100, 581, 170);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblDatabasePath = new JLabel("Database path:");
			lblDatabasePath.setBounds(10, 13, 93, 14);
			lblDatabasePath.setHorizontalAlignment(SwingConstants.LEFT);
			contentPanel.add(lblDatabasePath);
		}

		DocumentListener documentListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkTextfield();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// When text is inserted
				checkTextfield();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// When text is updated
				checkTextfield();
			}

			private void checkTextfield() {
				if (exportDatabaseField.getText().endsWith(".db") && !exportFileField.getText().equals("")) {
					okButton.setEnabled(true);
				} else {
					okButton.setEnabled(false);
				}
			}
		};

		exportDatabaseField = new JTextField();
		exportDatabaseField.getDocument().addDocumentListener(documentListener);

		exportDatabaseField.setBounds(103, 10, 353, 20);
		contentPanel.add(exportDatabaseField);
		exportDatabaseField.setColumns(10);

		JButton btnBrowseDatabase = new JButton("Browse");
		btnBrowseDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Please select your database file");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database File", "db");
				chooser.setFileFilter(filter);

				csvExportDialog.setEnabled(false);
				if (chooser.showOpenDialog(csvExportDialog) == JFileChooser.APPROVE_OPTION) {
					exportDatabaseField.setText(chooser.getSelectedFile().toString());
				} else {
					System.out.println("No Selection");
				}
				frame.setVisible(true);
				csvExportDialog.setEnabled(true);
				csvExportDialog.setVisible(true);
				csvExportDialog.hasFocus();
			}
		});
		btnBrowseDatabase.setBounds(466, 9, 89, 23);
		contentPanel.add(btnBrowseDatabase);

		JLabel lblExportFilePath = new JLabel("Export file path:");
		lblExportFilePath.setHorizontalAlignment(SwingConstants.LEFT);
		lblExportFilePath.setBounds(10, 50, 93, 14);
		contentPanel.add(lblExportFilePath);

		exportFileField = new JTextField();
		exportFileField.getDocument().addDocumentListener(documentListener);
		exportFileField.setColumns(10);
		exportFileField.setBounds(103, 47, 353, 20);
		contentPanel.add(exportFileField);

		JButton btnBrowseExportFile = new JButton("Browse");
		btnBrowseExportFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Please select a path to export the data!");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				csvExportDialog.setEnabled(false);
				if (chooser.showOpenDialog(csvExportDialog) == JFileChooser.APPROVE_OPTION) {
					exportFileField.setText(chooser.getSelectedFile().toString());
					csvExportDialog.setEnabled(true);
				} else {
					System.out.println("No Selection");
				}
				frame.setVisible(true);
				csvExportDialog.setEnabled(true);
				csvExportDialog.setVisible(true);
				csvExportDialog.hasFocus();
			}
		});
		btnBrowseExportFile.setBounds(466, 46, 89, 23);
		contentPanel.add(btnBrowseExportFile);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(103, 80, 44, 20);
		comboBox.addItem(";");
		comboBox.addItem(",");
		contentPanel.add(comboBox);

		JLabel lblSeperator = new JLabel("Seperator:");
		lblSeperator.setHorizontalAlignment(SwingConstants.LEFT);
		lblSeperator.setBounds(10, 83, 93, 14);
		contentPanel.add(lblSeperator);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Export");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Database database = Database.getInstance();
						database.setDatabasePath(exportDatabaseField.getText());
						database.loadDatabase();
						if (database.writeCSV(exportFileField.getText(), comboBox.getSelectedItem().toString())) {
							csvExportDialog.setModal(false);
							Output.print(true,
									String.format("Everything went fine. Data was exported to: %s\\tsf-export.csv",
											exportFileField.getText()));
						} else {
							csvExportDialog.setModal(false);
							Output.print(true,
									"Something went wrong. I could tell you, but i'm a lazy programmer and i didn't catch the exception!");
						}
					}
				});
				okButton.setEnabled(false);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
