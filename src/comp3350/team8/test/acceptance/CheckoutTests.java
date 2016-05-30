package comp3350.team8.test.acceptance;

import junit.framework.Assert;
import com.robotium.solo.Solo;
import comp3350.team8.inventorymanagement.business.*;
import comp3350.team8.inventorymanagement.presentation.*;
import comp3350.team8.inventorymanagement.persistence.*;
import android.test.ActivityInstrumentationTestCase2;

public class CheckoutTests extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public CheckoutTests(){
		super(MainActivity.class);
	}
	
	public void setUp() throws Exception
	{
		solo = new Solo(getInstrumentation(), getActivity());
		
		// Disable this for full acceptance test
		// System.out.println("Injecting stub database.");
		 DataAccessService.createDataAccess(new DataAccessStub("Stub"), "Stub");
	}
	
	@Override
	public void tearDown() throws Exception
	{
		solo.finishOpenedActivities();
	}
	
	public void testCheckoutOneItem(){
		solo.waitForActivity("MainActivity");
		solo.clickOnButton( "Inventory Management" );
		solo.assertCurrentActivity("Expected activity InventoryManagerActivity", "InventoryManagerActivity");
		
		solo.clickOnButton("Add Product");
		Assert.assertTrue(solo.searchText("Add Product"));
		solo.enterText( 0, "Product1" );
		solo.enterText( 1, "32" );//price
		solo.enterText( 2, "5" );//amount
		solo.enterText( 3, "first product" );
		solo.clickOnButton( "Confirm" );
		
		solo.goBack( );
		
		solo.waitForActivity("MainActivity");
		solo.clickOnButton( "Checkout Mode" );
		solo.assertCurrentActivity("Expected activity CheckoutModeActivity", "CheckoutModeActivity");
		
		solo.enterText( 0, "0" );
		solo.clickOnButton( "Enter" );
		Assert.assertTrue(solo.searchText("Product1"));
		solo.clickOnButton( "Checkout" );
		Assert.assertTrue(solo.searchText("All Items Sold"));
		solo.clickOnButton( "OK" );
		
		solo.goBack( );
		
		solo.waitForActivity("MainActivity");
		solo.clickOnButton( "Inventory Management" );
		solo.assertCurrentActivity("Expected activity InventoryManagerActivity", "InventoryManagerActivity");
		
		Assert.assertTrue(solo.searchText("Product1"));
		Assert.assertTrue(solo.searchText("4"));//new quantity
		solo.clickOnText( "Product1" );
		solo.clickOnButton( "Remove" );
		solo.clickOnButton( "Confirm" );
	}
	
	
}