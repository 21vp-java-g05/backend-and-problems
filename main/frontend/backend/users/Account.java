package main.frontend.backend.users;

import java.security.*;
import java.sql.*;

import main.frontend.backend.utils.DBconnect;

public class Account {
	private int id, role;
	private String username, password, mail, fullname;
	private boolean status;

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

	@Override
	public String toString() {		
		return "'" + fullname + "', '" + mail + "', '" + username + "', '" + password + "', " + role + ", "  + status;
	}
}	