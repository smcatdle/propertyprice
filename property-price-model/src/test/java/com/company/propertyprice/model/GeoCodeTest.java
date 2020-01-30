package com.company.propertyprice.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;




/**
 * Geocode 
 */
public class GeoCodeTest {  

    private final Logger logger = Logger.getLogger(GeoCodeTest.class.getName());
    
    @Test
    public void testMangle() {
	
	GeoCode geocode = new GeoCode();
	geocode.setLatitude(53.2904580000);
	geocode.setLongitude(-6.2278832000);
	
	logger.log(Level.INFO, "Unmangled latitude [" + geocode.getLatitude() + "] and longitude [" + geocode.getLongitude() + "]");
	
    }

}
