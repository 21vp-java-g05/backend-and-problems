package main.frontend.backend.orders;

import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;

public class ImportSheet {
	private int id;
	private Date ImportTime;
	private Employee employee;
	private float TotalCost;
	private BookList_price books;

	public ImportSheet() {}
	public ImportSheet(int id, Date ImportTime, Employee employee, float TotalCost, BookList_price books) {
		this.id = id;
		this.ImportTime = ImportTime;
		this.employee = employee;
		this.TotalCost = TotalCost;
		this.books = books;
	}
	public ImportSheet(ImportSheet other) { this(other.id, other.ImportTime, other.employee, other.TotalCost, other.books); }

	public int getId() { return id; }
	public Date getImportTime() { return ImportTime; }
	public Employee getEmployee() { return employee; }
	public float getTotalCost() { return TotalCost; }
	public BookList_price getBookList() { return books; }

	public void changeInfo(int id, Date ImportTime, Employee employee, BookList_price books) {
		this.id = id;
		this.ImportTime = ImportTime;
		if (employee != null) this.employee = employee;
		if (books != null) this.books = books;
	}

	public boolean load_fromFile(String FileName) {
		books = new BookList_price();
		
		TotalCost = 0;
		for (float get : books.getPrices()) TotalCost += get;

		java.util.Date current = new java.util.Date();
		ImportTime = new Date(current.getTime());

		return books.load_fromFile(FileName);
	}

	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			// Add to Imports
			// String value = "(DEFAULT, " + (ImportTime == null ? "DEFAULT" : String.valueOf(ImportTime));

			// Add to Imports_Book

			// Add to Book if have new book
			db.commit();
		} catch (SQLException e) {
			System.err.println("Connection error while adding import sheet: " + e.getMessage());
		} finally { db.close(); }
		return true;
	}
}
