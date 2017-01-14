package tsf_parser.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tsf_parser.database.Database;
import tsf_parser.output.Output;

public class TSFParser {
	// Numbers have the following meaning
	// Have a look at the leaderboard online
	// 0 1 2
	// Mar Feb Jan

	private int selectedMonth = 2;
	private ArrayList<String> monthsList;
	private ArrayList<TSFMember> memberList;
	private boolean displayGUI;
	private static Database database;

	// TODO pass parameters -> noGui
	// TODO Graphic interface
	// TODO select month from drop down menu and start sorting afterwards
	// TODO Create database at the beginning
	// TODO create table in db
	// TODO table name = year<current year yyyy> (example: year2016)

	public TSFParser(boolean displayGUI) {
		this.displayGUI = displayGUI;
		database = Database.getInstance();
	}

	public void parseUsers(String databasePath) {
		database.setDatabasePath(databasePath);
		database.loadDatabase();
		if (selectedMonth > 2 || selectedMonth < 0) {
			System.err.println("You fucked up! Please enter a month number between (including) 0 and 2!");
			System.exit(1);
		}

		memberList = new ArrayList<TSFMember>();

		try {
			Document doc = Jsoup.connect("https://core.telegram.org/tsi/leaderboards").get();

			Element month_table = doc.select("tbody").get(1);

			System.out.println(String.format("Month to be crawled is: %s", monthsList.get(selectedMonth)));

			Elements month_entries = month_table.select("tr");

			// Adds the members to the ArrayList
			for (Element row : month_entries) {
				Elements cols = row.select("td");
				Element name = row.select("th").get(0);
				TSFMember member = new TSFMember(name.text(), selectedMonth);
				int month_counter = 0;

				for (Element col : cols) {
					if (!col.text().equals("")) {
						member.setMonth(month_counter, Integer.parseInt(col.text().replaceAll("\\s+", "")));
					}
					month_counter++;
				}
				memberList.add(member);
				System.out.println(member.getName());
			}

			// TODO move to GUI
			processUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processUsers() {
		// Checks all the members and saves them to the database
		for (TSFMember member : memberList) {
			if (!database.isSaved(member.getName())) {
				// Insert new user and Update afterwards
				System.out.println("New User: " + member.getName());
				database.writeToDB(member.getSQLInsertStatement());
			}

			// Update the data in the database
			if (isValueZero(monthsList.get(selectedMonth), member.getName())) {
				database.writeToDB(member.getSQLStatement(monthsList.get(selectedMonth)));
			} else if (member.getMonth(selectedMonth) != 0) {
				String text = "The field is already filled! Member: " + member.getName();
				
//					System.err.println("The field is already filled! Member: " + member.getName());
//					Scanner scanner;
//					scanner = new Scanner(System.in);

					int currentValue = database.getFromDB(monthsList.get(selectedMonth), member.getName());
					int newValue = member.getMonth(selectedMonth);

//					System.out.print("Add " + newValue + " to " + currentValue + "? (Y/n): ");
					boolean modifyValue = Output.userDialog(displayGUI, text + "\nAdd " + newValue + " to " + currentValue + "? (Y/n): ");
					//TODO Add "No to all" field
//					String Answer = scanner.nextLine();
//					// scanner.close();
//
//					// TODO if console application -> this, else popup
//					if (Answer.equals("y") || Answer.equals("Y") || Answer.isEmpty()) {
//						writeToDB(String.format("UPDATE stats SET %s = %s + %s WHERE name='%s';",
//								monthsList.get(selectedMonth), monthsList.get(selectedMonth), newValue,
//								member.getName()));
//						System.out.println("Added both values to: "
//								+ database.getFromDB(monthsList.get(selectedMonth), member.getName()));
//					} else {
//						System.out.println("Value not modified!");
//						Output.print(displayGUI, "Value not modified!");
//					}
					if (modifyValue) {
						database.writeToDB(String.format("UPDATE stats SET %s = %s + %s WHERE name='%s';",
								monthsList.get(selectedMonth), monthsList.get(selectedMonth), newValue,
								member.getName()));
						//TODO sysout ändern
						System.out.println("Added both values to: "
								+ database.getFromDB(monthsList.get(selectedMonth), member.getName()));
					} else {
						Output.print(displayGUI, "Value not modified!");
					}
					
					
			} else {
				System.out.println("Value must be zero: " + member.getMonth(selectedMonth));
			}
		}
		System.out.println("All members processed. Number of processed members: " + memberList.size());
	}

	public ArrayList<String> getMonthsList() {
		return this.monthsList;
	}

	public ArrayList<TSFMember> getMembersList() {
		return this.memberList;
	}

	public void setMonth(int month) {
		this.selectedMonth = month;
		System.out.println(this.monthsList.get(month) + " selected.");
	}

	// Has to be called first
	// saves a list of (3) months, so that the user can choose what month to
	// pick
	public void parseMonths() {
		monthsList = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect("https://core.telegram.org/tsi/leaderboards").get();

			// Element month_table = doc.select("tbody").get(1);
			Elements all_dates = doc.select("thead th");

			for (Element date : all_dates) {
				if (!date.text().equals("") && !date.text().equals("Today") && !date.text().equals("Yesterday")) {
					monthsList.add(date.text().replaceAll(" \\d{4}", "").toLowerCase());
					System.out.println("Added: " + date.text().replaceAll(" \\d{4}", "").toLowerCase());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	// checks if a certain username is already saved in the database.
//	private static boolean isSaved(String name) {
//		Connection c = null;
//		try {
//			Class.forName("org.sqlite.JDBC");
//			// TODO variable db path
//			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
//			Statement statement = c.createStatement();
//			ResultSet resultSet = statement
//					.executeQuery(String.format("SELECT COUNT(*) FROM stats WHERE name='%s';", name));
//
//			int size = 0;
//
//			while (resultSet.next()) {
//				size = resultSet.getInt(1);
//			}
//
//			if (size > 0) {
//				statement.close();
//				c.close();
//				return true;
//			} else {
//				statement.close();
//				c.close();
//				return false;
//			}
//
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(1);
//		}
//		return true;
//	}
//
//	// executes a certain sql statement
//	private static void writeToDB(String sql) {
//		Connection c = null;
//		try {
//			Class.forName("org.sqlite.JDBC");
//			// TODO variable db path
//			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
//			Statement statement = c.createStatement();
//			statement.executeUpdate(sql);
//			statement.close();
//			c.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// private static int getFromDB(String month, String name) {
	// Connection c = null;
	// int result;
	// try {
	// Class.forName("org.sqlite.JDBC");
	// //TODO variable db path
	// c =
	// DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF
	// Parser\\src\\tsf-database.db");
	// Statement statement = c.createStatement();
	// ResultSet resultSet = statement.executeQuery(String.format("SELECT %s
	// FROM stats WHERE name='%s';", month, name));
	// resultSet.next();
	//
	// result = resultSet.getInt(month);
	// c.close();
	// return result;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return 0;
	// }

	// Checks if a certain user got 0 answers in a certain month
	private static boolean isValueZero(String month, String name) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			// TODO variable db path
			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT " + month + " FROM stats WHERE name='" + name + "';");
			resultSet.next();

			if (resultSet.getInt(month) > 0) {
				statement.close();
				c.close();
				return false;
			} else {
				statement.close();
				c.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}