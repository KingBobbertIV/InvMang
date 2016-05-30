package comp3350.team8.inventorymanagement.business;

import java.util.ArrayList;
import comp3350.team8.inventorymanagement.persistence.DBInterface;
import comp3350.team8.inventorymanagement.persistence.DataAccessStub;
import comp3350.team8.inventorymanagement.object.Order;

public class AccessOrders {
	private static DBInterface		dataAccess;
	
	/*
	 * constructor
	 * 
	 * use the database access stub
	 */
	public AccessOrders()
	{
		try{
			dataAccess = DataAccessService.getDataAccess( );
		} catch (Exception e){
			dataAccess =  DataAccessService.createDataAccess(new DataAccessStub("dbName"), "dbName");
		}
	}
	

	/*
	 * constructor
	 * 
	 * use the hsqldb database
	 */
	public AccessOrders(String name){
		try{
			dataAccess = DataAccessService.getDataAccess( );
		} catch (Exception e){
			dataAccess =  DataAccessService.createDataAccess();
		}
	}
	
	/*
	 * addOrder
	 * 
	 * add a order 
	 */
	public int insertOrder(Order order)
	{
		return dataAccess.addOrder( order );
	}
	
	public void deleteOrder(int orderID)
	{
		dataAccess.deleteOrder( orderID );
	}
	
	
	
	/*
	 * getOrder
	 * 
	 * get a order by specific order id 
	 */
	public Order getOrder(int orderId){
		return dataAccess.getOrder( orderId );
	}
	
	/*
	 * updateOrder
	 * 
	 * update a order by specific order instance 
	 */
	public int updateOrder(Order order){
		return dataAccess.updateOrder( order );
	}
	
	/*
	 * getOrders
	 * 
	 * update a order by specific order instance 
	 */
	public ArrayList<Order> getOrders(){
		ArrayList<Order> orders = new ArrayList<Order>( );
		dataAccess.getOrders( orders );
		return orders;
	}
}
