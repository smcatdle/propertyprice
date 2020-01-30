/**
 * 
 */
package com.company.priceengine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author smcardle
 * 
 */
public class BasicWebClientTest {
    
    
    public static final String START_PAGE = "https://www.propertypriceregister.ie";

    
    private static final Logger logger = Logger.getLogger(BasicWebClientTest.class
	    .getName());
    
    @Test
    public void test() {

	try {

	    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.closeAllWindows();
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.setUseInsecureSSL(true);

	    webClient.closeAllWindows();

	    HtmlPage page = (HtmlPage) webClient.getPage(START_PAGE);
	   
	    logger.log(Level.INFO, page.asXml());
	    
	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	      
	    e.printStackTrace();
	} catch (IOException e) {
	      
	    e.printStackTrace();
	} catch (GeneralSecurityException e) {
	      
	    e.printStackTrace();
	}

    }
    
    public static void main(String[] args) {
	
	try {

	    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.closeAllWindows();
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.setUseInsecureSSL(true);

	    webClient.closeAllWindows();

	    HtmlPage page = (HtmlPage) webClient.getPage(START_PAGE);
	   
	    logger.log(Level.INFO, page.asXml());
	    
	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	      
	    e.printStackTrace();
	} catch (IOException e) {
	      
	    e.printStackTrace();
	} catch (GeneralSecurityException e) {
	      
	    e.printStackTrace();
	}
	
    }
    
    
}
