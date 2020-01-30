package com.company.geo;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;
import com.company.utils.network.HttpUtils;

public class ViewPortRetrievalTest {

    private final Logger logger = Logger.getLogger(ViewPortRetrievalTest.class
	    .getName());

    /*@Test
    public void test() {

	String result = null;
	String url = null;
	ViewPortGridDivisor viewPortGridDivisor = null;
	int selectedGridReferenceId = 521300;

	viewPortGridDivisor = new ViewPortGridDivisor(ViewPort.VIEWPORT_IRELAND);
	viewPortGridDivisor.setNoLatitudeDivisions(1000);
	viewPortGridDivisor.setNoLongitudeDivisions(1000);

	// Get the current view port
	ViewPort cellViewPort = viewPortGridDivisor
		.getCellViewPort(selectedGridReferenceId);

	// Retrieve all the surrounding grids
	List<ViewPort> viewPorts = viewPortGridDivisor
		.getSurroundingGrids(cellViewPort);

	for (ViewPort viewPort : viewPorts) {

	    // TODO: Retrieve grids in batches from the server (similar to grid
	    // averages)
	    url = "http://webcache20-property20.rhcloud.com/web-cache-server/SaleRetrievalServlet?"
		    + "key" + "=" + viewPort.getKey();

	    try {
		result = HttpUtils.httpRequest(url, HttpUtils.METHOD_GET, null,
			null);
	    } catch (IOException e) {
		logger.log(Level.SEVERE, e.getMessage());
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    if (result != null && !"".equals(result) && !"null".equals(result)) {
		logger.log(Level.INFO, result);

		// Save the grid to the DB
		// PropertyMDODAO.getInstance(getBaseContext()).onAddPropertyMDO(result,
		// viewPortGridDivisor.getGridReferenceId(viewPort.getCenter()),
		// new Date());
	    }

	}
    }

    @Test
    public void testGetRadialGrids() {

	ViewPortGridDivisor viewPortGridDivisor = new ViewPortGridDivisor(
		ViewPort.VIEWPORT_IRELAND);
	viewPortGridDivisor.setNoLatitudeDivisions(1000);
	viewPortGridDivisor.setNoLongitudeDivisions(1000);

	String result = null;
	String url = null;
	int selectedGridReferenceId = 521300;

	ViewPort cellViewPort = viewPortGridDivisor
		.getCellViewPort(selectedGridReferenceId);

	List<ViewPort> viewPorts = viewPortGridDivisor.getRadialGrids(
		cellViewPort, 1);

	logger.log(Level.INFO, "Processed [" + viewPorts.size() + "]");

	for (ViewPort viewPort : viewPorts) {

	    // TODO: Retrieve grids in batches from the server (similar to grid
	    // averages)
	    url = "http://webcache20-property20.rhcloud.com/web-cache-server/SaleRetrievalServlet?"
		    + "key" + "=" + viewPort.getKey();

	    try {
		result = HttpUtils.httpRequest(url, HttpUtils.METHOD_GET, null,
			null);
	    } catch (IOException e) {
		logger.log(Level.SEVERE, e.getMessage());
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    if (result != null && !"".equals(result) && !"null".equals(result)) {
		logger.log(Level.INFO, result);

		// Save the grid to the DB
		// PropertyMDODAO.getInstance(getBaseContext()).onAddPropertyMDO(result,
		// viewPortGridDivisor.getGridReferenceId(viewPort.getCenter()),
		// new Date());
	    }
	}
    }*/

    @Test
    public void testCacheRetrieval() {

	ViewPortGridDivisor viewPortGridDivisor = new ViewPortGridDivisor(
		ViewPort.VIEWPORT_IRELAND);

	String result = null;
	String url = null;
	int selectedGridReferenceId = 0;

	for (int rowId = 363; rowId < 364; rowId++) {
	    for (int cellId = 613; cellId < 614; cellId++) {
		selectedGridReferenceId = (rowId * 1000) + cellId;

		logger.log(Level.INFO, "selectedGridReferenceId [" + selectedGridReferenceId + "]");
		
		ViewPort cellViewPort = viewPortGridDivisor
			.getCellViewPort(selectedGridReferenceId);
		logger.log(Level.INFO, "ViewPort [" + cellViewPort.toString()
			+ "]");

	    }
	}
    }
}
