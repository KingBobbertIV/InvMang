package comp3350.team8.test.business;

import comp3350.team8.inventorymanagement.business.AccessProducts;
import comp3350.team8.inventorymanagement.business.Cart;
import comp3350.team8.inventorymanagement.object.Product;
import junit.framework.TestCase;
import java.util.ArrayList;

public class CartTest extends TestCase 
{
	
	AccessProducts list;
	ArrayList<Product> aList;
	Cart cart;
	Product product;
	double cost;
	
	public CartTest(String arg0)
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
		
		cost = 0;
		
		cart = new Cart();
	}
	
	public void testEmptyCart()
	{
		cost = 0;
		cart.empty( );
		aList.clear( );
		
		assertEquals(cart.getCost( ), cost);
		
		assertEquals(cart.getList(), aList);
		
		assertFalse(cart.remove( 0 ));
	}
	
	public void testOneItem()
	{
		cost = 0;
		cart.empty( );
		aList.clear();
		
		addTestItem(1);
		
		assertEquals(cart.getCost( ), cost);
		
		assertEquals(cart.getList(), aList);
		
		assertTrue(cart.remove( 0 ));
		assertFalse(cart.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(cart.addToCart( 1 ));
		
		cart.sellAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 1);
		
		resetAccessProducts();
	}
	
	public void testTwoSameItem()
	{
		cost = 0;
		cart.empty( );
		aList.clear();
		
		addTestItem(1);
		
		addTestItem(1);
		
		assertEquals(cart.getCost( ), cost);
		
		assertEquals(cart.getList(), aList);
		
		assertTrue(cart.remove( 0 ));
		assertTrue(cart.remove( 0 ));
		assertFalse(cart.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(cart.addToCart( 1 ));
		assertNull(cart.addToCart( 1 ));
		
		cart.sellAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 0);
		
		resetAccessProducts();
	}
	
	public void testTwoDiffItem()
	{
		cost = 0;
		cart.empty( );
		aList.clear();
		
		addTestItem(1);
		
		addTestItem(2);
		
		assertEquals(cart.getCost( ), cost);
		
		assertEquals(cart.getList(), aList);
		
		assertTrue(cart.remove( 0 ));
		assertTrue(cart.remove( 0 ));
		assertFalse(cart.remove( 0 ));
		//once removed, there is no longer an item to remove
		
		assertNull(cart.addToCart( 1 ));
		assertNull(cart.addToCart( 2 ));
		
		cart.sellAll();
		
		assertEquals(list.getProductById( 1 ).getQuantity( ), 1);
		assertEquals(list.getProductById( 2 ).getQuantity( ), 7);
		
		resetAccessProducts();
	}
	
	public void testErrors()
	{
		cart.empty();
		
		assertNull(cart.addToCart( 1 ));
		assertNull(cart.addToCart( 1 ));
		
		assertEquals(cart.addToCart( 1 ), "There is not enough of this Product in stock");
		
		assertEquals(cart.addToCart( 3 ), "This Product does not exist");
		
		assertFalse(cart.remove( 6 ));
	}
	
	
	private void addTestItem(int id)
	{
		assertEquals(null, cart.addToCart( id ));
		aList.add( list.getProductById( id ) );
		cost += list.getProductById( id ).getPrice();
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
