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
import java.util.ArrayList;
import java.util.List;

import com.company.geo.GraphPoint;
import com.company.propertyprice.fragments.GraphFragment;
import com.company.utils.PerformanceTimer;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author smcardle
 * 
 */
public class GetGraphDataTask extends AsyncTask {

	private GraphFragment callback = null;

	public GetGraphDataTask() {
		
	}

	@Override
	protected String doInBackground(Object... urls) {

		// params comes from the execute() call: params[0] is the url.
		try {
			PerformanceTimer perfTimer = PerformanceTimer.getInstance();
			perfTimer.start("getGraph");

			String url = (String) urls[0];
			String result = "";

			if (url != null) {
				result = downloadUrl(url);
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
				perfTimer.getTime("getGraph", "");
				if (resultString != null
						&& !resultString.isEmpty()
						&& !resultString
								.contains("Unable to retrieve web page")
						&& resultString.contains("{")) {

					Type collectionType = new TypeToken<ArrayList<GraphPoint>>() {
					}.getType();
					List<GraphPoint> graphPoints = new GsonBuilder()
							.setDateFormat("MMM dd, yyyy").create()
							.fromJson((String) result, collectionType);

					callback.updatePlot(graphPoints);
				} else {
					//gridReferenceIdCache.remove(selectedGridReferenceId);
				}
				perfTimer.getTime("updateMarkers", "");
			}
		} catch (Exception ex) {
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
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d("GetGraphDataTask", "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readIt(is);

			Log.e("onPostExecute : ", "Retrieved content from server : ["
					+ contentAsString + "]");

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
	 * @param fragment
	 *            the callbackContext to set
	 */
	public void setCallback(GraphFragment fragment) {
		this.callback = fragment;
	}

}
