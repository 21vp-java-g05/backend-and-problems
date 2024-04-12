import main.frontend.backend.lists.*;
import main.frontend.backend.objects.*;
import main.frontend.backend.orders.*;
import main.frontend.backend.users.*;
import main.frontend.backend.utils.*;

public class Main {
	public static void main(String[] args) {
		AuthorList aList = new AuthorList();
		PublisherList pList = new PublisherList();
		CategoryList cList = new CategoryList();
		BookList bList = new BookList();

		bList.loadBooks_fromDatabase(null, pList, aList, cList);
		aList.load_fromDatabase(null);
		cList.load_fromDatabase(null);
		pList.load_fromDatabase(null);

		// System.out.println(aList);
		// System.out.println(cList);
		// System.out.println(pList);

		// Customer ct1 = new Customer("phamgaygiakhiem@gmail.com", "Pham Quyn Za Ziem", false);
		// ct1.add_toDatabase();

		// Account a1 = new Account("Pham Quyn Za Zie6m", "hubercraigf47455@gmail.com", "t1cyder", "Cyderxxv03", 1);
		// a1.add_toDatabase();

		Administrator admin = new Administrator("admin", "password", "admin@example.com", "Admin", 1);
		boolean added = admin.addAccount("John Doe", "john@example.com", "johndoe", "password123", 2, true);
		if (added) {
    		System.out.println("Account added successfully.");
		} else {
    		System.out.println("Failed to add account.");
		}
	}
}
