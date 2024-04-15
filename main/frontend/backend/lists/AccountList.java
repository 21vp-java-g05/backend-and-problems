package main.frontend.backend.lists;

import main.frontend.backend.users.Account;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class AccountList {
	private ArrayList<Account> accounts;
	
	public AccountList() { accounts = new ArrayList<>(); }
	public AccountList(AccountList other) { accounts = new ArrayList<>(other.accounts); }
	
	public void add(Account account) { accounts.add(account); }
	public void clear() { accounts.clear(); }
	public Account getAccountByID(int id) {
		for (Account account : accounts)
			if (account.getId() == id) return account;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		accounts = new ArrayList<Account>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet rs = db.view(null, "AUTHOR", condition);) {
			while (rs.next())
				accounts.add(new Account(
					rs.getInt("id"),
					rs.getString("fullname"),
					rs.getString("mail"),
					rs.getString("username"),
					rs.getString("password"),
					rs.getInt("role"),
					rs.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("Error in loading accounts: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	
	@Override
	public String toString() {
		String str = "There are " + accounts.size() + " accounts in the list.\n\n";
		for (Account account : accounts) str += account.toString() + "\n";
		return str;
	}
}
