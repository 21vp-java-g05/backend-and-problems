package main.frontend.backend.objects;

import java.sql.*;

import main.frontend.backend.lists.CategoryList;
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

	public boolean add_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			
			// Add book
			String value = "(DEFAULT, " + toString() + ")";
			id = db.add_getAuto("BOOK", value);
			if (id <= 0) return false;

			// Add category_book
			String id_Str = String.valueOf(id);
			value = "(";
			
			// If categories are not null, then add them to database
			if (categories != null) {
				for (Category ca : categories.getCategories())
					value += String.valueOf(ca.getId()) + ", " + id_Str + "), (";
				value = value.substring(0, value.length() - 3);
				if (db.add("CATEGORY_BOOK", value) <= 0) {
					db.rollback();
					return false;
				}
			}

			// Check if author, publisher, category enable when book is enabled
			if (status) {
				ResultSet aSet = db.view("status", "AUTHOR", "id = " + String.valueOf(author.getId()));
				ResultSet pSet = db.view("status", "PUBLISHER", "id = " + String.valueOf(publisher.getId()));

				try {
					if (
						(aSet.next() && !aSet.getBoolean("status")) ||
						(pSet.next() && !pSet.getBoolean("status"))
					) {
						status = false;
						System.out.println("Status is changed into false");
					} else {
						ResultSet cSet = db.view("status", "CATEGORY, CATEGORY_BOOK", "id = category_id AND book_id = " + String.valueOf(id));
						while (cSet.next())
							if (cSet.getBoolean("status")) {
								status = false;
								System.out.println("Status is changed into false");
							}
					}
					// Change status of Book
					if (db.changeStatus("BOOK", "id = " + String.valueOf(id), status) <= 0) {
						db.rollback();
						return false;
					}
				} catch (SQLException e) {
					System.err.println("Error at checking status of book: " + e.getMessage());
					db.rollback();
					return false;
				}
			}
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error while connecting to database in add book: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	public boolean update_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			// Check status of author, publisher and category
			if (status) {
				ResultSet aSet = db.view("status", "AUTHOR", "id = " + author);
				ResultSet pSet = db.view("status", "PUBLISHER", "id = " + publisher);

				if (
					(aSet.next() && !aSet.getBoolean("status")) ||
					(pSet.next() && !pSet.getBoolean("status"))
				) {
					status = false;
					System.out.println("Status is changed into false");
				} else {
					ResultSet cSet = db.view("status", "CATEGORY, CATEGORY_BOOK", "id = category_id AND book_id = " + String.valueOf(id));
					while (cSet.next())
						if (! cSet.getBoolean("status")) {
							status = false;
							System.out.println("Status is changed into false");
						}
				}
			}
			// Update book
			String value = "title = '" + title + "', isbn = '" + isbn + "', language = '" + language + "', number_of_pages = " + String.valueOf(number_of_pages) + ", publisher = " + String.valueOf(publisher) + ", author = " + String.valueOf(author) + ", status = " + String.valueOf(status);
			String condition = "id = " + String.valueOf(id);
			
			if (db.update("BOOK", value, condition) <= 0) return false;
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error while connecting to database in updating book: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		try {
			db.turnAutoCommitOff();
			String condition = "id = " + String.valueOf(id);
			if (db.delete("CATEGORY_BOOK", "book_" + condition) < 0) return false;
			
			if (db.delete("BOOK", condition) <= 0) {
				db.rollback();
				return false;
			}
			db.commit();
		} catch (SQLException e) {
			System.err.println("Error while connecting to database in deleting book: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		return "'" + title + "', '" + isbn + "', '" + language + "', " + String.valueOf(number_of_pages) + ", " + String.valueOf(publisher.getId()) + ", " + String.valueOf(author.getId()) + ", " + String.valueOf(status);
	}
}
