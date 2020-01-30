/**
 * 
 */
package com.company.priceengine;

import java.util.logging.Logger;

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;

/**
 * @author smcardle
 *
 */
public class CrossCheckFacade {

	private final Logger logger = Logger.getLogger(CrossCheckFacade.class
			.getName());

	private MyHomeCrossChecker myHomeCrossChecker = null;
	private OSIGeocodeFacade osiGeocodeFacade = null;
	
	
	public CrossCheckFacade() {
		myHomeCrossChecker = new MyHomeCrossChecker();
		osiGeocodeFacade = new OSIGeocodeFacade();
	}
	
	public GeoCode crossCheck(Address address, double price) {

		GeoCode geocode = null;
		GeoCode osiGeocode = null;
		GeoCode myHomeGeocode = null;
		Address cleanAddress = null;
		int codeMatchResult = 0;

		try {
			
			String formattedAddress = (address.getGeocode().getFormattedAddress() != null) ? address.getGeocode().getFormattedAddress().replace("Co. ", "").replaceAll("&(?!amp;)|[/,-\\.]", " ") : "";
			formattedAddress = formattedAddress.toUpperCase();
			
			// Remove unusual charachters from address
			cleanAddress = address.deepCopy()
								.cleanupAddress();
			
			// Return if the google formatted address exactly matches the original address, 
			// or address_line_1 matches the beginning of the formatted address  (as this usually means a geocode exact match when accompanied by a house number).
			if (cleanAddress.concatAddress().equalsIgnoreCase(formattedAddress)
					|| (formattedAddress != null && formattedAddress.contains(cleanAddress.getAddressLine1()) && cleanAddress.getAddressLine1() != null && cleanAddress.getAddressLine1().matches(".*[0-9]+.*"))) {
				
				return address.getGeocode();
			}
			
			
			// TODO: Define 'Cross Check' code into strategies (different
			// strategies for different address types).

			// TODO: Add Daft and .CN cross checkers
			// http://ir.ksou.cn/p.php?id=20263&q=East+Wall,%20Co.+Dublin

			try {

				myHomeGeocode = myHomeCrossChecker.crossCheck(cleanAddress, price);
				logger.info("CrossCheck:: Found match of lat : ["
						+ myHomeGeocode.getLatitude() + "] lng : ["
						+ myHomeGeocode.getLongitude() + "] from My Home");
			} catch (Exception ex) {
				logger.info("CrossCheck:: Error cross checking address for ["
						+ cleanAddress.getAddressLine1() + "]  " + ex.getMessage());
			}

			// TODO: Exit if google address exactly matches original address (To
			// save unnecessary processing).

			// Return original geocode if consensus with MyHome
			/*if (cleanAddress.getGeocode().isMyHomeConsensus(myHomeGeocode)) {
				logger.info("Exiting as MyHome consensus...");
				return cleanAddress.getGeocode();
			}*/

			try {
				osiGeocode = osiGeocodeFacade.osiSearch(cleanAddress);
				logger.info("CrossCheck ::Found match of lat : ["
						+ osiGeocode.getLatitude() + "] lng : ["
						+ osiGeocode.getLongitude() + "] from OSI");
			} catch (Exception ex) {
				logger.info("CrossCheck:: Error cross checking address for ["
						+ cleanAddress.getAddressLine1() + "]  " + ex.getMessage());
			}

			codeMatchResult = cleanAddress.getGeocode().checkIfGeoCodeMatch(
					osiGeocode, myHomeGeocode);

			// Check the code match result
			if (codeMatchResult == GeoCode.GEO_CROSS_CHECK_CODE_MYHOME_RECOM) {
				geocode = myHomeGeocode;
			} else if (codeMatchResult == GeoCode.GEO_CROSS_CHECK_CODE_OSI_RECOM) {
				geocode = osiGeocode;

			} else {

				geocode = cleanAddress.getGeocode();
			}
			geocode.setCrossCheckCode(codeMatchResult);

			logger.info("CrossCheck :: Original : [" + cleanAddress.concatAddress()
					+ "] Recom: [" + geocode.getFormattedAddress() + "] GeoCode : [" + cleanAddress.getGeocode().getFormattedAddress() + "]");

		} catch (Exception ex) {
			logger.info("CrossCheck:: Error checkIfGeoCodeMatch ["
					+ cleanAddress.getAddressLine1() + "]  " + ex.getMessage());
			codeMatchResult = -1;
		}

		return geocode;

	}

}
