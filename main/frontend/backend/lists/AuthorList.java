package main.frontend.backend.lists;

import main.frontend.backend.objects.Author;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class AuthorList {
	private ArrayList<Author> authors;
	
	public AuthorList() { authors = new ArrayList<>(); }
	public AuthorList(ArrayList<Author> authors) { authors = new ArrayList<>(authors); }
	public AuthorList(AuthorList other) { authors = new ArrayList<>(other.authors); }
	
	public int size() { return authors.size(); }
	public boolean isEmpty() { return authors.isEmpty(); }
	public void add(Author author) { authors.add(author); }
	
	public Author getAuthor_byID(int id) {
		for (Author author : authors)
			if (author.getId() == id) return author;
		return null;
	}
	public Author getAuthor_byName(String name) {
		for (Author author : authors)
			if (author.getName().compareTo(name) == 0) return author;
		return null;
	}

	public boolean load_fromDatabase(String condition) {
		authors = new ArrayList<Author>();
		DBconnect db = new DBconnect();
		
		try (ResultSet aSet = db.view(null, "AUTHOR", condition);) {
			while (aSet.next())
				authors.add(new Author(
					aSet.getInt("id"),
					aSet.getString("name"),
					aSet.getString("description"),
					aSet.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("next() error while loading authors: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
	
	@Override
	public String toString() {
		String str = "There are " + authors.size() + " authors in the list.\n\n";
		for (Author author : authors) str += author.toString() + "\n";
		return str;
	}
}
