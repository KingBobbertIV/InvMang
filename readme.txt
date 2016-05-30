					readme.txt


Project Title: Inventory Management
(Formerly Corner Store Inventory System)

Repository Path: /home/student/krestam/Group8_3350/Progress





Group Members:

	Yang Shi
	Jonathan Gillis
	Michael Kresta
	Peter Bridgeman
	Mickeal Nevakshonof



Packages:

	Application (GUI Launcher)
	Presentation (GUI)
	Business (Communication)
	Persistence (Stub database)
	Object (Data objects available to all packages)


Key Source Files:


	Application

-	InventoryApp.java
-	Main.java

	Presentation

-	MainActivity.java
-	InventoryManagerActivity.java
-	AddProductDialog.java
-	CheckoutModeActivity.java
-	ProductAdapter.java
-	Messages.java

	Business

-	AccessProduct.java
-	Cart.java
-	DataAccessService
	

	Persistence

-	DataAccessStub.java
-	DBAccess.java
-	DBInterface.java
-	ManageProductId.java

	Object

-	Product.java

	test

-	AllTests.java
-	AccessProductTest.java
-	CartTest.java
-	ProductTest.java
-	DBInterfaceTest.java

Behavioural Changes:
	
	android code is now only present in application and presentation packages
	application package now has no code in it that is used by other classes
	ability to switch between persistent and stub databases implemented in dropdown menu 
		on main Activity (Home)
	