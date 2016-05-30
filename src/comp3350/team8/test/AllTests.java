
package comp3350.team8.test;

import comp3350.team8.test.business.*;
import comp3350.team8.test.object.*;
import comp3350.team8.test.persistence.*;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static TestSuite	suite;
	
	public static Test suite() {
		suite = new TestSuite( "All tests" );
		testProducts( );
		testOrders();
		testDatabases( );
		return suite;
	}
	
	private static void testProducts() {
		suite.addTestSuite( ProductTest.class );
		suite.addTestSuite( AccessProductsTest.class );
		suite.addTestSuite( CartTest.class );
	}
	
	public static void testOrders(){
		suite.addTestSuite( OrderTest.class );
		suite.addTestSuite( AccessOrdersTest.class );
		suite.addTestSuite( OrderBuildTest.class );
	}
	
	private static void testDatabases() {
		suite.addTestSuite( DBInterfaceTest.class );
	}
}
