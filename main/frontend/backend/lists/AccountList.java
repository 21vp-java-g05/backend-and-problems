package main.frontend.backend.lists;

import main.frontend.backend.users.Account;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class AccountList {
	private ArrayList<Account> accounts;
	
	public AccountList() { accounts = new ArrayList<>(); }
	public AccountList(ArrayList<Account> accounts) { accounts = new ArrayList<>(accounts); }
	public AccountList(AccountList other) { accounts = new ArrayList<>(other.accounts); }
	
	public void add(Account account) { accounts.add(account); }
	public void clear() { accounts.clear(); }
	public int size() { return accounts.size(); }
	
	public Account getAccount_byID(int id) {
		for (Account account : accounts)
			if (account.getId() == id) return account;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		accounts = new ArrayList<Account>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet aSet = db.view(null, "ACCOUNT", condition);) {
			while (aSet.next())
				accounts.add(new Account(
					aSet.getInt("id"),
					aSet.getString("fullname"),
					aSet.getString("mail"),
					aSet.getString("username"),
					aSet.getString("password"),
					aSet.getInt("role"),
					aSet.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("next() error while loading accounts: " + e.getMessage());
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
