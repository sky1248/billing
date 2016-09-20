package com.upb.taxbilling.model.data;

/**
 * Contains the information of a company
 * @author Kevin Aguilar
 * @author Alejandra Navarro
 */
public class Company {
	
	private String address;
	private String employerBussinesName;
	private Integer nitNumber;
	
	/**
	 * Constructor method with parameters
	 * @param Address
	 * @param EmployerBussinesName
	 * @param NitNumber
	 */
	public Company(String address,String employerBussinesName,Integer nitNumber) {		
		this.address = address;
		this.employerBussinesName = employerBussinesName;
		this.nitNumber = nitNumber;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the employerBussinesName
	 */
	public String getEmployerBussinesName() {
		return employerBussinesName;
	}

	/**
	 * @return the nitNumber
	 */
	public Integer getNitNumber() {
		return nitNumber;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param employerBussinesName the employerBussinesName to set
	 */
	public void setEmployerBussinesName(String employerBussinesName) {
		this.employerBussinesName = employerBussinesName;
	}

	/**
	 * @param nitNumber the nitNumber to set
	 */
	public void setNitNumber(Integer nitNumber) {
		this.nitNumber = nitNumber;
	}
}
