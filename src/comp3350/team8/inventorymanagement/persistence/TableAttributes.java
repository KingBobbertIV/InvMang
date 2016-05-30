package comp3350.team8.inventorymanagement.persistence;

abstract class TableAttributes implements GeneralIDManager.DomainTable
{
	private String name;
	private String pKey;
	
	public String nameOfTable()
	{
		return name;
	}
	
	public String primaryKey()
	{
		return pKey;
	}
	
	public TableAttributes(String name, String pKey)
	{
		this.pKey = pKey;
		this.name = name;
	}
	
	public TableAttributes()
	{
		// default constructor
	}
}

class ProductAttributes extends TableAttributes
{
	public static final String		tableName = "Products";

	public static final String		IDField = "ProductID";
	public static final String		nameField = "Name";
	public static final String		descriptionField = "Description";
	public static final String		priceField = "Price";
	public static final String		quantityField = "AmountInStock";
	public static final String		lowStockField = "StockThreshold";
	public static final String		supplierCostField = "SupplierCost";
	
	public ProductAttributes()
	{
		super(tableName, IDField);
	}	
}


class OrderAttributes extends TableAttributes
{
	public static final String		tableName = "Orders";
	
	public static final String		IDField = "OrderID";
	public static final String		priceField = "Cost";
	public static final String		dateField = "DatePlaced";
	
	public static final String		orderItemProductId = "productId";
	public static final String		orderItemQuantity = "Quantity";
	
	public OrderAttributes()
	{
		super(tableName, IDField);
	}
	
}