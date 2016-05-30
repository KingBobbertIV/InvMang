
package comp3350.team8.inventorymanagement.presentation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import comp3350.team8.inventorymanagement.R;
import comp3350.team8.inventorymanagement.business.Cart;
import comp3350.team8.inventorymanagement.object.Product;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CheckoutModeActivity extends Activity {
	
	final private double 		PST = 0.08;
	final private double		GST = 0.05;
	
	private Button				btAddToCheckout;
	private Button				btRemoveFromCheckout;
	private TextView			subTotal;
	private TextView			pst;
	private TextView			gst;
	private TextView			total;
	private ArrayList<Product>	checkoutList;
	private ArrayList<Product>	selectedProducts;
	private EditText			idInput;
	private AlertDialog			popup;
	private View				prevView;
	private Cart				cart = new Cart();
	ArrayAdapter<Product>		productArrayAdapter;
	int 						selectedItemPosition = -1;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_checkoutmode );
		
	
	
		// Create the buttons and disable remove until a product is selected
		btAddToCheckout = (Button) findViewById( R.id.productAddButton );
		btRemoveFromCheckout = (Button) findViewById( R.id.productRemoveButton );
		subTotal = (TextView) findViewById(R.id.tvSubtotal);
		pst = (TextView) findViewById(R.id.tvPst);
		gst = (TextView) findViewById(R.id.tvGst);
		total = (TextView) findViewById(R.id.tvTotal);
		
		btRemoveFromCheckout.setEnabled( false );
		checkoutList = cart.getList( );
		//Populate the CheckoutList with nothing
		productArrayAdapter = new ArrayAdapter<Product>( this,
				android.R.layout.simple_list_item_activated_2, android.R.id.text1, checkoutList ){
			@Override
			public View getView( int position, View convertView, ViewGroup parent ){
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(checkoutList.get( position ).getName( ));
                text2.setText(""+ checkoutList.get( position ).getPrice( ));
				return view;
				
			}
		};
		final ListView listView = (ListView) findViewById( R.id.checkoutList);
		listView.setAdapter( productArrayAdapter );
		
		listView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if(position == selectedItemPosition){
					listView.setItemChecked( position, false );
					selectedItemPosition = -1;
					view.setBackgroundColor( 0x00000000 );
					btRemoveFromCheckout.setEnabled( false );
					
				} else {
					listView.setItemChecked( position, true );
					
					if(selectedItemPosition != -1){
						prevView.setBackgroundColor( 0x00000000 );
					}
					
					selectedItemPosition = position;
					view.setBackgroundColor( 0x856CC0E5 );
					prevView = view;
					btRemoveFromCheckout.setEnabled( true );
				}
				
			}
		} );
		
	}
	
	public void onClickAddToCheckout(View v) {
		int productId;
		String atcReturn; //AddToCarts Return message

		// Get entered values
		idInput = (EditText) findViewById( R.id.productIdEntry );
		
		try {
			productId = Integer.parseInt( idInput.getText( ).toString( ) );
		}
		catch (Exception e) {
			showMessageDialog( "Invalid ID" );
			return;
		}

		atcReturn = cart.addToCart( productId );
		
		if(atcReturn != null){
			//Something went wrong
			showMessageDialog( atcReturn );
			return;
		}
		
		checkoutList = cart.getList();
		productArrayAdapter.notifyDataSetChanged( );
		
		idInput.setText( "" );
		setPrices();
		return;
	}
	
	public void onClickRemoveFromCheckout(View v){
		prevView.setBackgroundColor( 0x00000000 );
		cart.remove( selectedItemPosition );
		checkoutList = cart.getList( );
		productArrayAdapter.notifyDataSetChanged( );
		btRemoveFromCheckout.setEnabled( false );
		selectedItemPosition = -1;
		setPrices();
	}
	
	public void onClickCheckout(View v){
		
		cart.sellAll();
		
		showMessageDialog("All Items Sold\n\nFor: " + total.getText());

		checkoutList = cart.getList( );
		productArrayAdapter.notifyDataSetChanged( );
		setPrices();
	}
	
	public void setPrices(){
		double subTot, pstVal, gstVal, tot;
		
		subTot = new BigDecimal( cart.getCost( ) ).setScale( 2, RoundingMode.HALF_UP ).doubleValue( );
		pstVal = new BigDecimal( subTot * PST ).setScale( 2, RoundingMode.HALF_UP ).doubleValue( );
		gstVal = new BigDecimal( subTot * GST ).setScale( 2, RoundingMode.HALF_UP ).doubleValue( );
		tot = new BigDecimal(  subTot + pstVal + gstVal ).setScale( 2, RoundingMode.HALF_UP ).doubleValue( );
		
		subTotal.setText( "$" + Double.toString(subTot) );
		pst.setText( "$" + Double.toString(pstVal) );
		gst.setText( "$" + Double.toString(gstVal) );
		total.setText( "$" + Double.toString(tot) );	
	}

	
	//TODO Refactor
	/*
	 * THIS IS A TEMPORARY SOLUTION. WE NEED TO FACTOR THIS CODE OUT INTO ONE
	 * CLASS AS IT IS DUPLICATED TO CONVIENTLY DISPLAY MESSAGES
	 */
	
	private void showMessageDialog(String message) {Messages.message( this, message );}
		
	
}
