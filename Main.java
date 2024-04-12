import main.frontend.backend.lists.*;
import main.frontend.backend.objects.*;
import main.frontend.backend.orders.*;
import main.frontend.backend.users.*;
import main.frontend.backend.utils.*;

public class Main {
	public static void main(String[] args) {
		AuthorList aList = new AuthorList();
		CategoryList cList = new CategoryList();
		PublisherList pList = new PublisherList();

		aList.load_fromDatabase(null);
		cList.load_fromDatabase(null);
		pList.load_fromDatabase(null);

		System.out.println(aList);
		System.out.println(cList);
		System.out.println(pList);
	}
}
