import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

import main.frontend.backend.lists.*;
import main.frontend.backend.objects.*;
import main.frontend.backend.orders.*;
import main.frontend.backend.users.*;
import main.frontend.backend.utils.*;

public class Main {
	public static void main(String[] args) {
		DBconnect db = new DBconnect();
		ResultSet resultSet = db.view(null, "AUTHOR", null);
		
		try {
			System.out.println(resultSet.wasNull());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		db.close();
	}
}
