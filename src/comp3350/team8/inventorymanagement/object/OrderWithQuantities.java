package comp3350.team8.inventorymanagement.object;
import java.util.Date;
import java.util.ArrayList;


public class OrderWithQuantities 
{
	
	private static final int DEFAULT_ID = -1;
	public static int DEFAULT_QTY = 50;
	
	private int orderId;
	private Date datePlaced;
	private double cost;
	private ArrayList<Integer> productIds;
	private ArrayList<Integer> quantities;
		
	public OrderWithQuantities(ArrayList<Integer> productIds, ArrayList<Integer> quantities, double amount)
	{
		this.orderId = DEFAULT_ID;
		this.datePlaced = new Date();
		this.productIds = productIds;
		this.quantities = quantities;
		this.cost = amount;
	}
		
	public int getOrderId()
	{
		return orderId;
	}
		
	public void setOrderId(int newID)
	{
		orderId = newID;
	}

	public double getOrderCost()
	{

		return cost;
	}
		
	public void setOrderCost(double cost)
	{
		this.cost = cost;
	}
		
	public Date getOrderDatePlaced()
	{
		return (Date) datePlaced.clone();
	}
	
	public void setOrderDatePlaced(Date date)
	{
		this.datePlaced = date;
	}
		
	public int[][] getProductData()
	{
		// first row of the resulting array is productIDs, second row is quantities
		// returnMe[n][0] = ID of product n
		// returnMe[n][1] = number of product n's in shipment
		
		int [][] returnMe = new int[productIds.size()][2];
		
		for( int i = 0; i <= quantities.size( ); i++)
		{
			returnMe[i][0] = productIds.get( i );
			returnMe[i][1] = quantities.get(i);
		}
			
		
		return returnMe;
		}

}

