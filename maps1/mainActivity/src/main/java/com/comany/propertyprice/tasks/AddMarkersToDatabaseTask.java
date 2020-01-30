/**
 * 
 */
package com.comany.propertyprice.tasks;


import java.util.List;

import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.dao.AddressDAO;
import com.company.propertyprice.dao.GeoCodeDAO;
import com.company.propertyprice.dao.PropertySaleDAO;
import com.company.propertyprice.model.mdo.PropertyMDO;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author smcardle
 *
 */
public class AddMarkersToDatabaseTask extends AsyncTask {
	
    private MainActivity activity = null;
    private List<PropertyMDO> properties = null;
    private int selectedGridReferenceId = 0;

	
    public AddMarkersToDatabaseTask(List<PropertyMDO> properties, int selectedGridReferenceId) {
	this.properties = properties;
	this.selectedGridReferenceId = selectedGridReferenceId;
    }
	

    @Override
    protected Boolean doInBackground(Object... urls) {
 
	PropertySaleDAO propertySaleDAO = PropertySaleDAO.getInstance(activity);
	AddressDAO addressDAO = AddressDAO.getInstance(activity);
	GeoCodeDAO geoCodeDAO = GeoCodeDAO.getInstance(activity);

	// First delete all properties inside a grid
	propertySaleDAO.onDeletePropertySaleItem(selectedGridReferenceId);
	addressDAO.onDeleteAddressItem(selectedGridReferenceId);
	geoCodeDAO.onDeleteGeoCodeItem(selectedGridReferenceId);
	
	// Add all the retrieved properties
	for (PropertyMDO property : properties) {
	    
		// Set the update date
		/*sale.setDateUpdated(new Date());
		address.setDateUpdated(new Date());
		geocode.setDateUpdated(new Date());

		// Save the property in the sqlite database
		propertySaleDAO.onAddPropertySale(sale, selectedGridReferenceId);
		addressDAO.onAddAddress(sale.getAddress(), selectedGridReferenceId);
		geoCodeDAO.onAddGeoCode(sale.getAddress().getGeocode(),
				selectedGridReferenceId);*/

	}
	
        return true;
    }
    
    
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Object result) {
        // params comes from the execute() call: params[0] is the url.
	
        try {
            
            	//PerformanceTimer perfTimer = PerformanceTimer.getInstance();
		//perfTimer.getTime("updateMarkers", "");
		
		//Toast.makeText(activity, "updateMarkers Timer " + perfTimer.toString("updateMarkers" ),
			//Toast.LENGTH_LONG).show();
        	
        } catch (Exception ex) {
        	Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
        	Log.e("ERROR", ex.getMessage());
        }
    }

}
