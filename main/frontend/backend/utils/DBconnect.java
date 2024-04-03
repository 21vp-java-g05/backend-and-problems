package main.frontend.backend.utils;

import java.sql.*;

public class DBconnect {
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql:Book_store";
	private static final String USER = "postgres";
	private static final String PASSWORD = "03102003Minh";

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
	public void commit() throws SQLException { connection.commit(); }
	public void rollback() throws SQLException { connection.rollback(); }

	// Method to execute a query and retrieve a ResultSet
	public ResultSet view(String column, String object, String condition) {
		String query = "SELECT " + ((column == null || column.isEmpty()) ? "*" : column) + " FROM " + object;
		if (condition != null)
			if (! condition.isEmpty()) query += " WHERE " + condition;
		ResultSet rs = null;
		
		try {
			Statement st = connection.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			System.err.println("View");
			e.printStackTrace();
		}
		
		return rs;
	}
	private int checkExists(String object, String condition) {
		try {
			if (view(null, object, condition).next()) return 1;
		} catch (SQLException e) {
			System.err.println("Check exits");
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	public boolean add(String object, String value, boolean checkExist) {
		String query;
		if (checkExist) {
			// Get id
			String name = value.split(",")[1];
			name = name.substring(1);
			
			// Check existing
			query = "name = " + name;
			if (checkExists(object, query) == 1) {
				System.err.println("This " + object + " is already existing");
				return false;
			}
		}
		
		// Add
		query = "INSERT INTO " + object + " VALUES " + value;
		int result;
		
		try {
			Statement st = connection.createStatement();
			result = st.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Add");
			e.printStackTrace();
			return false;
		}
		
		return result > 0;
	}
	public boolean update(String object, String value, String condition) {
		String query = "UPDATE " + object + " SET " + value + " WHERE " + condition;
		int result;
		
		try {
			Statement st = connection.createStatement();
			result = st.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Update");
			e.printStackTrace();
			return false;
		}

		return result > 0;
	}
	public boolean updateFrom(String object, String value, String sub, String condition) {
		String query = "UPDATE " + object + " SET " + value + "FROM (" + sub + ")" + " WHERE " + condition;
		int result;
		
		try {
			Statement st = connection.createStatement();
			result = st.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Update");
			e.printStackTrace();
			return false;
		}

		return result > 0;
	}
	public boolean changeStatus(String object, String condition, boolean status) {
		String query = "UPDATE " + object + " SET Enabled = " + String.valueOf(status) + " WHERE " + condition;
		int result;
		
		try {
			Statement st = connection.createStatement();
			result = st.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Change status");
			e.printStackTrace();
			return false;
		}
		
		return result > 0;
	}
	public boolean delete(String object, String condition) {
		String query = "DELETE FROM " + object + " WHERE " + condition;
		int result;
		
		try {
			Statement st = connection.createStatement();
			result = st.executeUpdate(query);
		} catch (SQLException e) {
			System.err.println("Delete");
			e.printStackTrace();
			return false;
		}

		return result > 0;
	}

	public Connection getConnection() {
		return connection;
	}
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error closing connection");
		}
	}
}
