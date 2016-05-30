
package comp3350.team8.inventorymanagement.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import comp3350.team8.inventorymanagement.persistence.DBInterface;
import comp3350.team8.inventorymanagement.persistence.DataAccessStub;
import comp3350.team8.inventorymanagement.object.Product;

public class AccessProducts {
	
	private static DBInterface	dataAccess;
	private ArrayList<Product>	products;
	
	public AccessProducts() {
		try {
			dataAccess = DataAccessService.getDataAccess( );
		}
		catch (Exception e) {
			dataAccess = DataAccessService.createDataAccess( new DataAccessStub( "dbName" ),
					"dbName" );
		}
		products = new ArrayList<Product>( );
	}
	
	public AccessProducts(String name) {
		try {
			dataAccess = DataAccessService.getDataAccess( );
		}
		catch (Exception e) {
			dataAccess = DataAccessService.createDataAccess( );
		}
		products = new ArrayList<Product>( );
	}
	
	public void useStubDatabase() {
		dataAccess = DataAccessService.createDataAccess( new DataAccessStub( "dbName" ), "Null" );
	}
	
	public void usePersistantDatabase() throws IOException {
		dataAccess = DataAccessService.createDataAccess( );
	}
	
	/**
	 * getPagedProducts
	 * 
	 * It provides an interface for GUI to get a page of product
	 * 
	 * Parameter: sortKey : it is used in future, for now, we only works for the
	 * product id startIndex: the first element index in the list num : count
	 * for the number of product we want to show in one page
	 * 
	 * return: return an ArrayList<Product> object, if the start index out of
	 * range, there will be nothing inside the return object
	 * 
	 * important note: this method return the copy of the product object in this
	 * list, not the object self in this.products.
	 * 
	 * author: Yang Shi data: Feb 10 ,2015 9:11am
	 */
	public ArrayList<Product> getPagedProducts(String sortKey, int startIndex, int num) {
		ArrayList<Product> list = new ArrayList<Product>( );
		Product product;
		int size;
		
		products.clear( );
		dataAccess.getProducts( this.products );
		
		size = this.products.size( );
		for (int i = startIndex; i < size && i < startIndex + num; i++) {
			
			product = (Product) products.get( i ).clone( );
			
			if (product != null)
				list.add( product );
			
		}
		
		return list;
	}
	
	/**
	 * getProducts
	 * 
	 * Get all products as a list.
	 * 
	 * return if there is no product in persistance layer, it return a ArrayList
	 * with 0 element. otherwise, it return a ArrayList contains all products
	 * 
	 * important note: this method return the copy of the product object in this
	 * list, not the object self in ProductList.
	 * 
	 * changed: Yang Shi data: Feb 10 ,2015 9:11am
	 */
	public ArrayList<Product> getProducts() {
		ArrayList<Product> list = new ArrayList<Product>( );
		Product product;
		int size;
		
		products.clear( );
		dataAccess.getProducts( this.products );
		
		size = this.products.size( );
		for (int i = 0; i < size; i++) {
			product = (Product) products.get( i ).clone( );
			if (product != null) {
				list.add( product );
			}
		}
		
		return list;
	}
	
	/**
	 * getProduct
	 * 
	 * get a product according to the productId field.
	 * 
	 * return an product object which productId equals to the parameter
	 * 
	 * important note: this method return the copy of the product object in this
	 * list, not the object self in ProductList.
	 * 
	 * author: Yang Shi data: Feb 10 ,2015 9:12am
	 */
	public Product getProductById(int productId) {
		Product product = null;
		boolean found = false;
		
		products.clear( );
		dataAccess.getProducts( this.products );
		for (int i = 0; i < this.products.size( ) && !found; i++) {
			product = this.products.get( i );
			if (product.getProductId( ) == productId) {
				found = true;
			}
		}
		
		if (!found) {
			product = null;
		}
		
		return product;
	}
	
	public int insertProduct(Product product) {
		return dataAccess.addProduct( product );
	}
	
	public void updateProduct(Product product) {
		dataAccess.updateProduct( product );
		return;
	}
	
	public void deleteProduct(Product product) {
		dataAccess.deleteProduct( product.getProductId( ) );
		return;
	}
	
	public int getSize() {
		return dataAccess.getProductsCount( );
	}
	
	public boolean purchase(int pID, int num) {
		try {
			dataAccess.recordPurchase( pID, num );
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public boolean lowItem() {
		products.clear( );
		dataAccess.getProducts( this.products );
		boolean result = false;
		Product product;
		
		for (int i = 0; i < products.size( ) && !result; i++) {
			product = products.get( i );
			if (product.isLow( )) {
				result = true;
			}
		}
		return result;
	}
	
	/*
	 * getAllProductsId
	 * 
	 * get all of product id in the inventory.
	 * 
	 * return an array list which contains all ids
	 */
	public List<Integer> getAllProductsId() {
		ArrayList<Product> list = new ArrayList<Product>( );
		List<Integer> productsId = new ArrayList<Integer>( );
		Product product;
		
		dataAccess.getProducts( list );
		
		for (int pos = 0; pos < list.size( ); pos++) {
			product = list.get( pos );
			productsId.add( Integer.valueOf( product.getProductId( ) ) );
		}
		
		list = null; // release memory
		
		return productsId;
	}
	
}