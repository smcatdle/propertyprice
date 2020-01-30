/**
 * 
 */
package com.company.propertyprice.model;

import java.util.List;
import java.util.Map;

import com.company.geo.GraphPoint;
import com.company.propertyprice.model.mdo.PropertyMDO;

/**
 * @author smcardle
 *
 */
public class GridWrapper {

    private List<PropertyMDO> p = null;
    private Map<String,GraphPoint> g = null;
    /**
     * @return the p
     */
    public List<PropertyMDO> getP() {
        return p;
    }
    /**
     * @param p the p to set
     */
    public void setP(List<PropertyMDO> p) {
        this.p = p;
    }
    /**
     * @return the g
     */
    public Map<String, GraphPoint> getG() {
        return g;
    }
    /**
     * @param g the g to set
     */
    public void setG(Map<String, GraphPoint> g) {
        this.g = g;
    }
 
    
}
