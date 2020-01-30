/**
 * 
 */
package com.company.propertyprice.model.android;

import java.util.Date;

/**
 * @author smcardle
 *
 */
public class Grid {

	private int id;
	private int gridId;
	private String json;
	private boolean update;
	private Date dateLastUsed;
	private Date dateUpdated;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGridId() {
		return gridId;
	}
	public void setGridId(int gridId) {
		this.gridId = gridId;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public Date getDateLastUsed() {
		return dateLastUsed;
	}
	public void setDateLastUsed(Date dateLastUsed) {
		this.dateLastUsed = dateLastUsed;
	}
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	
}
