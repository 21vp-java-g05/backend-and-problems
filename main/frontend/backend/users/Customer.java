package main.frontend.backend.users;

import main.frontend.backend.utils.DBconnect;

public class Customer {
	private int id;
	private String mail, fullname;
	private boolean gender, status;

	public Customer() {}
	public Customer(String mail, String fullname, boolean gender, boolean status) {
		this.mail = mail;
		this.fullname = fullname;
		this.gender = gender;
		this.status = status;
	}
	public Customer(String mail, String fullname, boolean gender) { this(mail, fullname, gender, true); }
	public Customer(Customer other) { this(other.mail, other.fullname, other.gender, other.status); }

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
		int rs = db.add("CUSTOMER", value);
		try {
			if (rs < 1) {
				if (rs == 0)
					System.err.println("This customer is already in the database");
				return false;
			}
			return true;
		} finally { db.close(); }
	}
	
	@Override
	public String toString() {
		return "'" + fullname + "', '" + mail + "', " + String.valueOf(gender) + ", " + String.valueOf(status);
	}
}
