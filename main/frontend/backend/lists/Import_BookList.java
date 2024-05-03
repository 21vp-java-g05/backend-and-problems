package main.frontend.backend.lists;

import java.util.ArrayList;

import main.frontend.backend.objects.Book.Book;
import main.frontend.backend.objects.Book.Import_Book;
import main.frontend.backend.objects.Book.Order_Book;
import main.frontend.backend.utils.DBconnect;

import java.io.*;
import java.sql.*;

public class Import_BookList {
	private ArrayList<Import_Book> books;

	public Import_BookList() {}
	public Import_BookList(ArrayList<Import_Book> books) { books = new ArrayList<>(books); }
	public Import_BookList(Import_BookList other) { books = new ArrayList<>(other.books); }
	public Import_BookList(Order_BookList other) {
		for (Order_Book book : other.getBooks())
			books.add(new Import_Book(book));
	}
	
	public int size() { return books.size(); }
	public boolean isEmpty() { return books.isEmpty(); }
	public void add(Import_Book book) { books.add(book); }

	public Import_Book get(int i) {
		return books.get(i);
	}
	public ArrayList<Import_Book> getBooks() { return books; }

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

				books.add(new Import_Book(
					new Order_Book(
						new Book(
							-1, parts[0], parts[1], parts[2],
							Integer.parseInt(parts[3]),
							publishers.getPublisher_byName(parts[4]),
							authors.getAuthor_byName(parts[5]),
							cList
						),
						quantity,
						Float.parseFloat(parts[7])
					),
					-1,
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
	
	public boolean loadImports_fromDatabase(String condition) {
		books = new ArrayList<>();
		DBconnect db = new DBconnect();
		
		try (ResultSet bSet = db.view(null, "IMPORTS_BOOK", condition);) {
			BookList bList = new BookList();
			if (! bList.loadBooks_fromDatabase(null)) return false;

			while (bSet.next())
				books.add(new Import_Book(
					new Order_Book(
						bList.getBook_byId(bSet.getInt("book_id")),
						bSet.getInt("quantity"),
						bSet.getFloat("price")
					),
					bSet.getInt("imports_id"),
					bSet.getInt("remaining")
				));
		} catch (SQLException e) {
			System.err.println("next() error while loading books: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	
	public boolean addImports_toDatabase() {
		DBconnect db = new DBconnect();
		String value = "";

		for (Import_Book book : books)
			value += "(" + book.ImpStr() + "), ";

		try { return db.add("IMPORTS" + "_BOOK", value.substring(0, value.length() - 2)) > 0; }
		finally { db.close(); }
	}
}
