/** Property Price Model   **/

package com.company.propertyprice.model;

import java.util.Date;

public class ServerConfig {

    private int id;
    private String masterIpAddress;
    private int batchSize;
    private Date dateCreated;
    private Date dateUpdated;
    
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
     * @return the masterIpAddress
     */
    public String getMasterIpAddress() {
        return masterIpAddress;
    }
    /**
     * @param masterIpAddress the masterIpAddress to set
     */
    public void setMasterIpAddress(String masterIpAddress) {
        this.masterIpAddress = masterIpAddress;
    }
    /**
     * @return the batchSize
     */
    public int getBatchSize() {
        return batchSize;
    }
    /**
     * @param batchSize the batchSize to set
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
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
     * @return the dateUpdated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }
    /**
     * @param dateUpdated the dateUpdated to set
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    
    
}
