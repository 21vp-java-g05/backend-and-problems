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
		String value = "(DEFAULT, " + this.toString() + ")";

		// Check author, publisher, category exits
		ResultSet aSet = db.view(null, "AUTHOR", "id = " + String.valueOf(author));
		ResultSet pSet = db.view(null, "PUBLISHER", "id = " + String.valueOf(publisher));
		ResultSet cSet = db.view(null, "CATEGORY", null);

		try {
			if (! aSet.next() || ! pSet.next() || ! cSet.next()) {
				System.err.println("Cannot found reference of book");
				db.close();
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Error at finding reference of book: " + e.getMessage());
			db.close();
			return false;
		}

		// Check author, publisher, category status
		try {
			if (status) {
			}
		} catch (SQLException e) {
			System.err.println("Error at checking status of book: " + e.getMessage());
			db.close();
			return false;
		}
		
		try {
			// Check status of author, publisher and category and change book's status
			if (status) {
				if (
					! db.view(null, "AUTHOR", "id = " + String.valueOf(author)).getBoolean("status") ||
					! db.view(null, "Publisher", "id = " + String.valueOf(publisher)).getBoolean("status")
				) {
					status = false;
					System.out.println("Status is changed into false");
				} else {
					try {
						ResultSet result = db.view("status", "CATEGORY, CATEGORY_BOOK", "id = category_id AND book_id = " + String.valueOf(id));
						while (result.next()) {
							if (! result.getBoolean("status")) {
								status = false;
								System.out.println("Status is changed into false");
								break;
							}
						}
					} catch (SQLException e) {
						System.err.println("Check category status");
						e.printStackTrace();
						return false;
					}
				}
			}

			db.turnAutoCommitOff();
			int rs = db.add("BOOK", value);
			if (rs < 1) {
				if (rs == 0)
					System.err.println("This book is already in the database");
				return false;
			}

			String id_Str = String.valueOf(id);
			value = "";
			for (Category ca : categories.getCategories())
				value += ", (" + String.valueOf(ca.getId()) + ", " + id_Str + ")";
			value = value.substring(2);

			if (db.add("CATEGORY_BOOK", value) < 0) {
				db.rollback();
				return false;
			}
			db.commit();
			return true;
		} catch (SQLException e) {
			System.err.println("Add book");
			e.printStackTrace();
			return false;
		} finally { db.close(); }
	}
	public boolean update_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "title = '" + title + "', isbn = '" + isbn + "', language = '" + language + "', number_of_pages = " + String.valueOf(number_of_pages) + ", publisher = " + String.valueOf(publisher) + ", author = " + String.valueOf(author) + ", status = " + String.valueOf(status);
		String condition = "id = " + String.valueOf(id);

		try {
			db.turnAutoCommitOff();
			// Check status of author, publisher and category
			if (status) {
				if (! db.view("status", "AUTHOR", "id = " + author).getBoolean("status")) return false;
				if (! db.view("status", "PUBLISHER", "id = " + publisher).getBoolean("status")) return false;
				ResultSet rs = db.view("status", "CATEGORY, CATEGORY_BOOK", "id = category_id AND book_id = " + String.valueOf(id));
				while (rs.next())
					if (! rs.getBoolean("status")) return false;
			}
			int rs = db.update("BOOK", value, condition);
			if (rs <= 0) {
				if (rs == 0)
					System.err.println("Book cannot found");
				return false;
			}
			
			db.commit();
			return true;
		} catch (SQLException e) {
			System.err.println("Change status");
			e.printStackTrace();
			return false;
		} finally { db.close(); }
		
	}
	public boolean delete_toDatabase() {
		DBconnect db = new DBconnect();
		String condition = "id = " + String.valueOf(id);
		
		try {
			db.turnAutoCommitOff();
			if (db.delete("CATEGORY_BOOK", "book_" + condition) < 0) return false;
			
			int rs = db.delete("BOOK", condition);
			if (rs <= 0) {
				if (rs == 0)
					System.err.println("Cannot found book");
				db.rollback();
				return false;
			}

			db.commit();
		} catch (SQLException e) {
			System.err.println("Delete table");
			e.printStackTrace();
			return false;
		} finally { db.close(); }

		return true;
	}

	@Override
	public String toString() {
		return "'" + title + "', '" + isbn + "', '" + language + "', " + String.valueOf(number_of_pages) + ", " + String.valueOf(publisher.getId()) + ", " + String.valueOf(author.getId()) + ", " + String.valueOf(status);
	}
}
