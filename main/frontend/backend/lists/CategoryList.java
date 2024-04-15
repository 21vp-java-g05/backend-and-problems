package main.frontend.backend.lists;

import main.frontend.backend.objects.Category;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class CategoryList {
	private ArrayList<Category> categories;
	
	public CategoryList() { categories = new ArrayList<Category>(); }
	public CategoryList(CategoryList other) { categories = new ArrayList<>(other.categories); }

	public void add(Category category) { categories.add(category); }
	public void clear() { categories.clear(); }
	public Category getCategoryByID(int id) {
		for (Category category : categories)
			if (category.getId() == id) return category;
		return null;
	}

	public ArrayList<Category> getCategories() { return categories; }

	public boolean load_fromDatabase(String name) {
		categories = new ArrayList<Category>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet rs = db.view(null, "CATEGORY", condition);) {
			while (rs.next())
				categories.add(new Category(
					rs.getInt("id"),
					rs.getString("name"),
					rs.getString("description"),
					rs.getBoolean("status")
				));
		} catch (SQLException e) {
			System.err.println("Error in loading categories: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		String str = "There are " + categories.size() + " categories in the list.\n\n";
		for (Category category : categories) str += category.toString() + "\n";
		return str.toString();
	}
}
