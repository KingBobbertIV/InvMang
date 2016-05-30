
package comp3350.team8.inventorymanagement.persistence;

import java.sql.Date;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import comp3350.team8.inventorymanagement.object.*;

public class DBAccess implements DBInterface {
	
	private static final java.util.Locale	SQL = Locale.CANADA;
	
	private Statement			commandLine;
	private Connection			dbIO;
	private ResultSet			cmdOut;
	
	private String				dbName;
	private String				dbType;
	private String				cmdString;
	
	private Exception			flag = null;
	//private GeneralIDManager	productIDManager		= null;
	//private GeneralIDManager	orderIDManager			= null;
	private SimpleDateFormat 	formater;
	
	public DBAccess(String dbName)
	{
		this.dbName = dbName;
		this.formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", SQL);
	}
	
	/*
	 *idea about test the DBAccess class
	 *
	 * get the number of 
	 */
	public void open(String dbPath) {
		String url;
		try {
			// Setup for HSQL
			dbType = "HSQL";
			Class.forName( "org.hsqldb.jdbcDriver" ).newInstance( );
			url = "jdbc:hsqldb:file:" + dbPath; // stored on disk mode
			dbIO = DriverManager.getConnection( url, "SA", "" );
			commandLine = dbIO.createStatement( );
		}
		catch (Exception e) {
			processDBError( e );
		}
		
		System.out.println( "Opened " + dbType + " database " + dbPath );
	}
	
	public void close() {
		try { // commit all changes to the database
			cmdString = "shutdown compact";
			cmdOut = commandLine.executeQuery( cmdString );
			dbIO.close( );
			
			//productIDManager = null;
			//orderIDManager = null;
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		System.out.println( "Closed " + dbType + " database " + dbName );
	}
	
	public ArrayList<Integer> lowStockpIDs()
	{
		ArrayList<Integer> lowStock = null;
		String sql;
		try
		{
			sql = String.format(SQL, "Select %s from %s where %d <= %d and %d >= 0", 
					ProductAttributes.IDField, ProductAttributes.tableName,
					ProductAttributes.quantityField, ProductAttributes.lowStockField, 
					ProductAttributes.lowStockField);
			cmdOut = commandLine.executeQuery(sql);
			// in the future, a negative lowStock field could be used to flag a product for deletion: delete it when its quantity is 0 and its ID isn't in any orderItems
			
			lowStock = new ArrayList<Integer>();
			
			while (cmdOut.next())
			{
				lowStock.add( cmdOut.getInt( 1 ) );
			}
			
		}
		catch (SQLException sqle)
		{
			processDBError(sqle);
		}
		
		return lowStock;
	}
	
	
	public void recordPurchase(int pID, int numberSold) throws Exception
	{
		Product product;
		Exception uncaught = null;
		String sql;
		
		try
		{
			sql = String.format(SQL, "Select * from Products where %s = %d", 
					ProductAttributes.IDField, pID);
			
			product = extractProduct(commandLine.executeQuery(sql));
			
			if (product == null && numberSold > 0)
			{
				uncaught = new Exception("ProductID: " + pID + " is not for sale.");
			}
			else if (product != null)
			{
				if (numberSold > product.getQuantity())
				{
					uncaught = new Exception("Only " + product.getQuantity() + " " + product.getName() + "s are for sale.");
				}
				else
				{
					sql = String.format( SQL, "UPDATE Products SET %s = %d WHERE productId = %d", 
							ProductAttributes.quantityField, (product.getQuantity() - numberSold), pID);
					commandLine.executeUpdate(sql);
				}
			}
		}
		catch (SQLException o)
		{
			uncaught = new Exception("Failed to update database.");
		}
		
		if (uncaught != null)
		{
			throw uncaught;
		}
		
	}
	
	/*
	 * getProductsCount
	 * 
	 * return the number of product in database 
	 */
	public int getProductsCount()
	{
		int result;
		
		try
		{
			cmdOut = commandLine.executeQuery("Select count(*) from Products");
			
			if (cmdOut.next())
			{
				result = cmdOut.getInt(1);
			}
			else
			{
				result = 0;
			}
			
			cmdOut.close( );
		}
		catch(Exception SQLErr)
		{
			result = -1;
		}
		
		return result;
	}
	
	/*
	 * extractProduct
	 * 
	 * construct a product object by the infomation from database 
	 * notice: the caller should confirm the qr.next is true
	 */
	private Product extractProduct(ResultSet qr) throws SQLException
	{
		Product result = null;

		result = new Product(
			qr.getInt(ProductAttributes.IDField), 
			qr.getString(ProductAttributes.nameField), 
			qr.getString(ProductAttributes.descriptionField),
			qr.getDouble(ProductAttributes.priceField), 
			qr.getDouble( ProductAttributes.supplierCostField ), 
			qr.getInt(ProductAttributes.quantityField),
			qr.getInt(ProductAttributes.lowStockField)
		);
		
		return result;
	}
	
	/*
	 * getProducts
	 * 
	 * return all products in the database
	 * 
	 * parameter:
	 * 		products: the array list use to take product back
	 */
	public void getProducts(ArrayList<Product> products)
	{
		Product currentProduct;
		
		try
		{
			cmdOut = commandLine.executeQuery( "Select * From Products" );
			
			while(cmdOut.next())
			{
				currentProduct = extractProduct(cmdOut);
				products.add(currentProduct);
			};
			
			cmdOut.close( );
		}
		catch (SQLException sqlErr)
		{
			processDBError(sqlErr);
		}
	}
	
	/*
	 * addProduct
	 * 
	 * add a product into the database, and return the product id
	 * parameter:
	 * 		currentProduct: the product object you want to add
	 * return 
	 * 		the product id for you added
	 */
	public int addProduct(Product product) {
		String sql;
		int productId = 0;

		try 
		{
			sql = String.format(SQL, "INSERT INTO Products VALUES (NULL, '%s', '%s', %.2f, %d, %d, %.2f)",
					product.getName( ), product.getDescription( ), product.getPrice( ),
					product.getQuantity( ), product.getLowStock( ), product.getCost());
			commandLine.executeUpdate( sql );
			
			//get the product id which generated by database
			cmdOut = commandLine.executeQuery("CALL IDENTITY();");
			if (cmdOut.next()) 
				productId = cmdOut.getInt(1);
			
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		product.setProductId(productId);
		
		return productId;
	}
	
	/*
	 * accessProduct
	 * 
	 * get a product by the product id specified in parameter
	 * parameter:
	 * 		productId: the product id for the product you want
	 * return 
	 * 		the product object when found the product, otherwise return null
	 */
	public Product getProduct(int productId) {
		Product myProduct = null;
		String sql;
		
		try 
		{
			sql = String.format(SQL, "Select * from Products where %s = %s", ProductAttributes.IDField, productId);
			cmdOut = commandLine.executeQuery( sql );
			
			if(cmdOut.next()){
				myProduct = extractProduct(cmdOut);
			}
			
			cmdOut.close( );
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		
		return myProduct;
	}
	
	/*
	 * updateProduct
	 * 
	 * update the product whos's product id is same as the parameter product's 
	 * parameter:
	 * 		product: the product you want to update
	 */
	public void updateProduct(Product product) {
		String sql;
		
		try {
			sql = "UPDATE Products SET %s='%s', %s='%s', %s=%.2f, %s=%d, %s=%d, %s=%.2f WHERE %s=%d";
			sql = String.format(SQL, sql, ProductAttributes.nameField, product.getName( ),
					ProductAttributes.descriptionField, product.getDescription( ),
					ProductAttributes.priceField, product.getPrice( ),
					ProductAttributes.quantityField, product.getQuantity( ),
					ProductAttributes.lowStockField, product.getLowStock(),
					ProductAttributes.supplierCostField, product.getCost(),
					ProductAttributes.IDField, product.getProductId( )
			);
			
			commandLine.executeUpdate( sql );
		}
		catch (SQLException e) {
			processDBError( e );
		}
		// return result;
	}
	
	/*
	 * dateleProduct
	 * 
	 * delete the product whos's product id is same as the parameter productId
	 * parameter:
	 * 		product: the product you want to update
	 */
	public void deleteProduct(int productId) {
		
		try {
			cmdString = "Delete from Products where " + ProductAttributes.IDField + "=" + productId;
			commandLine.executeUpdate( cmdString );
		}
		catch (SQLException e) {
			processDBError( e );
		}
		// return result;
	}
	
	/*
	 * addOrder
	 * 
	 * add an order to the database
	 */
	public int addOrder(Order order)
	{
		String sql;
		ArrayList<OrderItem> orderItems = order.getOrderItems( );
		int orderId = 0;
		OrderItem item; 
		
		try 
		{
			//insert order
			sql = String.format(SQL, "Insert into Orders Values(NULL, %.2f, '%s' ) ", 
					order.getOrderCost( ), this.formater.format( order.getOrderDatePlaced( ) ) );
			commandLine.executeUpdate( sql );
		
			//get the order id which generated by database
			cmdOut = commandLine.executeQuery("CALL IDENTITY();");
			if (cmdOut.next()) 
				orderId = cmdOut.getInt(1);
			
			for(int offset=0; offset<orderItems.size( ); offset ++ )
			{
				item = orderItems.get( offset );
				sql = String.format(SQL, "Insert into OrderItems Values( %d, %d, %d ) ", 
						orderId, item.getProductId( ), item.getQuantity( ) );
				commandLine.executeUpdate( sql );
			}
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		order.setOrderId(orderId);
		
		return orderId;
	}
	
	/*
	 * updateOrder
	 * 
	 * update an order in the database
	 * return the order id after finish the task
	 */
	public int updateOrder(Order order){
		String sql;
		ArrayList<OrderItem> orderItems = order.getOrderItems( );
		int orderId;
		OrderItem item;
		
		orderId = order.getOrderId( );
		
		try 
		{
			//update order information
			sql = String.format(SQL, "Update Orders Set %s = %.2f, %s = '%s' where %s = %d", 
					OrderAttributes.priceField, order.getOrderCost( ), 
					OrderAttributes.dateField, this.formater.format( order.getOrderDatePlaced( ) ), 
					OrderAttributes.IDField, orderId);
			commandLine.executeUpdate( sql );
			
			//delete all related order items
			sql = String.format(SQL, "DELETE  FROM OrderItems where OrderId = %d", orderId);
			commandLine.executeUpdate( sql );
			
			//re-insert order items
			for(int offset=0; offset < orderItems.size( ); offset ++ )
			{
				item = orderItems.get( offset );
				
				sql = String.format(SQL, "Insert into OrderItems Values( %d, %d, %d ) ", 
						orderId, item.getProductId( ), item.getQuantity( ) );
				commandLine.executeUpdate( sql );
			}
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		return orderId;
	}

	/*
	 * getOrder
	 * 
	 * return an order if the order exist, or return null if not found
	 */
	public Order getOrder(int orderId)
	{
		String sql;
		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
		Order order = null;	
		OrderItem item;
		int productId;
		int quantity;
		double totalAmount = 0.0;
		
		try 
		{
			//get all order items
			sql = "Select * from orderItems where orderId = " + orderId;
			cmdOut = commandLine.executeQuery( sql );
			
			while(cmdOut.next())
			{
				productId = cmdOut.getInt(ProductAttributes.IDField);
				quantity = cmdOut.getInt( OrderAttributes.orderItemQuantity );
				item = new OrderItem(productId, quantity);
				orderItems.add( item );
			}			
			
			//get order info
			sql = "Select * from Orders where orderId = " + orderId;
			cmdOut.close( );
			cmdOut = commandLine.executeQuery( sql );
			if (cmdOut.next())
			{
				totalAmount = cmdOut.getDouble(OrderAttributes.priceField);
				order = new Order(orderItems, totalAmount );
				order.setOrderId(orderId);
				order.setDatePlaced(cmdOut.getDate( OrderAttributes.dateField ));
			}
			
			cmdOut.close( );
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		return order;
	}


	/*
	 * getOrders
	 * 
	 * parameter: 
	 * 	orders: bring back all orders
	 */
	public void getOrders(ArrayList<Order> orders)
	{
		String sql;
		Order order;
		int productId;
		int quantity;
		Date dateplaced = null;
		double totalAmount;					//total amount money for the specific order
		ArrayList<OrderItem> items = null;	//product list for a order
		int orderId;
		ResultSet result;
		
		try 
		{    
			//get all orders with items
			sql = "Select * from Orders order by orderId asc";
			cmdOut = commandLine.executeQuery( sql );
			
			//get order one by one
			while(cmdOut.next()){
				orderId = cmdOut.getInt( OrderAttributes.IDField );
				totalAmount = cmdOut.getDouble( OrderAttributes.priceField );
				dateplaced = cmdOut.getDate( OrderAttributes.dateField );
				
				//get all items for a order
				items = new ArrayList<OrderItem>();
				sql = String.format( SQL, "Select * from OrderItems where orderId = %d", orderId );
				result = commandLine.executeQuery(sql);
				while(result.next()){
					productId = result.getInt(OrderAttributes.orderItemProductId);
					quantity = result.getInt(OrderAttributes.orderItemQuantity);
					items.add( new OrderItem(productId, quantity) );
				}
				result.close();
				
				order = new Order(items, totalAmount);
				order.setDatePlaced(dateplaced);
				order.setOrderId( orderId );
				
				//add an order into the list
				orders.add(order);
			}
			
			cmdOut.close( );
		}
		catch (SQLException e) {
			processDBError( e );
		}
		
		System.out.println("total number = " + orders.size());
		return;
	}
	
	/*
	 * deleteOrder
	 * 
	 * delete the order whose order id is equals to the parameter
	 */
	public void deleteOrder(int orderID)
	{
		String sql = String.format( SQL, "DELETE FROM %s WHERE %s = %s", 
				OrderAttributes.tableName, OrderAttributes.IDField, orderID );
		try
		{
			commandLine.executeUpdate(sql);
		}
		catch (SQLException sqle)
		{
			processDBError(sqle);
		}
	}
	
	/*
	 * getOrdersCount
	 * 
	 * return the number of orders in database
	 */
	public int getOrdersCount(){
		int result;
		
		try
		{
			cmdOut = commandLine.executeQuery("Select count(*) from Orders");
			
			if (cmdOut.next())
			{
				result = cmdOut.getInt(1);
			}
			else
			{
				result = 0;
			}
			
			cmdOut.close( );
		}
		catch(Exception SQLErr)
		{
			result = -1;
		}
		
		return result;
	}
	
	protected ResultSet specialAccess(String cmd) throws SQLException // for use by the DBMS
	{
		return commandLine.executeQuery( cmd );
	}
	
	private void processDBError(Exception e)
	{
		// Remember, this will NOT be seen by the user!
		this.flag = e;
		e.printStackTrace( );
		//throw new RuntimeException(e); // use if we want to stop the program the moment something fails
	}
	
	public Exception retrieveDBError()
	{
		return new Exception(flag.getMessage());
	}
	
}

