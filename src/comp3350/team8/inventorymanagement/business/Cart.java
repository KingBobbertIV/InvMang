package comp3350.team8.inventorymanagement.business;


import comp3350.team8.inventorymanagement.object.Product;


public class Cart extends ProdGroup {
	protected AccessOrders	accessOrders;
	
	public Cart(){
		super();
		accessOrders = new AccessOrders();
	}
	
	public String addToCart(int productID){
		Product newProd = accessProducts.getProductById( productID );
		int num;
		
		if(newProd == null) {
			return "This Product does not exist";
		}
		
		num = (quantities.get(productID) == null) ? 0 : quantities.get(productID);
				 
		if(num + 1 > newProd.getQuantity()){
			return "There is not enough of this Product in stock";
		}
		
		quantities.put(productID, num + 1 );
		contents.add( newProd );
		
		return null;
		
	}
	
	public boolean remove(int position) {
		if(position < contents.size()){
			int productID = contents.get( position ).getProductId( );
		
		
			if(quantities.get( productID ) == 0){
				return false;
			}
		
			quantities.put(productID, quantities.get(productID) - 1 );
			contents.remove(position);
		
			return true;
		}
		return false;
	}
	
	/*
	 * sellAll
	 * 
	 * clear all the products in cart, and create an order object pass to database
	 */
	public void sellAll(){
		
		for(int key: quantities.keySet()) {
			accessProducts.purchase(key , quantities.get(key));
		}
		
		empty();
	}

	public double getCost() {
		double total = 0;
		
		for(Product p : contents) {
			total += p.getPrice( );
		}
		
		return total;
	}

	
}
