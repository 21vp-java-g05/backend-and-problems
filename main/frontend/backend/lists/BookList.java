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
		String condition = name == null || name.isEmpty() ? null : ("title LIKE '%" + name + "%'");
		books = new ArrayList<Book>();
		
		publishers.load_fromDatabase(null);
		authors.load_fromDatabase(null);
		categories.load_fromDatabase(null);
		
		DBconnect db = new DBconnect();
		try (ResultSet rs = db.view(null, "BOOK", condition);) {
			while (rs.next()) {
				// Get book info
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String isbn = rs.getString("isbn");
				String language = rs.getString("language");
				int numberOfPages = rs.getInt("number_of_pages");
				int publisherID = rs.getInt("publisher");
				int authorID = rs.getInt("author");
				Boolean status = rs.getBoolean("status");

				// Get categories for each book
				CategoryList CategoriesEachBook = new CategoryList();
				String condition1 = "book_id = " + String.valueOf(id);
				try (ResultSet resultSet = db.view(null, "CATEGORY_BOOK", condition1);) {
					while (resultSet.next())
						CategoriesEachBook.add(categories.getCategoryByID(resultSet.getInt("category_id")));
				} catch (SQLException e) {
					System.err.println("Error loading categories for each book");
					return false;
				}

				Book book = new Book(id, title, isbn, language, numberOfPages, publishers.getPublisherByID(publisherID), authors.getAuthorByID(authorID), CategoriesEachBook, status);
				books.add(book);
			}
		} catch (SQLException e) {
			System.err.println("Error loading books");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String str = "There are " + books.size() + " books in the list.\n\n";

		for (Book book : books)
			str += book.toString() + "\n";
		
		return str;
	}
	
}
