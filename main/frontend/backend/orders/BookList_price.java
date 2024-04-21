package main.frontend.backend.orders;

import java.util.ArrayList;

import main.frontend.backend.lists.AuthorList;
import main.frontend.backend.lists.BookList;
import main.frontend.backend.lists.CategoryList;
import main.frontend.backend.lists.PublisherList;
import main.frontend.backend.objects.Book;
import main.frontend.backend.utils.DBconnect;

import java.io.*;

public class BookList_price {
	private BookList books;
	private ArrayList<Integer> quantities;
	private ArrayList<Float> prices;

	public BookList_price() {}
	public BookList_price(BookList books, ArrayList<Integer> quantities, ArrayList<Float> prices) {
		this.books = books;
		this.quantities = quantities;
		this.prices = prices;
	}
	public BookList_price(BookList_price other) { this(other.books, other.quantities, other.prices); }

	public void add(Book book, int quantity, float price) {
		books.add(book);
		quantities.add(quantity);
		prices.add(price);
	}
	public int size() { return books.size(); }

	public BookList getBooks() { return books; }
	public ArrayList<Integer> getQuantities() { return quantities; }
	public ArrayList<Float> getPrices() { return prices; }

	public ArrayList<Float> calPrices() {
		ArrayList <Float> rs = new ArrayList<Float>();
		for (int i = 0; i < books.size(); i++)
			rs.add(quantities.get(i) * prices.get(i));
		return rs;
	}

	public void changeInfo(BookList books, ArrayList<Integer> quantities, ArrayList<Float> prices) {
		this.books = books;
		this.quantities = quantities;
		this.prices = prices;
	}

	public boolean load_fromFile(String FileName) {
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(FileName), "UTF-8"))
		) {
			PublisherList publishers = new PublisherList();
			AuthorList authors = new AuthorList();
			CategoryList categories = new CategoryList();

			publishers.load_fromDatabase(null);
			authors.load_fromDatabase(null);
			categories.load_fromDatabase(null);
			
			// String str;
			// while ((str = reader.readLine()) != null) {
				// String[] parts = str.split(", ");
				// Book book = new Book();
				// books.add(book);
			// }
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
	
	public boolean load_fromDatabase(String object) {
		DBconnect db = new DBconnect();
		db.close();
		return true;
	}
	public boolean add_toDatabase(String object, String id) {
		DBconnect db = new DBconnect();
		String value = "";

		ArrayList<Book> bList = books.getBooks();
		for (int i = 0; i < books.size(); i++) {
			value += "(" + id + ", " + String.valueOf(bList.get(i).getId()) + ", " + String.valueOf(quantities.get(i)) + ", " + String.valueOf(prices.get(i)) + "), ";
		}

		try { return db.add(object + "_BOOK", value.substring(0, value.length() - 2)) > 0; }
		finally { db.close(); }
	}
}
