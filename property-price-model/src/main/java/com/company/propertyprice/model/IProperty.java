/** Property Price Model   **/


package com.company.propertyprice.model;


public interface IProperty {

	/**
	 * @return the id
	 */
	public int getId();


	/**
	 * @param id the id to set
	 */
	public void setId(int id);
	
	/**
	 * @return the propertyId
	 */
	public int getPropertyId();


	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(int propertyId);


	/**
	 * @return the address
	 */
	public Address getAddress();


	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address);


	/**
	 * @return the price
	 */
	public double getPrice();


	/**
	 * @param price the price to set
	 */
	public void setPrice(double price);


	/**
	 * @return the dwellingType
	 */
	public String getDwellingType();


	/**
	 * @param dwellingType the dwellingType to set
	 */
	public void setDwellingType(String dwellingType);


	/**
	 * @return the beds
	 */
	public int getBeds();


	/**
	 * @param beds the beds to set
	 */
	public void setBeds(int beds);


	/**
	 * @return the bath
	 */
	public int getBaths();


	/**
	 * @param bath the bath to set
	 */
	public void setBaths(int bath);

	/**
	 * @return the fileId
	 */
	public String getFileId();


	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId);
}
