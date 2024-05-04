package main.frontend.backend.users;

import main.frontend.backend.lists.AccountList;
import main.frontend.backend.utils.DBconnect;

public class Administrator extends Account {
	public Administrator() {}
	public Administrator(int id, String fullname, String mail, String username, String password, int role) {
		super(id, fullname, mail, username, password, role, true);
		checkRole();
	}
	public Administrator(int id, String fullname, String mail, String username, String password, int role, boolean status) {
		super(id, fullname, mail, username, password, role, status);
		checkRole();
	}
	public Administrator(Administrator other) { super(other); }
	public Administrator(Account other) {
		super(other);
		checkRole();
	}

	private void checkRole() {
		if (role != 0) throw new IllegalArgumentException("Role must be administrator");
	}

	public AccountList loadAccounts_fromDatabase() {
		AccountList accounts = new AccountList();
		return accounts.load_fromDatabase(null) ? accounts : null;
	}
	public boolean addAccount_toDatabase(Account account) {
		DBconnect db = new DBconnect();
		String value = "(DEFAULT, " + account.toString() + ")";

		try { return (id = db.add_getAuto("ACCOUNT", value)) > 0; }
		finally { db.close(); }
	}
	public boolean editAccount_fromDatabase(Account account) {
		return account.updateAccount_toDatabase();
	}

	// Giải quyết vấn đề truy cập vào các hàm load book và gửi đk sách
	// private OrderList revenue_d2d(java.util.Date from, java.util.Date to, String other_condition) {
	// 	String condition = new Time(from) + " <= order_time AND order_time <= " + new Time(to) + other_condition;
	// 	OrderList oList = loadOrders_fromDatabase(condition);
	// 	return oList.sortAscending_byDate();
	// }
	// public OrderList revenue_fromBook_d2d(int id, java.util.Date from, java.util.Date to) {
	// 	String bCon = " AND book_id = " + String.valueOf(id);
	// 	OrderList oList = revenue_d2d(from, to, null);
	// 	OrderList result = new OrderList();
		
	// 	// Get book by id
	// 	for (Order order : oList.getOrders()) {
			
	// 	}

	// 	return result;
	// }
	// public OrderList revenue_fromCategory_d2d(int id, java.util.Date from, java.util.Date to) {
	// 	String bCon1 = "category_id = " + String.valueOf(id);
	// 	OrderList oList = revenue_d2d(from, to, null);
		
	// 	return oList;
	// }
	// public OrderList revenue_fromCustomer_d2d(int id, java.util.Date from, java.util.Date to) {
	// 	String con = " AND customer = " + String.valueOf(id);
	// 	return revenue_d2d(from, to, con);
	// }
	// public OrderList revenue_fromEmployee_d2d(int id, java.util.Date from, java.util.Date to) {
	// 	String con = " AND employee = " + String.valueOf(id);
	// 	return revenue_d2d(from, to, con);
	// }

	@Override
	public String toString() {
		String str = "Administrator:" + "\n";
		return str + super.toString();
	}
}
