/**
 * 
 */
package com.company.geo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author smcardle
 * 
 *         * A model for dividing a large area into manageable grids. Properties
 *         can be updated by grid and date period.
 * 
 */
public class ViewPortGridDivisor {

	private final Logger logger = Logger.getLogger(ViewPortGridDivisor.class
			.getName());

	private ViewPort viewPort = null;
	//private int noLongitudeDivisions = 0;
	//private int noLatitudeDivisions = 0;
	//private float cellLengthLat = 0;
	//private float cellLengthLng = 0;
	private int noCellsInRow = 1000;
	private boolean initialised = false;

	public ViewPortGridDivisor() {
		initialise();

	}

	public ViewPortGridDivisor(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	private void initialise() {

		if (viewPort != null && viewPort.getTopLeftCoord() != null
				&& viewPort.getBottomRightCoord() != null) {
			/*cellLengthLat = Math.abs((Math.abs(viewPort.getTopLeftCoord()
					.getLat()) - Math.abs(viewPort.getBottomRightCoord()
					.getLat()))
					/ noLatitudeDivisions);
			cellLengthLng = Math.abs((Math.abs(viewPort.getTopLeftCoord()
					.getLng()) - Math.abs(viewPort.getBottomRightCoord()
					.getLng()))
					/ noLatitudeDivisions);
			noCellsInRow = Math.abs(new Float((Math.abs(viewPort
					.getTopLeftCoord().getLng()) - Math.abs(viewPort
					.getBottomRightCoord().getLng()))
					/ cellLengthLng).intValue());*/

			initialised = true;
		}

	}

	/**
	 * @param viewPort
	 *            the viewPort to set
	 */
	public void setViewPort(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	/**
	 * @param noLongitudeDivisions
	 *            the noLongitudeDivisions to set
	 */
	/*public void setNoLongitudeDivisions(int noLongitudeDivisions) {
		this.noLongitudeDivisions = noLongitudeDivisions;
	}*/

	/**
	 * @param noLatitudeDivisions
	 *            the noLatitudeDivisions to set
	 */
	/*public void setNoLatitudeDivisions(int noLatitudeDivisions) {
		this.noLatitudeDivisions = noLatitudeDivisions;
	}*/

	public int getGridReferenceId(Coordinate cord) {

		int gridReferenceId = 0;

		if (!initialised)
			initialise();

		// Grid References are allocated first along the longitudenal divisions
		// line (row), then latitudanal (rows).

		float latDifference = Math.abs(viewPort.getTopLeftCoord().getLat()
				- cord.getLat());
		float lngDifference = Math.abs(viewPort.getTopLeftCoord().getLng()
				- cord.getLng());

		int cellId = new Double(lngDifference
				/ ViewPort.VIEWPORT_IRELAND_CELL_WIDTH).intValue();

		logger.log(Level.INFO, "cell id : [" + cellId + "] floor [" + "]");

		int rowId = new Double(latDifference / ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT)
				.intValue();

		// Determine which cell on the grid the coordinate lives in
		gridReferenceId = (rowId * 1000) + cellId;

		return gridReferenceId;

	}

	public ViewPort getCellViewPort(int referenceId) {

		if (!initialised)
			initialise();

		int rowId = (int) Math.floor(referenceId/1000);
		int cellId = referenceId - (rowId*1000);
		
		logger.log(Level.INFO, "cell id : [" + cellId + "] rowId [" + rowId + "] floor ["
				+ Math.floor(referenceId/1000) + "]");

		double bottomRightLat = viewPort.getTopLeftCoord().getLat()
				- ((rowId + 1) * ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT);
		double bottomRightLong = viewPort.getTopLeftCoord().getLng()
				+ ((cellId + 1) * ViewPort.VIEWPORT_IRELAND_CELL_WIDTH);
		double topLeftLat = viewPort.getTopLeftCoord().getLat()
				- (rowId * ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT);
		double topLeftLong = viewPort.getTopLeftCoord().getLng()
				+ ((cellId) * ViewPort.VIEWPORT_IRELAND_CELL_WIDTH);

		ViewPort viewPort = new ViewPort(
				new Coordinate((float)topLeftLat, (float)topLeftLong), new Coordinate(
					(float)bottomRightLat, (float)bottomRightLong));

		return viewPort;
	}


	// Only cache sales within the general Leinster area (where most traffic
	// will be from)
	// 54.00935732030725--7.448730466250026-53.06264923908647-5.976562497500026
	public boolean checkIfGridWithinPeakArea(ViewPort cellViewPort) {

		Coordinate cord = cellViewPort.getTopLeftCoord();
		ViewPort peakViewPort = ViewPort.VIEWPORT_IRELAND_PEAK;
		
		return (cord.getLat() <= peakViewPort.getTopLeftCoord().getLat() && cord.getLat() >= peakViewPort.getBottomRightCoord().getLat()
				&& cord.getLng() >= peakViewPort.getTopLeftCoord().getLng() && cord.getLng() <= peakViewPort.getBottomRightCoord().getLng());
	}

	public boolean checkIfCoordWithinPeakArea(Coordinate cord) {

		ViewPort peakViewPort = ViewPort.VIEWPORT_IRELAND_PEAK;
		
		return (cord.getLat() <= peakViewPort.getTopLeftCoord().getLat() && cord.getLat() >= peakViewPort.getBottomRightCoord().getLat()
				&& cord.getLng() >= peakViewPort.getTopLeftCoord().getLng() && cord.getLng() <= peakViewPort.getBottomRightCoord().getLng());
	}
	
	// TODO: Need to fine tune this to bring back minimum number grids within
	// boundary
	public List<ViewPort> getSurroundingGrids(ViewPort viewPort) {

		Coordinate topLeft = viewPort.getTopLeftCoord();
		List<ViewPort> viewPorts = new ArrayList<ViewPort>();
		Coordinate coord = null;

		// Calculate offsets on topLeft coordinate for each surrounding grid.
		double[] latOffsets = { 0.3, 0.3, 0.3, -0.3, -1.3, -1.3, -1.3, -0.3 };
		double[] lngOffsets = { -0.3, 0.3, 1.3, 1.3, 1.3, 0.3, -0.3, -0.3 };
		// double[] latOffsets = { 0.3, -1.3};
		// double[] lngOffsets = { 0.3, 0.3};

		for (int i = 0; i < latOffsets.length; i++) {
			coord = new Coordinate(
					(float) (topLeft.getLat() + (latOffsets[i] * ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT)),
					(float) (topLeft.getLng() + (lngOffsets[i] * ViewPort.VIEWPORT_IRELAND_CELL_WIDTH)));
			int gridId = getGridReferenceId(coord);
			viewPorts.add(getCellViewPort(gridId));
		}

		return viewPorts;
	}
	

	public List<ViewPort> getRadialGrids(ViewPort viewPort, int radius) {

		Coordinate center = viewPort.getCenter();
		List<ViewPort> viewPorts = new ArrayList<ViewPort>();
		Coordinate coord = null;
		int containingGridWidth = ((radius*2)+1);
		
		// Calculate offsets on topLeft coordinate for each surrounding grid.
		double[] latOffsets = new double[containingGridWidth*containingGridWidth]; //{ 0.3, 0.3, 0.3, -0.3, -1.3, -1.3, -1.3, -0.3 };
		double[] lngOffsets = new double[containingGridWidth*containingGridWidth]; //{ -0.3, 0.3, 1.3, 1.3, 1.3, 0.3, -0.3, -0.3 };

		// Translate to the center of the top left grid of the new containing grid
		Coordinate topLeftGridCenter = new Coordinate(((float)center.getLat()+(radius*ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT)), ((float)center.getLng()-(radius*ViewPort.VIEWPORT_IRELAND_CELL_WIDTH)));
		
		// Create offsets
		for (int i = 0; i < containingGridWidth; i++) {
 
			for (int j=0; j < containingGridWidth; j++) {
				latOffsets[(i*containingGridWidth)+j] = -i;
				lngOffsets[(i*containingGridWidth)+j] = j;
			}
		
		}
		
		for (int i = 0; i < latOffsets.length; i++) {
			coord = new Coordinate(
					(float) (topLeftGridCenter.getLat() + (latOffsets[i] * ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT)),
					(float) (topLeftGridCenter.getLng() + (lngOffsets[i] * ViewPort.VIEWPORT_IRELAND_CELL_WIDTH)));
			int gridId = getGridReferenceId(coord);
			viewPorts.add(getCellViewPort(gridId));
		}

		return viewPorts;
	}
}
