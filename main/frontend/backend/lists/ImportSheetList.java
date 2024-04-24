package main.frontend.backend.lists;

import main.frontend.backend.orders.ImportSheet;
import main.frontend.backend.users.Employee;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class ImportSheetList {
	private ArrayList<ImportSheet> importSheets;
	
	public ImportSheetList() { importSheets = new ArrayList<>(); }
    public ImportSheetList(ArrayList<ImportSheet> importSheets) { importSheets = new ArrayList<>(importSheets); }
	public ImportSheetList(ImportSheetList other) { importSheets = new ArrayList<>(other.importSheets); }
	
	public void add(ImportSheet importSheet) { importSheets.add(importSheet); }
	public void clear() { importSheets.clear(); }
	public int size() { return importSheets.size(); }
	
    public ImportSheet getAuthor_byID(int id) {
		for (ImportSheet importSheet : importSheets)
			if (importSheet.getId() == id) return importSheet;
		return null;
	}

	public boolean load_fromDatabase(String name) {
        AccountList accountList = new AccountList();

        accountList.load_fromDatabase(null);

        importSheets = new ArrayList<ImportSheet>();
        DBconnect db = new DBconnect();
        String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");

        try (ResultSet iSet = db.view(null, "IMPORTS", condition)) {
            while (iSet.next()) {
                int id = iSet.getInt("id");
                BookList_forSell books = new BookList_forSell();
                if (! books.loadImports_fromDatabase(id)) return false;
                
                importSheets.add(new ImportSheet(
                    id,
                    new Date(iSet.getTimestamp("import_time").getTime()),
                    new Employee(accountList.getAccount_byID(iSet.getInt("employee"))),
                    iSet.getInt("total_cost"),
                    books
                ));
            }
        } catch (SQLException e) {
            System.err.println("next() error while loading import sheets: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
        return true;
    }
}
