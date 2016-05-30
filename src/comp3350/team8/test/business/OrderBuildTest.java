package comp3350.team8.test.business;

import comp3350.team8.inventorymanagement.business.AccessProducts;
import comp3350.team8.inventorymanagement.business.OrderBuild;
import comp3350.team8.inventorymanagement.object.Product;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.OrderItem;

import junit.framework.TestCase;
import java.util.ArrayList;

public class OrderBuildTest extends TestCase 
{
	
	AccessProducts list;
	ArrayList<Product> aList;
	ArrayList<OrderItem> oList;
	OrderBuild order;
	Order orderObj;
	Product product;
	double cost;
	
	public OrderBuildTest(String arg0)
	{
		super( arg0 );
	}
	
	protected void setUp()
	{
		list = new AccessProducts();
		list.useStubDatabase( );
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		list.insertProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		
		aList = new ArrayList<Product>();
		oList = new ArrayList<OrderItem>();
		
		cost = 0;
		
		order = new OrderBuild();
		orderObj = new Order (oList, 0.00);
	}
	
	public void testEmptyOrder()
	{
		cost = 0;
		order.empty( );
		aList.clear( );
		
		assertEquals(order.getPrice( ), cost);
		
		assertEquals(order.getList(), aList);
		
		assertFalse(order.remove( 0 ));
		
		assertEquals(order.build(), orderObj);
	}
	
	public void testOneItem()
	{
		cost = 0;
		order.empty( );
		aList.clear();
		
		addTestItem(1,1);
		
		assertEquals(order.getPrice( ), cost);
		
		assertEquals(order.getList(), aList);
		
		assertTrue(order.remove( 0 ));
		assertFalse(order.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(order.addToOrder( 1, 1 ));

		assertEquals(order.build(), orderObj);
		
		order.buyAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 3);
		
		resetAccessProducts();
	}
	
	public void testTwoSameItemTwoAdds()
	{
		cost = 0;
		order.empty( );
		aList.clear();
		
		addTestItem(1,1);
		
		addTestItem(1,1);
		
		assertEquals(order.getPrice( ), cost);
		
		assertEquals(order.getList(), aList);
		
		assertTrue(order.remove( 0 ));
		assertFalse(order.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(order.addToOrder( 1, 1 ));
		assertNull(order.addToOrder( 1, 1 ));

		assertEquals(order.build(), orderObj);
		
		
		order.buyAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 4);
		
		resetAccessProducts();
	}
	
	public void testTwoSameItemOneAdd()
	{
		cost = 0;
		order.empty( );
		aList.clear();
		
		addTestItem(1,2);
		
		assertEquals(order.getPrice( ), cost);
		
		assertEquals(order.getList(), aList);
		
		assertTrue(order.remove( 0 ));
		assertFalse(order.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(order.addToOrder( 1, 2 ));

		assertEquals(order.build(), orderObj);
		
		
		order.buyAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 4);
		
		resetAccessProducts();
	}
	
	public void testTwoDiffItem()
	{
		cost = 0;
		order.empty( );
		aList.clear();
		
		addTestItem(1,1);
		
		addTestItem(2,1);
		
		assertEquals(order.getPrice( ), cost);
		
		assertEquals(order.getList(), aList);
		
		assertTrue(order.remove( 0 ));
		assertTrue(order.remove( 0 ));
		assertFalse(order.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(order.addToOrder( 1 ,1 ));
		assertNull(order.addToOrder( 2, 1 ));
		
		assertEquals(order.build(), orderObj);
		
		
		order.buyAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 3);
		assertEquals(list.getProductById( 2 ).getQuantity( ), 9);
		
		resetAccessProducts();
	}
	
	public void testBuild(){
		order = new OrderBuild(orderObj);
		
		assertEquals( orderObj, order.build() );
	}
	
	public void testErrors()
	{
		order.empty();
		
		assertEquals(order.addToOrder( 3, 1 ), "This Product does not exist");
		
		assertFalse(order.remove( 6 ));
	}
	
	
	private void addTestItem(int id, int quant)
	{
		

		if(!order.has( id )){
			
			aList.add( list.getProductById( id ) );
			aList.get( aList.size()-1 ).setQuantity( quant );
			oList.add( new OrderItem(id, quant) );
			
			} else{
			
				int x = 0;
				while(x < aList.size() && !aList.get( x ).equals(list.getProductById( id ))){ x++;}
				aList.get( x ).setQuantity( aList.get( x ).getQuantity() + quant );
				oList.get( x ).addItems( new OrderItem(id, quant));
			
			}
		
		assertNull(order.addToOrder( id, quant ));
		cost += list.getProductById( id ).getCost()*quant;
		
		orderObj = new Order(oList, cost);
		
	}
	
	private void resetAccessProducts()
	{

		Product product = list.getProductById(1);
		product.setQuantity( 2 );
		list.updateProduct(product);
		

		product = list.getProductById(2);
		product.setQuantity( 8 );
		list.updateProduct(product);
		
	}
	
}
