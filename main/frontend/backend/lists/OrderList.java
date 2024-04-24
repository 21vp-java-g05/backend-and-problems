package main.frontend.backend.lists;

import main.frontend.backend.orders.Order;
import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class OrderList {
	private ArrayList<Order> orders;
	
	public OrderList() { orders = new ArrayList<>(); }
	public OrderList(ArrayList<Order> orders) { orders = new ArrayList<>(orders); }
	public OrderList(OrderList other) { orders = new ArrayList<>(other.orders); }
	
	public void add(Order author) { orders.add(author); }
	public void clear() { orders.clear(); }
	public int size() { return orders.size(); }
	
	public Order getAuthor_byID(int id) {
		for (Order order : orders)
			if (order.getId() == id) return order;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		AccountList accounts = new AccountList();
		CustomerList customers = new CustomerList();

        accounts.load_fromDatabase(null);
		customers.load_fromDatabase(null);
		
		orders = new ArrayList<Order>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet oSet = db.view(null, "ORDERS", condition);) {
			while (oSet.next()) {
				int id = oSet.getInt("id");
				BookList_forSell books = new BookList_forSell();
				if (! books.loadOrders_fromDatabase(id)) return false;


				orders.add(new Order(
					id,
					new Date(oSet.getTimestamp("order_time").getTime()),
					new Employee(accounts.getAccount_byID(oSet.getInt("employee"))),
					customers.getCustomer_byID(oSet.getInt("customer")),
					oSet.getInt("sale_price"),
					books
				));
			}
		} catch (SQLException e) {
			System.err.println("next() error while loading orders: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
}
