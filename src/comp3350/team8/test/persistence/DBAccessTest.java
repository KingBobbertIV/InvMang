package comp3350.team8.test.persistence;

import java.util.ArrayList;
import junit.framework.TestCase;
import comp3350.team8.inventorymanagement.business.DataAccessService;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.OrderItem;
import comp3350.team8.inventorymanagement.object.Product;
import comp3350.team8.inventorymanagement.persistence.DBInterface;


/*
 * how to test the DBAccess class
 * 
 * 1.product operation
 * 		a.testInsertProduct: confirm the product can be inserted into the database
 * 			before add a product, we have N products in database; after insert, we should have N+1 products.
 * 			after insert a product, we can get a exact same product object from database
 * 		b.testDeleteProduct: confirm the product can be deleted the from database
 * 			before delete a product, we have N products in database; after delete, we should have N-1 products.
 * 			after delete a product, we can not get the product
 * 		c.testUpdateProduct: confirm product update method works well
 * 			insert a product, get it back. change the price of the product, update that product. just get back once more to confirm
 *  	d.testGetProducts: confirm getProducts method gets all the products from database, 
 *  		the quantity of product the getProducts returned is equals to getProductCount
 *  		the each product return by getProducts is equals to getProduct returned 
 *  	d.confirm the product can be get if it exists in the DB
 * 			it is proved if a and b works
 * 
 * 2.order operation
 * 		a.testInsertOrder: confirm the order can be inserted into the database
 * 			before add a order, we have N orders in database; after insert, we should have N+1 orders.
 * 			after insert a order, we can get a exact same order object from database
 * 		b.testDeleteOrder: confirm the order can be deleted the from database
 * 			before delete a order, we have N orders in database; after delete, we should have N-1 orders.
 * 			after delete a order, we can not get the order
 * 		c.testUpdateOrder: confirm order update method works well
 * 			insert a order, get it back. change the price of the order, update that order. just get back once more to confirm
 *  	d.testGetOrders: confirm getOrders method gets all the orders from database, 
 *  		the quantity of order the getOrders returned is equals to getOrderCount
 *  		the each order return by getOrders is equals to getOrder returned 
 *  	d.confirm the order can be get if it exists in the DB
 * 			it is proved if a and b works
 * 
 */

public class DBAccessTest extends TestCase{
	DBInterface dbInterface;
	Product product;
	
	public DBAccessTest(String arg0)
	{
		super( arg0 );
	}
	
	protected void setUp(){
		dbInterface = DataAccessService.createDataAccess();
	}
	
	/*
	 * testInsertProduct
	 * 
	 * confirm the product can be inserted into the database
	 * a.before add a product, we have N products in database; after insert, we should have N+1 products.
	 * b.after insert a product, we can get a exact same product object from database
	 * 
	 */
	public void testInsertProduct(){
		Product product = new Product("test product 1", "it is the test product 1", 2.43, 100);
		Product retriveProduct;
		int number;
		int numberAfterInsert;
		int id;
		
		number = dbInterface.getProductsCount( );
		id = dbInterface.addProduct( product );
		
		numberAfterInsert = dbInterface.getProductsCount( );
		
		retriveProduct = dbInterface.getProduct(id);
		
		assertTrue(retriveProduct.equals(product));
		assertEquals(number + 1, numberAfterInsert);
	}
	
	/*
	 * testInsertProduct
	 * 
	 * confirm the product can be deleted the from database
	 * a.before delete a product, we have N products in database; after delete, we should have N-1 products.
	 * b.after delete a product, we can not get the product
	 */
	public void testDeleteProduct(){
		Product product = new Product("test product 1", "it is the test product 1", 2.43, 100);
		Product retriveProduct;
		int id = dbInterface.addProduct( product );
		int number;
		int numberAfterDelete;
		
		//number of row before delete
		number = dbInterface.getProductsCount( );
		
		//remove product
		dbInterface.deleteProduct( id );
		
		//number of row before delete
		numberAfterDelete= dbInterface.getProductsCount( );
		
		//try to retrive the product deleted
		retriveProduct = dbInterface.getProduct(id);
		
		assertTrue(retriveProduct == null);
		assertEquals(number - 1, numberAfterDelete);
		
	}
	
	/*
	 * testUpdateProduct
	 * 
	 * confirm product update method works well
	 * a.insert a product, get it back. change the price of the product, update that product. 
	 * just get back once more to confirm
	 */
	public void testUpdateProduct(){
		Product product = new Product("test product 1", "it is the test product 1", 2.43, 100);
		Product retriveProduct;
		int id;
		int number;
		int numberAfterUpdate;
		
		//add product
		id = dbInterface.addProduct( product );
		
		//number of row before update
		number = dbInterface.getProductsCount( );
		
		//change product, and update it
		product.setCost( 299 );
		product.setName( "asas" );
		dbInterface.updateProduct( product );
		
		//get new product
		retriveProduct = dbInterface.getProduct( product.getProductId( ) );
		
		//number of row before update
		numberAfterUpdate = dbInterface.getProductsCount( );
		
		//compare the quantity of product, compare the product
		assertTrue(product.equals(retriveProduct));
		assertEquals(number, numberAfterUpdate);
	}
	
	/*
	 *testGetProducts: 
	 *	confirm getProducts method gets all the products from database, 
	 *		a. the quantity of product the getProducts returned is equals to getProductCount
	 *		b. the each product return by getProducts is equals to getProduct returned 
	 */
	public void testGetProducts(){
		ArrayList<Product> products = new ArrayList<Product>();
		dbInterface.getProducts( products );
		int number = dbInterface.getProductsCount( );
		Product product;
		Product retriveProduct;
		assertEquals(number, products.size());
		
		for(int i=0; i<products.size(); i++){
			product = products.get(i);
			retriveProduct = dbInterface.getProduct( product.getProductId( ) );
			assertTrue(product.equals( retriveProduct ));
		}
	}

	/*
	 * testInsertOrder
	 * 
	 * confirm the order can be inserted into the database
	 * a.before add a order, we have N orders in database; after insert, we should have N+1 orders.
	 * b.after insert a order, we can get a exact same order object from database
	 * 
	 */
	public void testInsertOrder(){
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		double cost = 20.12;
		Order order;
		Order retriveOrder;
		
		int number;
		int numberAfterInsert;
		int id;
		
		//create an order
		items.add(new OrderItem(1,1));
		items.add(new OrderItem(2,1));
		order = new Order(items, cost);
		
		//the number of orders before insert new order
		number = dbInterface.getOrdersCount( );
		
		//insert order
		id = dbInterface.addOrder( order );
		order.setOrderId(id);
		
		//the number of orders after insert new order
		numberAfterInsert = dbInterface.getOrdersCount( );
		
		//get back the order inserted
		retriveOrder = dbInterface.getOrder( id );
		
		//confirm 
		assertTrue(order.equals(retriveOrder));
		assertEquals(number + 1, numberAfterInsert);
	}
	
	/*
	 * testDeleteOrder
	 * 
	 * confirm the order can be deleted the from database
	 * a.before delete an order, we have N orders in database; after delete, we should have N-1 orders.
	 * b.after delete a order, we can not get the order
	 * 
	 */
	public void testDeleteOrder(){
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		double cost = 20.12;
		Order order;
		
		int number;
		int numberAfterDelete;
		int id;
		
		//create an order
		items.add(new OrderItem(1,1));
		items.add(new OrderItem(2,1));
		order = new Order(items, cost);
		
		//insert a order
		id = dbInterface.addOrder( order );
		order.setOrderId(id);
		
		//the number of orders before delete new order
		number = dbInterface.getOrdersCount( );
		
		//remove the order
		dbInterface.deleteOrder( id );
		
		//the number of orders after delete new order
		numberAfterDelete = dbInterface.getOrdersCount( );
		
		//confirm
		assertEquals(number - 1, numberAfterDelete);
		assertTrue(dbInterface.getOrder(id) == null);
	}
	
	/*
	 * testUpdateOrder
	 * 
	 * confirm product update method works well
	 * a.insert a order, get it back. change the price of the order, update that order. 
	 * just get back once more to confirm
	 * 
	 */
	public void testUpdateOrder(){
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		double cost = 20.12;
		Order order;
		Order retriveOrder;
		int number;
		int numberAfterUpdate;
		int id;
		
		//create an order
		items.add(new OrderItem(1,1));
		items.add(new OrderItem(2,1));
		order = new Order(items, cost);
		
		//insert a order
		id = dbInterface.addOrder( order );
		order.setOrderId(id);
		
		//getback the order we inserted
		order = dbInterface.getOrder( id );
		
		//modify the order object: add order item , or remove order items 
		order.setOrderCost( 30.13 );
		//items = order.getOrderItems( );
		//items.add(new OrderItem(3,20));
		items.remove(0);
		order.setOrderItems(items);
		
		//number of order before update
		number = dbInterface.getOrdersCount( );
		
		//update the order
		dbInterface.updateOrder( order );
		
		//number of order after update
		numberAfterUpdate = dbInterface.getOrdersCount( );
				
		//get back the order
		retriveOrder = dbInterface.getOrder( id );
		
		//confirm
		assertTrue(order.equals( retriveOrder ));
		assertEquals(number, numberAfterUpdate);
	}
	
	/*
	 * testGetOrders
	 * 
	 * confirm getOrders method gets all the orders from database
	 * a.the quantity of order the getOrders returned is equals to getOrderCount
	 * b.the each order return by getOrders is equals to getOrder returned 
	 * 
	 */
	public void testGetOrders(){
		ArrayList<Order> orders = new ArrayList<Order>();
		int number;
		Order order;
		Order retriveOrder;
		
		//get all orders from database
		dbInterface.getOrders( orders );
		
		//get total number of orders
		number = dbInterface.getOrdersCount( );
		
		//confirm
		assertEquals(number, orders.size());
		
		for(int i=0; i<orders.size(); i++){
			order = orders.get(i);
			retriveOrder = dbInterface.getOrder( order.getOrderId( ) );
			assertTrue(order.equals( retriveOrder ));
		}
	}
}
