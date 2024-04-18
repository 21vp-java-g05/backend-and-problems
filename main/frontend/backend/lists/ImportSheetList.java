package main.frontend.backend.lists;

import main.frontend.backend.orders.ImportSheet;
import main.frontend.backend.utils.DBconnect;

import java.sql.*;
import java.util.ArrayList;

public class ImportSheetList {
	private ArrayList<ImportSheet> importSheets;
	
	public ImportSheetList() { importSheets = new ArrayList<>(); }
	public ImportSheetList(ImportSheetList other) { importSheets = new ArrayList<>(other.importSheets); }
	
	public void add(ImportSheet importSheet) { importSheets.add(importSheet); }
	public void clear() { importSheets.clear(); }
	public int size() { return importSheets.size(); }
	public ImportSheet getAuthorByID(int id) {
		for (ImportSheet importSheet : importSheets)
			if (importSheet.getId() == id) return importSheet;
		return null;
	}

	public boolean load_fromDatabase(String name) {
		importSheets = new ArrayList<ImportSheet>();
		DBconnect db = new DBconnect();
		String condition = name == null || name.isEmpty() ? null : ("name LIKE '%" + name + "%'");
		
		try (ResultSet rs = db.view(null, "", condition);) {
			while (rs.next())
				importSheets.add(new ImportSheet(
					
				));
		} catch (SQLException e) {
			System.err.println("Error in loading import sheets: " + e.getMessage());
			return false;
		} finally { db.close(); }
		return true;
	}
}
