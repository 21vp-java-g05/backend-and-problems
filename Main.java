import main.frontend.backend.lists.AuthorList;
import main.frontend.backend.lists.BookList;
import main.frontend.backend.lists.CategoryList;
import main.frontend.backend.lists.PublisherList;
import main.frontend.backend.objects.Author;
import main.frontend.backend.utils.DBconnect;

public class Main {
	public static void main(String[] args) {
		AuthorList aList = new AuthorList();
		PublisherList pList = new PublisherList();
		CategoryList cList = new CategoryList();
		BookList bList = new BookList();

		bList.loadBooks_fromDatabase(null, pList, aList, cList);

		System.out.println(cList);
		System.out.println(bList);
	}
}
