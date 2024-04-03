package main.frontend.backend.lists;

import main.frontend.backend.objects.Publisher;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class PublisherList {
	private ArrayList<Publisher> publishers;
	
	public PublisherList() { publishers = new ArrayList<>(); }
	public PublisherList(PublisherList other) { publishers = new ArrayList<>(other.publishers); }

	public void add(Publisher publisher) { publishers.add(publisher); }
	public void clear() { publishers.clear(); }
	public Publisher getPublisherByID(int id) {
		for (Publisher publisher : publishers)
			if (publisher.getId() == id) return publisher;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		DBconnect db = new DBconnect();
		publishers = new ArrayList<Publisher>();
		
		try (ResultSet rs = db.view(null, "PUBLISHER", condition);) {
			while (rs.next())
				publishers.add(new Publisher(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getBoolean("status")));
		} catch (Exception e) {
			System.err.println("Load publisher");
			e.printStackTrace();
			return false;
		} finally { db.close(); }

		return true;
	}

	@Override
	public String toString() {
		String str = "There are " + publishers.size() + " publishers in the list.\n\n";

		for (Publisher publisher : publishers)
			str += publisher.toString() + "\n";
		
		return str;
	}
}
