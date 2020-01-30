/**
 * 
 */
package com.company.propertyprice.model;

import java.util.Map;

import com.company.geo.GraphPoint;


/**
 * @author smcardle
 *
 */
public class GridAveragesWrapper {

    private Map<String,GraphPoint> g = null;

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
