import main.frontend.backend.lists.AuthorList;
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
		
		authorList.load_fromDatabase(null);
		publisherList.load_fromDatabase(null);
		categoryList.load_fromDatabase(null);

		Author author = authorList.getAuthorByID(1);
		Publisher publisher = publisherList.getPublisherByID(1);
		CategoryList categories = new CategoryList();
		categories.add(categoryList.getCategoryByID(1));

		Book book = new Book(40, "Minh", "1234567890123", "Vietnamese", 900, publisher, author, categories);

		// author.setName("He he");
		// System.out.println(author.update_toDatabase());

		// System.out.println(book);
		System.out.println(book.add_toDatabase());
		// book.update_toDatabase();
		// book.delete_toDatabase();
	}
}
