package main.frontend.backend.users;

import main.frontend.backend.utils.DBconnect;

public class Customer {
	private int id;
	private String mail, fullname;
	private boolean male, enabled;

	public Customer() {}
	public Customer(String mail, String fullname, boolean male, boolean enabled) {
		this.mail = mail;
		this.fullname = fullname;
		this.male = male;
		this.enabled = enabled;
	}
	public Customer(String mail, String fullname, boolean male) { this(mail, fullname, male, true); }
	public Customer(Customer other) { this(other.mail, other.fullname, other.male, other.enabled); }

	public int getId() { return id; }
	public String getMail() { return mail; }
	public String getFullname() { return fullname; }
	public boolean isMale() { return male; }

	public void changeInfo(String mail, String fullname, boolean male, boolean enabled) {
		this.mail = mail;
		this.fullname = fullname;
		this.male = male;
		this.enabled = enabled;
	}

	
	public boolean add_toDatabase() {
		String object = "CUSTOMER";
		String values = "(DEFAULT, '"
							+ this.fullname.replace("'", "''") + "', '"
							+ this.mail.replace("'", "''") + "', '"
							+ this.male + "', "
							+ this.enabled + ")";
		
		System.out.println("Values inserted: " + values);
		
		try (DBconnect db = new DBconnect()) {
			int result = db.add(object, values);
	
			if (result > 0) {
				System.out.println("Account successfully added to database.");
				return true;
			} else {
				System.out.println("Failed to add account to the database.");
				return false;
			}
		} catch (Exception e) {
			System.err.println("An error occurred while adding the account to the database.");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String toString() {
		String idStr = "\tID: " + String.valueOf(id) + "\n";
		String mailStr = "\tMail: " + mail + "\n";
		String fullnameStr = "\tFull name: " + fullname + "\n";
		String gender = "\tGender: " + (male ? "male" : "female") + "\n";
		String stsStr = "\tStatus: " + (enabled ? "enable" : "disable") + "\n";
		
		return idStr + mailStr + fullnameStr +  gender + stsStr;
	}
}
