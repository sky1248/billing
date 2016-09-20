package com.upb.taxbilling.model.data;

/**
 * Contains the information of a taxpayer
 * @author Vanessa Sanjinez
 * @author Gina Cardozo
 */
public class Taxpayer {

	private String nameLastname;
	private String address;
	private String expeditionPlace;
	private String email;
	private int identityNumber;

	/**
	 * Constructor method with parameters
	 */
	public Taxpayer(String nameLastname, String address, String expeditionPlace,
			String email, int identityNumber) {
		this.nameLastname = nameLastname;
		this.address = address;
		this.expeditionPlace = expeditionPlace;
		this.email = email;
		this.identityNumber = identityNumber;
	}

	/**
	 * @return the nameLastname
	 */
	public String getNameLastname() {
		return nameLastname;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the expeditionPlace
	 */
	public String getExpeditionPlace() {
		return expeditionPlace;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the identityNumber
	 */
	public int getIdentityNumber() {
		return identityNumber;
	}

	/**
	 * @param nameLastname the nameLastname to set
	 */
	public void setNameLastname(String nameLastname) {
		this.nameLastname = nameLastname;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param expeditionPlace the expeditionPlace to set
	 */
	public void setExpeditionPlace(String expeditionPlace) {
		this.expeditionPlace = expeditionPlace;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param identityNumber the identityNumber to set
	 */
	public void setIdentityNumber(int identityNumber) {
		this.identityNumber = identityNumber;
	}
}
