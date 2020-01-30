/**
 * 
 */
package com.company.priceengine.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



/**
 * @author smcardle
 *
 */
public class ConfigListener implements ServletContextListener {


	    
    private final static Logger logger = Logger.getLogger(ConfigListener.class .getName());

    public void contextInitialized(ServletContextEvent servletContextEvent) {
	logger.log(Level.INFO, "ConfigListener initialised.");
	

	
    }
    
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
	
    }
}
