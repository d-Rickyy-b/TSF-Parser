package tsf_parser.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

	private static Database instance;
	private Connection connection;
	private String databasePath;

	private Database() {
	}

	public static Database getInstance() {
		if (Database.instance == null) {
			Database.instance = new Database();
		}
		return Database.instance;
	}

	// Loads the database
	public void loadDatabase() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(databasePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Closes the DB connection
	public void unloadDatabase() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Sets the path on the HDD to the DB
	public boolean setDatabasePath(String path) {
		System.out.println("DB Path: " + path);
		if (!path.endsWith(".db"))
			return false;

		databasePath = String.format("jdbc:sqlite:%s", path);
		return true;
	}

	// Returns the value of a specific month for a user
	public int getFromDB(String month, String name) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery(String.format("SELECT %s FROM stats WHERE name='%s';", month, name));
			resultSet.next();
			return resultSet.getInt(month);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean isValueZero(String month, String name) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery(String.format("SELECT %s FROM stats WHERE name='%s';", month, name));
			resultSet.next();

			if (resultSet.getInt(month) > 0) {
				statement.close();
				return false;
			} else {
				statement.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	// checks if a certain username is already saved in the database.
	public boolean isSaved(String name) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery(String.format("SELECT COUNT(*) FROM stats WHERE name='%s';", name));

			int size = 0;

			while (resultSet.next()) {
				size = resultSet.getInt(1);
			}

			if (size > 0) {
				statement.close();
				return true;
			} else {
				statement.close();
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return true;
	}

	// executes a certain sql statement
	public void writeToDB(String sql) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO create the needed table for saving the data
	public void createTable() {

	}

	// TODO create a database file
	public void createDatabase() {

	}

	public boolean writeCSV(String exportPath, String seperator) {
		try {
			
			String csvContent = String.join(seperator, "Name", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec\n");
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM stats;");

			while (resultSet.next()) {
				String row = "";
				for (int month = 1; month <= 14; month++) {
					if (month != 2 && month != 14) {
						row += resultSet.getString(month) + seperator;
					} else if (month == 14) {
						row += resultSet.getString(month);
					}
				}
				csvContent += row + "\n";
			}

			try {
				// TODO works only on Windows due to backslashes
				PrintWriter writer = new PrintWriter(exportPath + "\\tsf-export.csv", "UTF-8");
				writer.print(csvContent);
				writer.close();
			} catch (IOException e) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
