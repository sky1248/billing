package com.upb.taxbilling.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.upb.taxbilling.R;
import com.upb.taxbilling.exceptions.BillException;
import com.upb.taxbilling.exceptions.ExportException;
import com.upb.taxbilling.model.data.Bill;
import com.upb.taxbilling.view.billtable.BillTableFragment;

/**
 * The fragment where the information about a User and Bill is export to a file
 * @author Kevin Aguilar
 */
public class ExportFragment extends Fragment{
		
	private double totalAmount;
	private boolean sdAvailable;
	private boolean sdWriteAccess;
	private Button export;
	private TextView nameAndLastName;
	private TextView address;
	private TextView expeditionPlace;
	private TextView identityNumber;
	private TextView addressCompany;
	private TextView employerBussinesName;
	private TextView nitNumber;
	private TextView email;
	private TextView showTotalAmount;
	private TextView showPaymentOnAccount;
	private TextView date;
	private TextView place_presentation;
	private Map<String, String> places;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_export_bill,
				container, false);
		initializePlacesMap();
		nameAndLastName = (TextView)view.findViewById(R.id.textView8);
		address = (TextView)view.findViewById(R.id.textView9);
		identityNumber = (TextView)view.findViewById(R.id.textView10);
		expeditionPlace = (TextView)view.findViewById(R.id.textView11);
		employerBussinesName  = (TextView)view.findViewById(R.id.textView12);
		nitNumber = (TextView)view.findViewById(R.id.textView13);
		addressCompany = (TextView)view.findViewById(R.id.textView14);
		email = (TextView)view.findViewById(R.id.textView20);
		showTotalAmount = (TextView)view.findViewById(R.id.textView16);
		showPaymentOnAccount = (TextView)view.findViewById(R.id.textView18);
		date = (TextView)view.findViewById(R.id.textView23);	
		place_presentation = (TextView)view.findViewById(R.id.textView25);
		export = (Button)view.findViewById(R.id.button1);
		
		RegisterFragment rf = new RegisterFragment();
		if(rf.isChecked()) {	
			this.showUserData(this.getUserData());
			this.showDate(this.getDate());
			place_presentation.setText(RegisterFragment.getPlace());
			this.showBillAmount();
			export.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clickExport(v);
				}
			});
		}
		else {
			Toast.makeText(getActivity(), "Faltan Datos de Usuario", Toast.LENGTH_SHORT).show();
		}
	    return view;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	/**
	 * Method to export user data and bill data
	 * This method is called by the button Export
	 * @param v
	 */
	
	public void clickExport(View v)	{
		try {
			if (totalAmount > 0 && verifyAllBills()) {
				exportData(getUserData(), convertBillsMapToStringArray(), getDate());
			} else {
				throw new ExportException("Problema al exportar facturas:\nEl monto total de las facturas es 0"
						+ "\n Las facturas no pueden ser exportadas.");
			}
		} catch (BillException e) {
			Toast.makeText(getActivity(), "Problema al exportar facturas:\n" + e.getMessage()
					, Toast.LENGTH_LONG).show();
		} catch (ExportException e) {
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Method to create the file and write user data and bill data
	 * This method receives as parameters an ArrayList of user data and an ArrayList of bill data
	 * @param ArrayUser
	 * @param ArrayBill
	 */
	public void exportData(List<String> arrayUser, List<String> arrayBill, List<String> arrayDate) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
		    sdAvailable = true;
		    sdWriteAccess = true;
		} else if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
		    sdAvailable = true;
		    sdWriteAccess = false;
		} else {
		    sdAvailable = false;
		    sdWriteAccess = false;
		}
		
		try	{
			if (sdAvailable && sdWriteAccess) {
			    File ruta_sd = Environment.getExternalStorageDirectory();
			    File f = new File(ruta_sd.getAbsolutePath(), "Factura.txt");
			    OutputStreamWriter fout =  new OutputStreamWriter(new FileOutputStream(f));
			
			    fout.write("DiCaprio");
			    fout.write("\n");
			    fout.write(arrayUser.get(0));
		   		fout.write("\n");
		   		fout.write(RegisterFragment.getPlace());
		   		fout.write("\n");
		   		
			    for(int i=0; i < arrayDate.size(); i++) {
			   		fout.write(arrayDate.get(i));
			   		fout.write("\n");
			    }
			    for(int i=1; i < arrayDate.size(); i++) {
			   		fout.write(arrayDate.get(i));
			   		fout.write("\n");
			    }
			    for(int i=1; i < arrayUser.size(); i++) {
			   		fout.write(arrayUser.get(i));
			   		fout.write("\n");
			    }
			    Toast.makeText(getActivity(), "Exportando Datos de Usuario", Toast.LENGTH_SHORT).show();
			    fout.write("\n");
			    for(int i=0; i < arrayBill.size(); i++) {
			    	fout.write(arrayBill.get(i));
			    	fout.write("\n");
			    }
			    Toast.makeText(getActivity(), "Exportando Facturas", Toast.LENGTH_SHORT).show();
			    fout.close();
			}
		}
		catch (Exception ex) {
		    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
		}		
	}
	
	/**
	 * This method returns an ArrayList with the date  
	 * @return
	 */
	public ArrayList<String> getDate(){
		RegisterFragment rf =  new RegisterFragment();
		ArrayList<String> arrayDate = new ArrayList<String>();
		
		arrayDate.add(Integer.toString(rf.getDate().get(Calendar.DAY_OF_MONTH)));
		arrayDate.add(Integer.toString(rf.getDate().get(Calendar.MONTH)));
		arrayDate.add(Integer.toString(rf.getDate().get(Calendar.YEAR)));
		
		return arrayDate;
	}
	
	/**
	 * This method show the date.
	 * This method receives as parameters an ArrayList with the date
	 * @param Date
	 */

	public void showDate(ArrayList<String> Date)
	{
		date.setText(Date.get(0)+"-"+
					 Date.get(1)+"-"+
					 Date.get(2));
	
	}
	
	/**
	 * This method returns an ArrayList of user data sorted  
	 * @return
	 */
	public ArrayList<String> getUserData() {		
		RegisterFragment rf =  new RegisterFragment();
		ArrayList<String> arrayUser = new ArrayList<String>();
		
		arrayUser.add(rf.getDataTaxpayer().getEmail());
		arrayUser.add(rf.getDataTaxpayer().getNameLastname());
		arrayUser.add(rf.getDataTaxpayer().getAddress());
		arrayUser.add(Integer.toString(rf.getDataTaxpayer().getIdentityNumber()));
		arrayUser.add(places.get(rf.getDataTaxpayer().getExpeditionPlace()));
		arrayUser.add(rf.getDataCompany().getEmployerBussinesName());
		arrayUser.add(Integer.toString(rf.getDataCompany().getNitNumber()));
		arrayUser.add(rf.getDataCompany().getAddress());
		
		return arrayUser;
	}
	
	/**
	 * @return the information of a bill in the form of a string.
	 */
	public String getBillInfoString(Bill bill) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		String info = Long.toString(bill.getNit()) + "|"
		              + Long.toString(bill.getBillNumber()) + "|"
		              + Long.toString(bill.getAuthorizationNumber()) + "|"
		              + df.format(bill.getEmissionDate()) + "|"
				      + Double.toString(bill.getAmount()) + "|";
		if (bill.getControlCode().trim().equals("")) {
			info += " ";
		} else {
			info += bill.getControlCode();
		}
		return info;
	}
	
	/**
	 * This method show user data.
	 * This method receives as parameters an ArrayList of user data
	 * @param UserData
	 */
	public void showUserData(ArrayList<String> userData) {
		nameAndLastName.setText(userData.get(1));
		address.setText(userData.get(2));
		identityNumber.setText(userData.get(3));
		expeditionPlace.setText(userData.get(4));
		employerBussinesName.setText(userData.get(5));
		nitNumber.setText(userData.get(6));
		addressCompany.setText(userData.get(7));
		email.setText(userData.get(0));
	}
	
	/**
	 * This method show total amount and payment on account.
	 */
	public void showBillAmount() {
		totalAmount = 0;
		try {
			for(int i : BillTableFragment.getBills().keySet()) {	
				totalAmount = (totalAmount + BillTableFragment.getBills().get(i).getAmount());
			}
			if (totalAmount == 0) {
				throw new ExportException("El monto total de las facturas es 0");
			}
		} catch (Exception e) {
			Toast.makeText(getActivity(), e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		DecimalFormat df =  new DecimalFormat("0.00");
		showTotalAmount.setText(df.format(totalAmount));
		showPaymentOnAccount.setText(df.format(totalAmount*0.13));
	}
	
	/**
	 * Converts the bills' map in BillTableFragment into an array of strings.
	 * @return ArrayList of string of the bills 
	 * @throws BillException when there are no bills
	 */
	private List<String> convertBillsMapToStringArray() throws BillException {
		try {
			List<String> billsInfo = new ArrayList<String>();
			for(int i : BillTableFragment.getBills().keySet()) {
				billsInfo.add(getBillInfoString(BillTableFragment.getBills().get(i)));
			}
			return billsInfo;
		} catch (Exception ex) {
			throw new BillException("Problema al exportar facturas, no tiene"
					+ " facturas registradas.\n\nDetalles:\n" + ex.getMessage());
		}
	}
	
	/**
	 * Verify that all the bills in the table doesn't have empty fields.
	 * @return true if the aren't empty fields
	 * @throws ExportException if a field is empty
	 */
	private boolean verifyAllBills() throws ExportException {
		boolean ok = true;
		String message = "";
		for(int i : BillTableFragment.getBills().keySet()) {
			if (BillTableFragment.getBills().get(i).getNit() == 0) {
				ok = false;
				message = "Nit";
			}
			if (BillTableFragment.getBills().get(i).getBillNumber() == 0) {
				ok = false;
				message = "Numero de factura";
			}
			if (BillTableFragment.getBills().get(i).getAuthorizationNumber() == 0) {
				ok = false;
				message = "Numero de autorizacion";
			}
			if (BillTableFragment.getBills().get(i).getAmount() == 0) {
				ok = false;
				message = "Importe";
			}
			if (BillTableFragment.getBills().get(i).getControlCode().trim().equals("")) {
				ok = false;
				message = "Codigo de control";
			}
			if (!ok) {
				throw new ExportException("Problema en la factura Nro. " + i + "\n" + message + " invalido.");
			}
		}
		return ok;
	}
	
	/**
	 * Initialize the places map.
	 */
	private void initializePlacesMap() {
		places = new HashMap<String, String>();
		places.put("La Paz", "LP");
		places.put("Cochabamba", "CB");
		places.put("Santa Cruz", "SC");
		places.put("Tarija", "TJ");
		places.put("Oruro", "OR");
		places.put("Potosi", "PT");
		places.put("Pando", "PA");
		places.put("Chuquisaca", "CH");
		places.put("Beni", "BN");
	}
}
