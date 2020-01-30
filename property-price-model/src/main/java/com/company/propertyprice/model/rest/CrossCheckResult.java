/**
 * 
 */
package com.company.propertyprice.model.rest;

/**
 * @author smcardle
 *
 */
public class CrossCheckResult {

	private String originalAddress;
	private String currentAddress;
	private String suggestedAddress;
	private int crossCheckCode;
	
	
	public String getOriginalAddress() {
		return originalAddress;
	}
	public void setOriginalAddress(String originalAddress) {
		this.originalAddress = originalAddress;
	}
	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	public String getSuggestedAddress() {
		return suggestedAddress;
	}
	public void setSuggestedAddress(String suggestedAddress) {
		this.suggestedAddress = suggestedAddress;
	}
	public int getCrossCheckCode() {
		return crossCheckCode;
	}
	public void setCrossCheckCode(int crossCheckCode) {
		this.crossCheckCode = crossCheckCode;
	}
	
	
}
