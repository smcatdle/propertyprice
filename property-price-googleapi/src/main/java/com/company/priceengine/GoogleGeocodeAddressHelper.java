package com.company.priceengine;

import java.util.HashMap;
import java.util.logging.Logger;

import com.company.propertyprice.model.Address;


public class GoogleGeocodeAddressHelper {

	private static final Logger logger = Logger.getLogger(GoogleGeocodeAddressHelper.class.getName());
  
	private static HashMap<String, String> abbreviations = new HashMap<String, String>();

	static {
		abbreviations.put("Square", "Square");
		abbreviations.put("Crescent", "Crescent");
		abbreviations.put("Road", "Road");
		abbreviations.put("Court", "Court");
		abbreviations.put("Avenue", "Avenue");
		abbreviations.put("Terrace", "Terrace");
		abbreviations.put("Green", "Green");
		abbreviations.put("Park", "Park");
	}
	
  	public static Address reformatAddress(Address address) {
  		
  		Address reformatedAddress = new Address();
  		
  		reformatedAddress.setAddressLine1(removeBadCharachters(address.getAddressLine1()));
  		reformatedAddress.setAddressLine2(removeBadCharachters(address.getAddressLine2()));
  		reformatedAddress.setAddressLine3(removeBadCharachters(address.getAddressLine3()));
  		reformatedAddress.setAddressLine4(removeBadCharachters(address.getAddressLine4()));
  		reformatedAddress.setAddressLine5(removeBadCharachters(address.getAddressLine5()));
  		
  		//reformatedAddress.setAddressLine1(expandAbbrev(reformatedAddress.getAddressLine1()));
  		//reformatedAddress.setAddressLine2(expandAbbrev(reformatedAddress.getAddressLine2()));
  		
  		return reformatedAddress;
  	}
  	
  	
  	private static String removeBadCharachters(String str) {
  		
  		String newString = "";
  		
  		if (str != null) {
	  		newString = str.replace("'","");
	  		newString = newString.replace("\"","");
	  		newString = newString.replace(".","");
	  		newString = newString.replace(",","");
  		}
  		
  		return newString;
  	}
  	
  	private static String expandAbbrev(String str) {
  		
  		String newString = "";
  		
  		if (str != null) {
	  		newString = str.replace("'","");
	  		newString = newString.replace("\"","");
	  		newString = newString.replace(".","");
	  		newString = newString.replace(",","");
  		}
  		
  		return newString;
  	}
  	
  	
}