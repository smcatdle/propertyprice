/**
 * 
 */
package com.comany.propertyprice.tasks;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.company.geo.Coordinate;
import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;
import com.company.propertyprice.MarkerStorage;
import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.fragments.MapFragment;
import com.google.android.gms.maps.model.Marker;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author smcardle
 *
 */
public class ClearRemoteMarkersTask extends AsyncTask {
	
	private ViewPort cellViewPort = null;
	private ViewPortGridDivisor viewPortGridDivisor = null;
	private Map gridReferenceIdCache = null;
	private MainActivity activity = null;
	
	public ClearRemoteMarkersTask(ViewPort cellViewPort, ViewPortGridDivisor viewPortGridDivisor, Map gridReferenceIdCache, MainActivity activity) {
		this.cellViewPort = cellViewPort;
		this.viewPortGridDivisor = viewPortGridDivisor;
		this.gridReferenceIdCache = gridReferenceIdCache;
		this.activity = activity;
		
	}
	

    @Override
    protected Boolean doInBackground(Object... urls) {
 
        return true;
    }
    
    
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Object result) {
        // params comes from the execute() call: params[0] is the url.
        try {
            
        	//Toast.makeText(activity, "starting ClearRemoteMarkersTask", Toast.LENGTH_SHORT).show();
        	
        	// Calculate the bounds of the grid to retain markers
        	double additionalLatBounds = ViewPort.VIEWPORT_IRELAND_CELL_HEIGHT * MapFragment.VISIBLE_MARKER_GRID;
        	double additionalLongBounds = ViewPort.VIEWPORT_IRELAND_CELL_WIDTH * MapFragment.VISIBLE_MARKER_GRID;
        	
        	List<Marker> markers = MarkerStorage.getMarkers();
        	List<Marker> markersToRemove = new ArrayList<Marker>();
        	
        	for (Marker marker : markers) {

        		double markerLat = marker.getPosition().latitude;
        		double markerLong = marker.getPosition().longitude;
        		
        		double topLeftLatBounds = cellViewPort.getTopLeftCoord().getLat() - additionalLatBounds;
        		double topLeftLongBounds = cellViewPort.getTopLeftCoord().getLng() - additionalLongBounds;
        		double bottomRightLatBounds = cellViewPort.getBottomRightCoord().getLat() + additionalLatBounds;
        		double bottomRightLongBounds = cellViewPort.getBottomRightCoord().getLng() + additionalLongBounds;
        		
        		if (markerLat > topLeftLatBounds && markerLat < bottomRightLatBounds && markerLong > topLeftLongBounds && markerLong < bottomRightLongBounds) {
        			
        		} else {   // Outside the configured boundary
        			markersToRemove.add(marker);

        		}
        	}
        	
        	// Remove the markers
        	for (Marker marker : markersToRemove) {
    			marker.remove();
    			MarkerStorage.removeMarker(marker);
    			
    			// ToDo: Store the gridreferenceId with the marker to improve performance
    			Coordinate coord = new Coordinate();
    			coord.setLat((float)marker.getPosition().latitude);
    			coord.setLng((float)marker.getPosition().longitude);
    			int selectedGridReferenceId = viewPortGridDivisor.getGridReferenceId(coord);
    			gridReferenceIdCache.remove(selectedGridReferenceId);
        	}
        	//Toast.makeText(activity, "completed ClearRemoteMarkersTask", Toast.LENGTH_SHORT).show();
        	
        } catch (Exception ex) {
        	Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
        	Log.e("ERROR", ex.getMessage());
        }
    }

}
