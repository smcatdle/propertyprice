package com.company.propertyprice.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.company.geo.Coordinate;
import com.company.propertyprice.model.PropertySale;


public class GeoPropertyServiceTest {
	
    
    private final Logger logger = Logger.getLogger(GeoPropertyServiceTest.class.getName());
    private GeoPropertyService facade =  null;
    
    
    @Before
    public void setup() {
    	
    	try {
	    	facade = GeoPropertyService.getInstance();
    	} catch (Exception ex) {
    		logger.log(Level.SEVERE, ex.getMessage());
    	}
    }

	@Test
	public void testGetPropertiesWithinViewport() {
		
		Coordinate topLeft = new Coordinate();
		Coordinate bottomRight = new Coordinate();
		
		topLeft.setLat((float)53.3404580000);
		topLeft.setLng((float)-6.2278832000);
		bottomRight.setLat((float)53.5004582000);
		bottomRight.setLng((float)-6.1078834000);
		
		List<PropertySale> sales = facade.getPropertiesWithinViewport(topLeft, bottomRight);
	    
		logger.log(Level.INFO, "size : [" + sales.size() + "]");
	}
  
}
