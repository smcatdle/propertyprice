/** Scanshop Price Engine   **/

package com.company.propertyprice.model;


public class PPRProperty extends Property {

    private String xsiType;
    private String baseProductId;
    private String isPermanentlyUnavailable;
    
    
    public PPRProperty() {
        xsiType = null;
        baseProductId = null;
        isPermanentlyUnavailable = null;
    }

    public String getXsiType() {
        return xsiType;
    }

    public void setXsiType(String xsiType) {
        this.xsiType = xsiType;
    }

    public String getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(String baseProductId) {
        this.baseProductId = baseProductId;
    }

    public String getIsPermanentlyUnavailable() {
        return isPermanentlyUnavailable;
    }

    public void setIsPermanentlyUnavailable(String isPermanentlyUnavailable) {
        this.isPermanentlyUnavailable = isPermanentlyUnavailable;
    }

}
