/** Property Price Model   **/

package com.company.propertyprice.model;

import java.util.Date;


public class UserEvent {
	
    public static final String EVENT_TYPE_GET_PROPERTIES_WITHIN_VIEWPORT = "P";
    public static final String DATA_TYPE_JSON = "J";
    
	private int id;
	private String ipAddress;
	private String eventType;
	private String dataType;
	private String data;
	private Date dateCreated;
	
	/**
	 * @return the id
	 */
	public int getId() {
	    return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
	    this.id = id;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
	    return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
	    this.ipAddress = ipAddress;
	}
	/**
	 * @return the eventType
	 */
	public String getEventType() {
	    return eventType;
	}
	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
	    this.eventType = eventType;
	}
	/**
	 * @return the dataType
	 */
	public String getDataType() {
	    return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
	    this.dataType = dataType;
	}
	/**
	 * @return the data
	 */
	public String getData() {
	    return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
	    this.data = data;
	}
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
	    return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
	    this.dateCreated = dateCreated;
	}

}
