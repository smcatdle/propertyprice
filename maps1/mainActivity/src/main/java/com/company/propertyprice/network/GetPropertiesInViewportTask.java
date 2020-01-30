/**
 * 
 */
package com.company.propertyprice.network;

import java.io.IOException;
import java.util.Map;

import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.fragments.MapFragment;
import com.company.propertyprice.model.GridWrapper;
import com.company.propertyprice.processor.GetPropertiesPostProcessor;
import com.company.propertyprice.util.RemoteLog;
import com.company.utils.PerformanceTimer;
import com.company.utils.network.HttpUtils;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author smcardle
 * 
 */
public class GetPropertiesInViewportTask extends AsyncTask {

	public static final String REQUEST_PARAM_KEY = "key";

	private MapFragment callback = null;
	private int selectedGridReferenceId = 0;
	private Map gridReferenceIdCache = null;
	private String url = null;
	Gson gson = null;
	private GetPropertiesPostProcessor getPropertiesPostProcessor = null;
	
	private long networkStartTime = 0;

	public GetPropertiesInViewportTask(int selectedGridReferenceId,
			Map gridReferenceIdCache) {
		this.selectedGridReferenceId = selectedGridReferenceId;
		this.gridReferenceIdCache = gridReferenceIdCache;
		this.gson = new Gson();
		getPropertiesPostProcessor = new GetPropertiesPostProcessor();
	}

	@Override
	protected String doInBackground(Object... urls) {

		// params comes from the execute() call: params[0] is the url.
		try {
			PerformanceTimer perfTimer = PerformanceTimer.getInstance();
			perfTimer.start("updateMarkers");

			url = (String) urls[0];
			String result = "";
			
			if (url != null) {
				result = HttpUtils.httpRequest(url, HttpUtils.METHOD_GET, null, null);
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
			
			long networkEndTime = System.currentTimeMillis();
			
			// TODO: Record user performance data in DB to check if acceptable (POST all results to server at pre-defined times).
			// This can record if DB or Network being used (and time taken).
			SharedPreferences sp = callback.getActivity().getSharedPreferences(MainActivity.INITIALIZATION_PREFERENCE_FILE, Context.MODE_PRIVATE);
			RemoteLog.network("Network latency is [" + (networkEndTime - networkStartTime) + "] with App UUID [" + sp.getString(MainActivity.APP_UUID, null));
			
			if (result != null) {
				String resultString = (String) result;
				PerformanceTimer perfTimer = PerformanceTimer.getInstance();
				perfTimer.getTime("updateMarkers", "");
				if (resultString != null
						&& !resultString.isEmpty()
						&& !resultString
								.contains("Unable to retrieve web page")
						&& resultString.contains("{")) {

						GridWrapper gridWrapper = getPropertiesPostProcessor.process(resultString);
	
						callback.updateMarkers(gridWrapper, selectedGridReferenceId);
				} else {
					gridReferenceIdCache.remove(selectedGridReferenceId);
					RemoteLog.log("url [" + url + "]");;
				}
				perfTimer.getTime("updateMarkers", "");
			}
		} catch (Exception ex) {
			RemoteLog.error(ex);;
			gridReferenceIdCache.remove(selectedGridReferenceId);
			Log.e("onPostExecute : ", "Error converting to json objects : "
					+ ex);
		}
	}

	/**
	 * @param mapFragment
	 *            the callbackContext to set
	 */
	public void setCallback(MapFragment mapFragment) {
		this.callback = mapFragment;
	}


	
}
