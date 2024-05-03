package main.frontend.backend.lists;

import main.frontend.backend.orders.Order;
import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderList {
	private ArrayList<Order> orders;
	
	public OrderList() { orders = new ArrayList<>(); }
	public OrderList(ArrayList<Order> orders) { orders = new ArrayList<>(orders); }
	public OrderList(OrderList other) { orders = new ArrayList<>(other.orders); }
	
	public int size() { return orders.size(); }
	public boolean isEmpty() { return orders.isEmpty(); }
	public void add(Order author) { orders.add(author); }
	
	public Order getAuthor_byID(int id) {
		for (Order order : orders)
			if (order.getId() == id) return order;
		return null;
	}
	public ArrayList<Order> getOrders() { return orders; }
	public OrderList sortAscending_byDate() {
		Collections.sort(orders, new Comparator<Order>() {
			@Override
			public int compare(Order order1, Order order2) {
				return order1.getOrderTime().compareTo(order2.getOrderTime());
			}
		});
		return this;
	}

	public boolean load_fromDatabase(String condition) {
		return load_fromDatabase(condition, null, null);
	}
	public boolean load_fromDatabase(String oCon, String bCon, String cCon) {
		AccountList accounts = new AccountList();
		CustomerList customers = new CustomerList();

        accounts.load_fromDatabase(null);
		customers.load_fromDatabase(null);
		
		orders = new ArrayList<Order>();
		DBconnect db = new DBconnect();
		
		try (ResultSet oSet = db.view(null, "ORDERS", oCon);) {
			while (oSet.next()) {
				int id = oSet.getInt("id");
				String o_bCon = "orders_id = " + String.valueOf(id);

				Order_BookList books = new Order_BookList();
				if (! books.loadOrders_fromDatabase(o_bCon, bCon, cCon)) return false;

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
