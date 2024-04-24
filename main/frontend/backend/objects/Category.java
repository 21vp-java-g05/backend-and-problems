package main.frontend.backend.objects;

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
		String value = "(DEFAULT, " + toString() + ")";
		
		try { return (id = db.add_getAuto("CATEGORY", value)) > 0; }
		finally { db.close(); }
	}
	public boolean update_toDatabase(int id) {
		DBconnect db = new DBconnect();
		String value = "name = '" + name + "', description = '" + description + "'";
		String condition = "id = " + String.valueOf(id);

		try { return db.update("CATEGORY", value, condition) > 0; }
		finally { db.close(); }
	}
	public boolean updateStatus_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);

		try {
			if (! db.setAutoCommit(false)) return false;
			
			if (db.changeStatus("CATEGORY", condition, status) < 0) return false;
			
			if (! status) {
				String value = "status = false FROM CATEGORY_BOOK";
				condition = "status = true AND book_id = id AND category_id = " + String.valueOf(id);
				if (db.update("BOOK", value, condition) < 0) {
					db.rollback();
					return false;
				}
			}

			if (! db.commit()) {
				db.rollback();
				return false;
			};
		} finally { db.close(); }
		return true;
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);

		try { return db.delete("CATEGORY", condition) > 0; }
		finally { db.close(); }
	}
	
	@Override
	public String toString() {
		return "'" + name + "', '" + description + "', " + String.valueOf(status);
	}
}
