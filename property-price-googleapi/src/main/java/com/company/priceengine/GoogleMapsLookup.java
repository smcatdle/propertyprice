/**
 * 
 */
package com.company.priceengine;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author smcardle
 *
 */
public class GoogleMapsLookup {

	public static final String GOOGLE_MAPS_URL = "https://www.google.ie/maps/@53.3550068,-6.2500853,12z";
	
    private final Logger logger = Logger.getLogger(GoogleMapsLookup.class.getName());
	
    public GeoCode getGeoCodeFromGoogleMapsPage(WebClient webClient, Address address) {
    	
    	HtmlPage returnPage = null;
    	GeoCode geoCode = null;
    		
    	/*WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.closeAllWindows();
        webClient.getOptions().setJavaScriptEnabled(true);*/
        
        try {
        	 webClient.closeAllWindows();
        	 Thread.sleep(2000L);
        	 
            HtmlPage page = (HtmlPage)webClient.getPage(GOOGLE_MAPS_URL);
            Thread.sleep(10000);
            
            String redirectedUrl = page.getUrl().toString();
            
            HtmlElement htmlElement = page.getDocumentElement();
            
            // Search Address Text
            List<HtmlElement> searchAddress = (List<HtmlElement>)htmlElement.getByXPath("//input [contains(@class,'gbqfif')]");
            HtmlInput searchAddressElement = (HtmlInput)searchAddress.get(0);
            String addressText = address.getAddressLine1().trim() + "," + address.getAddressLine2().trim();
            
            // Check the address3 field is not blank before appending
            if (address.getAddressLine3() != null && !"".equalsIgnoreCase(address.getAddressLine3())) {
            	addressText = addressText + "," + address.getAddressLine3().trim();
            }
            
            searchAddressElement.setValueAttribute(addressText);
            
            // Submit Button
            List<HtmlElement> submitList = (List<HtmlElement>)htmlElement.getByXPath("//button [contains(@class,'gbqfb')]");
            HtmlElement submitElement = (HtmlElement)submitList.get(0);
            returnPage = submitElement.click();
            
            Thread.sleep(10000);
            geoCode = extractGeoCodes(returnPage.getHead().asXml());
            logger.log(Level.INFO, "GeoCode is lat [" + geoCode.getLatitude() + "] : long [" + geoCode.getLongitude() + "]");

        } catch (Exception ex) {
        	logger.log(Level.SEVERE, "Returned url : [" + ex.getMessage() + "]");
        }
         
        
        return geoCode;
    }
    
    private GeoCode extractGeoCodes(String xml) {
    	
    	GeoCode geoCode = new GeoCode();
    	//int startIndex = xml.indexOf("amp;vp");
    	int startIndex = xml.indexOf("mod_mva");
    	
    	String substring = xml.substring(startIndex-500, startIndex+10000);
    	int startvpTokens = substring.indexOf("vp=");
    	int endvpTokens = substring.substring(startvpTokens).indexOf("amp");
    	String vpTokens = substring.substring(startvpTokens+3, startvpTokens+endvpTokens-1);
    	logger.log(Level.INFO, "vpTokens : [" + vpTokens + "]");
    	String[] vpTokensSplit = vpTokens.split(",");
    	
    	geoCode.setLatitude(new Double(vpTokensSplit[0]).doubleValue());
    	geoCode.setLongitude(new Double(vpTokensSplit[1]).doubleValue());
    	
    	return geoCode;
    }
}
