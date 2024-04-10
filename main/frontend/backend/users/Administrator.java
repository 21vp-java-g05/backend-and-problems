package main.frontend.backend.users;

public class Administrator extends Account {
	public Administrator(String username, String password, String mail, String fullname, int role) { super(username, password, mail, fullname, role); }
	public Administrator(String username, String password, String mail, boolean enabled, String fullname, int role) { super(username, password, mail, fullname, role, enabled); }
	public Administrator(Administrator other) { super(other); }

	@Override
	public String toString() {
		String str = "Administrator:" + "\n";
		return str + super.toString();
	}
}
