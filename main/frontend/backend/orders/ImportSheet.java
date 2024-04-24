package main.frontend.backend.orders;

import main.frontend.backend.lists.BookList_forSell;
import main.frontend.backend.objects.Book;
import main.frontend.backend.objects.Book_forSell;
import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;
import main.frontend.backend.utils.Time;

import java.sql.*;

public class ImportSheet {
	private int id;
	private Date ImportTime;
	private Employee employee;
	private float TotalCost;
	private BookList_forSell books;

	public ImportSheet() {}
	public ImportSheet(int id, Date ImportTime, Employee employee, float TotalCost, BookList_forSell books) {
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
	public BookList_forSell getBookList() { return books; }

	public void changeInfo(int id, Date ImportTime, Employee employee, BookList_forSell books) {
		this.id = id;
		this.ImportTime = ImportTime;
		this.employee = employee;
		this.books = books;
	}

	private float calPrice() {
		float price = 0;

		for (Book_forSell book : books.getBooks())
			price += book.getQuantity()*book.getPrice();
		
		return price;
	}

	public boolean load_fromFile(String FileName) {
		ImportTime = new Time().currentTime();
		
		books = new BookList_forSell();
		if (! books.load_fromFile(FileName)) return false;
		
		TotalCost = calPrice();
		
		return true;
	}

	public boolean add_toDatabase() {
		if (books == null) return false;

		DBconnect db = new DBconnect();
		String value = "(DEFAULT, " + toString() + ")";

		try {
			if (! db.setAutoCommit(false)) return false;

			if ((id = db.add_getAuto("IMPORTS", value)) <= 0) return false;
			
			// Add book if it's not existing
			// Get book's id
			int bID;
			for (Book_forSell book_forSell : books.getBooks()) {
				Book book = book_forSell.getBook();
				if ((bID = db.add_getAuto("BOOK", value)) <= 0) {
					if (bID < 0) {
						db.rollback();
						return false;
					}

					try {
						ResultSet rs = db.view(null, "BOOK", "isbn = " + book.getIsbn());
						if (! rs.next()) {
							db.rollback();
							return false;
						}
						
						bID = rs.getInt("id");
					} catch (SQLException e) {
						System.err.println("next() and getInt() error while loading accounts");
						return false;
					}
				}
				book.setId(bID);
				book_forSell.setSellID(id);
			}

			if (! books.addImports_toDatabase()) {
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
		return String.valueOf(new Timestamp(ImportTime.getTime())) + ", " + String.valueOf(employee.getId()) + ", " + String.valueOf(TotalCost);
	}
}
