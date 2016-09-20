package com.upb.taxbilling.view.billtable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.upb.taxbilling.R;
import com.upb.taxbilling.exceptions.BillException;
import com.upb.taxbilling.model.data.Bill;
import com.upb.taxbilling.view.billtable.events.AmountHeaderClickListener;
import com.upb.taxbilling.view.billtable.events.AuthorizationNumberHeaderClickListener;
import com.upb.taxbilling.view.billtable.events.BillNumberHeaderClickListener;
import com.upb.taxbilling.view.billtable.events.ControlCodeHeaderClickListener;
import com.upb.taxbilling.view.billtable.events.DateHeaderClickListener;
import com.upb.taxbilling.view.billtable.events.NitHeaderClickListener;

/**
 * The fragment where the table (list) of bills is stored.
 * @author Allan Leon
 * @author Franco Montiel
 * @author Gina Cardozo
 */
public class BillTableFragment extends Fragment {
	
	private static Map<Integer, Bill> bills;
	private static TableLayout contentTable;
	
	private static String controlCodeValue;
	private static String dateValue;
	
	/**
     * {@inheritDoc}
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bill_table, container, false);
		final Button buttonAdd = (Button) view.findViewById(R.id.ButtonAdd);
	        buttonAdd.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                	onClickAddButton(v);
	            }
	        });
        final Button buttonRemove = (Button) view.findViewById(R.id.ButtonRemove);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                	onClickRemoveButton(v);
            }
        });
        final Button buttonClean = (Button) view.findViewById(R.id.ButtonClean);
        buttonClean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                	onClickCleanButton(v);
            }
        });
        final TextView headerNit = (TextView) view.findViewById(R.id.headerNit);
        headerNit.setOnClickListener(new NitHeaderClickListener());
        final TextView headerBillNumber = (TextView) view.findViewById(R.id.headerBillNumber);
        headerBillNumber.setOnClickListener(new BillNumberHeaderClickListener());
        final TextView headerAuthorizationNumber = (TextView) view.findViewById(R.id.headerAuthorizationNumber);
        headerAuthorizationNumber.setOnClickListener(new AuthorizationNumberHeaderClickListener());
        final TextView headerDate = (TextView) view.findViewById(R.id.headerDate);
        headerDate.setOnClickListener(new DateHeaderClickListener());
        final TextView headerAmount = (TextView) view.findViewById(R.id.headerAmount);
        headerAmount.setOnClickListener(new AmountHeaderClickListener());
        final TextView headerControlCode = (TextView) view.findViewById(R.id.headerControlCode);
        headerControlCode.setOnClickListener(new ControlCodeHeaderClickListener());
        
        if (bills == null) {
        	bills = new TreeMap<Integer, Bill>();
        }
    	contentTable = (TableLayout) view.findViewById(R.id.ContentTable);
        updateRowsByList();
	    return view;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks
    	// on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method executes when the addButton is pressed.
     * Adds a blank row to the table.
     * @param view
     */
    public void onClickAddButton(View view) {
    	BillRow row = new BillRow(contentTable.getContext(), getNextRowNumber());
    	contentTable.addView(row);
   	}
    
    /**
     * This method executes when the removeButton is pressed.
     * Removes all the highlighted rows in the table and updates the remaining 
     * rows' numbers.
     * @param view
     */
    public void onClickRemoveButton(View view) {
    	removeHighlightedRows();
    	updateRowNumbers();
    	updateBillList();
   	}
    
    /**
     * This method executes when the clean button is pressed.
     * Removes all the rows in the table.
     * @param view
     */
    public void onClickCleanButton(View view) {
    	cleanTable();
    	updateBillList();
   	}
    
    /**
     * Adds a new bill to the bill table.
     * @param manualBill to be added to the table
     */
    public static void runManualBill(final Bill manualBill) {
    	final TableAlertDialog tad = new TableAlertDialog();
    	final View view = contentTable;

    	//Launches first pop-up message and waits for the user to type a value.
		tad.controlCodePopUpMessage(view, new TablePromptRunnable(){
			/**
			 * Saves the typed value into the Bill object at the Amount attribute.
			 * At user confirmation, launches the second pop-up message.
			 */
			public void run() {
				controlCodeValue = this.getValue();
				manualBill.setControlCode(controlCodeValue);
				tad.datePopUpMessage(view, new TablePromptRunnable() {
					/**
					 * Saves the typed value into the Bill object at the Date attribute.
					 * At user confirmation, prints the Bill object with the inserted data
					 * into the table. Also aggregates the bill to an independent ManualBill Array.
					 */
					public void run() {
						dateValue = this.getValue();
						try {
							Date convertedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(dateValue);
							manualBill.setEmissionDate(convertedDate);
							if (manualBill.verifyBill()) {
								BillRow row = new BillRow(contentTable.getContext(), getNextRowNumber(), manualBill);
								contentTable.addView(row);
							} else {
								throw new BillException("Fecha de emision invalida, la factura no puede ser agregada a la tabla.");
							} 
						} catch (ParseException e) {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
				            alertDialog.setTitle("Problema con la fecha");
				            alertDialog.setMessage("Hubo un error al registrar la fecha\n\n"
				            		+ "Detalles del error:\n" + e.toString());
				            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				        		public void onClick(DialogInterface dialog, int whichButton) {
				        			dialog.dismiss();
				        		}
				        	});
				            alertDialog.show();
						} catch (BillException e) {
							Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		});
    }
    
    /**
     * Adds a new bill to the bill table.
     * @param electronicBill to be added
     */
    public static void runElectronicBill(final Bill electronicBill) {
    	try {
	    	if (electronicBill.verifyBill()) {
	    		BillRow row = new BillRow(contentTable.getContext(), getNextRowNumber(), electronicBill);
	    		contentTable.addView(row);
	    	} else {
				throw new BillException("Fecha de emision invalida, la factura no puede ser agregada a la tabla.");
			}
    	} catch (BillException e) {
			Toast.makeText(contentTable.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
    }
    
    /**
     * Returns the next row number of the table, if the table is empty it return zero.
     * @return the next row number
     */
    private static int getNextRowNumber() {
    	int number = 0;
    	try {
    		TableRow lastRow = (TableRow) contentTable.getChildAt(contentTable.getChildCount()-1);
    		TextView lastNumber = (TextView) lastRow.getChildAt(0);
    		String text = lastNumber.getText().toString();
    		number = Integer.parseInt(text) + 1;
    	} catch (Exception e) {
    		number = 1;
    	}
    	return number;
    }
    
    /**
     * Creates a defined manual bill.
     * @return a user created manual bill with only the electronic parameters.
     */
    public static Bill newManualBill() {
    	Bill manu1 = new Bill(1008565022,9032,3904001124321L);    	
    	return manu1;
    }

    /**
     * Creates a defined electronic bill.
     * @return a user defined electronic bill with all fields filled.
     */
    public Bill newElectronicBill() {
    	String dateString = "31/05/2014";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.US); 
        Date convertedDate = null;
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	Bill elec1 = new Bill(1008565022,9032,3904001124321L, convertedDate, 5.80, "F8-27-08-0B-70");
    	return elec1;
    }
    
    /**
     * Updates the row number of all the rows in the contentTable.
     */
    public void updateRowNumbers() {
    	for(int i = 1; i < contentTable.getChildCount(); i++) {
    		BillRow row = (BillRow) contentTable.getChildAt(i);
    		row.setRowNumber(i);
    	}
    }
    
    /**
     * Removes the highlighted rows of the content table.
     */
    public void removeHighlightedRows() {
    	for (int i = 1; i < contentTable.getChildCount(); i++) {
    		BillRow row = (BillRow) contentTable.getChildAt(i);
    		if (row.isHighlighted()) {
    			contentTable.removeViewAt(i);
    			i--;
    		}
    	}
    }
    
    /**
     * Cleans all the rows in the table.
     */
    public void cleanTable() {
    	contentTable.removeViews(1, contentTable.getChildCount() - 1);
    	bills = new TreeMap<Integer, Bill>();
    }
    
    /**
     * Updates the list of bills based on the rows of the table.
     */
    public void updateBillList() {
    	bills = new TreeMap<Integer, Bill>();
    	for(int i = 1; i < contentTable.getChildCount(); i++) {
    		BillRow row = (BillRow) contentTable.getChildAt(i);
    		bills.put(row.getRowNumber(), row.getBill());
    	}
    }
    
    /**
     * Updates the rows of the table based on the list of bills.
     */
    public static void updateRowsByList() {
    	contentTable.removeViews(1, contentTable.getChildCount() - 1);
    	for(int i : bills.keySet()) {
    		BillRow row = new BillRow(contentTable.getContext(), i, bills.get(i));
        	contentTable.addView(row);
    	}
    }
    
    /**
     * @return the list of bills of the table.
     */
    public static Map<Integer, Bill> getBills() {
    	return bills;
    }
}
