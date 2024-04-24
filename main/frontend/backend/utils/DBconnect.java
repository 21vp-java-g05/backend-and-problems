package main.frontend.backend.utils;

import java.sql.*;

public class DBconnect {
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql:" + System.getenv("DB_URL");
	private static final String USER = System.getenv("DB_USER");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");

	private static Connection connection;

	// Constructor for initializing the connection
	public DBconnect() {
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connected");
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found");
		} catch (SQLException e) {
			System.err.println("Connection fail");
		}
	}

	public boolean setAutoCommit(boolean status) {
		try { connection.setAutoCommit(status); }
		catch (SQLException e) {
			System.err.println("Change commit's status error: " + e.getMessage());
			return false;
		}
		return true;
	}
	public boolean commit() {
		try { connection.commit(); }
		catch (SQLException e) {
			System.err.println("Committing error: " + e.getMessage());
			return false;
		}
		return true;
	}
	public boolean rollback() {
		try { connection.rollback(); }
		catch (SQLException e) {
			System.err.println("Rollback error: " + e.getMessage());
			return false;
		}
		return true;
	}

	// Method to execute a query and retrieve a ResultSet
	public ResultSet view(String column, String object, String condition) {
		String query = "SELECT " + (column == null || column.isEmpty() ? "*" : column) + " FROM " + object;
		if (condition != null)
			if (! condition.isEmpty()) query += " WHERE " + condition;
		
		try { return connection.createStatement().executeQuery(query); }
		catch (SQLException e) {
			System.err.println("Viewing error: " + e.getMessage());
			return null;
		}
	}
	public boolean checkExists(String object, String condition) {
		ResultSet resultSet = view(null, object, condition);
		try { return resultSet.next(); }
		catch (SQLException e) {
			System.err.println("Checking exists error: " + e.getMessage());
			return false;
		}
	}
	public int add(String object, String value) {
		String query = "INSERT INTO " + object + " VALUES " + value;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Adding error: " + e.getMessage());
			return -1;
		}
	}
	public int add_getAuto(String object, String value) {
		String query = "INSERT INTO " + object + " VALUES " + value;
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = statement.getGeneratedKeys();
			return rs.next() ? rs.getInt(1) : 0;
		}
		catch (SQLException e) {
			System.err.println("Adding auto error: " + e.getMessage());
			return -1;
		}
	}
	public int update(String object, String value, String condition) {
		String query = "UPDATE " + object + " SET " + value + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Update error: " + e.getMessage());
			return -1;
		}
	}
	public int changeStatus(String object, String condition, boolean status) {
		String query = "UPDATE " + object + " SET status = " + String.valueOf(status) + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Changing status error: " + e.getMessage());
			return -1;
		}
	}
	public int delete(String object, String condition) {
		String query = "DELETE FROM " + object + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Deleting error: " + e.getMessage());
			return -1;
		}
	}

	public void close() {
		try { connection.close(); }
		catch (SQLException e) { System.err.println("Error closing connection"); }
	}
}
