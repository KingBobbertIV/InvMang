package comp3350.team8.inventorymanagement.object;

import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;
import comp3350.team8.inventorymanagement.object.Product;

/*
 * Order
 * 
 * 
 */
public class Order {
	
	class OrderItemCompator implements Comparator<OrderItem>{
	    @Override
	    public int compare(OrderItem item1, OrderItem item2) {
	    	int result = 0;
			int pid1 = item1.getProductId();
			int pid2 = item2.getProductId();
			if ( pid1 < pid2 )
				result = -1;
			else if( pid1 == pid2 )
				result = 0;
			else if( pid1 > pid2 )
				result = 1;
			
			return result;
	    }
	}

		
	private static final int DEFAULT_ID = -1;
	
	int orderId;
	Date datePlaced;
	double cost;
	double fee;
	ArrayList<OrderItem> orderItems;
	
	public Order(ArrayList<OrderItem> orderItems, double cost)
	{
		this.orderId = DEFAULT_ID;
		this.datePlaced = new Date();
		this.cost = cost;
		this.fee = 0;
		this.setOrderItems( orderItems );
	}
	
	public Order(double fee)
	{
		this.fee = fee;
		this.cost = fee;
		this.datePlaced = new Date();
		this.orderItems = new ArrayList<OrderItem>();
	}
	
	public Order(int orderId, double fee)
	{
		this.orderId = orderId;
		this.fee = fee;
		this.cost = fee;
		this.orderItems = new ArrayList<OrderItem>();
	}
	
	public int getOrderId(){
		return orderId;
	}
	
	public void setOrderId(int id){
		this.orderId = id;
	}
	
	public double getOrderCost(){
		return cost;
	}
	
	public void setOrderCost(double newCost){
		this.cost = newCost;
	}
	
	public void setShipmentFee(double fee)
	{
		this.fee = fee;
	}
	
	public double getShipmentFee()
	{
		return fee;
	}
	
	public Date getOrderDatePlaced()
	{
		return (Date) datePlaced.clone();
	}
	
	public void setDatePlaced(Date rewriteTime)
	{
		this.datePlaced = rewriteTime;
	}
	
	public ArrayList<OrderItem> getOrderItems()
	{
		ArrayList<OrderItem> deepCopy = new ArrayList<OrderItem>();
		
		for (int i = 0; i < orderItems.size(); i++)
		{
			deepCopy.add( this.get( i ) );
		}
				
		return deepCopy;
	}
	
	public void setOrderItems(ArrayList<OrderItem> items){
		this.orderItems = items;
		Collections.sort(orderItems,new OrderItemCompator());
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof Order){
			Order order= (Order)object;
			return (this.orderId == order.orderId 
					&& this.cost == order.cost
					&& this.orderItems.equals( order.getOrderItems( ) ));
		}else{
			System.out.println("you cannot compare a OrderItem object with another object which is not OrderItem.");
			return false;
		}
	}
	
	public int search(int productId)
	{
		return Collections.binarySearch(orderItems, new OrderItem(productId, 0), new OrderItemCompator());
	}
	
	
	public void insert(Product product, int quantity)
	{
		int index = search(product.getProductId());
		
		if (index < 0)
		{
			index = -(index + 1);
			orderItems.add( index, new OrderItem(product.getProductId(), quantity) );
		}
		else
		{
			orderItems.get( index ).addItems(new OrderItem(product.getProductId(), quantity));
		}
		
		this.cost += (quantity * product.getCost());
	}
	
	
	public OrderItem get(int index)
	{
		return (OrderItem) orderItems.get( index ).clone();
	}
	
	public void remove( int toRemove)
	{
		this.orderItems.remove( toRemove );
	}
	
	public int productIdsCount()
	{
		return orderItems.size();
	}
	
	public String toString(){
		String output = "";
		output = String.format(java.util.Locale.CANADA, "order id: %d   total amount: $%.2f   placed date:%s \n\t%s", 
				this.orderId, this.cost, this.datePlaced, this.orderItems );
		return output;
	}

}
