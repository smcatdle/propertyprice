/**
 * 
 */
package com.company.priceengine;

import org.junit.Test;

import com.company.propertyprice.model.Address;


/**
 * @author smcardle
 *
 */
public class OSIGeocodeFacadeTest {

	@Test
	public void testOSIGeocodeFacade() {
		
	    Address address = new Address();
	    address.setAddressLine1("99 Hyde Park");
	    address.setAddressLine2("Grace Park Road");
	    address.setAddressLine3("Drumcondra");
	    //address.setAddressLine4("Dublin 1");
	    address.setAddressLine5("Dublin");
	    
	    OSIGeocodeFacade osiGeocodeFacade = new OSIGeocodeFacade();
	    osiGeocodeFacade.osiSearch(address);
	
	}   
}
