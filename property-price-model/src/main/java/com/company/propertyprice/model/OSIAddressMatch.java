/**
 * 
 */
package com.company.propertyprice.model;

/**
 * @author smcardle
 *
 */
public class OSIAddressMatch {

    private String id;
    private String index;
    private String addressString;
    private double matchingIndex;
    
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }
    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }
    /**
     * @return the addressString
     */
    public String getAddressString() {
        return addressString;
    }
    /**
     * @param addressString the addressString to set
     */
    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }
    /**
     * @return the matchingIndex
     */
    public double getMatchingIndex() {
        return matchingIndex;
    }
    /**
     * @param matchingIndex the matchingIndex to set
     */
    public void setMatchingIndex(double matchingIndex) {
        this.matchingIndex = matchingIndex;
    }
    
    
}
