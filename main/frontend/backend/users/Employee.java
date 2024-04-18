package main.frontend.backend.users;

import java.util.ArrayList;

import main.frontend.backend.orders.Order;

public class Employee extends Account {
	private static final float diff = 1.95f;
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
		ArrayList<Integer> quantity = order.getQuantity();
		ArrayList<Float> ImportPrice = order.getImportPrice();

		float SalesPrice = 0;
		for (int i = 0; i < order.getBooks().size(); i++)
			SalesPrice += diff*quantity.get(i)*ImportPrice.get(i);
		
		// Check if customer exists
			if (
				customer != null &&
				customer.check()
			) SalesPrice *= 0.95;
		
		order.changeInfo(-1, null, this, SalesPrice, null, null, null, customer);

		return order.add_toDatabase() ? order : null;
	}

	@Override
	public String toString() {
		String str = "Employee:" + "\n";
		return str + super.toString();
	}
}
