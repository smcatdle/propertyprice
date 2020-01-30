/**
 * 
 */
package com.comany.propertyprice.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.company.exception.GoogleLimitException;
import com.company.priceengine.GoogleGeocodeFacade;
import com.company.propertyprice.fragments.MapFragment;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.mdo.PropertyMDO;

/**
 * @author smcardle
 *
 */
public class SearchResultsActivityTask extends AsyncTask {
	
    private MapFragment callback;
    
	public SearchResultsActivityTask(MapFragment callback) {
    	this.callback = callback;
	}
	
    @Override
    protected GeoCode doInBackground(Object... urls) {
          
        // params comes from the execute() call: params[0] is the url.
    	return handleSearchAddress((String)urls[0]);

    }
    
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Object result) {
	
    PropertyMDO property = (PropertyMDO)result;
	if (property != null && property.getL() != 0 && property.getO() != 0) {
	    callback.findLocationOnMap(property);
	} 
   }
    
    private GeoCode handleSearchAddress(String query) {

    	String[] addressStrings = query.split(",");
		Address address = new Address();
		GeoCode geocode = null;
		
		if (addressStrings.length > 0) {
			address.setAddressLine1(addressStrings[0]);
		}
		if (addressStrings.length > 1) {
			address.setAddressLine2(addressStrings[1]);
		}
		if (addressStrings.length > 2) {
			address.setAddressLine3(addressStrings[2]); 
		}
		if (addressStrings.length > 3) {
			address.setAddressLine4(addressStrings[3]);
		}
		if (addressStrings.length > 4) {
			address.setAddressLine5(addressStrings[4]);
		}
		
		GoogleGeocodeFacade facade = GoogleGeocodeFacade.getInstance();

		try {
			
			geocode = facade.getGeocode(address);
			Log.e("SearchResultsActivity", geocode.getLatitude() + "," + geocode.getLongitude());
			return geocode;
	    	
		} catch (GoogleLimitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return geocode;
    }
    
    
}
