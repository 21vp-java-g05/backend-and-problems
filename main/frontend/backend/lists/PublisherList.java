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
	public int size() { return publishers.size(); }
	public Publisher getPublisherByID(int id) {
		for (Publisher publisher : publishers)
			if (publisher.getId() == id) return publisher;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		publishers = new ArrayList<Publisher>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet pSet = db.view(null, "PUBLISHER", condition);) {
			while (pSet.next())
				publishers.add(new Publisher(
					pSet.getInt("id"),
					pSet.getString("name"),
					pSet.getString("description"),
					pSet.getBoolean("status")
				));
		} catch (Exception e) {
			System.err.println("Connection error while loading publishers: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}

	@Override
	public String toString() {
		String str = "There are " + publishers.size() + " publishers in the list.\n\n";
		for (Publisher publisher : publishers) str += publisher.toString() + "\n";
		return str;
	}
}
