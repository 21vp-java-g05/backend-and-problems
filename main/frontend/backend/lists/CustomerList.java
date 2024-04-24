package main.frontend.backend.lists;

import main.frontend.backend.users.Customer;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class CustomerList {
	private ArrayList<Customer> customers;

	public CustomerList() { customers = new ArrayList<>(); }
	public CustomerList(ArrayList<Customer> customers) { customers = new ArrayList<>(customers); }
	public CustomerList(CustomerList other) { customers = new ArrayList<>(other.customers); }

	public void add(Customer customer) { customers.add(customer); }
	public void clear() { customers.clear(); }
	public int size() { return customers.size(); }
	
	public Customer getCustomer_byID(int id) {
		for (Customer customer : customers)
			if (customer.getId() == id) return customer;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		customers = new ArrayList<>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet cSet = db.view(null, "CUSTOMER", condition);) {
			while (cSet.next())
				customers.add(new Customer(
					cSet.getInt("id"),
					cSet.getString("fullname"),
					cSet.getString("mail"),
					cSet.getBoolean("gender"),
					cSet.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("next() error while loading customers: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
}
