/**
 * 
 */
package com.company.priceengine;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.company.propertyprice.model.PPRSearchVariables;

/**
 * @author smcardle
 *
 */
public class RyanairEngineTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testPropertyPriceEngine() {
		
		RyanairEngine engine = new RyanairEngine();
		ArrayList<String> searchLinks = new ArrayList<String>();

		engine.start(RyanairEngine.START_PAGE, searchLinks);
		
	}

}
