package main.frontend.backend.orders;

import main.frontend.backend.users.Customer;

import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;

public class Order {
	private int id;
	private Date OrderTime;
	private Employee employee;
	private Customer customer;
	private float SalesPrice;
	private BookList_price books;

	public Order() {}
	public Order(int id, Date OrderTime, Employee employee, Customer customer, float SalesPrice, BookList_price books) {
		this.id = id;
		this.OrderTime = OrderTime;
		this.employee = employee;
		this.customer = customer;
		this.SalesPrice = SalesPrice;
		this.books = books;
	}
	public Order(Order other) { this(other.id, other.OrderTime, other.employee, other.customer, other.SalesPrice, other.books); }

	public int getId() { return id; }
	public Date getOrderTime() { return OrderTime; }
	public Employee getEmployee() { return employee; }
	public Customer getCustomer() { return customer; }
	public float getSalesPrice() { return SalesPrice; }
	public BookList_price getBooks() { return books; }

	public void changeInfo(int id, Date OrderTime, Employee employee, Customer customer, float SalesPrice, BookList_price books) {
		this.id = id;
		this.OrderTime = OrderTime;
		if (employee != null) this.employee = employee;
		if (employee != null) this.customer = customer;
		this.SalesPrice = SalesPrice;
		if (books != null) this.books = books;
	}

	public boolean add_toDatabase() {
		if (books == null) return false;

		DBconnect db = new DBconnect();
		String object = "ORDERS";
		String value = "(DEFAULT, " + toString() + ")";
		
		try {
			db.turnAutoCommitOff();

			if ((id = db.add_getAuto(object, value)) <= 0) return false;
			
			if (! books.add_toDatabase(object, id)) {
				db.rollback();
				return false;
			}

			try { db.commit(); }
			catch (SQLException e) {
				System.err.println("Committing error while adding order: " + e.getMessage());
				db.rollback();
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Connection error while adding order: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(new Timestamp(OrderTime.getTime())) + ", " + String.valueOf(employee.getId()) + ", " + String.valueOf(customer.getId()) + ", " + String.valueOf(SalesPrice);
	}
}
