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
public class MyHomeCrossCheckerTest {

	@Test
	public void testGoogleMapsLookup() {
		
	    Address address = new Address();
	    address.setAddressLine1(" 291 The Meadows");
	    address.setAddressLine2("Belgard Heights");
	    address.setAddressLine3("Tallaght");
	    address.setAddressLine4("Dublin 24");
	    address.setAddressLine5("Dublin");
		
	    MyHomeCrossChecker myHomeCrossChecker = new MyHomeCrossChecker();
	    myHomeCrossChecker.crossCheck(address, 100000);
	
	    //logger.info("Formatted Address : [" + geocode.getFormattedAddress() + "]");
	}
}
