package main.frontend.backend.users;

import main.frontend.backend.utils.DBconnect;

public class Customer {
	private int id;
	private String mail, fullname;
	private boolean gender, status;

	public Customer() {}
	public Customer(int id, String fullname, String mail, boolean gender, boolean status) {
		this.id = id;
		this.mail = mail;
		this.fullname = fullname;
		this.gender = gender;
		this.status = status;
	}
	public Customer(int id, String fullname, String mail, boolean gender) { this(id, fullname, mail, gender, true); }
	public Customer(Customer other) { this(other.id, other.mail, other.fullname, other.gender, other.status); }

	public int getId() { return id; }
	public String getMail() { return mail; }
	public String getFullname() { return fullname; }
	public boolean getGender() { return gender; }

	public void changeInfo(String mail, String fullname, boolean gender, boolean status) {
		this.mail = mail;
		this.fullname = fullname;
		this.gender = gender;
		this.status = status;
	}
	
	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "(DEFAULT, " + toString() + ")";
		
		try { return (id = db.add_getAuto("CUSTOMER", value)) > 0; }
		finally { db.close(); }
	}
	public boolean check() {
		DBconnect db = new DBconnect();
		try {
			return db.checkExists("CUSTOMER", "mail = " + mail) ? true : add_toDatabase();
		} finally { db.close(); }
	}
	
	@Override
	public String toString() {
		return "'" + fullname + "', '" + mail + "', " + String.valueOf(gender) + ", " + String.valueOf(status);
	}
}
