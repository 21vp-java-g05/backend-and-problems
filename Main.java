import main.frontend.backend.users.Account;
import main.frontend.backend.users.Administrator;

public class Main {
	public static void main(String[] args) {
		Account acc = new Account(-1, "Nguyen Tran Chau Minh", "03102003chauminh@gmail.com", "03102003Minh", "123", 0);
		Administrator ad = new Administrator();
		System.out.println(acc);
		ad.addAccount_toDatabase(acc);
	}
}
