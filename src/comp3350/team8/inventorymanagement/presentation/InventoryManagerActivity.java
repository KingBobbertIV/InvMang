
package comp3350.team8.inventorymanagement.presentation;

import comp3350.team8.inventorymanagement.R;
import comp3350.team8.inventorymanagement.object.Product;
import comp3350.team8.inventorymanagement.business.AccessProducts;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class InventoryManagerActivity extends Activity implements AdapterView.OnItemClickListener,
		DialogInterface.OnClickListener {
	
	private ListView			listView;				// listview object
	private ProductAdapter		productAdapter;		// data adapter for the
														// listview
	private AccessProducts		accessProducts;		// the product data
														// access
	private ArrayList<Product>	selectedProducts;		// selected products in
														// the listview
	private ArrayList<Product>	productList;			// list of product
														// object
	private Button				btEditProduct;
	private Button				btRemoveProduct;
	private AlertDialog			addProductDialog;
	private AlertDialog			editProductDialog;
	private AlertDialog			removeProductDialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_inventorymanager );
		
		// get first page of data
		accessProducts = new AccessProducts( );
		productList = accessProducts.getPagedProducts( "productId", 0, 10 );
		
		productAdapter = new ProductAdapter( this, productList );
		
		// Attach the adapter to a ListView
		listView = (ListView) findViewById( R.id.product_list_view );
		listView.setAdapter( productAdapter );
		
		// on select event
		listView.setOnItemClickListener( this );
		
		// get the object for three button
		btEditProduct = (Button) findViewById( R.id.button_edit_product );
		btRemoveProduct = (Button) findViewById( R.id.button_remove_product );
		
		// initialize an empty ArrayList for the selected item
		selectedProducts = new ArrayList<Product>( );
		
		// disable the edit button
		btEditProduct.setEnabled( false );
		btRemoveProduct.setEnabled( false );
	}
	
	/**
	 * onItemClick
	 * 
	 * process the click event for item in listview click
	 */
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Product product;
		boolean found;
		
		product = productAdapter.getItem( position );
		found = ( -1 != selectedProducts.indexOf( product ) );
		
		if (found) {
			view.setBackgroundColor( (product.isLow())? 0x85FFCCCC : 0x00000000 );
			selectedProducts.remove( product );
		} else {
			view.setBackgroundColor( 0x856CC0E5 );
			selectedProducts.add( product );
		}
		updateButtons( );
	}
	
	/*
	 * onClickAddProduct
	 * 
	 * this method is called on the button AddProduct is clicked.
	 */
	public void onClickAddProduct(View v) {
		
		LayoutInflater li = LayoutInflater.from( this );
		View view = li.inflate( R.layout.dialog_product_view, null );
		
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( "Add Product" );
		
		builder.setView( view );
		
		// add the cancel button
		builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener( ) {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss( );
			}
		} );
		
		// set the confirm button
		builder.setPositiveButton( "Confirm", this );
		
		// show dialog
		addProductDialog = builder.create( );
		addProductDialog.show( );
	}
	
	/*
	 * onClickEditProduct
	 * 
	 * this method is called on the button EditProduct is clicked.
	 */
	public void onClickEditProduct(View v) {
		// View view = listView.getSelectedView( );
		if (selectedProducts.size( ) == 1) {
			// get the selected product
			Product product = selectedProducts.get( 0 );
			
			// show up the dialog to process edit task
			LayoutInflater li = LayoutInflater.from( this );
			View view = li.inflate( R.layout.dialog_product_view, null );
			
			// Populate the fields with existing information
			TextView tvName = (TextView) view.findViewById( R.id.productNameEntry );
			tvName.setText( product.getName( ) );
			TextView tvDescription = (TextView) view.findViewById( R.id.productDescriptionEntry );
			tvDescription.setText( product.getDescription( ) );
			TextView tvPrice = (TextView) view.findViewById( R.id.productPriceEntry );
			tvPrice.setText( Double.toString( product.getPrice( ) ) );
			TextView tvQuantity = (TextView) view.findViewById( R.id.productQuantityEntry );
			tvQuantity.setText( Integer.toString( product.getQuantity( ) ) );
			
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setTitle( "Edit Product" );
			
			builder.setView( view );
			
			// add the cancel button
			builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener( ) {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss( );
				}
			} );
			
			// set the confirm button
			builder.setPositiveButton( "Confirm", this );
			
			// show dialog
			editProductDialog = builder.create( );
			editProductDialog.show( );
		} else {
			
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setTitle( "Message" );
			builder.setMessage( "There is no item selected." );
			
			// add the cancel button
			builder.setNeutralButton( "OK", new DialogInterface.OnClickListener( ) {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss( );
				}
			} );
			
			// show dialog
			editProductDialog = builder.create( );
			editProductDialog.show( );
			// messageDialog
		}
	}
	
	/*
	 * onClickRemoveProduct
	 * 
	 * this method is called on the button RemoveProduct is clicked.
	 */
	public void onClickRemoveProduct(View view) {
		if (selectedProducts.size( ) >= 1) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setTitle( "Confirm Remove Products" );
			
			// add the cancel button
			builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener( ) {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss( );
				}
			} );
			
			// set the confirm button
			builder.setPositiveButton( "Confirm", this );
			
			// show dialog
			removeProductDialog = builder.create( );
			removeProductDialog.show( );
		} else {
			
			showMessageDialog( "There is no item selected." );
		}
	}
	
	@Override
	public void onResume() {
		super.onResume( );
		if (productAdapter != null)
			productAdapter.notifyDataSetChanged( );
	}
	
	/*
	 * onClick
	 * 
	 * this method called when the user clicked the confirm button in add
	 * product dialog.
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		AlertDialog mydialog = (AlertDialog) dialog;
		AccessProducts accessProducts = new AccessProducts( );
		String name;
		String description;
		int quantity;
		double price;
		Product product = null;
		
		// get the user input data, and create a product object
		
		// If the remove button is pressed, there is no need to try and get
		// input from a non existent field,
		// However if it is one of the other two the else statement will be
		// called and setup for the other two methods
		if (mydialog == removeProductDialog) {
			for (int index = 0; index < selectedProducts.size( ); index++) {
				product = selectedProducts.get( index );
				accessProducts.deleteProduct( product );
			}
			
		} else {
			name = getStringContent( mydialog, R.id.productNameEntry );
			description = getStringContent( mydialog, R.id.productDescriptionEntry );
			price = getNumericContent( mydialog, R.id.productPriceEntry );
			price = new BigDecimal( price ).setScale( 2, RoundingMode.HALF_UP ).doubleValue( );
			quantity = (int) getNumericContent( mydialog, R.id.productQuantityEntry );
			
			if (name == null || description == null || price == -1 || quantity == -1) {
				showMessageDialog( "All the fields must have an entry" );
				return;
			}
			
			product = new Product( name, description, price, quantity );
		}
		// This section either updates the product information gathered or adds
		// a new one to the list
		if (mydialog == addProductDialog) {
			// insert product object
			accessProducts.insertProduct( product );
		} else if (mydialog == editProductDialog) {
			product.setProductId( selectedProducts.get( 0 ).getProductId( ) );
			accessProducts.updateProduct( product );
		}
		
		// refresh the product ListView
		clearSelectedPosition( );
		
		productList = accessProducts.getPagedProducts( "productId", 0, 10 );
		listView.setAdapter( productAdapter );
		productAdapter.clear( );
		productAdapter.addAll( productList );
		productAdapter.notifyDataSetChanged( );
		updateButtons( );
		
	}
	
	private void clearSelectedPosition() {
		selectedProducts.clear( );
	}
	
	private void showMessageDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( "Message" );
		builder.setMessage( message );
		
		// add the cancel button
		builder.setNeutralButton( "OK", new DialogInterface.OnClickListener( ) {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss( );
			}
		} );
		
		// show dialog
		editProductDialog = builder.create( );
		editProductDialog.show( );
	}
	
	// The following methods were written as part of the refactoring process,
	// the functionality
	// has remained exactly the same but the duplicate code has now been removed
	
	private String getStringContent(DialogInterface dialog, int id) {
		EditText tv;
		AlertDialog mydialog = (AlertDialog) dialog;
		tv = (EditText) mydialog.findViewById( id );
		
		if (tv.getText( ).toString( ).equals( "" )) {
			return null; 
		}
		return tv.getText( ).toString( );
	}
	
	private double getNumericContent(DialogInterface dialog, int id) {
		EditText tv;
		AlertDialog mydialog = (AlertDialog) dialog;
		tv = (EditText) mydialog.findViewById( id );
		double returnValue;
		try {
			returnValue = Double.parseDouble( tv.getText( ).toString( ) );
		}
		catch (Exception e) {
			returnValue = -1;
		}
		
		return returnValue;
	}
	
	private void updateButtons() {
		if (selectedProducts.size( ) == 1) {
			btEditProduct.setEnabled( true );
		} else {
			btEditProduct.setEnabled( false );
		}
		if (selectedProducts.size( ) == 0) {
			btEditProduct.setEnabled( false );
			btRemoveProduct.setEnabled( false );
		} else {
			btRemoveProduct.setEnabled( true );
		}
		return;
	}
}
