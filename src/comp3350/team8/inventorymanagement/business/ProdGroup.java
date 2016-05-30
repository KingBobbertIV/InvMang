package comp3350.team8.inventorymanagement.business;

import java.util.ArrayList;
import java.util.HashMap;
import comp3350.team8.inventorymanagement.object.Product;


abstract class ProdGroup {

	protected ArrayList<Product>			contents;
	protected HashMap<Integer, Integer>		quantities;
	protected AccessProducts				accessProducts;
	
	public ProdGroup(){
		contents = new ArrayList<Product>();
		quantities = new HashMap<Integer,Integer>();
		accessProducts = new AccessProducts();
	}

	public ArrayList<Product> getList() {
		return contents;
	}

	public void empty() {
		contents.clear( );
		quantities.clear();
	}
	
}
