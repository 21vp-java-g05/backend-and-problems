package main.frontend.backend.users;

public class Employee extends Account {
	public Employee(String username, String password, String mail, String fullname, int role) { super(username, password, mail, fullname, role); }
	public Employee(String username, String password, String mail, boolean enabled, String fullname, int role) { super(username, password, mail, fullname, role, enabled); }
	public Employee(Employee other) { super(other); }

	@Override
	public String toString() {
		String str = "Employee:" + "\n";
		return str + super.toString();
	}
}
