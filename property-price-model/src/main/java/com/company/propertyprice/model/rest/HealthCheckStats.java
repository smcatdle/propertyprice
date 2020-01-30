/**
 * 
 */
package com.company.propertyprice.model.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smcardle
 *
 */
public class HealthCheckStats {
    
    private List<NodeStatus> healthCheckStats;
    
    public HealthCheckStats() {
	healthCheckStats = new ArrayList<NodeStatus>();
    }

    /**
     * @return the healthCheckStats
     */
    public List<NodeStatus> getHealthCheckStats() {
        return healthCheckStats;
    }

    /**
     * @param healthCheckStats the healthCheckStats to set
     */
    public void setHealthCheckStats(List<NodeStatus> healthCheckStats) {
        this.healthCheckStats = healthCheckStats;
    }
    
    
}
