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
		String value = "(" + this.toString() + ")";
		int rs = db.add("PUBLISHER", value, true);
		if (rs < 1) {
			if (rs == 0)
				System.err.println("This publisher is already in the database");
			return false;
		}
		
		db.close();
		return true;
	}
	public boolean update_toDatabase(int id) {
		DBconnect db = new DBconnect();
		String value = "name = '" + name + "', description = '" + description + "', status = " + String.valueOf(status);
		String condition = "id = " + String.valueOf(id);

		try {
			db.turnAutoCommitOff();
			int rs = db.update("PUBLISHER", value, condition);
			if (rs <= 0) {
				if (rs == 0)
					System.err.println("Publisher cannot found");
				return false;
			}

			if (! status) {
				condition = "publisher = " + String.valueOf(id) + " AND status = true";
				if (db.update("BOOK", "status = false", condition) < 0) {
					db.rollback();
					return false;
				}
			}

			db.commit();
		} catch (SQLException e) {
			System.err.println("Update publisher");
			e.printStackTrace();
		} finally { db.close(); }
		return true;
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);
		int rs = db.delete("PUBLISHER", condition);
		if (rs <= 0) {
			if (rs == 0)
				System.err.println("Cannot found publisher");
			return false;
		}
		
		db.close();
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(id) + ", '" + name + "', '" + description + "', " + String.valueOf(status);
	}
}
