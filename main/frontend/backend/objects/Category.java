package main.frontend.backend.objects;

import java.sql.*;

import main.frontend.backend.utils.DBconnect;

public class Category {
	private int id;
	private String name, description;
	private boolean status;
	
	public Category() {}
	public Category(int id, String name, String description, boolean status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
	}
	public Category(int id, String name, String description) { this(id, name, description, true); }
	public Category(Category other) { this(other.id, other.name, other.description, other.status); }

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
		boolean rs = db.add("CATEGORY", value, true);
		
		db.close();
		return rs;
	}
	public boolean update_toDatabase(int id) {
		DBconnect db = new DBconnect();
		String value = "name = '" + name + "', description = '" + description + "', status = " + String.valueOf(status);
		String condition = "id = " + String.valueOf(id);

		try {
			db.turnAutoCommitOff();
			if (! db.update("CATEGORY", value, condition)) return false;

			if (! status) {
				condition = "book_id = id AND category_id = " + String.valueOf(id);
				if (! db.updateFrom("BOOK", "status = false", "CATEGORY_BOOK", condition)) {
					db.rollback();
					return false;
				}
			}

			db.commit();
		} catch (SQLException e) {
			System.err.println("Update category");
			e.printStackTrace();
		} finally { db.close(); }
		return true;
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);
		boolean rs = db.delete("CATEGORY", condition);
		
		db.close();
		return rs;
	}
	public boolean changeId(int id) {
		DBconnect db = new DBconnect();
		String value = "id = " + String.valueOf(this.id);
		String condition = "id = " + String.valueOf(id);
		
		try {
			db.turnAutoCommitOff();
			if (! db.update("CATEGORY", value, condition)) return false;
			
			// Change category_id to category_book
			if (! db.update("CATEGORY_BOOK", value, "category_" + condition)) {
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
