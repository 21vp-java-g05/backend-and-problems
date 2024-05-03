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
	
	public int size() { return importSheets.size(); }
	public boolean isEmpty() { return importSheets.isEmpty(); }
	public void add(ImportSheet importSheet) { importSheets.add(importSheet); }
	
    public ImportSheet getAuthor_byID(int id) {
		for (ImportSheet importSheet : importSheets)
			if (importSheet.getId() == id) return importSheet;
		return null;
	}

	public boolean load_fromDatabase(String condition) {
        AccountList accountList = new AccountList();

        accountList.load_fromDatabase(null);

        importSheets = new ArrayList<ImportSheet>();
        DBconnect db = new DBconnect();

        try (ResultSet iSet = db.view(null, "IMPORTS", condition)) {
            while (iSet.next()) {
                int id = iSet.getInt("id");
                String con = "imports_id = " + String.valueOf(id);
                
                Import_BookList books = new Import_BookList();
                if (! books.loadImports_fromDatabase(con)) return false;
                
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
