/**
 * 
 */
package com.company.priceengine;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.company.propertyprice.model.PPRSearchVariables;
import com.company.propertyprice.model.SalesWorkUnit;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author smcardle
 *
 */
public class PropertyPriceEngineTest {

    private static final Logger logger = Logger.getLogger(PropertyPriceEngineTest.class
	    .getName());
    
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testPropertyPriceEngine() {
		
		PPRPriceEngine engine = new PPRPriceEngine();
		ArrayList<String> searchLinks = new ArrayList<String>();

		/*String[] uuids = {
				"8CD18DE2E40BE89780257C3F004654CB"};
		
		for (String uuid : uuids) {
			searchLinks.add("https://www.propertypriceregister.ie/website/npsra/PPR/npsra-ppr.nsf/eStampUNID/UNID-" + uuid + "?OpenDocument");
		}*/
	        
		List<PPRSearchVariables> pprSearchCriteriaList = new ArrayList<PPRSearchVariables>();
		PPRSearchVariables pprSearchVariables = new PPRSearchVariables();
		List<String> counties = new ArrayList();
		List<String> years = new ArrayList();
		
		/*counties.add("Carlow");
		counties.add("Cavan");
		counties.add("Clare");
		counties.add("Cork");
		counties.add("Donegal");*/
		counties.add("Dublin");
		/*counties.add("Galway");
		counties.add("Kerry");
		counties.add("Kildare");
		counties.add("Kilkenny");
		counties.add("Laois");
		counties.add("Limerick");
		counties.add("Longford");
		counties.add("Louth");
		counties.add("Mayo");
		counties.add("Meath");
		counties.add("Monaghan");
		counties.add("Offaly");
		counties.add("Roscommon");
		counties.add("Sligo");
		counties.add("Tipperary");
		counties.add("Waterford");
		counties.add("Westmeath");
		counties.add("Wexford");
		counties.add("Wicklow");*/

		years.add("2010");
		years.add("2011");
		years.add("2012");
		years.add("2013");
		years.add("2014");
		
		pprSearchVariables.setCounties(counties);
		pprSearchVariables.setYears(years);
		engine.setSearchCriteria(pprSearchVariables);
		

		int count = engine.start(PPRPriceEngine.START_PAGE, searchLinks);
		logger.log(Level.INFO, "Added [" + count + "] properties.");
	}

}
