package main.frontend.backend.orders;

import main.frontend.backend.objects.Book;
import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

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

	private float calPrice(ArrayList<Float> prices) {
		float price = 0;
		for (float get : prices)
			price += get;
		return price;
	}

	public boolean load_fromFile(String FileName) {
		books = new BookList_price();
		
		TotalCost = 0;
		for (float get : books.getPrices()) TotalCost += get;

		java.util.Date current = new java.util.Date();
		ImportTime = new Date(current.getTime());

		if (! books.load_fromFile(FileName)) return false;

		TotalCost = calPrice(books.getPrices());
		return true;
	}

	public boolean add_toDatabase() {
		if (books == null) return false;

		DBconnect db = new DBconnect();
		String object = "IMPORTS";
		String value = "(DEFAULT, " + toString() + ")";

		try {
			db.turnAutoCommitOff();

			if ((id = db.add_getAuto(object, value)) <= 0) return false;
			
			// Add book if it's not existing
			// Get book's id
			int bID;
			for (Book book : books.getBooks().getBooks()) {
				if ((bID = db.add_getAuto("BOOK", "")) <= 0) {
					if (bID < 0) {
						db.rollback();
						return false;
					}

					ResultSet rs = db.view(null, "BOOK", "isbn = " + book.getIsbn());
					if (! rs.next()) {
						db.rollback();
						return false;
					}
					
					bID = rs.getInt("id");
				}
				book.setId(bID);
			}

			if (! books.add_toDatabase(object, id)) {
				db.rollback();
				return false;
			}

			try { db.commit(); }
			catch (SQLException e) {
				System.err.println("Committing error while adding import sheet: " + e.getMessage());
				db.rollback();
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Connection error while adding import sheet: " + e.getMessage());
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(new Timestamp(ImportTime.getTime())) + ", " + String.valueOf(employee.getId()) + ", " + String.valueOf(TotalCost);
	}
}
