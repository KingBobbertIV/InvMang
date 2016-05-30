package comp3350.team8.inventorymanagement.persistence;

import java.util.Stack;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneralIDManager
{
	protected interface DomainTable
	{
		public String nameOfTable();
		public String primaryKey();
	}
	
	
	public static final int DATABASE_MIN_ID = 1;
	
	
	
	/*
	 * PersistenceUnit: supports the use of a persistent database
	 * 
	 * 
	 */
	
	private class PersistenceUnit
	{
		private boolean [] restoration;
	
		private int processResultSet(ResultSet rs, ArrayList<Integer> destination) throws SQLException
		{
			int curVal;
			int maximum = GeneralIDManager.DATABASE_MIN_ID;
			destination.clear( );
			
			while (rs.next())
			{
				curVal = rs.getInt( 1 );
				maximum = (maximum > curVal)?maximum:curVal;
				destination.add( curVal );
			}
			
			return maximum;
		}
		
		private void buildPersistenceUnit(ArrayList<Integer> IDs, int maxID)
		{
			if (IDs.size() > 0)
			{
				restoration = new boolean[1 + maxID - GeneralIDManager.DATABASE_MIN_ID];
				
				for (int j = 0; j < IDs.size( ); j++)
				{
					restoration[IDs.get( j ) - GeneralIDManager.DATABASE_MIN_ID] = true;
					// store which IDs are present in the database
				}
			}
			else
			{
				restoration = new boolean [0]; // a boolean array containing nothing
			}
		}
		
		
		public PersistenceUnit(DBAccess persistence, DomainTable table) throws SQLException
		{
			ArrayList<Integer> IDs;
			ResultSet dbOut;
			int maxID;
			String sql;
			
			sql = "select " + table.primaryKey() + " from " + table.nameOfTable() + " where " + table.primaryKey() + " >= " + DATABASE_MIN_ID;
			IDs = new ArrayList<Integer>();
			dbOut = persistence.specialAccess( sql );
			maxID = processResultSet(dbOut, IDs);
			
			dbOut.close( );
			buildPersistenceUnit(IDs, maxID);
		}

		/*
		 * retrieveMaxId
		 * 
		 * returns the largest productID in the database, or PRODUCTS_MIN_ID - 1 if the db is empty
		 * 
		 */

		public int retrieveMaxId()
		{
			return restoration.length - 1 + GeneralIDManager.DATABASE_MIN_ID;
		}
		
		public Stack<Integer> retrieveRecycleBin()
		{
			Stack<Integer> recycling = new Stack<Integer>();
			for (int j = 0; j < restoration.length; j++)
			{
				
				if (!restoration[j])
				{
					// the ID this position represents is not in the database: it has been deleted
					recycling.push( j + GeneralIDManager.DATABASE_MIN_ID );	
				}
			}
			
			return recycling;
			
		}
	}	
	
	
// -----------   GeneralIDManager data & methods ----------- \\
	
	
	
	private int nextID;
	private Stack<Integer> recycleBin;
	
	// constructor for non-persistent storage
	public GeneralIDManager()
	{
		this.nextID = DATABASE_MIN_ID;
		this.recycleBin = new Stack<Integer>();
	}
	
	
	// constructor for persistent storage
	public GeneralIDManager(DBAccess persistence, DomainTable table) throws SQLException
	{		
		PersistenceUnit memory = new PersistenceUnit(persistence, table);
		
		nextID = memory.retrieveMaxId() + 1;
		recycleBin = memory.retrieveRecycleBin();
	}
	
	public void recycleID(int id)
	{
		recycleBin.push(id);
	}
	
	public int nextIDCode()
	{
		int newId;
		
		if (recycleBin.isEmpty())
		{
			newId = nextID;
			nextID++;
		}
		else
		{
			newId = recycleBin.pop();
		}

		return newId;
	}
	
	private boolean inRecycling(int id)
	{
		boolean here;
		int top;
		
		if (recycleBin.isEmpty())
		{
			here = false;
		}
		else
		{
			top = recycleBin.pop( );
			here = (id == top || inRecycling(id));
			recycleBin.push(top);
		}
		
		return here;
	}
	
	/*
	 * inDB
	 * 
	 * If the IDManager instance is used to assign all new IDs
	 * and productIDs are recycled whenever a product is deleted, this
	 * method can tell you whether a given productID exists in the database.
	 *
	 * To loop through all products in the db, we can use a for loop employing
	 * this method:
	 * 
	 * 		for (int ID = ManageProductIds.PRODUCTS_MIN_ID; ID < IdManager.D(); ID++)
	 * 		{
	 * 			if (IdManager.inDB(ID))
	 * 			{
	 * 				// process product
	 * 			}
	 * 		}
	 *
	 * THIS METHOD SHOULD ONLY EVER BE USED TO TEST THE GENERALIDMANAGER, AND SHOULD BE REMOVED AFTER TESTING
	 *
	 */
	
	public boolean inDB(int handledID)
	{
		return ((DATABASE_MIN_ID <= handledID) && (handledID <= nextID) && !(inRecycling(handledID)));
	}
	
}