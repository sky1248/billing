package com.upb.taxbilling.exceptions;

import android.widget.EditText;
import android.widget.Spinner;

/**
 * Verifies that all the fields on the user register fragment aren't empty.
 * @author Kevin Aguilar
 * @author Alejandra Navarro
 */
public class UserDataException {
		
		/**
		 * This method checks that all user data were entered
		 * @param nameLastname
		 * @param address
		 * @param expeditionPlace
		 * @param identityNumber
		 * @param employerBussinesName
		 * @param nitNumber
		 * @param addressCompany
		 * @param email
		 * @return
		 */
		public String userData(EditText nameLastname, EditText address,
				EditText identityNumber, 
				EditText employerBussinesName, EditText nitNumber,
				EditText addressCompany, EditText email) {
			if(fieldEmpty(nameLastname.getText().toString())
			   || fieldEmpty(address.getText().toString())
			   || fieldEmpty(identityNumber.getText().toString())
			   || fieldEmpty(employerBussinesName.getText().toString())
			   || fieldEmpty(nitNumber.getText().toString())
			   || fieldEmpty(addressCompany.getText().toString())
			   || fieldEmpty(email.getText().toString())) {
				return "Error";
			} else {
				return "";
			}
		}
		
		/**
		 * This method checks that a field is filled
		 * @param value
		 * @return
		 */
		public boolean fieldEmpty(String value) {
			boolean error = false;
			if(value.equals("")) {
				error = true;
			}			
			return error;
		}
		
		/**
		 * This method verifies that month was selected
		 * Receives as parameter a spinner of the months
		 * @param month
		 * @return
		 */
		
		public int changeDays(Spinner month){
			if(Integer.parseInt(month.getSelectedItem().toString())==4 ||
			   Integer.parseInt(month.getSelectedItem().toString())==6 ||
		       Integer.parseInt(month.getSelectedItem().toString())==9 ||
		       Integer.parseInt(month.getSelectedItem().toString())==11){
				return 0;
			}
			else{
				if(Integer.parseInt(month.getSelectedItem().toString())==2){
					return 1;
				}
				else{
					return 2;
				}
			}
		}
}
