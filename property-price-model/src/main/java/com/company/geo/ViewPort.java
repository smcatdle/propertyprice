/**
 * 
 */
package com.company.geo;


/**
 * @author smcardle
 *
 */
public class ViewPort {
	
	public static final ViewPort VIEWPORT_IRELAND = new ViewPort(new Coordinate((float)55.421, (float)-10.493), new Coordinate((float)51.421, (float)-5.493));
	public static final ViewPort VIEWPORT_IRELAND_PEAK = new ViewPort(new Coordinate((float)54.00935, (float)-7.44873), new Coordinate((float)53.06264, (float)-5.97656));
	public static final double VIEWPORT_IRELAND_CELL_HEIGHT = 0.004;
	public static final double VIEWPORT_IRELAND_CELL_WIDTH = 0.005;

			
	private Coordinate topLeftCoord = null;
	private Coordinate bottomRightCoord = null;
	
	public ViewPort(Coordinate topLeftCoord, Coordinate bottomRightCoord) {
		this.topLeftCoord = topLeftCoord;
		this.bottomRightCoord = bottomRightCoord;
	}
	
	/**
	 * @return the topLeftCoord
	 */
	public Coordinate getTopLeftCoord() {
		return topLeftCoord;
	}	
	
	/**
	 * @param topLeftCoord the topLeftCoord to set
	 */
	public void setTopLeftCoord(Coordinate topLeftCoord) {
		this.topLeftCoord = topLeftCoord;
	}
	/**
	 * @return the bottomRightCoord
	 */
	public Coordinate getBottomRightCoord() {
		return bottomRightCoord;
	}
	/**
	 * @param bottomRightCoord the bottomRightCoord to set
	 */
	public void setBottomRightCoord(Coordinate bottomRightCoord) {
		this.bottomRightCoord = bottomRightCoord;
	}

	public String toString() {
		
		return "Viewport coordinates : topLeftLat [" + topLeftCoord.getLat() + "] \n topLeftLong [" + topLeftCoord.getLng() + "] \n bottomRightLat [" + bottomRightCoord.getLat() + "] \n \nbottomRightLong [" + bottomRightCoord.getLng() + "]";
		                                                                                                                                                                   				
	}
	
	public String getKey() {
		return topLeftCoord.getLat() + "-"
				+ topLeftCoord.getLng() + "-"
				+ bottomRightCoord.getLat() + "-"
				+ bottomRightCoord.getLng();
	}
	
	public Coordinate getCenter() {
		return new Coordinate((bottomRightCoord.getLat() + (topLeftCoord.getLat() - bottomRightCoord.getLat())/2), (bottomRightCoord.getLng() + (topLeftCoord.getLng() - bottomRightCoord.getLng())/2));
	}
	
}
