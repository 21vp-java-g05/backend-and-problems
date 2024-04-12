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

<<<<<<< HEAD
		bList.loadBooks_fromDatabase(null, pList, aList, cList);
=======
		aList.load_fromDatabase(null);
		cList.load_fromDatabase(null);
		pList.load_fromDatabase(null);

		// System.out.println(aList);
		// System.out.println(cList);
		// System.out.println(pList);

		Customer ct1 = new Customer("phamgaygiakhiem@gmail.com", "Pham Quyn Za Ziem", false);
		ct1.add_toDatabase();
>>>>>>> 42a0b98ce0e3b454b36d50ec1d845a8ac306ccd8
	}
}
