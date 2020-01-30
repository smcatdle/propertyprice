/**
 * 
 */
package com.company.geo;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;



/**
 * @author smcardle
 *
 */
public class ViewPortGridDivisorTest {

	private final Logger logger = Logger.getLogger(ViewPortGridDivisorTest.class.getName());
	
	@Test
	public void testGetGridReferenceId() {
	
		try {
			Coordinate testCoord = new Coordinate();
			/*return (cord.getLat() <= 54.00935 && cord.getLat() >= 53.06264
				&& cord.getLng() >= -7.44873 && cord.getLng() <= -5.97656);*/
			
			testCoord.setLat((float)54.00935);
			testCoord.setLng((float)-5.97656);
			
			ViewPortGridDivisor viewPortGridDivisor = new ViewPortGridDivisor();
			viewPortGridDivisor.setViewPort(ViewPort.VIEWPORT_IRELAND);
			
			int gridReferenceId = viewPortGridDivisor.getGridReferenceId(testCoord);
			
			logger.log(Level.INFO, "The grid reference id is : [" + gridReferenceId + "]");
			
  		} catch (Exception ex) {
  			logger.log(Level.SEVERE, ex.getMessage());
  		}
	}
	
	@Test
	public void testgetCellViewPort() {	
		
		ViewPortGridDivisor viewPortGridDivisor = new ViewPortGridDivisor();
		viewPortGridDivisor.setViewPort(ViewPort.VIEWPORT_IRELAND);
		
		ViewPort cellViewPort = viewPortGridDivisor.getCellViewPort(352903);
		
		logger.log(Level.INFO, "The cell viewport is : [" + cellViewPort + "]");
	}
	
	
}
