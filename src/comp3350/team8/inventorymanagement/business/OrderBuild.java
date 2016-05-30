package comp3350.team8.inventorymanagement.business;


import java.util.ArrayList;
import comp3350.team8.inventorymanagement.object.Product;
import comp3350.team8.inventorymanagement.object.Order;
import comp3350.team8.inventorymanagement.object.OrderItem;

public class OrderBuild extends ProdGroup {

	private Order thisOrder;
	
	public OrderBuild(){
		super();
		thisOrder = new Order(new ArrayList<OrderItem>(), 0.00);
	}
	
	public OrderBuild(Order toBuild){
		super();
		thisOrder = toBuild;
		
		ArrayList<OrderItem> currItems = thisOrder.getOrderItems( );
		
		for(OrderItem x : currItems){
			addToOrder(x.getProductId( ), x.getQuantity( ));
		}
		
	}
	
	public String addToOrder(int productID, int quantity){
		Product newProd = accessProducts.getProductById( productID );
		boolean prodPresent = has(productID);
		int num;
		if(!prodPresent){
			num = 0;
		} else{
			num = quantities.get(productID);
		}
		
		if(newProd == null) {
			return "This Product does not exist";
		}
		
				 
		
		quantities.put(productID, num + quantity );
		if(!prodPresent){
		
		contents.add( newProd );
		contents.get( contents.size()-1 ).setQuantity( quantity );
		
		} else{
		
			int x = 0;
			while(x < contents.size() && !contents.get( x ).equals(newProd)){ x++;}
				contents.get( x ).setQuantity( contents.get( x ).getQuantity() + quantity );
		
		}
		
		return null;
		
	}
	
	public boolean remove(int position) {
		if(position < contents.size()){
			int productID = contents.get( position ).getProductId( );
		
		
			if(quantities.get( productID ) == 0){
				return false;
			}
		
			quantities.put(productID, null);
			contents.remove(position);
		
			return true;
		}
		return false;
	}
	
	public void buyAll(){
		for(int key: quantities.keySet()) {
			//TODO: I don't like doing it this way, but it should work
			accessProducts.purchase(key , -quantities.get(key));
		}
		
		empty();
		
	}
	
	public int getID(){
		return thisOrder.getOrderId( );
	}
	
	public String getDatePlaced(){
		String[] dates = thisOrder.getOrderDatePlaced( ).toString( ).split( "\\s+" );
		return dates[5] + ", " + dates[1] + " " + dates[2];
	}
	
	
	public Order build(){
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		
		for(int key: quantities.keySet()) {
			items.add( new OrderItem(key, quantities.get( key )) );
		}
		
		thisOrder.setOrderItems( items );
		thisOrder.setOrderCost(getPrice());
		return thisOrder;
	}

	public double getPrice() {
		double total = 0;
		
		for(Product p : contents) {
			total += p.getCost( ) * p.getQuantity( );
		}
		
		return total;
	}
	
	public void empty()
	{
		super.empty();
		thisOrder = new Order(new ArrayList<OrderItem>(), 0.00);
	}
	
	public boolean has(int pID){
		return quantities.get( pID ) != null;
	}
	
}
