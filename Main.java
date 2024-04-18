import main.frontend.backend.objects.*;
import main.frontend.backend.lists.*;
import main.frontend.backend.orders.ImportSheet;
import main.frontend.backend.orders.Order;

import java.sql.ResultSet;

import main.frontend.backend.lists.*;

public class Main {
	public static void main(String[] args) {
        ImportSheetList importSheetList = new ImportSheetList();
        OrderList orderList = new OrderList();

        if(importSheetList.load_fromDatabase(null)) {
            System.out.println("Import sheets loaded from database:");
            for (int i = 0; i < importSheetList.size(); i++) {
                ImportSheet importSheet = importSheetList.getAuthorByID(i + 1);
                if (importSheet != null) {
                    System.out.println("ID: " + importSheet.getId() + " Timestamp: " + importSheet.getImportTime() + " TotalPrice: " + importSheet.getTotalCost());
                }
            }
        } else {
            System.out.println("Failed to load import sheets from database.");
        }

        if(orderList.load_fromDatabase(null)) {
            System.out.println("Orders loaded from database:");
            for (int i = 0; i < orderList.size(); i++) {
                Order order = orderList.getAuthorByID(i + 1);
                if (order != null) {
                    System.out.println("ID: " + order.getId() + " Order Time: " + order.getOrderTime() + " Sales Price: " + order.getSalesPrice());
                }
            }
        } else {
            System.out.println("Failed to load orders from database.");
        }
	}
}
