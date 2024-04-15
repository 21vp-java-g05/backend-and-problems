package main.frontend.backend.users;

import main.frontend.backend.lists.AccountList;
import main.frontend.backend.utils.DBconnect;

public class Administrator extends Account {
	public Administrator(int id, String username, String password, String mail, String fullname, int role) {
		super(id, username, password, mail, fullname, role);
		checkRole();
	}
	public Administrator(int id, String username, String password, String mail, String fullname, int role, boolean status) {
		super(id, username, password, mail, fullname, role, status);
		checkRole();
	}
	public Administrator(Administrator other) { super(other); }
	public Administrator(Account other) {
		super(other);
		checkRole();
	}

	public AccountList load_fromDatabase() {
		AccountList accounts = new AccountList();
		return accounts.load_fromDatabase(null) ? accounts : null;
	}
	public boolean add_toDatabase(Account account) {
		DBconnect db = new DBconnect();
		String value = "";
		try { return db.add("ACCOUNT", value) > 0; }
		finally { db.close(); }
	}

	private void checkRole() {
		if (getRole() != 0) throw new IllegalArgumentException("Role must be administrator");
	}

	@Override
	public String toString() {
		String str = "Administrator:" + "\n";
		return str + super.toString();
	}
}
