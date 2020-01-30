/**
 * 
 */
package com.company.propertyprice;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertypricetest.R;
import com.company.utils.DateUtils;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/**
 * @author smcardle
 *
 */
public class PropertyInfoWindowAdapter implements InfoWindowAdapter {

	private LayoutInflater inflater = null;
	private Context applicationContext = null;
	private Marker marker = null;
	
	public PropertyInfoWindowAdapter(LayoutInflater inflater, Context context) {
		this.inflater = inflater;
		this.applicationContext = context;
	}
	
	
    // Use default InfoWindow frame
    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    // Defines the contents of the InfoWindow
    @Override
    public View getInfoContents(Marker marker) {

    	PropertyMDO property = MarkerStorage.getMarker(marker);
    	
        // Getting view from the layout file info_window_layout
        View view = inflater.inflate(R.layout.info_window, null);
        
        // Dont display images with location_type= 'A' and type = 'L'
        //if (formattedAddress != null && !"".equals(formattedAddress) &&
        		//!GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE.equals(geocode.getLocationType()) && !GeoCode.GEO_TYPE_CODE_LOCALITY.equals(geocode.getType())) {
        	
        
	        //GetStreetViewImageTask task = new GetStreetViewImageTask();
	    	//task.execute("http://maps.googleapis.com/maps/api/streetview?size=" + URLEncoder.encode("160x160") + "&location=" + URLEncoder.encode(formattedAddress) + "&pitch=10");

	    	TextView addressText = (TextView) view.findViewById(R.id.address_text);
	    	TextView dateOfSaleText = (TextView) view.findViewById(R.id.date_of_sale_text);
	    	TextView priceText = (TextView) view.findViewById(R.id.price_text);
	    	Button viewButton = (Button) view.findViewById(R.id.view_property_button);
	    	
	    	addressText.setText(property.getAddress().formatAddress());
	    	dateOfSaleText.setText(DateUtils.getString(property.getD()));
	    	priceText.setText(Double.toString(property.getP()));
	    	viewButton.bringToFront();
	    	
	    	/*while (!imageRetrieved && imageRetrievalCount++ < MAX_IMAGE_RETRIEVAL_ATTEMPTS) {
	    		try {
					Thread.sleep(IMAGE_RETRIEVAL_SLEEP_PERIOD);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}*/ 
	    	
	        return view;
        /*} else {
        	return null;
        }*/
        
        // Returning the view containing InfoWindow contents


    }

    
    /*class GetStreetViewImageTask extends AsyncTask {
    	
    	private MainActivity callback = null;
    	
        @Override
        protected InputStream doInBackground(Object... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                InputStream is = downloadUrl((String)urls[0]);
                return is;
            } catch (IOException e) {
                return null;
            }
        }
        
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
        	
        	try {

        		InputStream is = (InputStream)result;
    	    	
    	    	if (is != null ) {


		            
		            marker.hideInfoWindow();
		            marker.showInfoWindow();
    	    	}
        	} catch (Exception ex) {
        		Log.e("onPostExecute : ", "Error converting to json objects : " + ex);
        	}
       }
        
    	 // Given a URL, establishes an HttpUrlConnection and retrieves
    	 // the web page content as a InputStream, which it returns as
    	 // a string.
    	 private InputStream downloadUrl(String myurl) throws IOException {
    	     InputStream is = null;
    	         
    	     Log.e("downloadUrl : ", myurl);
    	     
    	     try {
    	         URL url = new URL(myurl);
    	         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	         
    	         Log.e("onPostExecute : ", "Opened connection to url : [" + myurl + "]");
    	         
    	         conn.setReadTimeout(10000 );
    	         conn.setConnectTimeout(15000 );
    	         conn.setRequestMethod("GET");
    	         conn.setDoInput(true);
    	         // Starts the query
    	         conn.connect();
    	         int response = conn.getResponseCode();
    	         Log.d("GetPropertiesInViewportTask", "The response is: " + response);
    	         is = conn.getInputStream();
    	
		            Bitmap bitmap = BitmapFactory.decodeStream(is);
		            //Toast.makeText(applicationContext, "decoded",
		                    //Toast.LENGTH_LONG).show();
		            image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
		            imageRetrieved = true;
		        	
    	         return is;

    	         
    	     // Makes sure that the InputStream is closed after the app is
    	     // finished using it.
    	     } finally {
     
    	     }
    	 }
    	 
    }*/
}
