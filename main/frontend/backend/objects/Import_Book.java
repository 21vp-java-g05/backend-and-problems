package main.frontend.backend.objects;

import main.frontend.backend.utils.DBconnect;

public class Import_Book extends Order_Book {
	private int importID, remaining;

	public Import_Book() {}
	public Import_Book(Order_Book book, int importID, int remaining) {
		super(book);
		this.importID = importID;
		this.remaining = remaining;

		if (! checkRemaining_valid()) throw new IllegalStateException("Remaining invalid");
	}
	public Import_Book(Import_Book other) {
		this(other, other.importID, other.remaining);
	}
	public Import_Book(Order_Book other) {
		super(other);
	}

	public int getSellID() { return importID; }
	public Book getBook() { return book; }
	public int getQuantity() { return quantity; }
	public float getPrice() { return price; }
	public int getRemaining() { return remaining; }
	
	public void setSellID(int importID) { this.importID = importID; }
	public void setRemaining(int remaining) { this.remaining = remaining; }

	public void changeInfo(Book book, int quantity, float price, int importID, int remaining) {
		super.changeInfo(book, quantity, price);
		this.importID = importID;
		this.remaining = remaining;
	}

	private boolean checkRemaining_valid() { return 0 <= remaining && remaining <= quantity; }

	public boolean updateRemaining_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "remaining = " + String.valueOf(remaining);
		String condition = "imports_id = " + String.valueOf(importID) + " AND book_id = " + String.valueOf(book.getId());
		
		try { return db.update("IMPORTS_BOOK", value, condition) > 0; }
		finally { db.close(); }
	}

	public String ImpStr() {
		return String.valueOf(importID) + ", " + OrdStr() + ", " + String.valueOf(remaining);
	}
}
