package main.frontend.backend.objects;

import java.sql.*;

import main.frontend.backend.utils.DBconnect;

public class Author {
	private int id;
	private String name, description;
	private boolean status;

	public Author() {}
	public Author(int id, String name, String description, boolean status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
	}
	public Author(int id, String name, String description) { this(id, name, description, true); }
	public Author(Author author) { this(author.id, author.name, author.description); }

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
		boolean rs = db.add("AUTHOR", value, true);
		
		db.close();
		return rs;
	}
	public boolean update_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "name = '" + name + "', description = '" + description + "', status = " + String.valueOf(status);
		String condition = "id = " + String.valueOf(id);

		try {
			db.turnAutoCommitOff();
			if (! db.update("AUTHOR", value, condition)) return false;

			if (! status) {
				condition = "author = " + String.valueOf(id) + " AND status = true";
				if (! db.update("BOOK", "status = false", condition)) {
					db.rollback();
					return false;
				}
			}

			db.commit();
		} catch (SQLException e) {
			System.err.println("Update author");
			e.printStackTrace();
		} finally { db.close(); }
		return true;
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);
		boolean rs = db.delete("AUTHOR", condition);
		
		db.close();
		return rs;
	}
	public boolean changeId(int id) {
		DBconnect db = new DBconnect();
		String value = "id = " + String.valueOf(this.id);
		String condition = "id = " + String.valueOf(id);
		
		try {
			db.turnAutoCommitOff();
			if (! db.update("AUTHOR", value, condition)) return false;
			
			value = "author = " + String.valueOf(id);
			if (! db.update("BOOK", value, "author = " + String.valueOf(this.id))) {
				db.rollback();
				return false;
			}
			
			this.id = id;
			db.commit();
		} catch (SQLException e) {
			System.err.println("Change id");
			e.printStackTrace();
			return false;
		} finally { db.close(); }

		return true;
	}

	@Override
	public String toString() {		
		return String.valueOf(id) + ", '" + name + "', '" + description + "', " + String.valueOf(status);
	}
}
