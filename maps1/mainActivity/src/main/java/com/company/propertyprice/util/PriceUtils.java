/**
 * 
 */
package com.company.propertyprice.util;


/**
 * @author smcardle
 *
 */
public class PriceUtils {

	private static double colour = 0;
	
	public synchronized static double colourCheck(double price) {
		
		if (price < 100000) {
			colour = 240.0;
		} else if (price < 1000000) {

			// Gradient scale from 0 to 180
			// Calculate the number of gradients for this price
			double gradients = Math.abs(1 -(price-100000)/900000);
			colour = Math.floor(gradients * 180);
			
		} else {
			colour = 0;
		}
		
		return colour;
	}
	
	
}
