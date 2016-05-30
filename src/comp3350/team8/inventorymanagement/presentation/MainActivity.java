
package comp3350.team8.inventorymanagement.presentation;

import java.io.IOException;
import comp3350.team8.inventorymanagement.R;
import comp3350.team8.inventorymanagement.business.AccessProducts;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
//import comp3350.team8.inventorymanagement.application.InventoryApp;

public class MainActivity extends Activity
{
	
//	private InventoryApp	app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		checkStock();
		// setup the application object
//		app = (InventoryApp) getApplication( );
	}
	
	/*  This Is code to implement Switching between the stub and real database from within the app
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater( ).inflate( R.menu.main, menu );
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId( );
		AccessProducts dataAccess = new AccessProducts();
		
		switch (id){
			case R.id.action_use_stub:
				dataAccess.useStubDatabase();
				Messages.warning( this, "Using Stub Database" );
				checkStock();
			return true;
			
			case R.id.action_use_real:
				try { dataAccess.usePersistantDatabase();}
				catch(IOException e){
					Messages.warning(this, e.getMessage( ) );
				}
				
			checkStock();
			return true;
			
		}
		
		return super.onOptionsItemSelected( item );
	}
	
	*
	 * OnClickInventry
	 * 
	 * the event processing code for InventoryManagement button.
	 */
	// @Override
	public void OnClickInventory(View view)
	{
		// final Button button = (Button)
		// findViewById(R.id.button_start_inventory_management);
		Intent intent = new Intent( MainActivity.this,
				InventoryManagerActivity.class );
		startActivity( intent );
	}
	
	
	public void OnClickCheckout(View view){
		Intent intent = new Intent( MainActivity.this, 
				CheckoutModeActivity.class);
		startActivity( intent );
	}
	
	public void OnClickSupplier(View view){
		Intent intent = new Intent( MainActivity.this, 
				OrderManagementActivity.class);
		startActivity( intent );
	}
	
	public void checkStock()
	{	
		AccessProducts ap = new AccessProducts();
		Toast todo = Toast.makeText( getApplicationContext( ),
				"You Currently Have Low Stock", Toast.LENGTH_LONG );
		if(ap.lowItem( ))
			todo.show( );
	}
}
