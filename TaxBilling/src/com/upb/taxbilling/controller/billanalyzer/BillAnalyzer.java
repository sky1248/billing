package com.upb.taxbilling.controller.billanalyzer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.upb.taxbilling.exceptions.BillException;
import com.upb.taxbilling.model.data.Bill;
import com.upb.taxbilling.view.billtable.BillTableFragment;

/**
 * Analyzes and creates a bill based on a text and it's information.
 * @author Allan Leon
 */
public class BillAnalyzer {
	
	private static String DIVIDER = "\\|";
	
	/**
	 * Creates a bill based on a given text.
	 * @param billText containing the bill's info
	 * @return a new bill with billText's info
	 * @throws BillException if the bill couldn't be created
	 */
	public static Bill parseBill(String billText) throws BillException {
		List<String> billInfo = tokenizeBillText(billText);
		final Bill bill;
		if (billInfo.size() < 8) {
			bill = createManualBill(billInfo);
			BillTableFragment.runManualBill(bill);
		} else {
			bill = createElectronicBill(billInfo);
			BillTableFragment.runElectronicBill(bill);
		}
		return bill;
	}
	
	/**
	 * Divides a given string into tokens using the divider of this class
	 * and then returns a list of the tokens obtained.
	 * @param billText string to be divided in tokens
	 * @return a list of the tokens obtained
	 */
	private static List<String> tokenizeBillText(String billText) {
		List<String> billInfo = Arrays.asList(billText.split(DIVIDER));
		return billInfo;
	}
	
	/**
	 * Creates a manual bill with a given list of a bill's information.
	 * @param billInfo information of the bill
	 * @return a new manual bill
	 * @throws BillException if the bill couldn't be created
	 */
	private static Bill createManualBill(List<String> billInfo) throws BillException {
		try {
			int nit = Integer.parseInt(billInfo.get(0));
			String name = billInfo.get(1);
			int autorizationNumber = Integer.parseInt(billInfo.get(2));
			Date limitEmissionDate = new SimpleDateFormat(
					"dd.MM.yyyy", Locale.ENGLISH).parse(billInfo.get(3));
			int aux = 4;
			Double ammount = 0.0;
			if (billInfo.size() == 7) {
				ammount = Double.parseDouble(billInfo.get(aux));
				aux++;
			}
			int economicActivity = Integer.parseInt(billInfo.get(aux));
			int subsidiary = Integer.parseInt(billInfo.get(aux + 1));
			
			return new Bill(nit, name, autorizationNumber, limitEmissionDate,
					ammount, economicActivity, subsidiary);
		} catch (Exception ex) {
			throw new BillException("No se pudo reconocer la factura manual," +
					"el problema puede ser debido a:\n" + ex.getMessage());
		}
	}
	
	/**
	 * Creates an electronic bill with a given list of a bill's information.
	 * @param billInfo information of the bill
	 * @return a new electronic bill
	 * @throws BillException if the bill couldn't be created
	 */
	private static Bill createElectronicBill(List<String> billInfo) throws BillException {
		try {
		int nit = Integer.parseInt(billInfo.get(0));
		String name = billInfo.get(1);
		int billNumber = Integer.parseInt(billInfo.get(2));
		int autorizationNumber = Integer.parseInt(billInfo.get(3));
		Date emissionDate = new SimpleDateFormat(
				"dd.MM.yyyy", Locale.ENGLISH).parse(billInfo.get(4));
		Double amount = Double.parseDouble(billInfo.get(5));
		String controlCode = billInfo.get(6);
		Date limitEmissionDate = new SimpleDateFormat(
				"dd.MM.yyyy", Locale.ENGLISH).parse(billInfo.get(7));
		int aux = 8;
		Double iceAmount = 0.0;
		Double noTaxSaleAmount = 0.0;
		if (billInfo.size() == 12) {
			iceAmount = Double.parseDouble(billInfo.get(aux));
			aux++;
			noTaxSaleAmount = Double.parseDouble(billInfo.get(aux));
			aux++;
		}
		String taxpayerNIT = billInfo.get(aux);
		String taxpayerName = billInfo.get(aux + 1);
		
		return new Bill(nit, name, billNumber, autorizationNumber, emissionDate,
				amount, controlCode, limitEmissionDate, iceAmount, noTaxSaleAmount,
				taxpayerNIT, taxpayerName);
		} catch (Exception ex) {
			throw new BillException("No se pudo reconocer la factura electronica," +
					"el problema puede ser debido a:\n" + ex.getMessage());
		}
	}
	
	/**
	 * Verifies that the bill has a valid emission date.
	 * @param emissionDate of the bill
	 * @return true if it's valid, else returns false
	 */
	public static boolean verifyBillDate(Date emissionDate) {
		Bill bill = new Bill(0, 0, 0, emissionDate, 0.0, "");
		bill.calculateLimitEmmisionDate();
		return bill.verifyBill();
	}
}
