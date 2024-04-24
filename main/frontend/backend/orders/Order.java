package main.frontend.backend.orders;

import main.frontend.backend.lists.BookList_forSell;
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
	private BookList_forSell books;

	public Order() {}
	public Order(int id, Date OrderTime, Employee employee, Customer customer, float SalesPrice, BookList_forSell books) {
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
	public BookList_forSell getBooks() { return books; }

	public void changeInfo(int id, Date OrderTime, Employee employee, Customer customer, float SalesPrice, BookList_forSell books) {
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
		return String.valueOf(new Timestamp(OrderTime.getTime())) + ", " + String.valueOf(employee.getId()) + ", " + String.valueOf(customer.getId()) + ", " + String.valueOf(SalesPrice);
	}
}
