import main.frontend.backend.lists.AuthorList;
import main.frontend.backend.lists.BookList;
import main.frontend.backend.lists.CategoryList;
import main.frontend.backend.lists.PublisherList;
import main.frontend.backend.objects.Author;
import main.frontend.backend.objects.Book;
import main.frontend.backend.objects.Publisher;
import main.frontend.backend.users.Account;

public class Main {
	public static void main(String[] args) {
		AuthorList aList = new AuthorList();
		aList.load_fromDatabase(null);
	}
}
