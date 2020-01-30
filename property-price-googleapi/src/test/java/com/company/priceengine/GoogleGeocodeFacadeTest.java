/**
 * 
 */
package com.company.priceengine;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;

/**
 * @author smcardle
 * 
 */
public class GoogleGeocodeFacadeTest {

    private final Logger logger = Logger
	    .getLogger(GoogleGeocodeFacadeTest.class.getName());

    @Test
    public void testGeocodingSample() {

	try {
	    Address address = new Address();
	    address.setAddressLine1("4 KIMMAGE COURT");
	    //address.setAddressLine2("KIMMAGE RD");
	    //address.setAddressLine3("DUBLIN 6W");
	    //address.setAddressLine4("Dublin 1");
	    address.setAddressLine5("Dublin");

	    GoogleGeocodeFacade facade = GoogleGeocodeFacade.getInstance();
	    GeoCode geocode = facade.getGeocode(address);

	    logger.info("Formatted Address : [" + geocode.getFormattedAddress()
		    + "]");
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	}
    }
    
    
    @Test
    public void testRemoveLeftOfDigits() {
	
	try {
	    GoogleGeocodeFacade facade = GoogleGeocodeFacade.getInstance();
	    String strippedString = facade.removeLeftOfDigits("Apt. 6 Level 2 Boyne House");
	    
	    logger.info("Stripped string is [" + strippedString + "]");
	    
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    ex.printStackTrace();
	}
	    
    }
}
