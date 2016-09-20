package com.upb.taxbilling.view;

import java.util.Calendar;
import java.util.regex.Pattern;

import android.app.Fragment;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.upb.taxbilling.R;
import com.upb.taxbilling.exceptions.RegisterException;
import com.upb.taxbilling.exceptions.UserDataException;
import com.upb.taxbilling.model.data.Company;
import com.upb.taxbilling.model.data.Taxpayer;

/**
 * The fragment where the information about a bill is registered.
 * @author Kevin Aguilar
 * @author Vanessa Sanjinez
 * @author Alejandra Navarro
 */
public class RegisterFragment extends Fragment {
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static boolean isChecked = false;
	private static Taxpayer taxpayer;
	private static Company company;
	private static Calendar date;
	private static String place;
	
	private Button saveButton;
	private EditText nameLastname;
	private EditText address;
	private EditText identityNumber;
	private EditText employerBussinesName;
	private EditText nitNumber;
	private EditText addressCompany;
	private EditText email;
	private TextView year;
	private Spinner expeditionPlace;
	private Spinner place_presentation;
	private Spinner day;
	private Spinner month;
	private Calendar actualDate;
	
	private Pattern pattern;
	
	/**
     * {@inheritDoc}
     */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register,
				container, false);
		
		nameLastname = (EditText)view.findViewById(R.id.editText1);
		address = (EditText)view.findViewById(R.id.editText2);
		expeditionPlace = (Spinner)view.findViewById(R.id.spinner3);
		identityNumber = (EditText)view.findViewById(R.id.editText3);
		employerBussinesName  = (EditText)view.findViewById(R.id.editText5);
		nitNumber = (EditText)view.findViewById(R.id.editText6);
		addressCompany = (EditText)view.findViewById(R.id.editText7);
		email = (EditText)view.findViewById(R.id.editText10);
		place_presentation = (Spinner)view.findViewById(R.id.spinner4);
		year = (TextView)view.findViewById(R.id.textView13);
		saveButton = (Button)view.findViewById(R.id.button1);
		month = (Spinner)view.findViewById(R.id.spinner1);
		day = (Spinner)view.findViewById(R.id.spinner2);
		actualDate = Calendar.getInstance();
		year.setText(Integer.toString(actualDate.get(Calendar.YEAR)));
		
		InputFilter filterLetterOrDigitSpace = new InputFilter() {
			
			@SuppressWarnings("deprecation")
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				 for (int i = start; i < end; i++) { 
                     if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpace(source.charAt(i))) { 
                             return ""; 
                     } 
             } 
				return null;
			}
		};
		
		InputFilter filterLetterSpace = new InputFilter() {
			
			@SuppressWarnings("deprecation")
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				 for (int i = start; i < end; i++) { 
                     if (!Character.isLetter(source.charAt(i)) && !Character.isSpace(source.charAt(i))) { 
                             return ""; 
                     } 
             } 
				return null;
			}
		};
		
		InputFilter filterDigit = new InputFilter() {
			
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				 for (int i = start; i < end; i++) { 
                     if (!Character.isDigit((source.charAt(i)))) { 
                             return ""; 
                     } 
             } 
				return null;
			}
		};
		
		nameLastname.setFilters(new InputFilter[] {new InputFilter.LengthFilter(50),filterLetterSpace});
		address.setFilters(new InputFilter[] {new InputFilter.LengthFilter(50),filterLetterOrDigitSpace});
		employerBussinesName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30),filterLetterSpace});
		addressCompany.setFilters(new InputFilter[] {new InputFilter.LengthFilter(50),filterLetterOrDigitSpace});
		nitNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10),filterDigit});
		identityNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10),filterDigit});
		
		/**
		 * Change day according to month
		 */
		month.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> view, View arg1,
					int arg2, long arg3) {
				
				// TODO Auto-generated method stub
				UserDataException usde = new UserDataException();
				switch(usde.changeDays(month)){
					case 0:
						ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(view.getContext(),R.array.days_30,android.R.layout.simple_spinner_item);
						adapter0.setDropDownViewResource(android.R.layout.simple_spinner_item);
						day.setAdapter(adapter0);
						break;
					case 1:
						ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(view.getContext(),R.array.days_28,android.R.layout.simple_spinner_item);
						adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
						day.setAdapter(adapter1);
						break;
					case 2:
						ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(view.getContext(),R.array.days_31,android.R.layout.simple_spinner_item);
						adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
						day.setAdapter(adapter2);
						break;
						
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}		
		});
		
		
	
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					clickSaveData(v);
				} catch (RegisterException e) {
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		
	    return view;
	}
	
	/**
	 * Method to save user data in taxpayer and company
	 * @param v
	 * @throws RegisterException if the email is invalid
	 */
	public void clickSaveData(View v) throws RegisterException {
		UserDataException usde = new UserDataException();
		if(usde.userData(nameLastname, address,
				identityNumber, employerBussinesName, nitNumber,
				addressCompany, email).equals("")) {
			if (!pattern.matcher(email.getText().toString()).matches()) {
				isChecked = false;
				throw new RegisterException("Email invalido");
			}
		taxpayer = new Taxpayer(nameLastname.getText().toString(),
				address.getText().toString(), expeditionPlace.getSelectedItem().toString(),
				email.getText().toString(), Integer.parseInt(identityNumber.getText().toString()));
		company = new Company(addressCompany.getText().toString(),
				employerBussinesName.getText().toString(),
				Integer.parseInt(nitNumber.getText().toString()));
		date = Calendar.getInstance();
		date.set(date.get(Calendar.YEAR), 
				 Integer.parseInt(month.getSelectedItem().toString()),
				 Integer.parseInt(day.getSelectedItem().toString()));		 
		setPlace(place_presentation.getSelectedItem().toString());
			Toast.makeText(getActivity(), "Datos guardados", Toast.LENGTH_SHORT).show();
			isChecked = true;
		} else {
			Toast.makeText(getActivity(), "Faltan Datos de Usuario", Toast.LENGTH_LONG).show();
			isChecked = false;
		}
	}
	
	/**
	 * @return true if it's checked else it returns false.
	 */
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * Method to return information saved in Taxpayer
	 * This return an Taxpayer
	 * @return
	 */
    public Taxpayer getDataTaxpayer() {	
		return taxpayer;		
    }
    
    /**
     * Method to return information saved in Company
     * This return an Company
     * @return
     */
    public Company getDataCompany() {  	
		return company;		
    }
    
    /**
     * Method to return the date
     * @return
     */
    
    public Calendar getDate(){
    	return date;
    }
    
    /**
     * This method to return the place of presentation
     * @return
     */
    
    public static String getPlace() {
		return place;
	}
    
    /**
     * This method to change the place of presentation
     */
    
	public static void setPlace(String place) {
		RegisterFragment.place = place;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
