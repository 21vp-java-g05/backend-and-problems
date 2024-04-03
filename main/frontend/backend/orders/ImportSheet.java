package main.frontend.backend.orders;

import main.frontend.backend.lists.BookList;
import main.frontend.backend.users.Employee;

import java.sql.*;
import java.util.ArrayList;

public class ImportSheet {
	private int id;
	private Date ImportTime;
	private Employee employee;
	private float TotalCost;
	private BookList books;
	private ArrayList<Integer> quantity;
	private ArrayList<Float> ImportPrice;

	public ImportSheet() {}
	public ImportSheet(int id, Date ImportTime, Employee employee, float TotalCost, BookList books, ArrayList<Integer> quantity, ArrayList<Float> ImportPrice) {
		this.id = id;
		this.ImportTime = ImportTime;
		this.employee = employee;
		this.TotalCost = TotalCost;
		this.books = books;
		this.quantity = quantity;
		this.ImportPrice = ImportPrice;
	}
	public ImportSheet(ImportSheet other) {
		this(other.id, other.ImportTime, other.employee, other.TotalCost, other.books, other.quantity, other.ImportPrice);
	}

	public int getId() { return id; }
	public Date getImportTime() { return ImportTime; }
	public Employee getEmployee() { return employee; }
	public float getTotalCost() { return TotalCost; }
	public BookList getBookList() { return books; }
	public ArrayList<Integer> getQuantity() { return quantity; }
	public ArrayList<Float> getImportPrice() { return ImportPrice; }

	public void changeInfo(int id, Date ImportTime, Employee employee, float TotalCost, BookList books, ArrayList<Integer> quantity, ArrayList<Float> ImportPrice) {
		this.id = id;
		this.ImportTime = ImportTime;
		this.employee = employee;
		this.TotalCost = TotalCost;
		this.books = books;
		this.quantity = quantity;
		this.ImportPrice = ImportPrice;
	}
}
