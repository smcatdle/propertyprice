/**
 * 
 */
package com.company.propertyprice.model;

import java.util.List;

/**
 * @author smcardle
 *
 */
public class PPRSearchVariables {

	private List<String> counties = null;
	private List<String> years = null;
	
	/**
	 * @return the counties
	 */
	public List<String> getCounties() {
		return counties;
	}
	/**
	 * @param counties the counties to set
	 */
	public void setCounties(List<String> counties) {
		this.counties = counties;
	}
	/**
	 * @return the years
	 */
	public List<String> getYears() {
		return years;
	}
	/**
	 * @param years the years to set
	 */
	public void setYears(List<String> years) {
		this.years = years;
	}


}
