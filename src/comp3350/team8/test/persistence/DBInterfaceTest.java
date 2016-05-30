
package comp3350.team8.test.persistence;

import junit.framework.TestCase;
import comp3350.team8.inventorymanagement.business.DataAccessService;
import comp3350.team8.inventorymanagement.object.Product;
import comp3350.team8.inventorymanagement.persistence.*;
import java.util.ArrayList;

public class DBInterfaceTest extends TestCase
{
	DBInterface dbInterface;
	Product product;
	
	public DBInterfaceTest(String arg0)
	{
		super( arg0 );
	}
	
	protected void setUp(){
		dbInterface = DataAccessService.createDataAccess( new DataAccessStub("stub"), "Stub" );
	}
	
	
	public void testProductListEmpty()
	{
		try{
			dbInterface.getProduct( 4 );
			fail();
		} catch (Exception e){
			
		}
	}
	
	public void testProductListSizeOne()
	{
		dbInterface.addProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		
		product = dbInterface.getProduct( 1 );
		
		assertEquals( product.getProductId( ), 1 );
		assertTrue( product.getName( ).equals( "product1" ) );
		assertTrue( product.getDescription( ).equals( "my goods 1" ) );
		assertEquals( product.getPrice( ), 1.25 );
		assertEquals( product.getQuantity( ), 2 );
	}
	
	public void testProductSequential(){
		dbInterface.addProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		dbInterface.addProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		
		ArrayList<Product> products = null;
		dbInterface.getProducts( products );
		assertNull(products);
		
		products = new ArrayList<Product>();
		dbInterface.getProducts( products );
		
		assertEquals(products.size( ), 2);
	}
	
	public void testDelete(){
		dbInterface.addProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		dbInterface.addProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		
		ArrayList<Product> products = new ArrayList<Product>();
		dbInterface.getProducts( products );
		assertEquals(products.size( ), 2);
		
		dbInterface.deleteProduct( 1 );
		products = new ArrayList<Product>();
		dbInterface.getProducts( products );
		assertEquals(products.size( ), 1);
	}
	
	public void testUpdate(){
		dbInterface.addProduct( new Product( "product1", "my goods 1", 1.25, 2 ) );
		dbInterface.addProduct( new Product( "product2", "my goods 2", 5.00, 8 ) );
		
		ArrayList<Product> products = new ArrayList<Product>();
		dbInterface.getProducts( products );
		assertEquals(products.size( ), 2);
		
		product = dbInterface.getProduct( 2 );
		product.setPrice( 10.00 );
		product.setQuantity( 4 );
		
		dbInterface.updateProduct( product );
		
		product = dbInterface.getProduct( 1 );
		assertEquals( product.getProductId( ), 1 );
		assertTrue( product.getName( ).equals( "product1" ) );
		assertTrue( product.getDescription( ).equals( "my goods 1" ) );
		assertEquals( product.getPrice( ), 1.25 );
		assertEquals( product.getQuantity( ), 2 );
		
		product = dbInterface.getProduct( 2 );
		assertEquals( product.getProductId( ), 2 );
		assertTrue( product.getName( ).equals( "product2" ) );
		assertTrue( product.getDescription( ).equals( "my goods 2" ) );
		assertEquals( product.getPrice( ), 10.00 );
		assertEquals( product.getQuantity( ), 4 );
	}
}