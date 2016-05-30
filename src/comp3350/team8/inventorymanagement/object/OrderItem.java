package comp3350.team8.inventorymanagement.object;

/*
 * OrderItem
 * 
 * this class is used to represents each item in a order.
 * for each item in a order, it shows like follow form:
 * 		(product id, quantity)
 * 
 * (if we need store more, we can add new fields into this class)
 * 
 * Yang Shi
 * March 25, 2014
 * 
 */
public class OrderItem{

	private int productId;
	private int quantity;
		
	public OrderItem(int productId, int quantity){
		this.productId = productId;
		this.quantity = quantity;
	}
		
	public int getProductId(){
		return this.productId;
	}
		
	public int getQuantity(){
		return this.quantity;
	}
	
	public String toString(){
		return "product Id: " + this.productId + " quantity: " + this.quantity;
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof OrderItem){
			OrderItem item= (OrderItem)object;
			return (this.productId == item.productId && this.quantity == item.quantity);
		}else{
			System.out.println("you cannot compare a OrderItem object with another object which is not OrderItem.");
			return false;
		}
	}
	
	@Override
	public Object clone()
	{
		return new OrderItem(productId, quantity);
	}
	
	public void addItems(OrderItem otherItem) throws IllegalArgumentException
	{
		if (otherItem.productId != this.productId && otherItem.quantity != 0)
		{
			throw new IllegalArgumentException("Cannot add any productID" + otherItem.productId + " to a collection of productID" + this.productId + ".");
		}
		else
		{
			this.quantity += otherItem.quantity;
		}
	}
	
	
}
