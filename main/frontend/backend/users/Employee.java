package main.frontend.backend.users;

import main.frontend.backend.orders.Order;

public class Employee extends Account {
	public Employee(int id, String username, String password, String mail, String fullname, int role) {
		super(id, username, password, mail, fullname, role);
		checkRole();
	}
	public Employee(int id, String username, String password, String mail, String fullname, int role, boolean status) {
		super(id, username, password, mail, fullname, role, status);
		checkRole();
	}
	public Employee(Employee other) { super(other); }
	public Employee(Account other) {
		super(other);
		checkRole();
	}

	private void checkRole() {
		if (getRole() != 1) throw new IllegalArgumentException("Role must be employee");
	}

	public Order order(Customer customer) {
		Order order = new Order();

		
		
		float SalesPrice = 0;

		if (customer != null) SalesPrice *= 0.95;
		
		order.changeInfo(-1, null, this, SalesPrice, null, null, null, customer);

		return order.add_toDatabase() ? order : null;
	}

	@Override
	public String toString() {
		String str = "Employee:" + "\n";
		return str + super.toString();
	}
}
