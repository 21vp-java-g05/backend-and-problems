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

	public Account(int id, String username, String password, String mail, String fullname, int role, boolean enabled) {
		this.id = id;
		this.username = username;
		this.password = hashPassword(password);
		this.mail = mail;
		this.fullname = fullname;
		this.enabled = enabled;
		this.role = role;
	}
	public Account(int id, String username, String password, String mail, String fullname, int role) { this(id, username, password, mail, fullname, role, true); }
	public Account(Account other) { this(other.id, other.username, other.password, other.mail, other.fullname, other.role, other.enabled); }

	public int getId() { return id; }
	public String getAccountUsername() { return username; }
	public String getAccountPassword() { return password; }
	public String getFullname() {return fullname;}
	public String getMail() { return mail; }
	public int getRole() {return role;}
	public boolean isEnabled() { return enabled; }

	public void changeInfo(int id, String username, String password, String mail, String fullname, boolean enabled, int role) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.fullname = fullname;
		this.enabled = enabled;
		this.role = role;
	}

	public boolean login(String username, String password) {
		try (DBconnect connection = new DBconnect()) {
			String hashedPassword = hashPassword(password);
			String query = "SELECT * FROM ACCOUNT WHERE username = ? AND password = ?";
			PreparedStatement ps = connection.getConnection().prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, hashedPassword);
			ResultSet rs = ps.executeQuery();

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
        String object = "ACCOUNT(username, password, mail, status, fullname, role)";

        String valuePlaceholder = "(?, ?, ?, ?, ?, ?)";

        try (DBconnect db = new DBconnect()) {
            PreparedStatement ps = db.getConnection().prepareStatement(
                "INSERT INTO " + object + " VALUES " + valuePlaceholder
            );


            ps.setString(1, this.username);
            ps.setString(2, this.password);
            ps.setString(3, this.mail);
            ps.setBoolean(4, this.enabled);
			ps.setString(5, fullname);
			ps.setInt(6, this.role);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Account successfully added to database.");
                return true;
            } else {
                System.out.println("Failed to add account to the database.");
                return false;
            }
        } catch (SQLException e) {
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
