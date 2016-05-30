package comp3350.team8.test.object;

import java.util.ArrayList;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.OrderItem;
import junit.framework.TestCase;

public class OrderTest extends TestCase{
	
	public OrderTest(String arg0)
	{
		super( arg0 );
	}
	
	public void testCreateOrderObject()
	{
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		
		Order order = new Order( items, 23.13 );
		
		assertEquals(order.getOrderItems( ), items);
		assertEquals(order.getOrderCost( ), 23.13);
	}
	
}
