package main.frontend.backend.orders;

import main.frontend.backend.lists.BookList;
import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.io.*;
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
		if (employee != null) this.employee = employee;
		this.TotalCost = TotalCost;
		if (books != null) this.books = books;
		if (quantity != null) this.quantity = quantity;
		if (ImportPrice != null) this.ImportPrice = ImportPrice;
	}

	public boolean load_fromFile(String FileName) {
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(FileName), "UTF-8"))
		) {

		} catch (FileNotFoundException e) {
			System.err.println("Cannot find file: " + e.getMessage());
			return false;
		} catch (UnsupportedEncodingException e) {
			System.err.println("Encoding error: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.err.println("Error in closing file: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			// Add to Imports


			// Add to Imports_Book
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error while connecting to database in add import sheet: " + e.getMessage());
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		return "";
	}
}
