/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author smcardle
 *
 */
public class DaftPriceEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testPropertyPriceEngine() {
		
		DaftPriceEngine engine = new DaftPriceEngine();
		ArrayList<String> searchLinks = new ArrayList<String>();
		
		engine.start(DaftPriceEngine.START_PAGE, searchLinks);
		
	}

}
