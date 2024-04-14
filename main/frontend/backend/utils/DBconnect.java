package main.frontend.backend.utils;

import java.sql.*;

public class DBconnect implements AutoCloseable {
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

	public void turnAutoCommitOff() throws SQLException { connection.setAutoCommit(false); }
	public void turnAutoCommitOn() throws SQLException { connection.setAutoCommit(true); }
	public void commit() throws SQLException { connection.commit(); }
	public void rollback() throws SQLException { connection.rollback(); }

	// Method to execute a query and retrieve a ResultSet
	public ResultSet view(String column, String object, String condition) {
		String query = "SELECT " + (column == null || column.isEmpty() ? "*" : column) + " FROM " + object;
		if (condition != null)
			if (! condition.isEmpty()) query += " WHERE " + condition;
		
		try { return connection.createStatement().executeQuery(query); }
		catch (SQLException e) {
			System.err.println("Error in view: " + e.getMessage());
			return null;
		}
	}
	public boolean checkExists(String object, String condition) {
		ResultSet resultSet = view(null, object, condition);
		try { return resultSet.next(); }
		catch (SQLException e) {
			System.err.println("Error in view: " + e.getMessage());
			return false;
		}
	}
	public int add(String object, String value) {
		String query = "INSERT INTO " + object + " VALUES " + value;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Error in add" + e.getMessage());
			return -1;
		}
	}
	public int update(String object, String value, String condition) {
		String query = "UPDATE " + object + " SET " + value + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Error in update" + e.getMessage());
			return -1;
		}
	}
	public int updateFrom(String object, String value, String sub, String condition) {
		String query = "UPDATE " + object + " SET " + value + "FROM (" + sub + ")" + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Error in update from" + e.getMessage());
			return -1;
		}
	}
	public int changeStatus(String object, String condition, boolean status) {
		String query = "UPDATE " + object + " SET Enabled = " + String.valueOf(status) + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Error in change statusS" + e.getMessage());
			return -1;
		}
	}
	public int delete(String object, String condition) {
		String query = "DELETE FROM " + object + " WHERE " + condition;
		try { return connection.createStatement().executeUpdate(query); }
		catch (SQLException e) {
			System.err.println("Error in delete" + e.getMessage());
			return -1;
		}
	}

	public void close() {
		try { connection.close(); }
		catch (SQLException e) { System.err.println("Error closing connection"); }
	}
}
