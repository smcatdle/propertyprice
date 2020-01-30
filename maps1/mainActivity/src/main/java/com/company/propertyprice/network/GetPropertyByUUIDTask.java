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

import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertyprice.util.RemoteLog;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author smcardle
 * 
 */
public class GetPropertyByUUIDTask extends AsyncTask {

	private MainActivity callback = null;

	public GetPropertyByUUIDTask() {

	}

	@Override
	protected String doInBackground(Object... urls) {

		// params comes from the execute() call: params[0] is the url.
		try {

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
				if (resultString != null
						&& !resultString.isEmpty()
						&& !resultString
								.contains("Unable to reach endpoint")
						&& resultString.contains("{")) {

				    RemoteLog.network("GetPropertyByUUIDTask : " + resultString);
				    
					Type collectionType = new TypeToken<PropertyMDO>() {
					}.getType();
					PropertyMDO property = new GsonBuilder()
							.setDateFormat("MMM dd, yyyy").create()
							.fromJson((String) result, collectionType);

				    RemoteLog.network("GetPropertyByUUIDTask : " + property.getA1());
					callback.viewProperty(property);
				}
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
			Log.d("GetPropertyByUUIDTask", "The response is: " + response);
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
	 * @param callback
	 *            the callbackContext to set
	 */
	public void setCallback(MainActivity callback) {
		this.callback = callback;
	}

}
