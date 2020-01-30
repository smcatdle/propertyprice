/**
 * 
 */
package com.company.propertyprice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.propertyprice.model.mdo.PropertyMDO;
import com.google.android.gms.maps.model.Marker;

/**
 * @author smcardle
 *
 */
public class MarkerStorage {

	private static Map<Integer, PropertyMDO> sales = new HashMap<Integer, PropertyMDO>();
	private static List<Marker> markers = new ArrayList<Marker>();
	
	public static void addMarker(Marker marker, PropertyMDO propertySale) {
		sales.put(marker.hashCode(), propertySale);
		markers.add(marker);
	}
	
	public static PropertyMDO removeMarker(Marker marker) {
		markers.remove(marker);
		return sales.remove(marker.hashCode());
	}
	
	public static PropertyMDO getMarker(Marker marker) {
		return sales.get(marker.hashCode());
	}
	
	public static List<Marker> getMarkers() {
		return markers;
	}
	
	public static void removeAllMarkers() {

	    sales.clear();
	    markers.clear();
	}
	
}
