package main.frontend.backend.orders;

import main.frontend.backend.lists.Order_BookList;
import main.frontend.backend.users.Customer;

import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;
import main.frontend.backend.utils.Time;

import java.sql.*;

public class Order {
	private int id;
	private Date OrderTime;
	private Employee employee;
	private Customer customer;
	private float SalesPrice;
	private Order_BookList books;

	public Order() {}
	public Order(int id, Date OrderTime, Employee employee, Customer customer, float SalesPrice, Order_BookList books) {
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
	public Order_BookList getBooks() { return books; }

	public void changeInfo(int id, Date OrderTime, Employee employee, Customer customer, float SalesPrice, Order_BookList books) {
		this.id = id;
		this.OrderTime = OrderTime;
		this.employee = employee;
		this.customer = customer;
		this.SalesPrice = SalesPrice;
		this.books = books;
	}

	public boolean add_toDatabase() {
		if (books == null) return false;

		DBconnect db = new DBconnect();
		String value = "(DEFAULT, " + toString() + ")";
		
		try {
			if (! db.setAutoCommit(false)) return false;
			if ((id = db.add_getAuto("ORDERS", value)) <= 0) return false;
			if (! books.addOrders_toDatabase(id)) {
				db.rollback();
				return false;
			}

			if (! db.commit()) {
				db.rollback();
				return false;
			}
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		return new Time(OrderTime) + ", " + String.valueOf(employee.getId()) + ", " + String.valueOf(customer.getId()) + ", " + String.valueOf(SalesPrice);
	}
}
