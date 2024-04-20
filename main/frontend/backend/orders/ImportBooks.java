package main.frontend.backend.orders;

import java.util.ArrayList;

import main.frontend.backend.lists.BookList;

public class ImportBooks {
	private BookList books;
	private ArrayList<Integer> quantity;
	private ArrayList<Float> ImportPrice;

	public ImportBooks() {}
	public ImportBooks(BookList books, ArrayList<Integer> quantity, ArrayList<Float> ImportPrice) {
		this.books = books;
		this.quantity = quantity;
		this.ImportPrice = ImportPrice;
	}
	public ImportBooks(ImportBooks other) { this(other.books, other.quantity, other.ImportPrice); }

	public BookList getBooks() { return books; }
	public ArrayList<Integer> getQuantity() { return quantity; }
	public ArrayList<Float> getImportPrice() { return ImportPrice; }

	public void changeInfo(BookList books, ArrayList<Integer> quantity, ArrayList<Float> ImportPrice) {
		this.books = books;
		this.quantity = quantity;
		this.ImportPrice = ImportPrice;
	}
}
