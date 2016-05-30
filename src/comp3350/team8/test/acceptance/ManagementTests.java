package comp3350.team8.test.acceptance;

import junit.framework.Assert;
import com.robotium.solo.Solo;
import comp3350.team8.inventorymanagement.business.*;
import comp3350.team8.inventorymanagement.presentation.*;
import comp3350.team8.inventorymanagement.persistence.*;
import android.test.ActivityInstrumentationTestCase2;

public class ManagementTests extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	
	public ManagementTests(){
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
	
	public void testAddProduct(){
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
		
		solo.waitForActivity( "InventoryManagerActivity" );
		Assert.assertTrue( solo.searchText( "Product1" ) );
		solo.clickOnText( "Product1" );
		solo.clickOnButton( "Remove" );
		solo.clickOnButton( "Confirm" );
	}
	
	public void testEditProduct(){
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
		
		solo.waitForActivity( "InventoryManagerActivity" );
		Assert.assertTrue( solo.searchText( "Product1" ) );
		solo.clickOnText( "Product1" );
		solo.clickOnButton( "Edit" );
		
		Assert.assertTrue(solo.searchEditText("Product1"));
		Assert.assertTrue(solo.searchEditText("5"));
		Assert.assertTrue(solo.searchEditText("first product"));
		
		solo.clearEditText(0);
		solo.enterText(0, "Product2");
		solo.clearEditText(2);
		solo.enterText(2, "10");
		solo.clickOnButton( "Confirm" );
		
		solo.waitForActivity( "InventoryManagerActivity" );
		Assert.assertTrue( solo.searchText( "Product2" ) );
		Assert.assertTrue( solo.searchText( "10" ) );
		solo.clickOnText( "Product2" );
		solo.clickOnButton( "Remove" );
		solo.clickOnButton( "Confirm" );
	}
}