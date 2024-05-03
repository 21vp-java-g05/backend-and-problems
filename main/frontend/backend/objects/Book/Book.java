package main.frontend.backend.objects.Book;

import java.sql.*;

import main.frontend.backend.lists.CategoryList;
import main.frontend.backend.objects.Author;
import main.frontend.backend.objects.Category;
import main.frontend.backend.objects.Publisher;
import main.frontend.backend.utils.DBconnect;

public class Book {
	private int id, number_of_pages;
	private String isbn, title, language;
	private Publisher publisher;
	private Author author;
	private CategoryList categories;
	private boolean status;
	
	public Book() {}
	public Book(int id, String title, String isbn, String language, int number_of_pages, Publisher publisher, Author author, CategoryList categories, boolean status) {
		this.id = id;
		this.title = title;
		this.isbn = isbn;
		this.language = language;
		this.number_of_pages = number_of_pages;
		this.publisher = publisher;
		this.author = author;
		this.categories = categories;
		this.status = status;
	}
	public Book(int id, String title, String isbn, String language, int number_of_pages, Publisher publisher, Author author, CategoryList categories) {
		this(id, title, isbn, language, number_of_pages, publisher, author, categories, true);
	}
	public Book(Book other) {
		this(other.id, other.isbn, other.title, other.language, other.number_of_pages, other.publisher, other.author, other.categories, other.status);
	}

	public int getId() { return id; }
	public String getTitle() { return title; }
	public String getIsbn() { return isbn; }
	public String getLanguage() { return language; }
	public int getNumber_of_pages() { return number_of_pages; }
	public Publisher getPublisher() { return publisher; }
	public Author getAuthor() { return author; }
	public CategoryList getCategories() { return categories; }
	public boolean getStatus() { return status; }

	public void setId(int id) { this.id = id; }
	public void setTitle(String title) { this.title = title; }
	public void setIsbn(String isbn) { this.isbn = isbn; }
	public void setLanguage(String language) { this.language = language; }
	public void setNumber_of_pages(int number_of_pages) { this.number_of_pages = number_of_pages; }
	public void setPublisher(Publisher publisher) { this.publisher = publisher; }
	public void setAuthor(Author author) { this.author = author; }
	public void setCategories(CategoryList categories) { this.categories = categories; }
	public void setStatus(boolean status) { this.status = status; }
	
	public void changeInfo(int id, String title, String isbn, String language, int number_of_pages, Publisher publisher, Author author, CategoryList categories, boolean status) {
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.language = language;
		this.number_of_pages = number_of_pages;
		this.publisher = publisher;
		this.author = author;
		this.categories = categories;
		this.status = status;
	}

	private boolean checkStatus() throws SQLException {
		DBconnect db = new DBconnect();
		ResultSet aSet = db.view("status", "AUTHOR", "id = " + String.valueOf(author.getId()));
		ResultSet pSet = db.view("status", "PUBLISHER", "id = " + String.valueOf(publisher.getId()));

		try {
			if (
				(aSet.next() && !aSet.getBoolean("status")) ||
				(pSet.next() && !pSet.getBoolean("status"))
			) return false;
			
			if (categories != null) {
				CategoryList cList = new CategoryList();
				cList.load_fromDatabase(null);

				for (Category c : categories.getCategories())
					if (! cList.getCategory_byID(c.getId()).getStatus()) return false;
			}
		} finally { db.close(); }
		return true;
	}
	private int addCategory_toDatabase() {
		if (categories == null) return -1;
		DBconnect db = new DBconnect();
		
		String id_Str = String.valueOf(id);
		String value = "(";
		
		for (Category ca : categories.getCategories())
			value += String.valueOf(ca.getId()) + ", " + id_Str + "), (";
		value = value.substring(0, value.length() - 3);

		try { return db.add("CATEGORY_BOOK", value); }
		finally { db.close(); }
	}

	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			if (status)
				try { status = checkStatus(); }
				catch (SQLException e) {
					System.err.println("next() error while checking status");
					return false;
				}
			
			if (! db.setAutoCommit(false)) return false;
			
			// Add book
			String value = "(DEFAULT, " + toString() + ")";
			if ((id = db.add_getAuto("BOOK", value)) <= 0) return false;

			// Add category_book
			if (addCategory_toDatabase() == 0) {
				db.rollback();
				return false;
			}

			if (! db.commit()) {
				db.rollback();
				return false;
			};
		} finally { db.close(); }
		return true;
	}
	public boolean update_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "title = '" + title + "', isbn = '" + isbn + "', language = '" + language + "', number_of_pages = " + String.valueOf(number_of_pages) + ", publisher = " + String.valueOf(publisher.getId()) + ", author = " + String.valueOf(author.getId());
		String condition = "id = " + String.valueOf(id);
		
		try { return db.update("BOOK", value, condition) > 0; }
		finally { db.close(); }
	}
	public boolean updateCategory_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			// Delete category book
			if (db.delete("CATEGORY_BOOK", "book_id = " + String.valueOf(id)) < 0) return false;
			
			// Add category book
			return addCategory_toDatabase() > 0;
		}
		finally { db.close(); }
	}
	public boolean updateStatus_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			if (status) status = checkStatus();
			
			return db.changeStatus("BOOK", "id = " + String.valueOf(id), status) > 0;
		} catch (SQLException e) {
			System.err.println("Connection error while checking book's status: " + e.getMessage());
			return false;
		} finally { db.close(); }
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			if (! db.setAutoCommit(false)) return false;
			
			String condition = "id = " + String.valueOf(id);
			if (db.delete("CATEGORY_BOOK", "book_" + condition) < 0) return false;
			
			if (db.delete("BOOK", condition) <= 0) {
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
		return "'" + title + "', '" + isbn + "', '" + language + "', " + String.valueOf(number_of_pages) + ", " + String.valueOf(publisher.getId()) + ", " + String.valueOf(author.getId()) + ", " + String.valueOf(status);
	}
}
