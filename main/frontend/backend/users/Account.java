package main.frontend.backend.users;

import java.security.*;
import java.sql.*;

import main.frontend.backend.lists.CustomerList;
import main.frontend.backend.lists.ImportSheetList;
import main.frontend.backend.lists.OrderList;
import main.frontend.backend.utils.DBconnect;

public class Account {
	protected int id, role;
	protected String username, password, mail, fullname;
	protected boolean status;

	public Account(int id, String fullname, String mail, String username, String password, int role, boolean status) {
		this.id = id;
		this.username = username;
		this.password = hashPassword(password);
		this.mail = mail;
		this.fullname = fullname;
		this.status = status;
		this.role = role;
	}
	public Account(int id, String fullname, String mail, String username, String password, int role) { this(id, fullname, mail, username, password, role, true); }
	public Account(Account other) { this(other.id, other.fullname, other.mail, other.username, other.password, other.role, other.status); }

	public int getId() { return id; }
	public String getAccountUsername() { return username; }
	public String getAccountPassword() { return password; }
	public String getFullname() {return fullname;}
	public String getMail() { return mail; }
	public int getRole() {return role;}
	public boolean getStatus() { return status; }

	public void changeInfo(int id, String fullname, String mail, String username, String password, int role, boolean status) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.fullname = fullname;
		this.status = status;
		this.role = role;
	}

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            String sb = "";
            for (byte b : hashedBytes)
				sb += String.format("%02x", b);
            return sb;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error in hash: " + e.getMessage());
            return null;
        }
    }

	public Account login(String username, String password) {
		DBconnect db = new DBconnect();
		try {
			String hashedPassword = hashPassword(password);
			String condition = "username = '" + username + "' AND password = '" + hashedPassword + "'";
			ResultSet rs = db.view(null, "ACCOUNT", condition);
	
			if (rs.next()) {
				int role = rs.getInt("role");
            	System.out.println("Login successful. Role: " + role);
				return role == 0 ? new Administrator(this) : new Employee(this);
			}
			System.err.println("Invalid username or password");
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally { db.close(); }
	}
	public boolean logout() {
		return true;
	}

	public CustomerList loadCustomers_fromDatabase() {
		CustomerList customers = new CustomerList();
		return customers.load_fromDatabase(null) ? customers : null;
	}
	public boolean updateAccount_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "";
		String condition = "id = " + String.valueOf(id);
		try { return db.update("ACCOUNT", value, condition) > 0; }
		finally { db.close(); }
	}

	public ImportSheetList loadImportSheets_fromDatabase() {
		ImportSheetList importSheets = new ImportSheetList();
		return importSheets.load_fromDatabase(null) ? importSheets : null;
	}
	public OrderList loadOrders_fromDatabase() {
		OrderList orders = new OrderList();
		return orders.load_fromDatabase(null) ? orders : null;
	}

	@Override
	public String toString() {		
		return "'" + fullname + "', '" + mail + "', '" + username + "', '" + password + "', " + role + ", "  + status;
	}
}	