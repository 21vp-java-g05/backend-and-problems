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

	private void checkRole() {
		if (role != 0) throw new IllegalArgumentException("Role must be administrator");
	}

	public AccountList loadAccounts_fromDatabase() {
		AccountList accounts = new AccountList();
		return accounts.load_fromDatabase(null) ? accounts : null;
	}
	public boolean addAccount_toDatabase(Account account) {
		DBconnect db = new DBconnect();
		String value = "(DEFAULT, " + account.toString() + ")";

		try { return (id = db.add_getAuto("ACCOUNT", value)) > 0; }
		finally { db.close(); }
	}
	public boolean editAccount_fromDatabase(Account account) {
		return account.updateAccount_toDatabase();
	}

	public void revenue_fromBook() {

	}
	public void revenue_fromCategory() {
		
	}
	public void revenue_fromCustomer() {
		
	}
	public void revenue_fromEmployee() {
		
	}

	@Override
	public String toString() {
		String str = "Administrator:" + "\n";
		return str + super.toString();
	}
}
