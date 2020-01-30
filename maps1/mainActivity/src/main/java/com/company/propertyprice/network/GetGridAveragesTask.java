/**
 * 
 */
package com.company.propertyprice.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.company.geo.ViewPort;
import com.company.propertyprice.fragments.MapFragment;
import com.company.propertyprice.model.GridAveragesWrapper;
import com.company.propertyprice.util.RemoteLog;
import com.company.utils.PerformanceTimer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author smcardle
 * 
 */
public class GetGridAveragesTask extends AsyncTask {
	
	private MapFragment callback = null;
	private String url = null;
	Gson gson = null;
	private List<ViewPort> viewPorts = null;

	public GetGridAveragesTask(MapFragment callback) {
		this.callback = callback;
		this.gson = new Gson();
	}

	@Override
	protected String doInBackground(Object... urls) {

		// params comes from the execute() call: params[0] is the url.
		try {
			PerformanceTimer perfTimer = PerformanceTimer.getInstance();
			perfTimer.start("updateGridIcons");

			url = (String) urls[0];
			String result = "";
			String requestParam = "";
			int counter = 0;
			String comma = "";
			
			if (url != null) {
				
				// Add the viewport keys as a request param
				for (ViewPort viewPort : viewPorts) {
					if (counter++ > 0) comma = ",";
					requestParam = requestParam + comma + viewPort.getKey();
				}
				
				RemoteLog.log("viewPorts [" + viewPorts.size() + "] requestParam [" + requestParam + "]");
				
				result = downloadUrl(url + requestParam);
			}

			return result;
		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(Object result) {
		Log.e("onPostExecute : ", (String) result);

		try {
			if (result != null) {
				String resultString = (String) result;
				PerformanceTimer perfTimer = PerformanceTimer.getInstance();
				perfTimer.getTime("updateGridIcons", "");
				if (resultString != null
						&& !resultString.isEmpty()
						&& !resultString
								.contains("Unable to retrieve web page")
						&& resultString.contains("{")) {

					String jsonString = resultString.substring(10, resultString.length()-2);
					
					RemoteLog.log("jsonString [" + jsonString + "]");
					
					Type collectionType = new TypeToken<List<GridAveragesWrapper>>() {
					}.getType();
					List<GridAveragesWrapper> gridAveragesWrapperList =(List<GridAveragesWrapper>) new GsonBuilder()
							.create()
							.fromJson((String) jsonString, collectionType);
						
						//RemoteLog.log("gridAveragesWrapper [" + gridAveragesWrapperList.get(0).getG().get("2014").getA() + "]");
						
						callback.updateGridIcons(viewPorts, gridAveragesWrapperList);
				} else {
					RemoteLog.log("url [" + url + "]");
				}
				
			}
		} catch (Exception ex) {
			RemoteLog.error(ex);
			Log.e("onPostExecute : ", "Error converting to json objects : "
					+ ex);
		}
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;

		Log.e("downloadUrl : ", myurl);

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			Log.e("onPostExecute : ", "Opened connection to url : [" + myurl
					+ "]");

			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			is = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readIt(is);

			return contentAsString;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		final StringBuilder out = new StringBuilder();
		final char[] buffer = new char[1024];
		reader = new InputStreamReader(stream, "UTF-8");

		for (;;) {
			int rsz = reader.read(buffer, 0, buffer.length);
			if (rsz < 0)
				break;
			out.append(buffer, 0, rsz);
		}

		return new String(out);
	}

	/**
	 * @param mapFragment
	 *            the callbackContext to set
	 */
	public void setCallback(MapFragment mapFragment) {
		this.callback = mapFragment;
	}

	public void setViewPorts(List<ViewPort> viewPorts) {
		this.viewPorts = viewPorts;
	}


	
}
