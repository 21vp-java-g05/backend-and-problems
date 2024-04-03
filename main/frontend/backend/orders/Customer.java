package main.frontend.backend.orders;

public class Customer {
	private int id;
	private String mail, fullname, phone;
	private boolean male, enabled;

	public Customer() {}
	public Customer(int id, String mail, String fullname, String phone, boolean male, boolean enabled) {
		this.id = id;
		this.mail = mail;
		this.fullname = fullname;
		this.phone = phone;
		this.male = male;
		this.enabled = enabled;
	}
	public Customer(int id, String mail, String fullname, String phone, boolean male) { this(id, mail, fullname, phone, male, true); }
	public Customer(Customer other) { this(other.id, other.mail, other.fullname, other.phone, other.male, other.enabled); }

	public int getId() { return id; }
	public String getMail() { return mail; }
	public String getFullname() { return fullname; }
	public String getPhone() { return phone; }
	public boolean isMale() { return male; }

	public void changeInfo(int id, String mail, String fullname, String phone, boolean male, boolean enabled) {
		this.id = id;
		this.mail = mail;
		this.fullname = fullname;
		this.phone = phone;
		this.male = male;
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		String idStr = "\tID: " + String.valueOf(id) + "\n";
		String mailStr = "\tMail: " + mail + "\n";
		String fullnameStr = "\tFull name: " + fullname + "\n";
		String phoneStr = "\tPhone: " + phone + "\n";
		String gender = "\tGender: " + (male ? "male" : "female") + "\n";
		String stsStr = "\tStatus: " + (enabled ? "enable" : "disable") + "\n";
		
		return idStr + mailStr + fullnameStr + phoneStr + gender + stsStr;
	}
}
