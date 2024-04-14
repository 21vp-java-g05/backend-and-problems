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

		// Author author = new Author(1, "Minh", "The best", false);
		// author.add_toDatabase();
		Category category = new Category(1, "Hi", "hi", false);
		category.add_toDatabase();
	}
}
