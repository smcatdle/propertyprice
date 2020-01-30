/**
 * 
 */
package com.company.priceengine;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.company.propertyprice.model.Address;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;


/**
 * @author smcardle
 *
 */
public class GoogleMapsLookupTest {

    private final Logger logger = Logger.getLogger(GoogleMapsLookupTest.class.getName());
    private  WebClient webClient;
    
	@Before
	public void setup() {
        webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.closeAllWindows();
        webClient.getOptions().setJavaScriptEnabled(true);
        //webClient.setAjaxController(new NicelyResynchronizingAjaxController());
	}
	
	@Test
	public void testGoogleMapsLookup() {
		
		Address address = new Address();
		address.setAddressLine1("14 Cluain Mhuire");
		address.setAddressLine2("Pallaskenry");
		address.setAddressLine5("Limerick");
		
		GoogleMapsLookup lookup = new GoogleMapsLookup();
		lookup.getGeoCodeFromGoogleMapsPage(webClient, address);
	}
    
}
