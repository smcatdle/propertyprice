/**
 * 
 */
package com.company.priceengine;

import java.util.logging.Logger;

import org.junit.Test;

import com.company.exception.GoogleLimitException;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;

/**
 * @author smcardle
 *
 */
public class CrossCheckFacadeTest {

	private final Logger logger = Logger.getLogger(CrossCheckFacadeTest.class
			.getName());

	@Test
	public void testGoogleMapsLookup() {
 
	    Address address = new Address();
	    address.setAddressLine1("BLOCK A");
	    address.setAddressLine2("131 ADELAIDE SQ");
	    address.setAddressLine3("WHITEFRIAR ST");
	    address.setAddressLine4("Dublin 8");
	    address.setAddressLine5("Dublin");

		CrossCheckFacade crossCheckFacade = new CrossCheckFacade();
	    GoogleGeocodeFacade facade = GoogleGeocodeFacade.getInstance();
	    
	    try {
			GeoCode geocode = facade.getGeocode(address);			

			address.setGeocode(geocode);

			GeoCode geocodeResult = crossCheckFacade.crossCheck(address, 100000);

			logger.info("codeMatchResult : ["
					+ geocodeResult.getFormattedAddress() + " : "
					+ geocodeResult.getFormattedAddressBck() + "]");
			
		} catch (GoogleLimitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
