package comp3350.team8.inventorymanagement.presentation;

import java.util.ArrayList;
import comp3350.team8.inventorymanagement.R;
import comp3350.team8.inventorymanagement.business.AccessOrders;
import comp3350.team8.inventorymanagement.business.AccessProducts;
import comp3350.team8.inventorymanagement.business.OrderBuild;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.OrderItem;
import comp3350.team8.inventorymanagement.object.Product;
import android.R.anim;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



public class OrderManagementActivity extends Activity{
	
	private	ArrayList<Order> 				orders; 
	private	ArrayAdapter<Order> 			orderArrayAdapter;
	private OrderBuild						orderBuild;
	private ArrayList<Product>				itemsInOrder;
	private ArrayAdapter<Product>			itemsInOrderAA; //AA is ArrayAdapter to keep variable names short
	
	private AccessProducts					accessProducts;
	private AccessOrders					accessOrders;
	private Order							currentOrder;
	
	private int 							selectedOrderPosition = -1; //-1 Represents Not Selected
	private int 							selectedItemPosition = -1; //-1 Represents Not Selected
	
	private View 							prevOrderView;
	private View							prevItemView;
	private View							currSelectedOrder;
	
	private EditText						mainItemEdit;
	private EditText						subItemEdit;
	private TextView						mainItemView;
	private TextView						subItemView;
	
	private Button							btAddItem;
	private Button							btApplyEdit;
	private Button							btRemove;
	
	private int								displayStatus = 0; //0 is Neither, 1 is top, 2 is bottom
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_ordermanagement );
		
		accessProducts = new AccessProducts( );
		accessOrders = new AccessOrders( );
		orders = accessOrders.getOrders( );
		itemsInOrder = new ArrayList<Product>();
		
		//The following fields will be updating dynamically depending on what the user has pressed
		// or what the program considers to be the 'active' display
		
		mainItemEdit = (EditText) findViewById(R.id.mainItemEditText);
		subItemEdit = (EditText) findViewById(R.id.subItemEditText);
		
		mainItemView = (TextView) findViewById(R.id.mainItemTextView);
		subItemView = (TextView) findViewById(R.id.subItemTextView);
		
		btAddItem = (Button) findViewById( R.id.addItem );
		btApplyEdit = (Button) findViewById( R.id.applyEdit );
		btRemove = (Button) findViewById( R.id.removeItem );
		
		updateInterface( 0 );
		
		//Populate the first array adapter
		orderArrayAdapter = new ArrayAdapter<Order>
		(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, orders) {
			@Override
			public View getView( int position, View convertView, ViewGroup parent) { 
				View view = super.getView( position, convertView, parent );
				
				TextView text1 = (TextView) view.findViewById( android.R.id.text1 );
				TextView text2 = (TextView) view.findViewById( android.R.id.text2 );
				
				text1.setText( "" + orders.get( position ).getOrderId( ) );
				text2.setText("");// TODO: Fix the cost function         + orders.get( position ).getOrderCost( ) );
				return view;
				
			}
		};
		
		//Setup the second array adapter
		itemsInOrderAA = new ArrayAdapter<Product>
		(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, itemsInOrder) {
			
			@Override
			public View getView( int position, View convertView, ViewGroup parent) { 
				View view = super.getView( position, convertView, parent );
				
				TextView text1 = (TextView) view.findViewById( android.R.id.text1 );
				TextView text2 = (TextView) view.findViewById( android.R.id.text2 );
				
				text1.setText( itemsInOrder.get( position ).getName( ) );
				text2.setText( itemsInOrder.get( position ).getQuantity( ) + " for "
						+ itemsInOrder.get( position ).getPrice( ) + "$ each");
				return view;
				
			}
			
		};
		
		//Make the Adapters Active
		final ListView orderView = (ListView) findViewById(R.id.orderView);
		orderView.setAdapter( orderArrayAdapter );
		
		final ListView itemView = (ListView) findViewById(R.id.orderedProductsView);
		itemView.setAdapter( itemsInOrderAA );
		
		//Setup the click listener for the first array adapter
		orderView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				//Assign the currently selected order to a variable for management outside this function
				currSelectedOrder = view;
				
				//If the position was already selected, and it is being tapped again (deselect)
				if(position == selectedOrderPosition){
					orderView.setItemChecked( position, false );
					selectedOrderPosition = -1;	
					currSelectedOrder = null;
					//Nothing is selected anymore now reflect that on the display
					view.setBackgroundColor( 0x00000000 );
					
					//Remove all items from the ArrayList the second ArrayAdapter is using and update it
					itemsInOrder.clear( );
					itemsInOrderAA.notifyDataSetChanged( );
					
					//Deselect any items in the lower pain (providing they exist)
					if(prevItemView != null){ 
						prevItemView.setBackgroundColor( 0x00000000 );
					}
					
					//Set interface accordingly
					updateInterface(0);
				} 
				
				//The position was not already selected, so select it and update the second array adapter
				else {
					orderView.setItemChecked( position, true );
					
					//Set the previously selected item to be de-selected
					if(selectedOrderPosition != -1){
						prevOrderView.setBackgroundColor( 0x00000000 );
					}
					//Log what order is selected to response calculation for next click
					selectedOrderPosition = position;
					view.setBackgroundColor( 0x856CC0E5 );
					//Assign current view to variable to reset on next click
					prevOrderView = view;
					
					//Set interface accordingly - top is active section
					updateInterface(1);
					if(prevItemView != null){ //Deselect any items in the lower pain (providing they exist)
						prevItemView.setBackgroundColor( 0x00000000 );
					}
					
					
					//Update second ArrayAdapter
					//This may have caused a bug- possibly undo later
					updateSecondAA( );
				}
			}
		} );
		
		itemView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//If the position was already selected, and it is being tapped again
				if(position == selectedItemPosition){
					
					//Uncheck tapped item
					itemView.setItemChecked( position, false );
					selectedItemPosition = -1;	
					
					//The bottom ArrayAdapter isn't selected anymore so return control to the top one
					view.setBackgroundColor( 0x00000000 );
					prevOrderView.setBackgroundColor( 0x856CC0E5 );
					updateInterface(1);
				}
				//The position was not already selected, update the bottom text fields
				else {
					itemView.setItemChecked( position, true );
				
					//If there was an item selected previously, set its background color to default
					if(selectedItemPosition != -1){
						prevItemView.setBackgroundColor( 0x00000000 );
					}
					
					//Select the new item and update the display
					selectedItemPosition = position;
					view.setBackgroundColor( 0x856CC0E5 );
					prevItemView = view;
					
					prevOrderView.setBackgroundColor( Color.LTGRAY );
					
					updateInterface(2);
					
				}
			
				
				
			}
		} );	
	}
	
	private void updateInterface(int setDisplayStatus){
		displayStatus = setDisplayStatus;
		
		setButtons();
		setTextFields();
		setEntryFields();
		
	}
	
	
	public void onClickAddButtonOM(View v){
		
		//Top half is active: New button creates 
		if(displayStatus == 0){
			orderBuild = new OrderBuild();
			accessOrders.insertOrder( orderBuild.build( ) );
			orders.clear( );
			orders.addAll( accessOrders.getOrders( ) );
			orderArrayAdapter.notifyDataSetChanged( );
		}
		//Bottom half is active, either with an item in the top half selected or an item in 
		//the bottom half selected. So now create a new product in the order instead.
		else {
			//The following code gets the input
			int inputtedProductID = -1;
			int inputtedAmount = -1;
			String getProductId = mainItemEdit.getText( ).toString();
			String getAmount = subItemEdit.getText( ).toString( );
			try{
				inputtedProductID = Integer.parseInt( getProductId );
				inputtedAmount = Integer.parseInt( getAmount );
			} catch (Exception e){
				Messages.warning( this, "Invalid ID, please enter a number" );
				return;
			}
			
			
			Product toInsert = accessProducts.getProductById( inputtedProductID );
			orders.get( selectedOrderPosition ).insert( toInsert, inputtedAmount );

			accessOrders.updateOrder( orders.get( selectedOrderPosition ) );
			orderArrayAdapter.notifyDataSetChanged( );
			updateSecondAA( );
		}
	}
	
	public void onClickRemoveButtonOM(View v){
		if(displayStatus == 1){

			prevOrderView.setBackgroundColor( 0x00000000 );
//			selectedOrderPosition = -1;
			accessOrders.deleteOrder( orders.get( selectedOrderPosition ).getOrderId( ) ); //Because the database starts at 1 it needs 1 added to it
			orders.remove( selectedOrderPosition );
			orderArrayAdapter.notifyDataSetChanged( );
			itemsInOrderAA.clear( );
			itemsInOrderAA.notifyDataSetChanged( );
			updateInterface( 0 );
		} 
		else {
			
			orders.get( selectedOrderPosition ).remove( selectedItemPosition );
			accessOrders.updateOrder( orders.get( selectedOrderPosition ) );
			updateSecondAA();
			
		}
	}
	
	
	public void onClickAcceptButtonOM(View v){
		OrderBuild addToInventory = new OrderBuild( orders.get( selectedOrderPosition ) );
		addToInventory.buyAll( );
//		orders.remove( selectedOrderPosition );
		orderArrayAdapter.notifyDataSetChanged( );
		itemsInOrderAA.clear( );
		itemsInOrderAA.notifyDataSetChanged( );
		onClickRemoveButtonOM( v );
		Messages.message( this, "Order contents added to inventory" );
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//The following methods just update the dynamic components of the display
	private void updateFirstAA(){
		
	}
	
	private void updateSecondAA(){
		orderBuild = new OrderBuild( orders.get( selectedOrderPosition ) );
		itemsInOrder.clear( );
		itemsInOrder.addAll(  orderBuild.getList( ) );
		itemsInOrderAA.notifyDataSetChanged( );		
	}
	
	private void setButtons(){
		
		switch(this.displayStatus){
			case 0:
				btAddItem.setText( "New Order" );
				btAddItem.setEnabled( true );
				//btApplyEdit.setText( "Edit" );
				btApplyEdit.setEnabled( false );
				//btRemove.setText( "Remove" );
				btRemove.setEnabled( false );
				break;
			case 1: 
				btAddItem.setText( "Add Product" );
				btAddItem.setEnabled( true );
				btApplyEdit.setText( "Add to Inventory" );
				btApplyEdit.setEnabled( true );
				btRemove.setText( "Remove Order" );
				btRemove.setEnabled( true );
				break;
			case 2:
				btAddItem.setText( "Add Product" );
				btAddItem.setEnabled( true );
				btApplyEdit.setText( "" );
				btApplyEdit.setEnabled( false );
				btRemove.setText( "Remove from Order" );
				btRemove.setEnabled( true );
				break;
		}	
	}
	private void setEntryFields(){
		switch(this.displayStatus){
			case 0:
				mainItemEdit.setText( "" );
				subItemEdit.setText( "" );
				subItemEdit.setEnabled( false );
				mainItemEdit.setEnabled( false );
				break;
			case 1:
				mainItemEdit.setText( "" );
				subItemEdit.setText( "" );
				subItemEdit.setEnabled( true );
				mainItemEdit.setEnabled( true );
				break;
			case 2:
				mainItemEdit.setText( "" + itemsInOrder.get(selectedItemPosition).getProductId( ) );
				subItemEdit.setText( "" + itemsInOrder.get(selectedItemPosition).getQuantity( ) );
				subItemEdit.setEnabled( true );
				mainItemEdit.setEnabled( true );
				break;
		}
	}
	private void setTextFields(){
		
		switch(this.displayStatus){
			case 0:
				mainItemView.setText( "" );
				subItemView.setText( "" );
				break;
			case 1:
			case 2:
				mainItemView.setText( "ID" );
				subItemView.setText( "Amount" );
				break;
		}
	}

	
}
