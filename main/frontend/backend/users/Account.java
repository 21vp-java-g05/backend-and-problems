package main.frontend.backend.users;

public class Account {
	private int id;
	private String username, password, mail;
	private boolean enabled;

	public Account(int id, String username, String password, String mail, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.enabled = enabled;
	}
	public Account(int id, String username, String password, String mail) { this(id, username, password, mail, true); }
	public Account(Account other) { this(other.id, other.username, other.password, other.mail, other.enabled); }

	public int getId() { return id; }
	public String getAccountUsername() { return username; }
	public String getAccountPassword() { return password; }
	public String getMail() { return mail; }
	public boolean isEnabled() { return enabled; }

	public void changeInfo(int id, String username, String password, String mail, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		String idStr = "\tID: " + id + "\n";
		String nameStr = "\tUser name: " + username + "\n";
		String disStr = "\tPassword: " + password + "\n";
		String mailStr = "\tMail: " + mail + "\n";
		String stsStr = "\tStatus: " + (enabled ? "enable" : "disable") + "\n";
		
		return idStr + nameStr + disStr + mailStr + stsStr;
	}
}
