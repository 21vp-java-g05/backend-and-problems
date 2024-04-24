package main.frontend.backend.lists;

import java.util.ArrayList;

import main.frontend.backend.objects.Book;
import main.frontend.backend.objects.Book_forSell;
import main.frontend.backend.utils.DBconnect;

import java.io.*;
import java.sql.*;

public class BookList_forSell {
	private ArrayList<Book_forSell> books;

	public BookList_forSell() {}
	public BookList_forSell(ArrayList<Book_forSell> books) { books = new ArrayList<>(books); }
	public BookList_forSell(BookList_forSell other) { books = new ArrayList<>(other.books); }
	
	public void add(Book_forSell book) { books.add(book); }
	public void clear() { books.clear(); }
	public int size() { return books.size(); }

	public Book_forSell get(int i) {
		return books.get(i);
	}
	public ArrayList<Book_forSell> getBooks() { return books; }

	public boolean load_fromFile(String FileName) {
		books = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(FileName), "UTF-8"))
		) {
			PublisherList publishers = new PublisherList();
			AuthorList authors = new AuthorList();
			CategoryList categories = new CategoryList();

			publishers.load_fromDatabase(null);
			authors.load_fromDatabase(null);
			categories.load_fromDatabase(null);
			
			String str;
			while ((str = reader.readLine()) != null) {
				String[] parts = str.split(", ");
				
				CategoryList cList = new CategoryList();
				for (int i = 8; i < parts.length; i++)
					cList.add(categories.getCategory_byName(parts[i]));

				int quantity = Integer.parseInt(parts[6]);
				
				books.add(new Book_forSell(
					-1, new Book (
						-1, parts[0], parts[1], parts[2],
						Integer.parseInt(parts[3]),
						publishers.getPublisher_byName(parts[4]),
						authors.getAuthor_byName(parts[5]),
						cList
					),
					quantity,
					Float.parseFloat(parts[7]),
					quantity
				));
			}
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
	
	public boolean loadOrders_fromDatabase(int id) {
		books = new ArrayList<>();
		DBconnect db = new DBconnect();
		String object = "ORDERS";
		String condition = object + "_id = " + String.valueOf(id);
		
		try (ResultSet bSet = db.view(null, object + "_BOOK", condition);) {
			BookList bList = new BookList();
			if (! bList.loadBooks_fromDatabase(null)) return false;

			while (bSet.next())
				books.add(new Book_forSell(
					bSet.getInt("orders_id"),
					bList.getBook_byId(bSet.getInt("book_id")),
					bSet.getInt("quantity"),
					bSet.getFloat("price"),
					-1
				));
		} catch (SQLException e) {
			System.err.println("next() error while loading books: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	public boolean loadImports_fromDatabase(int id) {
		books = new ArrayList<>();
		DBconnect db = new DBconnect();
		String object = "IMPORTS";
		String condition = object + "_id = " + String.valueOf(id);
		
		try (ResultSet bSet = db.view(null, object + "_BOOK", condition);) {
			BookList bList = new BookList();
			if (! bList.loadBooks_fromDatabase(null)) return false;

			while (bSet.next())
				books.add(new Book_forSell(
					bSet.getInt("orders_id"),
					bList.getBook_byId(bSet.getInt("book_id")),
					bSet.getInt("quantity"),
					bSet.getFloat("price"),
					bSet.getInt("remaining")
				));
		} catch (SQLException e) {
			System.err.println("next() error while loading books: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	
	public boolean addOrders_toDatabase(int id) {
		DBconnect db = new DBconnect();
		String value = "";

		for (Book_forSell book : books)
			value += "(" + String.valueOf(id) + ", " + book.OrdStr() + "), ";

		try { return db.add("ORDERS" + "_BOOK", value.substring(0, value.length() - 2)) > 0; }
		finally { db.close(); }
	}
	public boolean addImports_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "";

		for (Book_forSell book : books)
			value += "(" + book.ImpStr() + "), ";

		try { return db.add("IMPORTS" + "_BOOK", value.substring(0, value.length() - 2)) > 0; }
		finally { db.close(); }
	}
}
