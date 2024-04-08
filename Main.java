import main.frontend.backend.lists.AuthorList;
import main.frontend.backend.lists.BookList;
import main.frontend.backend.lists.CategoryList;
import main.frontend.backend.lists.PublisherList;
import main.frontend.backend.objects.Author;
import main.frontend.backend.objects.Book;
import main.frontend.backend.objects.Publisher;

public class Main {
	public static void main(String[] args) {
		AuthorList authorList = new AuthorList();
		PublisherList publisherList = new PublisherList();
		CategoryList categoryList = new CategoryList();

		BookList bList = new BookList();
		bList.loadBooks_fromDatabase(null, publisherList, authorList, categoryList);

		Book book = bList.getBookById(1);
		book.setId(31);
		// System.out.println(book.delete_toDatabase());
		// book.setStatus(false);
		// book.add_toDatabase();
	}
}
