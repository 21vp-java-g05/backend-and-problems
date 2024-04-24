package main.frontend.backend.objects;

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
		String value = "(DEFAULT, " + toString() + ")";
		
		try { return (id = db.add_getAuto("AUTHOR", value)) > 0; }
		finally { db.close(); }
	}
	public boolean update_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "name = '" + name + "', description = '" + description + "'";
		String condition = "id = " + String.valueOf(id);
		
		try { return db.update("AUTHOR", value, condition) > 0; }
		finally { db.close(); }
	}
	public boolean updateStatus_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);
		
		try {
			if (! db.setAutoCommit(false)) return false;
			
			if (db.changeStatus("AUTHOR", condition, status) < 0) return false;
			
			if (! status) {
				condition = "author = " + String.valueOf(id) + " AND status = true";
				if (db.changeStatus("BOOK", condition, status) < 0) {
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
		
		try { return db.delete("AUTHOR", condition) > 0; }
		finally { db.close(); }
	}

	@Override
	public String toString() {		
		return "'" + name + "', '" + description + "', " + String.valueOf(status);
	}
}
