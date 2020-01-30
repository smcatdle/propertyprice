/**
 * 
 */
package com.company.priceengine;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import com.company.geo.Coordinate;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.PropertySale;

/**
 * @author smcardle
 *
 */
public class CrossCheckBulkFacadeTest {

	private final Logger logger = Logger.getLogger(CrossCheckBulkFacadeTest.class
			.getName());

	@Test
	public void testGoogleMapsLookup() {

		float topLeftLat = (float) 53.40131;
		float topLeftLng = (float) -6.43798;
		float bottomRightLat = (float) 53.33248;
		float bottomRightLng = (float) -6.32812;

		Coordinate topLeft = new Coordinate();
		topLeft.setLat(topLeftLat);
		topLeft.setLng(topLeftLng);
		Coordinate bottomRight = new Coordinate();
		bottomRight.setLat(bottomRightLat);
		bottomRight.setLng(bottomRightLng);

		CrossCheckFacade crossCheckFacade = new CrossCheckFacade();
		
		// 1. Only Cross Check geocodes of suspect addresses for the moment
		// (type 'L' or "N').
		// 2. We also need to identify addresses that dont have a consensus
		// 3. Possible match on MyHome if address string stripped and words
		// like 'Floor', 'Apt', 'House' are removed.
		List<PropertySale> sales = PropertyDAO.getInstance()
				.findBadAddressProperties(topLeft, bottomRight);

		for (PropertySale sale : sales) {

			Address address = sale.getAddress();

			// address.setGeocode(geocode);

			GeoCode geocodeResult = crossCheckFacade.crossCheck(address, sale.getPrice());

			logger.info("codeMatchResult : ["
					+ geocodeResult.getFormattedAddress() + " : "
					+ geocodeResult.getFormattedAddressBck() + "]");
		}

	}
}
