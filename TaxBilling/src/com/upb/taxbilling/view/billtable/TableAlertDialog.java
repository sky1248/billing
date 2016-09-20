package com.upb.taxbilling.view.billtable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * The alert dialog that pops up when asking for additional data for the manual bill registration.
 * @author Franco Montiel
 * @author Guillermo Torrez
 */	
public class TableAlertDialog {
	
	private String value;
	
    /**
     * Launches a pop up message with a EditText component and OK/Cancel buttons for user input.
     * @param view 
     * @param postrun Functional class that waits for the user input, validation
     * 				  and then executes the next command. 
     */
    public void controlCodePopUpMessage(View view, final TablePromptRunnable postrun) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
    	alert.setTitle("Codigo de Control faltante");
    	alert.setMessage("Introduzca el codigo de control correspondiente a la factura anteriormente registrada:");

    	final EditText input = new EditText(view.getContext());
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			value = input.getText().toString();
    			dialog.dismiss();
    			postrun.setValue(value);
    			postrun.run();
    			return;
    		}
    	});

    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    	    // Canceled.
    		}
    	});
    	alert.show();
    }

	/**
	 * Launches a pop up message with a EditText component and OK/Cancel buttons for user input.
	 * @param view 
	 * @param postrun Functional class that waits for the user input, validation
	 * 				  and then executes the next command. 
	 */
	public void datePopUpMessage(View view, final TablePromptRunnable postrun) {
		AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
		alert.setTitle("Fecha faltante");
		alert.setMessage("Introduzca la fecha de emisión correspondiente a la factura anteriormente registrada:");
		
		final DatePicker dp = new DatePicker(view.getContext());
		dp.setCalendarViewShown(false);
		alert.setView(dp);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int day = dp.getDayOfMonth();
				int month = dp.getMonth() + 1;
				int year = dp.getYear();
				value = day + "/" + month + "/" + year;
				System.out.println(value);
				dialog.dismiss();
				postrun.setValue(value);
				postrun.run();
				return;
			}
		});	

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		alert.show();
	}
}
