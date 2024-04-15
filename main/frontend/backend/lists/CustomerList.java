package main.frontend.backend.lists;

import main.frontend.backend.users.Customer;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class CustomerList {
	private ArrayList<Customer> customers;

	public CustomerList() { customers = new ArrayList<>(); }
	public CustomerList(CustomerList other) { customers = new ArrayList<>(other.customers); }

	public void add(Customer customer) { customers.add(customer); }
	public void clear() { customers.clear(); }
	public Customer getCustomerByID(int id) {
		for (Customer customer : customers)
			if (customer.getId() == id) return customer;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		customers = new ArrayList<>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet rs = db.view(null, "CUSTOMER", condition);) {
			while (rs.next())
				customers.add(new Customer(
					rs.getInt("id"),
					rs.getString("fullname"),
					rs.getString("mail"),
					rs.getBoolean("gender"),
					rs.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("Error in loading customers: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
}
