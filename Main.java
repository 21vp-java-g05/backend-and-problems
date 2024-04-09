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
		Book book = new Book(65, "Minh", "1234567890223", "Vietnamese", 900, publisher, author, categories);

		Account a1 = new Account(1, "cyderxxv", "Cyderxxv03", "phamnguyengiakhiem@gmail.com", "Pham Nguyen Gia Khiem", 1);
		System.out.println("Account 1:");
        System.out.println(a1);
        System.out.println();

		a1.add_toDatabase();

		Account account2 = new Account(a1);
        System.out.println("Account 2 (copy of Account 1):");
        System.out.println(account2);
        System.out.println();

		String hashedPassword = a1.hashPassword("Cyderxxv03");
        System.out.println("Hashed password: " + hashedPassword);
		
		boolean isLoggedIn = a1.login("cyderxxv", "Cyderxxv03");
		System.out.println("Login successed is " + isLoggedIn);
		// book.add_toDatabase();
		// System.out.println(book.add_toDatabase());
		// BookList bList = new BookList();
		// bList.loadBooks_fromDatabase(null, publisherList, authorList, categoryList);

		// Book book = bList.getBookById(1);
		// book.setId(31);
		// System.out.println(book.delete_toDatabase());
		// book.setStatus(false);
		book.add_toDatabase();
	}
}
