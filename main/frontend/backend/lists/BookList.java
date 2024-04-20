package main.frontend.backend.lists;

import main.frontend.backend.objects.Book;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class BookList {
	private ArrayList<Book> books;
	
	public BookList() { books = new ArrayList<>(); }
	public BookList(BookList other) { books = new ArrayList<>(other.books); }

	public void add(Book book) { books.add(book); }
	public void clear() { books.clear(); }
	public int size() { return books.size(); }
	public Book getBookById(int id) {
		for (Book book : books)
			if (book.getId() == id) return book;
		return null;
	}

	public ArrayList<Book> getBooks() { return books; }

	private CategoryList getCategories_forBook(CategoryList categories, String id) {
		DBconnect db = new DBconnect();
		CategoryList cEachBook = new CategoryList();
		String condition = "book_id = " + id;
		
		try (ResultSet cSet = db.view("category_id", "CATEGORY_BOOK", condition);) {
			while (cSet.next())
				cEachBook.add(categories.getCategoryByID(cSet.getInt("category_id")));
		} catch (SQLException e) {
			System.err.println("Connection error while loading categories for each book");
			return null;
		} finally { db.close(); }
		return cEachBook;
	}

	public boolean loadBooks_fromDatabase(String name) {
		PublisherList publishers = new PublisherList();
		AuthorList authors = new AuthorList();
		CategoryList categories = new CategoryList();
		
		publishers.load_fromDatabase(null);
		authors.load_fromDatabase(null);
		categories.load_fromDatabase(null);
		
		books = new ArrayList<Book>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("title LIKE '%" + name + "%'");
		
		try (ResultSet bSet = db.view(null, "BOOK", condition);) {
			while (bSet.next()) {
				int id = bSet.getInt("id");

				Book book = new Book(
					id, bSet.getString("title"),
					bSet.getString("isbn"),
					bSet.getString("language"),
					bSet.getInt("number_of_pages"),
					publishers.getPublisherByID(bSet.getInt("publisher")),
					authors.getAuthorByID(bSet.getInt("author")),
					getCategories_forBook(categories, String.valueOf(id)),
					bSet.getBoolean("status")
				);
				books.add(book);
			}
		} catch (SQLException e) {
			System.err.println("Connection error while loading books: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		String str = "There are " + books.size() + " books in the list.\n\n";
		for (Book book : books) str += book.toString() + "\n";
		return str;
	}
	
}
