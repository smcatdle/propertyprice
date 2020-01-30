/**
 * 
 */
package com.company.propertyprice.model.android;

/**
 * @author smcardle
 *
 */
public class PropertyListItem {

	private long id = 0;
	private long propertysaleid = 0;
	private String json = null;
	
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the propertysaleid
	 */
	public long getPropertysaleid() {
		return propertysaleid;
	}
	/**
	 * @param propertysaleid the propertysaleid to set
	 */
	public void setPropertysaleid(long propertysaleid) {
		this.propertysaleid = propertysaleid;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

}
