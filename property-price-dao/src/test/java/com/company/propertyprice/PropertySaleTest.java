/**
 * 
 */
package com.company.propertyprice;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import com.company.geo.ViewPort;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.PropertySale;
import com.company.propertyprice.model.PropertySaleError;

/**
 * @author smcardle
 *
 */
public class PropertySaleTest {

	private static final Log logger = LogFactory.getLog(PropertySaleTest.class);
	
    private static SimpleDateFormat dateFormatter = null;
    private static String dateFormat = "dd/MM/yyyy";
    
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		dateFormatter = new SimpleDateFormat(dateFormat);
	}


	/*@Test
	public void testAddPropertySale() throws Exception {
		
		double random = Math.random();
		
		try {
			Address address = new Address();
			address.setAddressLine1(new Double(random).toString() +  " FITZWILLIAM QUAY");
			address.setAddressLine2("RINGSEND");
			address.setAddressLine3("DUBLIN 4");
			address.setAddressLine4("");
			address.setAddressLine5("Dublin");
			address.setGeocodeLatitude(53.3404581);
			address.setGeocodeLongitude(-6.227883299999999);
			address.setAccuracy(GeoCode.GEO_ACCURACY_CORRECT);
			
			PropertySale sale = new PropertySale();
			sale.setDateOfSale(new Date());
			sale.setFullMarketPrice(true);
			sale.setPrice(199000.99);
			sale.setAddress(address);
			sale.setDateCreated(new Date());
			sale.setDateUpdated(new Date());
			sale.setPprUrl("https://www.propertypriceregister.ie/Website/npsra/PPR/npsra-ppr.nsf/eStampUNID/UNID-702880CB6847040880257C" + new Double(random).toString() + "?OpenDocument");
			PropertyDAO.getInstance().attachDirty(sale);
			
			// Now retrieve the added entry
			PropertySale retrievedSale = PropertyDAO.getInstance().findById(sale.getId());
			
			logger.info("Found sale : Address [" + retrievedSale.getId() + "]");

			
		} catch (Exception ex) {
			logger.error("testAddPropertySale : [" + ex);
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void testGetPropertiesWithinViewport() {
		
		Coordinate topLeft = new Coordinate();
		Coordinate bottomRight = new Coordinate();
		
		topLeft.setLat((float)53.3404580000);
		topLeft.setLng((float)-6.2278832000);
		bottomRight.setLat((float)53.5004582000);
		bottomRight.setLng((float)-6.1078834000);
	
		long startTime = System.currentTimeMillis();
		List<PropertySale> sales = PropertyDAO.getInstance().getPropertiesWithinViewport(topLeft, bottomRight);
		long endTime = System.currentTimeMillis();
		
		logger.info("getPropertiesWithinViewport() took [" + (endTime - startTime) + "] milliseconds.");
		
        for (PropertySale sale : sales) {
        	logger.info("The sale price is : [" + sale.getPrice() + "]");
        	logger.info("The address is : [" + sale.getAddress().getAddressLine1() + "]");
        }
        
        //assert(!sales.isEmpty());
	}*/
	
	/*@Test
	public void testGetPropertiesByDateWithinViewport() {
		
		Coordinate topLeft = new Coordinate();
		Coordinate bottomRight = new Coordinate();
		
		Date dateFrom = null;
		Date dateTo = null;
		
		try {
			dateFrom = dateFormatter.parse("01/07/2014");
			dateTo = dateFormatter.parse("09/07/2014");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		topLeft.setLat((float)53.0004580000);
		topLeft.setLng((float)-6.8278832000);
		bottomRight.setLat((float)53.9004582000);
		bottomRight.setLng((float)-6.0078834000);
	
		long startTime = System.currentTimeMillis();
		List<PropertySale> sales = PropertyDAO.getInstance().getPropertiesByDateWithinViewport(topLeft, bottomRight, dateFrom, dateTo);
		long endTime = System.currentTimeMillis();
		
		logger.info("getPropertiesWithinViewport() took [" + (endTime - startTime) + "] milliseconds.");
		
        for (PropertySale sale : sales) {
        	logger.info("The sale price is : [" + sale.getPrice() + "]");
        	logger.info("The address is : [" + sale.getAddress().getAddressLine1() + "]");
        }
        
        //assert(!sales.isEmpty());
	}*/
	
	/*@Test
	public void testAddPropertySaleError() {
		
		PropertySaleError sale = new PropertySaleError();
		sale.setIssue("GEO");
		sale.setPprUrl("https://propertypriceregister.ie/website/npsra/PPR/npsra-ppr.nsf/eStampUNID/UNID-FC6D7523CAC85A1280257C680059AC88?OpenDocument");
		sale.setDateCreated(new Date());
		sale.setDateUpdated(new Date());
		
		PropertyDAO.getInstance().attachDirty(sale);
		
	}*/
	
	@Test
	public void testFindExistingAddress() {
	    
    		Address address = new Address();
    		address.setAddressLine1("14 Cluain Mhuire");
    		address.setAddressLine2("Pallaskenry");
    		address.setAddressLine3("Pallaskenry");
    		address.setAddressLine4("Pallaskenry");
    		address.setAddressLine5("Limerick");
		
	    List<PropertySale> sales = PropertyDAO.getInstance().findExistingAddress(address);
	    
	    for (PropertySale sale : sales) {
		logger.info("Address Line 1 : [" + sale.getAddress().getAddressLine1() + "]");
		logger.info("Address Line 2 : [" + sale.getAddress().getAddressLine2() + "]");
		logger.info("Address Line 3 : [" + sale.getAddress().getAddressLine3() + "]");
		logger.info("Address Line 4 : [" + sale.getAddress().getAddressLine4() + "]");
		logger.info("Address Line 5 : [" + sale.getAddress().getAddressLine5() + "]");
	    }
	}
	
	@Test
	public void testGetGridsWithinViewPort() {

		// Get a hibernate session
		Session session = PropertyDAO.getInstance().getSession();

		ViewPort peakViewPort = ViewPort.VIEWPORT_IRELAND_PEAK;
	    List<Integer> gridIds = PropertyDAO.getInstance().getGridsInsideViewPort(peakViewPort.getTopLeftCoord(), peakViewPort.getBottomRightCoord(), session);
	    
		logger.info("Found : [" + gridIds.size() + "] grids within peak ViewPort");

		List<PropertySale> sales = PropertyDAO.getInstance().findPropertiesInGrid(gridIds.get(0), session);
		
		logger.info("Found Property : [" + sales.get(0).getAddress().getAddressLine1() + "]");
	}
}
