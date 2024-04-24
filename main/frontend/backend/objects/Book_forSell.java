package main.frontend.backend.objects;

import main.frontend.backend.utils.DBconnect;

public class Book_forSell {
	private int sellID, quantity, remaining;
	private Book book;
	private float price;

	public Book_forSell() {}
	public Book_forSell(int sellID, Book book, int quantity, float price, int remaining) {
		this.sellID = sellID;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
		this.remaining = remaining;
	}
	public Book_forSell(Book_forSell other) {
		this(other.sellID, other.book, other.quantity, other.price, other.remaining);
	}

	public int getSellID() { return sellID; }
	public Book getBook() { return book; }
	public int getQuantity() { return quantity; }
	public float getPrice() { return price; }
	public int getRemaining() { return remaining; }

	public void setSellID(int sellID) { this.sellID = sellID; }
	public void setBook(Book book) { this.book = book; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public void setPrice(float price) { this.price = price; }
	public void setRemaining(int remaining) { this.remaining = remaining; }

	public void changeInfo(int sellID, Book book, int quantity, float price, int remaining) {
		this.sellID = sellID;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
		this.remaining = remaining;
	}

	public boolean updateRemaining_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "remaining = " + String.valueOf(remaining);
		String condition = "imports_id = " + String.valueOf(sellID) + " AND book_id = " + String.valueOf(book.getId());
		
		try { return db.update("AUTHOR", value, condition) > 0; }
		finally { db.close(); }
	}

	public String OrdStr() {
		return String.valueOf(book.getId()) + ", " + String.valueOf(quantity) + ", " + String.valueOf(price);
	}

	public String ImpStr() {
		return String.valueOf(sellID) + ", " + OrdStr() + ", " + String.valueOf(remaining);
	}
}
