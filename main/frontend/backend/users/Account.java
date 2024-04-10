package main.frontend.backend.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.lang.AutoCloseable;

import main.frontend.backend.utils.DBconnect;

public class Account {
	private int id, role;
	private String username, password, mail, fullname;
	private boolean enabled;

	public Account(String fullname, String mail, String username, String password, int role, boolean enabled) {
		this.username = username;
		this.password = hashPassword(password);
		this.mail = mail;
		this.fullname = fullname;
		this.enabled = enabled;
		this.role = role;
	}
	public Account(String fullname, String mail, String username, String password, int role) { this(fullname, mail, username, password, role, true); }
	public Account(Account other) { this(other.fullname, other.mail, other.username, other.password, other.role, other.enabled); }

	public int getId() { return id; }
	public String getAccountUsername() { return username; }
	public String getAccountPassword() { return password; }
	public String getFullname() {return fullname;}
	public String getMail() { return mail; }
	public int getRole() {return role;}
	public boolean isEnabled() { return enabled; }

	public void changeInfo(int id, String fullname, String mail, String username, String password, int role, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.fullname = fullname;
		this.enabled = enabled;
		this.role = role;
	}

	public boolean login(String username, String password) {
		try (DBconnect db = new DBconnect()) {
			String hashedPassword = hashPassword(password);
			String condition = "username = '" + username + "' AND password = '" + hashedPassword + "'";
	
			ResultSet rs = db.view("*", "ACCOUNT", condition);
	
			if (rs.next()) {
				return true;
			} else {
				System.out.println("Invalid username or password");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

	public boolean add_toDatabase() {
		String object = "ACCOUNT";
		String values = "(DEFAULT, '"
							+ this.fullname.replace("'", "''") + "', '"
							+ this.mail.replace("'", "''") + "', '"
							+ this.username.replace("'", "''") + "', '"
							+ this.password + "', "
							+ this.role + ", "
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
		String idStr = "\tID: " + id + "\n";
		String nameStr = "\tUser name: " + username + "\n";
		String disStr = "\tPassword: " + password + "\n";
		String mailStr = "\tMail: " + mail + "\n";
		String stsStr = "\tStatus: " + (enabled ? "enable" : "disable") + "\n";
		
		return idStr + nameStr + disStr + mailStr + stsStr;
	}
}
