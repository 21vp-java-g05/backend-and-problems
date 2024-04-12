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
	public Book getBookById(int id) {
		for (Book book : books)
			if (book.getId() == id) return book;
		return null;
	}

	public boolean loadBooks_fromDatabase(String name, PublisherList publishers, AuthorList authors, CategoryList categories) {
		publishers.load_fromDatabase(null);
		authors.load_fromDatabase(null);
		categories.load_fromDatabase(null);
		
		books = new ArrayList<Book>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("title LIKE '%" + name + "%'");
		
		try (ResultSet rs = db.view(null, "BOOK", condition);) {
			while (rs.next()) {
				int id = rs.getInt("id");
				CategoryList CategoriesEachBook = new CategoryList();
				String condition1 = "book_id = " + String.valueOf(id);
				
				try (ResultSet resultSet = db.view("category_id", "CATEGORY_BOOK", condition1);) {
					while (resultSet.next())
						CategoriesEachBook.add(categories.getCategoryByID(resultSet.getInt("category_id")));
				} catch (SQLException e) {
					System.err.println("Error loading categories for each book");
					return false;
				}

				Book book = new Book(
					id, rs.getString("title"),
					rs.getString("isbn"),
					rs.getString("language"),
					rs.getInt("number_of_pages"),
					publishers.getPublisherByID(rs.getInt("publisher")),
					authors.getAuthorByID(rs.getInt("author")),
					CategoriesEachBook, rs.getBoolean("status")
				);
				books.add(book);
			}
		} catch (SQLException e) {
			System.err.println("Error loading books");
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
