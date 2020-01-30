/**
 * 
 */
package com.company.propertyprice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.priceengine.PPRPriceEngine;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.SalesWorkUnit;

/**
 * @author smcardle
 *
 */
public class SalesService {

    private final static Logger logger = Logger.getLogger(SalesService.class .getName());
	
	
    private static SalesService instance = null;
    
    private SalesService() {
    	
    }
    
    public static SalesService getInstance() {
    	
    	if (instance == null) {
    		instance = new SalesService();
    	}
    	
    	return instance;
    }
    
    
    /*public void uploadSales(List<PropertySale> sales) {
	
	try {
	    
	    for (PropertySale sale : sales) {
		PropertyDAO.getInstance().attachDirty(sale);
	    }
	    
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    ex.getStackTrace();
	} 
	
    }
	
    public void processSales(List<SalesWorkUnit> salesWorkUnits) {
	
	try {
	    
	    List<String> searchLinks = new ArrayList<String>();
	    
	    for (SalesWorkUnit salesWorkUnit : salesWorkUnits) {
		searchLinks.add(salesWorkUnit.getLink());
		logger.info("Added link [" + salesWorkUnit.getLink() + "] to process engine");
	    }
	    
	    // Start the engine to process the sales
	    PPRPriceEngine engine = new PPRPriceEngine();
	    engine.start(PPRPriceEngine.START_PAGE, searchLinks);

	    
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    ex.getStackTrace();
	} 
	
    }*/
    
}
