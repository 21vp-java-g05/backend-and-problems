package main.frontend.backend.lists;

import main.frontend.backend.objects.Author;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class AuthorList {
	private ArrayList<Author> authors;
	
	public AuthorList() { authors = new ArrayList<>(); }
	public AuthorList(AuthorList other) { authors = new ArrayList<>(other.authors); }
	
	public void add(Author author) { authors.add(author); }
	public void clear() { authors.clear(); }
	public Author getAuthorByID(int id) {
		for (Author author : authors)
			if (author.getId() == id) return author;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		authors = new ArrayList<Author>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet rs = db.view(null, "AUTHOR", condition);) {
			while (rs.next())
				authors.add(new Author(
					rs.getInt("id"),
					rs.getString("name"),
					rs.getString("description"),
					rs.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("Error in loading authors: " + e.getMessage());
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
