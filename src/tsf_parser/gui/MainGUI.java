package tsf_parser.gui;

import java.awt.BorderLayout;
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
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JComboBox<String> comboBox;
	private TSFParser parser;
	private JLabel lblMonth;
	private JButton btnNewButton;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		// Declarations for the appearance of the JFrame
		setTitle("TSF Parser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 677, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel databasePanel = new JPanel();
		databasePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		databasePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDatabasePath = new JLabel("Database path:");
		lblDatabasePath.setHorizontalAlignment(SwingConstants.LEFT);
		databasePanel.add(lblDatabasePath, BorderLayout.WEST);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setColumns(10);
//		textField.setBorder(new EmptyBorder(5,5,5,5));
		databasePanel.add(textField, BorderLayout.CENTER);
		
		btnNewButton = new JButton("Browse");
		databasePanel.add(btnNewButton, BorderLayout.EAST);
		
		
		
		contentPane.add(databasePanel, "North");
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblMonth = new JLabel("Month:");
		lblMonth.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblMonth);
		
		
		
		comboBox = new JComboBox<String>();
		panel.add(comboBox);
		
		JButton btnStartDownload = new JButton("Start download");
		btnStartDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startParsing();
			}
		});
		contentPane.add(btnStartDownload, BorderLayout.SOUTH);
		
		// Start of parsing the months, so they can be displayed in the ComboBox
		parser = new TSFParser();
		parser.parseMonths();
		ArrayList<String> months = parser.getMonthsList();
		for(String month: months){
			comboBox.addItem(month);
		}
		
	}
	
	private void startParsing(){
		this.parser.setMonth(this.comboBox.getSelectedIndex());
		System.out.println(this.comboBox.getSelectedIndex());
		this.parser.parseUsers();
	}
}
