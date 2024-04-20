# BACKEND AND PROBLEMS
## Note
* Add driver vào Reference Libraries (https://jdbc.postgresql.org/download/)
* Test code ở Main.java

## To do list
* Thêm ImportSheet::add_toDatabase()
* Thêm Order::add_toDatabase()

* Employee::order() {
	- book đã thanh toán thành công
	tính theo số lượng còn lại trong importsheet
}

Trước khi employee thanh toán phải nhập BookList_price

* BookList_price::load_fromFile()

* Thêm ImportSheetList::load_fromDatabase()
* Thêm OrderList::load_fromDatabase()

### Chưa xác định
* View revenue statistics in a week, a month or from date to date:
	* Book
	* Book category
	* Customer
	* Employee
