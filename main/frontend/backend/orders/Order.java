package main.frontend.backend.orders;

import main.frontend.backend.lists.BookList;
import main.frontend.backend.users.Employee;

import java.sql.*;
import java.util.ArrayList;

public class Order {
	private int id;
	private Date OrderTime;
	private Employee employee;
	private float SalesPrice;
	private BookList books;
	private ArrayList<Integer> quantity;
	private ArrayList<Float> ImportPrice;
	private Customer customer;

	public Order() {}
	public Order(int id, Date OrderTime, Employee employee, float SalesPrice, BookList books, ArrayList<Integer> quantity, ArrayList<Float> ImportPrice, Customer customer) {
		this.id = id;
		this.OrderTime = OrderTime;
		this.employee = employee;
		this.SalesPrice = SalesPrice;
		this.books = books;
		this.quantity = quantity;
		this.ImportPrice = ImportPrice;
		this.customer = customer;
	}
	public Order(Order other) {
		this(other.id, other.OrderTime, other.employee, other.SalesPrice, other.books, other.quantity, other.ImportPrice, other.customer);
	}

	public int getId() { return id; }
	public Date getOrderTime() { return OrderTime; }
	public Employee getEmployee() { return employee; }
	public float getSalesPrice() { return SalesPrice; }
	public BookList getBooks() { return books; }
	public ArrayList<Integer> getQuantity() { return quantity; }
	public ArrayList<Float> getImportPrice() { return ImportPrice; }
	public Customer getCustomer() { return customer; }

	public void changeInfo(int id, Date OrderTime, Employee employee, float SalesPrice, BookList books, ArrayList<Integer> quantity, ArrayList<Float> ImportPrice, Customer customer) {
		this.id = id;
		this.OrderTime = OrderTime;
		this.employee = employee;
		this.SalesPrice = SalesPrice;
		this.books = books;
		this.quantity = quantity;
		this.ImportPrice = ImportPrice;
		this.customer = customer;
	}
}
