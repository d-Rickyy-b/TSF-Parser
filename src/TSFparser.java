import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TSFparser {
	// Numbers have the following meaning
	// Have a look at the leaderboard online
	// 0	  1		 2
	// Mar	  Feb	 Jan
	
	private static int selectedMonth = 2;
	
	//TODO pass parameters -> noGui
	//TODO Graphic interface
	//TODO select month from drop down menu and start sorting afterwards
	//TODO Create database at the beginning
	//TODO textfield for database path
	//TODO create table in db
	//TODO table name = year<current year yyyy> (example: year2016)
	
	
	public static void main(String[] args) {
		if (selectedMonth > 2 || selectedMonth < 0){
			System.err.println("You fucked up! Please enter a month number between (including) 0 and 2!");
			System.exit(1);
		}
		
		ArrayList<TSFMember> memberlist = new ArrayList<TSFMember>();
		ArrayList<String> datesList = new ArrayList<String>();
		
		try {
			Document doc = Jsoup.connect("https://core.telegram.org/tsi/leaderboards").get();

			Element month_table = doc.select("tbody").get(1);
			Elements all_dates = doc.select("thead th");

			for (Element date : all_dates) {
				if (!date.text().equals("") && !date.text().equals("Today") && !date.text().equals("Yesterday")) {
					datesList.add(date.text().replaceAll(" \\d{4}", "").toLowerCase());
					System.out.println("Added: " + date.text().replaceAll(" \\d{4}", "").toLowerCase());
				}
			}
			
			System.out.println(String.format("Month to be crawled is: %s", datesList.get(selectedMonth)));
			
			Elements month_entries = month_table.select("tr");

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
				memberlist.add(member);
			}

			for (TSFMember member : memberlist) {
				if (!isSaved(member.getName())) {
					// Insert new user and Update afterwards
					System.out.println("New User: " + member.getName());
					writeToDB(member.getSQLInsertStatement());
				}

				// Update the data in the database
				if (isValueZero(datesList.get(selectedMonth), member.getName())) {
					writeToDB(member.getSQLStatement(datesList.get(selectedMonth)));
				} else if(member.getMonth(selectedMonth) != 0) {
					System.err.println("The field is already filled! Member: " + member.getName());
					
					Scanner scanner;											
					scanner = new Scanner(System.in);
					
					int currentValue = getFromDB(datesList.get(selectedMonth), member.getName());
					int newValue = member.getMonth(selectedMonth);
					
					System.out.print("Add " + newValue + " to " + currentValue + "? (Y/n): ");
					String Answer = scanner.nextLine();
					//scanner.close();
					
					if (Answer.equals("n")) {
						System.out.println("Value not modified!");
					}
					if (Answer.equals("y") || Answer.equals("Y") || Answer.isEmpty()) {
						writeToDB(String.format("UPDATE stats SET %s = %s + %s WHERE name='%s';", datesList.get(selectedMonth), datesList.get(selectedMonth), newValue, member.getName()));
						System.out.println("Added both values to: " + getFromDB(datesList.get(selectedMonth), member.getName()));
					}
				} else {
					System.out.println("Value must be zero: " + member.getMonth(selectedMonth));
				}
			}
			
			System.out.println("All members processed. Number of processed members: " + memberlist.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// checks if a certain username is already saved in the database.
	private static boolean isSaved(String name) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT(*) FROM stats WHERE name='%s';", name));

			int size = 0;

			while (resultSet.next()) {
				size = resultSet.getInt(1);
			}

			if (size > 0) {
				statement.close();
				c.close();
				return true;
			} else {
				statement.close();
				c.close();
				return false;
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
		}
		return true;
	}

	// executes a certain sql statement
	private static void writeToDB(String sql) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
			Statement statement = c.createStatement();
			statement.executeUpdate(sql);
			statement.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int getFromDB(String month, String name) {
		Connection c = null;
		int result;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(String.format("SELECT %s FROM stats WHERE name='%s';", month, name));
			resultSet.next();
			
			result = resultSet.getInt(month);
			c.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Checks if a certain user got 0 answers in a certain month
	private static boolean isValueZero(String month, String name) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rico\\workspace\\TSF Parser\\src\\tsf-database.db");
			Statement statement = c.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT " + month + " FROM stats WHERE name='" + name + "';");
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
