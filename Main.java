import main.frontend.backend.objects.*;

import java.sql.ResultSet;

import main.frontend.backend.lists.*;

public class Main {
	public static void main(String[] args) {
		Publisher publisher = new Publisher(1, "Test Publisher", "shit", true);
        Author author = new Author(1, "Test Author", "test@author.com");
        CategoryList categories = new CategoryList();
        categories.add(new Category(1, "Test Category", "?"));

        // Assuming the Book constructor signature matches your actual implementation
        Book testBook = new Book(0, "Test Book", "123456789", "English", 300, publisher, author, categories, true);

        // Attempt to add the book to the database
        boolean result = testBook.add_toDatabase();

        // Print result
        if (result) {
            System.out.println("Book added to database successfully.");
        } else {
            System.out.println("Failed to add book to database.");
        }

		// ResultSet a = null;
		// try {
		// 	System.out.println(a.next());
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
	}
}
