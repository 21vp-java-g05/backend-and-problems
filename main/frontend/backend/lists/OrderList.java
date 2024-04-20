package main.frontend.backend.lists;

import main.frontend.backend.orders.Order;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class OrderList {
	private ArrayList<Order> orders;
	
	public OrderList() { orders = new ArrayList<>(); }
	public OrderList(OrderList other) { orders = new ArrayList<>(other.orders); }
	
	public void add(Order author) { orders.add(author); }
	public void clear() { orders.clear(); }
	public int size() { return orders.size(); }
	public Order getAuthorByID(int id) {
		for (Order order : orders)
			if (order.getId() == id) return order;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		orders = new ArrayList<Order>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet oSet = db.view(null, "ORDERS", condition);) {
			while (oSet.next())
				orders.add(new Order(
					
				));
		} catch (SQLException e) {
			System.err.println("Connection error while loading orders: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
}
