/**
 * 
 */
package com.company.geo;

/**
 * @author smcardle
 *
 */
public class Coordinate {

	private float lat;
	private float lng;
	
	public Coordinate() {
		
	}
	
	public Coordinate(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	/**
	 * @param d
	 * @param e
	 */
	public Coordinate(double d, double e) {
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * @return the lat
	 */
	public float getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}
	/**
	 * @return the lng
	 */
	public float getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(float lng) {
		this.lng = lng;
	}
	
	

	
	
}
