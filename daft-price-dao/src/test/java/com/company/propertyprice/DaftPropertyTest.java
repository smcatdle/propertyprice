/**
 * 
 */
package com.company.propertyprice;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;


import com.company.propertyprice.dao.DaftPropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.Property;

/**
 * @author smcardle
 *
 */
public class DaftPropertyTest {

	private static final Log logger = LogFactory.getLog(DaftPropertyTest.class);
	
    private static SimpleDateFormat dateFormatter = null;
    private static String dateFormat = "dd/MM/yyyy";
    
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dateFormatter = new SimpleDateFormat(dateFormat);
	}


	@Test
	public void testAddPropertySale() throws Exception {
		
		double random = Math.random();
		
		try {
			Address address = new Address();
			address.setAddressLine1(new Double(random).toString() +  " FITZWILLIAM QUAY");
			address.setAddressLine2("RINGSEND");
			address.setAddressLine3("DUBLIN 4");
			address.setAddressLine4("");
			address.setAddressLine5("Dublin");
			
			Property sale = new Property();
			sale.setPrice(199000.99);
			sale.setAddress(address);
			sale.setDateCreated(new Date());
			sale.setDateUpdated(new Date());
			sale.setDwellingType("Terraced House");
			DaftPropertyDAO.getInstance().attachDirty(sale);
			
			// Now retrieve the added entry
			/*DaftProperty retrievedSale = DaftPropertyDAO.getInstance().;
			
			logger.info("Found sale : Address [" + retrievedSale.getId() + "]");*/

			
		} catch (Exception ex) {
			logger.error("testAddPropertySale : [" + ex);
			ex.printStackTrace();
			//fail(ex.getMessage());
		}
	}

}
