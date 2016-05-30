
package comp3350.team8.inventorymanagement.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import comp3350.team8.inventorymanagement.business.DataAccessService;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;


/*
 * Application class
 * 
 * This class is used for share global object all over the app.
 * 
 * It is consisted of: ProductList
 */
public class InventoryApp extends Application
{
	private static String DB_PATH = "database";
	private static boolean copyAssets = true; // Set to true when you want the android device to take an updated DB.script from inventorymanagement's assets folder.
												// will overide anything saved on the android devices database and replace it with our script file.
	/*
	 * OnCreate
	 */
	@Override
	public void onCreate()
	{
		copyDatabaseToDevice();
    	
		Main.startUp();
	}
	
	@Override
	public void onTerminate(){
		Main.shutDown();
	}
	
	private void copyDatabaseToDevice() {
    	final String DB_PATH = "db";

    	String[] assetNames;
    	Context context = getApplicationContext();
    	File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
    	AssetManager assetManager = getAssets();
    	
    	try {

    		assetNames = assetManager.list(DB_PATH);
    		for (int i = 0; i < assetNames.length; i++) {
    			assetNames[i] = DB_PATH + "/" + assetNames[i];
    		}

    		copyAssetsToDirectory(assetNames, dataDirectory);
    		
    		DataAccessService.setDBPathName(dataDirectory.toString() + "/" + DataAccessService.dbName);

    	} catch (IOException ioe) {
    		ioe.printStackTrace( );
    		//Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
    	}
    }
	
	public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
    	AssetManager assetManager = getAssets();

    	for (String asset : assets) {
    		String[] components = asset.split("/");
    		String copyPath = directory.toString() + "/" + components[components.length - 1];
    		char[] buffer = new char[1024];
    		int count;
    		
    		File outFile = new File(copyPath);
    		
    		if (!outFile.exists() || copyAssets) {
    			InputStreamReader in = new InputStreamReader(assetManager.open(asset));
	    		FileWriter out = new FileWriter(outFile);
	    		
	    		count = in.read(buffer);
	    		while (count != -1) {
	    			out.write(buffer, 0, count);
	        		count = in.read(buffer);
	    		}
	    		
	    		out.close();
	    		in.close();
    		}
    	}
	}
}
