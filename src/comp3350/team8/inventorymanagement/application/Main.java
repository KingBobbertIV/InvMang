package comp3350.team8.inventorymanagement.application;

import comp3350.team8.inventorymanagement.business.DataAccessService;


public class Main
{
	
	public static void startUp()
	{
		DataAccessService.createDataAccess(/*Services.dbName, Services.getDBPathName( )*/);
	}

	public static void shutDown()
	{
		DataAccessService.closeDataAccess();
	}
}
