
package comp3350.team8.test.object;


import junit.framework.TestCase;

import comp3350.team8.inventorymanagement.object.*;

public class ProductTest extends TestCase
{
	
	public ProductTest(String arg0)
	{
		super( arg0 );
	}
	
	public void assertPositive(double qnty)
	{
		if (qnty < 0)
		{
			fail( );
		}
	}
	
	public void test()
	{
		Product pro = new Product( 1, "product1", "my goods", 1.25, 8 );
		assertEquals( pro.getProductId( ), 1 );
		assertTrue( pro.getName( ).equals( "product1" ) );
		assertTrue( pro.getDescription( ).equals( "my goods" ) );
		assertEquals( pro.getPrice( ), 1.25 );
		assertEquals( pro.getQuantity( ), 8 );
	}
	
	public void testNegativeQuantity()
	{
		Product pro = new Product( 1, "product1", "my goods", 1.25, -8 );
		assertEquals( pro.getProductId( ), 1 );
		assertTrue( pro.getName( ).equals( "product1" ) );
		assertTrue( pro.getDescription( ).equals( "my goods" ) );
		assertEquals( pro.getPrice( ), 1.25 );
		assertPositive( pro.getQuantity( ) );
	}
	
	public void testNegativePrice()
	{
		Product pro = new Product( 1, "product1", "my goods", -1.25, 8 );
		assertEquals( pro.getProductId( ), 1 );
		assertTrue( pro.getName( ).equals( "product1" ) );
		assertTrue( pro.getDescription( ).equals( "my goods" ) );
		assertPositive( pro.getPrice( ) );
		assertEquals( pro.getQuantity( ), 8 );
	}
	
	public void testClone()
	{
		Product pro = new Product( 1, "product1", "my goods", 1.25, 8 );
		Product pro2 = (Product) pro.clone( );
		
		pro2.setName( "newname" );
		pro2.setDescription( "newgoods" );
		pro2.setQuantity( 5 );
		pro2.setPrice( 2.50 );
		
		//test the address of not equal
		assertTrue(pro != pro2);
		
		//test the content of object
		assertEquals( pro.getProductId( ), 1 );
		assertTrue( pro.getName( ).equals( "product1" ) );
		assertTrue( pro.getDescription( ).equals( "my goods" ) );
		assertEquals( pro.getPrice( ), 1.25 );
		assertEquals( pro.getQuantity( ), 8 );
		
		assertEquals( pro2.getProductId( ), 1 );
		assertTrue( pro2.getName( ).equals( "newname" ) );
		assertTrue( pro2.getDescription( ).equals( "newgoods" ) );
		assertEquals( pro2.getPrice( ), 2.50 );
		assertEquals( pro2.getQuantity( ), 5 );
	}
	
	/*
	 * testEquals
	 * 
	 * the two products is equals if only if the product id is same.
	 * because the upper level may changed the content of the product.
	 */
	
	public void testEquals()
	{
		Product pro;
		Product pro2;
		
		pro = new Product( 1, "product1", "my goods 1", 2.25, 1 );
		pro2 = new Product( 1, "product2", "my goods 2", 1.25, 8 );
		assertTrue(pro.equals( pro2 ));
		
		pro2 = new Product( 2, "product2", "my goods 2", 1.25, 8 );
		assertFalse(pro.equals( pro2 ));
		
	}
	
	public void testLowStock(){
		Product pro;
		Product pro2;
		
		pro = new Product( 1, "product1", "my goods 1", 2.25, 1 );
		pro2 = new Product( 1, "product2", "my goods 2", 1.25, 8 );
		
		pro.setLowStock(2);
		assertTrue(pro.isLow( ));
		pro2.setLowStock( 8 );
		assertTrue( pro2.isLow( ) );
	}
}
