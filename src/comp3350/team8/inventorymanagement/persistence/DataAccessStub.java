
package comp3350.team8.inventorymanagement.persistence;

import java.util.ArrayList;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.Product;

public class DataAccessStub implements DBInterface
{
	
	private String				dbName;
	private String				dbType			= "stub";
	
	private ArrayList<Product>	products;
	private GeneralIDManager 	productIdManager	=  new GeneralIDManager(); 
	
	private ArrayList<Order>	orders;
	private GeneralIDManager 	orderIdManager		=  new GeneralIDManager(); 
															
	public DataAccessStub(String dbName)
	{
		this.dbName = dbName;
	}
	
	/*
	 * open
	 * 
	 * it is used to open a database which name is specified by parameter
	 */
	public void open(String dbName)
	{
		
		products = new ArrayList<Product>( );
		orders = new ArrayList<Order>( );
		
		System.out.println( "Opened " + dbType + " database " + dbName );
	}
	
	public void close()
	{
		System.out.println( "Closed " + dbType + " database " + dbName );
	}
	
	public Exception retrieveDBError() { 
		return null; 
	}
	
	public void recordPurchase(int productId, int numberSold) throws Exception
	{
		int productIndex = searchProducts(productId);
		Product shallowCopy;
		
		if (productIndex < 0 && numberSold > 0)
		{
			throw new Exception("ProductID: " + productId + " is not for sale.");
		}
		else if (productIndex >= 0)
		{
			shallowCopy = products.get( productIndex );

			if (numberSold > products.get( productIndex ).getQuantity())
			{
				throw new Exception("Only " + shallowCopy.getQuantity() + " " + shallowCopy.getName() + "s are for sale.");
			}
			else
			{
				shallowCopy.setQuantity(shallowCopy.getQuantity() -  numberSold);
				//We are allowing for -'ve purchases as we use this for orders as well
			}
		}
	
	}
	
	/*
	 * getProductSequential
	 * 
	 * put all products object into the ArrayList specified in parameter
	 */
	public void getProducts(ArrayList<Product> productResult)
	{
		if (productResult != null)
		{
			for (int j = 0; j < products.size( ); j++)
			{
				productResult.add( products.get( j ).clone() );
			}
		}
	}
	
	/*
	 * addNewProduct
	 * 
	 * this method is responsible for inserting a product object into list
	 * 
	 * this method will assign a product id through incrementing MAX_ID
	 */
	public int addProduct(Product product)
	{
		// All checking will be done by IdManager
		product.setProductId(productIdManager.nextIDCode());
		products.add( product.clone() );
		
		return product.getProductId();
	}
	
	
	private int searchProducts(int productId)
	{
		int index;
		boolean found = false;

		for (index = 0; index < products.size() && !found; index++)
		{
			found = (products.get(index).getProductId() == productId);
		}
		
		index--;
		
		return (found)?index:-1;
	}
	
	private int searchOrders(int orderId)
	{
		int index;
		boolean found = false;

		for (index = 0; index < orders.size() && !found; index++)
		{
			found = (orders.get(index).getOrderId() == orderId);
		}
		
		index--;
		
		return (found)?index:-1;
		
	}
	
	public Product getProduct(int productId) throws IndexOutOfBoundsException
	{
		int index = searchProducts(productId);
		
		if (index < 0)
		{
			throw new IndexOutOfBoundsException("No product with product Id: " + productId + " Exists in " + dbName + ".");
		}
		
		return products.get( index ).clone();
	}
	
	public ArrayList<Integer> lowStockpIDs()
	{
		return null;
	}
	
	
	/*
	 * updateProduct
	 * 
	 * update the product which product id is equals the parameter product's.
	 * this method try to find the product in datastub, if found update.
	 */
	public void updateProduct(Product product)
	{
		int index = searchProducts(product.getProductId());
		
		if (index >= 0)
		{
			products.set( index, product.clone( ) );
		}
		else
		{
			addProduct(product.clone());
		}
		
	}
	
	/*
	 * deleteProduct
	 * 
	 * delete the product which product id is equals the parameter product's.
	 * this method try to find the product in datastub, if found delete.
	 */
	public void deleteProduct(int productId)
	{
		int index = searchProducts(productId);
		
		if (index >= 0)
		{
			products.remove( index );
			productIdManager.recycleID( productId );
		}
		
	}
	
	public void clear()
	{
		products.clear( );
		productIdManager = new GeneralIDManager();
		orders.clear();
		orderIdManager = new GeneralIDManager();
	}
	
	/*
	 * getProductCount
	 * 
	 * this method return the total number of product in datastub
	 */
	public int getProductsCount()
	{
		return products.size( );
	}
	
	
	public void addTestProducts(){
		Product product;
		product = new Product( "Product 1", "my goods 1", 1.25, 8 );
		addProduct(product);
		
		product = new Product( "Product 2", "my goods 2", 2.50, 5 );
		addProduct(product);
		
		product = new Product( "Product 3", "my goods 3", 5, 3 );
		addProduct(product);
		
		product = new Product( "Product 4", "my goods 4", 10, 2 );
		addProduct(product);
	}
	
	/*
	 * addOrder
	 * 
	 * add an order to the database,
	 */
	
	public int addOrder(Order order){
		int orderId = orderIdManager.nextIDCode( );
		order.setOrderId( orderId );
		orders.add( order );
		return orderId;
	}
	
	/*
	 * updateOrder
	 * 
	 * update an order
	 */
	public int updateOrder(Order order){
		int orderId = order.getOrderId( );
		int index = searchOrders(orderId);
		
		if (index != -1)
		{
			orders.set(index, order);
		}

		return orderId;
	}
	
	/*
	 * getOrder
	 * 
	 * return an exact order which has specified order id
	 */
	public Order getOrder(int orderId){
		Order order = null;
		int index = searchOrders(orderId);
		
		if (index != -1)
		{
			order = orders.get(index);
		}
	
		return order;
	}
	
	/*
	 * getOrders
	 * 
	 * return all orders as an array list
	 */
	public void getOrders(ArrayList<Order> orders)
	{
		for(Order order: this.orders){
			orders.add(order);
		}
		return;
	}
	
	
	public void deleteOrder(int orderId)
	{
		int index = searchOrders(orderId);
		if (index != -1)
		{
			orders.remove( index );
		}
	}
	
	public int getOrdersCount(){
		return orders.size();
	}
}

