package comp3350.team8.inventorymanagement.business;

import java.io.IOException;
import comp3350.team8.inventorymanagement.persistence.DBAccess;
import comp3350.team8.inventorymanagement.persistence.DBInterface;

public class DataAccessService {
	
	private static DBInterface accessDatabase = null;
	
	public static final String dbName="DB";

	private static String dbPathName = "database/DB";
	
	public static DBInterface createDataAccess()
	{
		if (accessDatabase == null || !(accessDatabase instanceof DBAccess)){
			accessDatabase = new DBAccess(dbName);
			accessDatabase.open( dbPathName );
		}
		
		return accessDatabase;
	}
	
	
	public static DBInterface createDataAccess(DBInterface alternative, String dbPath)
	{
		
		if (accessDatabase != null)
		{
			accessDatabase.close();
		}
		
		accessDatabase = alternative;
		accessDatabase.open(dbPath);
		
		return accessDatabase;
	}
	
	
	public static DBInterface getDataAccess() throws IOException
	{
		if (accessDatabase == null)
		{
			throw new IOException("Data access not initialized.");
		}
		
		return accessDatabase;
	}
	
	
	public static void setDBPathName(String newPath)
	{
		dbPathName = newPath;
	}
	
	
	public static void closeDataAccess()
	{
		if (accessDatabase != null)
		{
			accessDatabase.close();
			accessDatabase = null;
		}
	}
}
