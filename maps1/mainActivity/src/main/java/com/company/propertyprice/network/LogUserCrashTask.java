/**
 * 
 */
package com.company.propertyprice.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import com.company.propertyprice.model.UserCrash;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author smcardle
 * 
 */
public class LogUserCrashTask extends AsyncTask {

	public static final String LOG_USER_CRASH_ENDPOINT = "http://sales-propertyprod.rhcloud.com/property-price-server/LogUserCrashServlet";
	
	private UserCrash userCrash;
	private Gson gson = null;

	public LogUserCrashTask() {
		gson = new Gson();

	}

	@Override
	protected String doInBackground(Object... urls) {

		// params comes from the execute() call: params[0] is the url.
		try {
			String result = "";

			result = downloadUrl(LOG_USER_CRASH_ENDPOINT);

			return result;
		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(Object result) {
		Log.e("onPostExecute : ", (String) result);

	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;

		Log.e("downloadUrl : ", LOG_USER_CRASH_ENDPOINT);

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			String postParam = gson.toJson(userCrash);
			
			Log.e("onPostExecute : ", "Opened connection to url : [" + LOG_USER_CRASH_ENDPOINT
					+ "]");

			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length",
					"" + Integer.toString(postParam.getBytes().length));
			conn.setRequestProperty("Content-Language", "en-US");

			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(postParam);
			wr.flush();
			wr.close();

			int response = conn.getResponseCode();
			Log.d("LogUserCrashTask", "The response is: " + response);
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

	public UserCrash getUserCrash() {
		return userCrash;
	}

	public void setUserCrash(UserCrash userCrash) {
		this.userCrash = userCrash;
	}



}
