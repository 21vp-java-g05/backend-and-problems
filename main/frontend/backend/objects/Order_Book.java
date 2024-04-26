package main.frontend.backend.objects;

public class Order_Book {
	protected Book book;
	protected int quantity;
	protected float price;

	public Order_Book() {}
	public Order_Book(Book book, int quantity, float price) {
		this.book = book;
		this.quantity = quantity;
		this.price = price;
	}
	public Order_Book(Order_Book other) {
		this(other.book, other.quantity, other.price);
	}
	public Order_Book(Import_Book other) {
		this(other.book, other.quantity, other.price);
	}

	public Book getBook() { return book; }
	public int getQuantity() { return quantity; }
	public float getPrice() { return price; }

	public void setBook(Book book) { this.book = book; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public void setPrice(float price) { this.price = price; }

	public void changeInfo(Book book, int quantity, float price) {
		this.book = book;
		this.quantity = quantity;
		this.price = price;
	}

	public String OrdStr() {
		return String.valueOf(book.getId()) + ", " + String.valueOf(quantity) + ", " + String.valueOf(price);
	}
}
