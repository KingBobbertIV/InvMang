
package comp3350.team8.inventorymanagement.presentation;

import comp3350.team8.inventorymanagement.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import comp3350.team8.inventorymanagement.object.Product;
import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> implements
		OnItemClickListener {
	
	private ArrayList<Product>	selectedProducts;
	
	public ProductAdapter(Context context, ArrayList<Product> users) {
		super( context, 0, users );
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Product product = getItem( position );
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from( getContext( ) ).inflate(
					R.layout.item_product, parent, false );
		}
		
		// Lookup view for data population
		TextView tvId = (TextView) convertView.findViewById( R.id.tvId );
		TextView tvName = (TextView) convertView.findViewById( R.id.tvName );
		TextView tvPrice = (TextView) convertView.findViewById( R.id.tvPrice );
		TextView tvQuantity = (TextView) convertView
				.findViewById( R.id.tvQuantity );
		
		// Populate the data into the template view using the data object
		tvId.setText( "" + product.getProductId( ) );
		tvName.setText( product.getName( ) );
		tvPrice.setText( "" + product.getPrice( ) );
		tvQuantity.setText( "" + product.getQuantity( ) );
		// Return the completed view to render on screen
		
		if(product.isLow( )){convertView.setBackgroundColor(0x85FFCCCC);}
		else{convertView.setBackgroundColor(0x00000000);}
		//set the product to display as pink if it is low
		
		return convertView;
	}
	
	/*
	 * addSelectedProduct
	 * 
	 * add a selected product into the selected array list
	 */
	public void addSelectedProduct(Product product) {
		int index = selectedProducts.indexOf( product );
		if (index != -1) {
			selectedProducts.add( product );
		}
	}
	
	/*
	 * isProductSelected
	 * 
	 * return true if the give product inside the array list
	 */
	public boolean isProductSelected(Product product) {
		int index = selectedProducts.indexOf( product );
		boolean result;
		
		if (index != -1) {
			result = false;
		} else {
			result = true;
		}
		
		return result;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		view.setBackgroundColor( 0xEFFFCC00 );
	}
}
