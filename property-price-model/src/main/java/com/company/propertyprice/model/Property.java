/** Property Price Model   **/

package com.company.propertyprice.model;

import java.util.Date;

public class Property implements IProperty {

    private int id;
    private int propertyId;
    private String url;
    private Address address;
    private double price;
    private String dwellingType;
    private String description;
    private int beds;
    private int baths;
    private double size;
    private Date dateCreated;
    private Date dateUpdated;
    private String fileId;

    /**
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * @return the propertyId
     */
    public int getPropertyId() {
	return propertyId;
    }

    /**
     * @param propertyId
     *            the propertyId to set
     */
    public void setPropertyId(int propertyId) {
	this.propertyId = propertyId;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
	return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(Address address) {
	this.address = address;
    }

    /**
     * @return the price
     */
    public double getPrice() {
	return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(double price) {
	this.price = price;
    }

    /**
     * @return the dwellingType
     */
    public String getDwellingType() {
	return dwellingType;
    }

    /**
     * @param dwellingType
     *            the dwellingType to set
     */
    public void setDwellingType(String dwellingType) {
	this.dwellingType = dwellingType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the beds
     */
    public int getBeds() {
	return beds;
    }

    /**
     * @param beds
     *            the beds to set
     */
    public void setBeds(int beds) {
	this.beds = beds;
    }

    /**
     * @return the bath
     */
    public int getBaths() {
	return baths;
    }

    /**
     * @param bath
     *            the baths to set
     */
    public void setBaths(int baths) {
	this.baths = baths;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
	return dateCreated;
    }

    /**
     * @param dateCreated
     *            the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
	this.dateCreated = dateCreated;
    }

    /**
     * @return the dateUpdated
     */
    public Date getDateUpdated() {
	return dateUpdated;
    }

    /**
     * @param dateUpdated
     *            the dateUpdated to set
     */
    public void setDateUpdated(Date dateUpdated) {
	this.dateUpdated = dateUpdated;
    }

    /**
     * @return the fileId
     */
    public String getFileId() {
	return fileId;
    }

    /**
     * @param fileId
     *            the fileId to set
     */
    public void setFileId(String fileId) {
	this.fileId = fileId;
    }

    /**
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(double size) {
        this.size = size;
    }

    
}
