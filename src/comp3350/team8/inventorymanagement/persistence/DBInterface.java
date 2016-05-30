package comp3350.team8.inventorymanagement.persistence;

import comp3350.team8.inventorymanagement.object.*;
import java.util.ArrayList;

public interface DBInterface {
	
	public void open(String path);
	
	public void close();
	
	public int addProduct(Product newProduct); // adds the product to the database & returns productId
	
	public Product getProduct(int productId);
	
	public void getProducts(ArrayList<Product> productResult);
	
	public int getProductsCount(); // count the number of types of products in the store
	
	public void recordPurchase(int productID, int numberSold) throws Exception;
	
	public ArrayList<Integer> lowStockpIDs();
	
	public void updateProduct(Product product);
	
	public void deleteProduct(int productId);
	
	//public void clear();
	
	public int addOrder(Order order);
	
	public Order getOrder(int orderId);
	
	public void getOrders(ArrayList<Order> list);
	
	public int getOrdersCount();
	
	public int updateOrder(Order order);
	
	public void deleteOrder(int orderID);
	
	public Exception retrieveDBError();
}
