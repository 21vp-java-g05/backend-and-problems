package main.frontend.backend.lists;

import java.util.ArrayList;

import main.frontend.backend.objects.Order_Book;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;

public class Order_BookList {
	private ArrayList<Order_Book> books;

	public Order_BookList() {}
	public Order_BookList(ArrayList<Order_Book> books) { books = new ArrayList<>(books); }
	public Order_BookList(Order_BookList other) { books = new ArrayList<>(other.books); }
	public Order_BookList(Import_BookList other) { books = new ArrayList<>(other.getBooks()); }
	
	public void add(Order_Book book) { books.add(book); }
	public int size() { return books.size(); }

	public Order_Book get(int i) {
		return books.get(i);
	}
	public ArrayList<Order_Book> getBooks() { return books; }
	
	public boolean loadOrders_fromDatabase(int id) {
		books = new ArrayList<>();
		DBconnect db = new DBconnect();
		String object = "ORDERS";
		String condition = object + "_id = " + String.valueOf(id);
		
		try (ResultSet bSet = db.view(null, object + "_BOOK", condition);) {
			BookList bList = new BookList();
			if (! bList.loadBooks_fromDatabase(null)) return false;

			while (bSet.next())
				books.add(new Order_Book(
					bList.getBook_byId(bSet.getInt("book_id")),
					bSet.getInt("quantity"),
					bSet.getFloat("price")
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

		for (Order_Book book : books)
			value += "(" + String.valueOf(id) + ", " + book.OrdStr() + "), ";

		try { return db.add("ORDERS" + "_BOOK", value.substring(0, value.length() - 2)) > 0; }
		finally { db.close(); }
	}
}
