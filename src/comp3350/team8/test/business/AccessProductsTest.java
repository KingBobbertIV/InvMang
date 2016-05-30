
package comp3350.team8.test.business;

import comp3350.team8.inventorymanagement.business.AccessProducts;
import comp3350.team8.inventorymanagement.object.Product;
import junit.framework.TestCase;
import java.util.ArrayList;

public class AccessProductsTest extends TestCase
{
	
	AccessProducts	list;
	ArrayList<Product> plist;
	Product			product;
	
	public AccessProductsTest(String arg0)
	{
		super( arg0 );
	}
	
	protected void setUp()
	{
		list = new AccessProducts( );
		list.useStubDatabase( );
		plist = new ArrayList<Product>();
		product = null;
	}
	
	public void testProductListSizeOne()
	{
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		list.getProducts( );
		assertEquals( list.getSize( ), 1 );
		
		product = list.getProductById( 1 );
		
		assertEquals( product.getProductId( ), 1 );
		assertTrue( product.getName( ).equals( "product1" ) );
		assertTrue( product.getDescription( ).equals( "my goods 1" ) );
		assertEquals( product.getPrice( ), 1.25 );
		assertEquals( product.getQuantity( ), 2 );
		// assertEqual();
	}
	
	public void testProductListEmpty()
	{
		assertEquals( null, list.getProductById( 8 ) );
	}
	
	public void testProductListSizeTwo()
	{
		
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 1 );
		list.insertProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 2 );
		
		product = list.getProductById( 1 );
		
		assertEquals( product.getProductId( ), 1 );  //the product id is assigned by DataStub 
		assertTrue( product.getName( ).equals( "product1" ) );
		assertTrue( product.getDescription( ).equals( "my goods 1" ) );
		assertEquals( product.getPrice( ), 1.25 );
		assertEquals( product.getQuantity( ), 2 );
		
		product = list.getProductById( 2 );
		
		assertEquals( product.getProductId( ), 2 );
		assertTrue( product.getName( ).equals( "product2" ) );
		assertTrue( product.getDescription( ).equals( "my goods 2" ) );
		assertEquals( product.getPrice( ), 5.00 );
		assertEquals( product.getQuantity( ), 8 );
	}
	
	public void testInvalidGetProductsCalls()
	{
		ArrayList<Product> list2 = new ArrayList<Product>( );
		
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		list.getProducts( );
		assertEquals( list.getSize( ), 1 );
		list.insertProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		list.getProducts( );
		assertEquals( list.getSize( ), 2 );
		
		list2 = list.getPagedProducts( "productId", 1, 4 );
		assertEquals(list2.size( ), 1);
		
		list2 = list.getPagedProducts( "productId", 0, 1 );
		assertEquals(list2.size( ), 1);
		
		list2 = list.getPagedProducts( "productId", 0, 0 );
		assertEquals(list2.size( ), 0);
		
		list2 = list.getPagedProducts( "productId", 0, -3 );
		assertTrue( list2.isEmpty( ) );
	}
	
	public void testUpdate(){
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 1 );
		list.insertProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 2 );
		
		product = list.getProductById( 1 );
		product.setName( "product3" );
		list.updateProduct( product );
		
		product = list.getProductById( 1 );
		
		assertTrue( product.getName( ).equals( "product3" ) );
		assertEquals( product.getDescription( ), "my goods 1" );
		assertEquals( product.getPrice( ), 1.25 );
		assertEquals( product.getQuantity( ), 2 );
	}
	
	public void testDelete(){
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 1 );
		list.insertProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 2 );
		
		product = list.getProductById( 1 );
		list.deleteProduct( product );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 1 );
	}
	
	public void testLowStock(){
		list.insertProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 1 );
		list.insertProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		plist = list.getProducts( );
		assertEquals( plist.size( ), 2 );
		
		product = list.getProductById( 1 );
		product.setLowStock( 6 );
		list.updateProduct( product );
		assertTrue(list.lowItem( ));
	}
}
