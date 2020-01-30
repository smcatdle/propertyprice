/**
 * 
 */
package com.company.propertyprice.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.propertyprice.model.Address;


/**
 * @author smcardle
 * 
 */
public class AddressUtils {

    private static final Logger logger = Logger.getLogger(AddressUtils.class
	    .getName());

    public static synchronized String concatAddress(Address address) {

	String addressText = "";

	addressText = addAddressLine(addressText, address.getAddressLine1());
	addressText = addAddressLine(addressText, address.getAddressLine2());
	addressText = addAddressLine(addressText, address.getAddressLine3());
	addressText = addAddressLine(addressText, address.getAddressLine4());
	addressText = addAddressLine(addressText, address.getAddressLine5());

	addressText = addressText + ", Ireland";

	logger.log(Level.INFO, "The concat address is : [" + addressText + "]");

	return addressText;
    }

    public String formatAddress(Address address) {

	String addressText = address.getAddressLine1().trim();

	// Check the address2 field is not blank before appending
	if (address.getAddressLine2() != null
		&& address.getAddressLine2().length() > 1) {
	    addressText = addressText + ",\n"
		    + address.getAddressLine2().trim();
	}

	// Check the address3 field is not blank before appending
	if (address.getAddressLine3() != null
		&& address.getAddressLine3().length() > 1) {
	    addressText = addressText + ",\n"
		    + address.getAddressLine3().trim();
	}

	// Check the address4 field is not blank before appending
	if (address.getAddressLine4() != null
		&& address.getAddressLine4().length() > 1) {
	    addressText = addressText + ",\n"
		    + address.getAddressLine4().trim();
	}

	// Check the address5 field is not blank before appending
	if (address.getAddressLine5() != null
		&& address.getAddressLine5().length() > 1) {
	    addressText = addressText + ",\n"
		    + address.getAddressLine5().trim();
	}

	addressText = addressText + ".\n";

	logger.log(Level.INFO, "The formatted address is : [" + addressText
		+ "]");

	return addressText;
    }

    private static synchronized String addAddressLine(
	    String currentAddressLine, String line) {
	String addressLine = "";

	// Check the address line field is not blank before appending
	if (line != null && line.length() > 1) {
	    if (currentAddressLine != null & currentAddressLine.length() > 0) {
		addressLine = currentAddressLine + "," + line.trim();
	    } else {
		addressLine = line;
	    }

	} else {
	    addressLine = currentAddressLine;
	}

	return addressLine;
    }
}
