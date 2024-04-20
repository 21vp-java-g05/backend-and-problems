package main.frontend.backend.orders;

import java.util.ArrayList;

import main.frontend.backend.lists.BookList;

import java.io.*;

public class ImportBooks {
	private BookList books;
	private ArrayList<Integer> quantities;
	private ArrayList<Float> ImportPrice;

	public ImportBooks() {}
	public ImportBooks(BookList books, ArrayList<Integer> quantities, ArrayList<Float> ImportPrice) {
		this.books = books;
		this.quantities = quantities;
		this.ImportPrice = ImportPrice;
	}
	public ImportBooks(ImportBooks other) { this(other.books, other.quantities, other.ImportPrice); }

	public BookList getBooks() { return books; }
	public ArrayList<Integer> getQuantities() { return quantities; }
	public ArrayList<Float> getImportPrice() { return ImportPrice; }

	public ArrayList<Float> getPrices() {
		ArrayList <Float> prices = new ArrayList<Float>();
		for (int i = 0; i < books.size(); i++)
			prices.add(quantities.get(i) * ImportPrice.get(i));
		return prices;
	}

	public void changeInfo(BookList books, ArrayList<Integer> quantities, ArrayList<Float> ImportPrice) {
		this.books = books;
		this.quantities = quantities;
		this.ImportPrice = ImportPrice;
	}

	public boolean load_fromFile(String FileName) {
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(FileName), "UTF-8"))
		) {
			
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
}
