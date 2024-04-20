package main.frontend.backend.orders;

import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.io.*;

public class ImportSheet {
	private int id;
	private Date ImportTime;
	private Employee employee;
	private float TotalCost;
	private ImportBooks books;

	public ImportSheet() {}
	public ImportSheet(int id, Date ImportTime, Employee employee, float TotalCost, ImportBooks books) {
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
	public ImportBooks getBookList() { return books; }

	public void changeInfo(int id, Date ImportTime, Employee employee, float TotalCost, ImportBooks books) {
		this.id = id;
		this.ImportTime = ImportTime;
		if (employee != null) this.employee = employee;
		this.TotalCost = TotalCost;
		if (books != null) this.books = books;
	}

	public boolean load_fromFile(String FileName) {
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(FileName), "UTF-8"))
		) {
			// Initial info
			id = -1;
			// Get current time
			// Load books from file
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
