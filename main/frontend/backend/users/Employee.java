package main.frontend.backend.users;

import main.frontend.backend.lists.BookList_forSell;
import main.frontend.backend.objects.Book_forSell;
import main.frontend.backend.orders.ImportSheet;
import main.frontend.backend.orders.Order;
import main.frontend.backend.utils.Time;

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
	private float calPrice(BookList_forSell books) {
		float price = 0;

		for (Book_forSell book : books.getBooks())
			price += book.getQuantity()*book.getPrice();
		
		return price*diff;
	}

	public Order order(Customer customer, BookList_forSell books) {
		// Quantity is number of books bought
		float SalesPrice = calPrice(books);
		
		// Check if customer exists
		if (
			customer != null &&
			customer.check()
		) SalesPrice *= 0.95;
			
		Order order = new Order(-1, new Time().currentTime(), this, customer, SalesPrice, books);

		if (! order.add_toDatabase()) return null;

		// Update remaining book
		for (Book_forSell book : books.getBooks()) {
			book.setRemaining(book.getRemaining() - book.getQuantity());
			book.updateRemaining_toDatabase();
		}

		return order;
	}

	public boolean getImportSheet(String FileName) {
		ImportSheet importSheet = new ImportSheet();
		importSheet.changeInfo(-1, null, this, null);
		
		if (! importSheet.load_fromFile(FileName)) return false;
		return importSheet.add_toDatabase();
	}

	@Override
	public String toString() {
		String str = "Employee:" + "\n";
		return str + super.toString();
	}
}
