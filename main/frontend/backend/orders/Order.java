package main.frontend.backend.orders;

import main.frontend.backend.users.Customer;

import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;

public class Order {
	private int id;
	private Date OrderTime;
	private Employee employee;
	private float SalesPrice;
	private ImportBooks books;
	private Customer customer;

	public Order() {}
	public Order(int id, Date OrderTime, Employee employee, float SalesPrice, ImportBooks books, Customer customer) {
		this.id = id;
		this.OrderTime = OrderTime;
		this.employee = employee;
		this.SalesPrice = SalesPrice;
		this.books = books;
		this.customer = customer;
	}
	public Order(Order other) { this(other.id, other.OrderTime, other.employee, other.SalesPrice, other.books, other.customer); }

	public int getId() { return id; }
	public Date getOrderTime() { return OrderTime; }
	public Employee getEmployee() { return employee; }
	public float getSalesPrice() { return SalesPrice; }
	public ImportBooks getBooks() { return books; }
	public Customer getCustomer() { return customer; }

	public void changeInfo(int id, Date OrderTime, Employee employee, float SalesPrice, ImportBooks books, Customer customer) {
		this.id = id;
		this.OrderTime = OrderTime;
		if (employee != null) this.employee = employee;
		this.SalesPrice = SalesPrice;
		if (books != null) this.books = books;
		if (employee != null) this.customer = customer;
	}

	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			// Add to Orders
			

			// Add to Orders_Book
			db.commit();
		} catch (SQLException e) {
			System.err.println("Connection error while add order: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
}
