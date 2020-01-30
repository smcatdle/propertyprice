/** User Crash Model   **/

package com.company.propertyprice.model;

import java.util.Date;


public class UserCrash {
	
    public static final String CRASH_TYPE_UNEXPECTED = "U";
    public static final String DATA_TYPE_JSON = "J";
    
	private int id;
	private String ipAddress;
	private String device;
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
	 * @return the device
	 */
	public String getDevice() {
	    return device;
	}
	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
	    this.device = device;
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
	/**
	 * @return the crashTypeUnexpected
	 */
	public static String getCrashTypeUnexpected() {
	    return CRASH_TYPE_UNEXPECTED;
	}
	/**
	 * @return the dataTypeJson
	 */
	public static String getDataTypeJson() {
	    return DATA_TYPE_JSON;
	}
	
	public void setException(Throwable exception) {
		
		StackTraceElement[] arr = exception.getStackTrace();
		String report = exception.toString() + "\n\n";
		for (int i = 0; i < arr.length; i++) {
			report += "    " + arr[i].toString() + "\n";
		}
		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		Throwable cause = exception.getCause();
		if (cause != null) {
			report += cause.toString() + "\n\n";
			arr = cause.getStackTrace();
			for (int i = 0; i < arr.length; i++) {
				report += "    " + arr[i].toString() + "\n";
			}
		}
		
		this.data = this.data + "\n" + report;
	}
	
}
