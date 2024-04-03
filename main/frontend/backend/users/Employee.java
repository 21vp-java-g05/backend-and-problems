package main.frontend.backend.users;

public class Employee extends Account {
	public Employee(int id, String username, String password, String mail) { super(id, username, password, mail); }
	public Employee(int id, String username, String password, String mail, boolean enabled) { super(id, username, password, mail, enabled); }
	public Employee(Employee other) { super(other); }

	@Override
	public String toString() {
		String str = "Employee:" + "\n";
		return str + super.toString();
	}
}
