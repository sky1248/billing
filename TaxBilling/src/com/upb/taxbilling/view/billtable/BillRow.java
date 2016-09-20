package com.upb.taxbilling.view.billtable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.upb.taxbilling.R;
import com.upb.taxbilling.controller.billanalyzer.BillAnalyzer;
import com.upb.taxbilling.exceptions.BillException;
import com.upb.taxbilling.model.data.Bill;
import com.upb.taxbilling.view.billtable.events.RowNumberClickListener;

/**
 * The TableRow where a bill is stored.
 * @author Allan Leon
 */
public class BillRow extends TableRow {

	private final static String DECIMAL_PATTERN = "[0-9]{0," + 7 + "}+((\\.[0-9]{0," + 2 + "})?)||(\\.)?";
	
	private Bill bill;
	private int rowNumber;
	private boolean isHighlighted;
	private DateFormat dateFormat;
	private InputFilter[] inputFiltersSmall;
	private InputFilter[] inputFiltersLarge;
	private InputFilter[] inputFiltersDecimal;
	private Pattern decimalPattern;
	
	/**
	 * Default constructor of a TableRow, receives a context as a parameter.
	 * @param context
	 */
	public BillRow(Context context) {
		super(context);
		this.rowNumber = 0;
		this.isHighlighted = false;
		this.bill = new Bill();
		BillTableFragment.getBills().put(this.rowNumber, this.bill);
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		initializeComponents();
	}

	/**
	 * Constructor method that receives the context and the number of the row as
	 * parameters.
	 * @param context
	 * @param rowNumber
	 */
	public BillRow(Context context, int rowNumber) {
		super(context);
		this.rowNumber = rowNumber;
		this.isHighlighted = false;
		this.bill = new Bill();
		BillTableFragment.getBills().put(this.rowNumber, this.bill);
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		initializeComponents();
	}

	/**
	 * Constructor method that receives the context, the number of the row
	 * and a bill as parameters, then updates it's components based on the
	 * bill it contains.
	 * @param context
	 * @param rowNumber
	 * @param bill
	 */
	public BillRow(Context context, int rowNumber, Bill bill) {
		super(context);
		this.rowNumber = rowNumber;
		this.bill = bill;
		this.isHighlighted = false;
		BillTableFragment.getBills().put(this.rowNumber, this.bill);
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		initializeComponents();
		try {
			updateRowInfo();
		} catch (BillException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param bill the bill to set.
	 */
	public void setBill(Bill bill) {
		this.bill = bill;
		BillTableFragment.getBills().put(this.rowNumber, this.bill);
	}
	
	/**
	 * @return the bill that this row contains.
	 */
	public Bill getBill() {
		return bill;
	}
	
	/**
	 * @param rowNumber the rowNumber to be set.
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
		TextView t1 = (TextView) this.getChildAt(0);
		t1.setText(Integer.toString(rowNumber));
		BillTableFragment.getBills().put(this.rowNumber, this.bill);
	}
	
	/**
	 * @return returns this row's number.
	 */
	public int getRowNumber() {
		return rowNumber;
	}
	
	/**
	 * @return true if the row is highlighted else returns false.
	 */
	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	/**
	 * Updates the row's highlight, if the row is already highlighted the highlight
	 * is removed, else the row is highlighted.
	 */
	public void updateHighlight() {
		if (isHighlighted) {
			isHighlighted = false;
		} else {
			isHighlighted = true;
		}
	}
	
	private void createInputFilters() {
		InputFilter maxLengthFilterSmall = new InputFilter.LengthFilter(10);
		InputFilter maxLengthFilterLarge = new InputFilter.LengthFilter(15);
		InputFilter decimalDigitsFilter = new InputFilter(){

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (!decimalPattern.matcher(new StringBuilder().append(dest.subSequence(0, dstart))
						.append(source.subSequence(start, end)).append(dest.subSequence(dend, dest.length())))
					.matches()) {
					return "";
				}
				return null;
			}
			
		};
		inputFiltersSmall = new InputFilter[]{maxLengthFilterSmall};
		inputFiltersLarge = new InputFilter[]{maxLengthFilterLarge};
		inputFiltersDecimal = new InputFilter[]{maxLengthFilterSmall, decimalDigitsFilter};
	}

	/**
	 * Initializes the seven EditText fields in this row.
	 */
	private void initializeComponents() {
		decimalPattern = Pattern.compile(DECIMAL_PATTERN);
		createInputFilters();
		
		TextView t1 = new TextView(this.getContext());
    	t1.setText(Integer.toString(rowNumber));
    	t1.setOnClickListener(new RowNumberClickListener());
    	
    	this.addView(t1);
    	this.addView(createNitEditText());
    	this.addView(createBillNumberEditText());
    	this.addView(createAuthorizationNumberEditText());
    	this.addView(createDateEditText());
    	this.addView(createAmountEditText());
    	this.addView(createControlCodeEditText());
    	
    	setFieldsStyle();
	}

    /**
     * Updates the editTexts fields of this row with the information of the bill
     * it contains.
     * @throws BillException when the bill is null
     */
    public void updateRowInfo() throws BillException {
        if (bill != null) {
        	EditText t1 = (EditText) this.getChildAt(1);
        	t1.setText(Long.toString(bill.getNit()));
        	
        	EditText t2 = (EditText) this.getChildAt(2);
        	t2.setText(Long.toString(bill.getBillNumber()));
        	
        	EditText t3 = (EditText) this.getChildAt(3);
        	t3.setText(Long.toString(bill.getAuthorizationNumber()));
        	
        	EditText t4 = (EditText) this.getChildAt(4);
        	t4.setText(dateFormat.format(bill.getEmissionDate()));
        	
        	BigDecimal bd = new BigDecimal(bill.getAmount());
            bd = bd.setScale(2, RoundingMode.HALF_UP);
        	EditText t5 = (EditText) this.getChildAt(5);
        	t5.setText(Double.toString(bd.doubleValue()));
        	
        	EditText t6 = (EditText) this.getChildAt(6);
        	t6.setText(bill.getControlCode());
        }
        throw new BillException("The bill doesn't contain information.");
    }

    /**
     * Sets the text style of the fields of this row.
     */
    private void setFieldsStyle() {
    	TextView tv = (TextView) this.getChildAt(0);
    	tv.setTextColor(Color.rgb(1, 3, 38));
    	tv.setTextSize(20);
    	tv.setTypeface(null, Typeface.BOLD);
    	tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    	for(int i = 1; i < 7; i++) {
    		EditText view = (EditText) this.getChildAt(i);
        	view.setTextColor(Color.rgb(1, 3, 38));
    	}
    	setBackgroundColor(getResources().getColor(R.color.RowNormalColor));
    	TableLayout.LayoutParams tableRowParams =
    			  new TableLayout.LayoutParams
    			  (TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
    	tableRowParams.setMargins(0, 0, 0, 1);
    	setLayoutParams(tableRowParams);
    }
    
    /**
     * @return new edit text that contains the nit.
     */
    private EditText createNitEditText() {
    	final EditText t2 = new EditText(this.getContext());
    	t2.setInputType(InputType.TYPE_CLASS_NUMBER);
    	t2.setFilters(inputFiltersSmall);
    	t2.setText("");
    	t2.addTextChangedListener(new TextWatcher() {
			
    		/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			/**
    		 * Updates the bill's nit with the nit's edit text info.
    		 */
			@Override
			public void afterTextChanged(Editable s) {
				try {
					bill.setNit(Long.parseLong(t2.getText().toString()));
				} catch (Exception ex) {
					bill.setNit(0);
					ex.printStackTrace();
				}
				BillTableFragment.getBills().put(rowNumber, bill);
			}
		});
    	return t2;
    }
    
    /**
     * @return new edit text that contains the bill number.
     */
    private EditText createBillNumberEditText() {
    	final EditText t3 = new EditText(this.getContext());
    	t3.setInputType(InputType.TYPE_CLASS_NUMBER);
    	t3.setFilters(inputFiltersSmall);
    	t3.setText("");
    	t3.addTextChangedListener(new TextWatcher() {
			
    		/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			/**
    		 * Updates the bill's bill number with the
    		 * bill number's edit text info.
    		 */
			@Override
			public void afterTextChanged(Editable s) {
				try {
					bill.setBillNumber(Long.parseLong(t3.getText().toString()));
				} catch (Exception ex) {
					bill.setBillNumber(0);
					ex.printStackTrace();
				}
				BillTableFragment.getBills().put(rowNumber, bill);
			}
		});
    	return t3;
    }
    
    /**
     * @return new edit text that contains the authorization number.
     */
    private EditText createAuthorizationNumberEditText() {
    	final EditText t4 = new EditText(this.getContext());
    	t4.setInputType(InputType.TYPE_CLASS_NUMBER);
    	t4.setFilters(inputFiltersLarge);
    	t4.setText("");
    	t4.addTextChangedListener(new TextWatcher() {
			
    		/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			/**
    		 * Updates the bill's authorization number with the 
    		 * authorization number's edit text info.
    		 */
			@Override
			public void afterTextChanged(Editable s) {
				try {
					bill.setAuthorizationNumber(Long.parseLong(t4.getText().toString()));
				} catch (Exception ex) {
					bill.setAuthorizationNumber(0);
					ex.printStackTrace();
				}
				BillTableFragment.getBills().put(rowNumber, bill);
			}
		});
    	return t4;
    }

    /**
     * @return new edit text that contains the date.
     */
    private EditText createDateEditText() {
    	final EditText t5 = new EditText(this.getContext());
    	t5.setInputType(InputType.TYPE_CLASS_DATETIME);
    	t5.setText("");
    	t5.setFocusableInTouchMode(false);
    	t5.setKeyListener(null);
    	
    	final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                    int selectedMonth, int selectedDay) {
                try {
                	String year = String.valueOf(selectedYear);
                    String month = String.valueOf(selectedMonth + 1);
                    String day = String.valueOf(selectedDay);
                    String date = day + "/" + month + "/" + year;
                    Date eDate = dateFormat.parse(date);
                    if (BillAnalyzer.verifyBillDate(eDate)) {
                    	t5.setText(date);
                    	bill.setEmissionDate(dateFormat.parse(date));
                    	BillTableFragment.getBills().put(rowNumber, bill);
                    } else {
                		throw new BillException("Fecha de emision invalida");
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
					Toast.makeText(t5.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
            }
        };
    	
    	t5.setOnClickListener(new OnClickListener() {
			
    		/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                		datePickerListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Selecciona la fecha");
                datePicker.show();
			}
		});
    	return t5;
    }

    /**
     * @return new edit text that contains the amount.
     */
    private EditText createAmountEditText() {
    	final EditText t6 = new EditText(this.getContext());
    	t6.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
    	t6.setFilters(inputFiltersDecimal);
    	t6.setText("");
    	t6.addTextChangedListener(new TextWatcher() {
			
    		/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			/**
    		 * Updates the bill's amount with the amount's edit text info.
    		 */
			@Override
			public void afterTextChanged(Editable s) {
				try {
					bill.setAmount(Double.parseDouble(t6.getText().toString()));
				} catch (Exception ex) {
					bill.setAmount(0.0);
					ex.printStackTrace();
				}
				BillTableFragment.getBills().put(rowNumber, bill);
			}
		});
    	return t6;
    }

    /**
     * @return new edit text that contains the control code.
     */
    private EditText createControlCodeEditText() {
    	final EditText t7 = new EditText(this.getContext());
    	t7.setText("");
    	t7.setFilters(inputFiltersLarge);
    	t7.addTextChangedListener(new TextWatcher() {
			
    		/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			/**
    		 * {@inheritDoc}
    		 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			/**
    		 * Updates the control code's amount with the 
    		 * control code's edit text info.
    		 */
			@Override
			public void afterTextChanged(Editable s) {
				bill.setControlCode(t7.getText().toString());
				BillTableFragment.getBills().put(rowNumber, bill);
			}
		});
    	return t7;
    }
}
