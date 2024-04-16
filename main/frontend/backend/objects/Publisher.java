package main.frontend.backend.objects;

import java.sql.*;

import main.frontend.backend.utils.DBconnect;

public class Publisher {
	private int id;
	private String name, description;
	private boolean status;
	
	public Publisher() {}
	public Publisher(int id, String name, String description, boolean status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
	}
	public Publisher(int id, String name, String description) { this(id, name, description, true); }
	public Publisher(Publisher other) { this(other.id, other.name, other.description, other.status); }

	public int getId() { return id; }
	public String getName() { return name; }
	public String getDescription() { return description; }
	public boolean getStatus() { return status; }

	public void setId(int id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setDescription(String description) { this.description = description; }
	public void setStatus(boolean status) { this.status = status; }

	public void changeInfo(int id, String name, String description, boolean status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
	}

	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "(DEFAULT, " + toString() + ")";
		try { return db.add("PUBLISHER", value) > 0; }
		finally { db.close(); }
	}
	public boolean update_toDatabase(int id) {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			String value = "name = '" + name + "', description = '" + description + "', status = " + String.valueOf(status);
			String condition = "id = " + String.valueOf(id);
			if (db.update("PUBLISHER", value, condition) <= 0)  return false;

			if (! status) {
				condition = "publisher = " + String.valueOf(id) + " AND status = true";
				if (db.update("BOOK", "status = false", condition) < 0) {
					db.rollback();
					return false;
				}
			}
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error in updating publisher: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);
		boolean rs = db.delete("PUBLISHER", condition) > 0;
		db.close();
		return rs;
	}

	@Override
	public String toString() {
		return "'" + name + "', '" + description + "', " + String.valueOf(status);
	}
}
