import main.frontend.backend.lists.*;
import main.frontend.backend.objects.*;
import main.frontend.backend.orders.*;
import main.frontend.backend.users.*;
import main.frontend.backend.utils.*;
import java.sql.*;

public class Main {
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql:" + System.getenv("DB_URL");
	private static final String USER = System.getenv("DB_USER");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");

	private static Connection connection;
	public static void main(String[] args) {
		Author author = new Author(1, "Minh", "Hi");
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connected");

			String str = "INSERT INTO AUTHOR VALUES (DEFAULT, 'Minh', 'Hi')";
			Statement st = connection.createStatement();
			st.executeUpdate(str, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			System.out.println(rs.getInt(1));
			connection.close();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found");
		} catch (SQLException e) {
			System.err.println("Connection fail");
		}

	}
}
