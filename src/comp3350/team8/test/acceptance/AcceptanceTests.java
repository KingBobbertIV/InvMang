package comp3350.team8.test.acceptance;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AcceptanceTests
{
	public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(ManagementTests.class);
        suite.addTestSuite( CheckoutTests.class );
        return suite;
    }
}