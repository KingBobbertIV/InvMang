package comp3350.team8.inventorymanagement.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class Messages {

	private static final String FATAL_ERROR = "Fatal Error";
	private static final String WARNING = "Warning"; 
	private static final String MESSAGE = "Message";
	
	public static void fatalError(final Activity owner, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder( owner );
		builder.setTitle( FATAL_ERROR );
		builder.setMessage( message );
		
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				owner.finish();
			}
		});
		
		create(builder);
	}
	
	public static void warning(Activity owner, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder( owner );
		builder.setTitle( WARNING );
		builder.setMessage( message );
		create(builder);
	}
	
	public static void message(Activity owner, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder( owner );
		builder.setTitle( MESSAGE );
		builder.setMessage( message );
		create(builder);
		
	}
	
	public static void create(AlertDialog.Builder builder){
		AlertDialog popup;
		
		builder.setNeutralButton( "OK", new DialogInterface.OnClickListener( ) {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss( );
			}
		} );
		
		// show dialog
		popup = builder.create( );
		popup.show( );
		
		
	}
}
