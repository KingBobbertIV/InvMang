package comp3350.team8.test.business;

import java.util.ArrayList;
import comp3350.team8.inventorymanagement.business.AccessOrders;
import comp3350.team8.inventorymanagement.business.AccessProducts;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.OrderItem;
import junit.framework.TestCase;


public class AccessOrdersTest extends TestCase{
	AccessOrders	accessOrders;
	ArrayList<Order> orderlist;
	Order			order;
	
	public AccessOrdersTest(String arg0)
	{
		super( arg0 );
	}
	
	protected void setUp()
	{
		accessOrders = new AccessOrders( "hsqldb" );

		orderlist = new ArrayList<Order>();
		
		//create an order used for test
		ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add( new OrderItem(0, 1) );
		orderItems.add( new OrderItem(1, 2) );
		order = new Order(orderItems, 20.12);
	}
	
	public void testInsertGetOrder(){
		//insert an order
		int orderId = accessOrders.insertOrder(order);
		
		assertEquals(orderId, order.getOrderId());
		
		//get back the order which was inserted
		Order retrivedOrder = accessOrders.getOrder(orderId);
		
		//assert compare		
		assertTrue(order.equals(retrivedOrder));
	}
	
	public void testUpdateOrder(){
		int orderId = accessOrders.insertOrder(order);
		Order order = accessOrders.getOrder(orderId);
		ArrayList<OrderItem> items = order.getOrderItems( );
		
		//modify the order
		items.add( new OrderItem(2, 3) );
		order.setOrderItems( items );
		
		//update order
		accessOrders.updateOrder(order);
		
		//get order which is updated
		Order retrivedOrder = accessOrders.getOrder(orderId);
		
		//assert compare		
		assertEquals(order, retrivedOrder);
	}
	
	public void testGetOrders(){
		ArrayList<Order> orders = new ArrayList<Order>();
		
		//get all orders
		orders = accessOrders.getOrders();
		
		//compare each one
		for (Order order:orders){
			Order retriveOrder = accessOrders.getOrder( order.getOrderId() );
			if (! order.equals(retriveOrder)){
				System.out.println("not same ");
				System.out.println("myorder : " + order);
				System.out.println("retrive order : " + retriveOrder);
				System.out.println();
			}else{
				System.out.println("same");
			}
			assertEquals(order, retriveOrder);
		}
	}
}
