package main.frontend.backend.lists;

import main.frontend.backend.objects.Publisher;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class PublisherList {
	private ArrayList<Publisher> publishers;
	
	public PublisherList() { publishers = new ArrayList<>(); }
	public PublisherList(ArrayList<Publisher> publishers) { publishers = new ArrayList<>(publishers); }
	public PublisherList(PublisherList other) { publishers = new ArrayList<>(other.publishers); }

	public int size() { return publishers.size(); }
	public boolean isEmpty() { return publishers.isEmpty(); }
	public void add(Publisher publisher) { publishers.add(publisher); }
	
	public Publisher getPublisher_byID(int id) {
		for (Publisher publisher : publishers)
			if (publisher.getId() == id) return publisher;
		return null;
	}
	public Publisher getPublisher_byName(String name) {
		for (Publisher publisher : publishers)
			if (publisher.getName().compareTo(name) == 0) return publisher;
		return null;
	}

	public boolean load_fromDatabase(String condition) {
		publishers = new ArrayList<Publisher>();
		DBconnect db = new DBconnect();
		
		try (ResultSet pSet = db.view(null, "PUBLISHER", condition);) {
			while (pSet.next())
				publishers.add(new Publisher(
					pSet.getInt("id"),
					pSet.getString("name"),
					pSet.getString("description"),
					pSet.getBoolean("status")
				));
		} catch (Exception e) {
			System.err.println("next() error while loading publishers: " + e.getMessage());
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
