/**
 * 
 */
package com.company.propertyprice;

import java.util.HashMap;

import android.app.Application;

import com.company.propertyprice.util.RemoteLog;
import com.company.propertypricetest.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * @author smcardle
 *
 */
public class IrishPropertyApplication extends Application {

	private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 *
	 * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
	 * storing them all in Application object helps ensure that they are created only once per
	 * application instance.
	 */
	public enum TrackerName {
	  //APP_TRACKER, // Tracker used only in this app.
	  GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
	  //ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	}
	
	
	public IrishPropertyApplication() {
		super();
		RemoteLog.setDebugEnabled(false);
		RemoteLog.setCrashReportEnabled(true);
		RemoteLog.setNetworkStatsEnabled(true);
	}
	
	
	public synchronized Tracker getTracker(TrackerName trackerId) {
		  if (!mTrackers.containsKey(trackerId)) {

		    GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		    Tracker t = analytics.newTracker(R.xml.global_tracker);
		    
		    /*Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
			        : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
			            : analytics.newTracker(R.xml.ecommerce_tracker);*/
		    mTrackers.put(trackerId, t);

		  }
		  return mTrackers.get(trackerId);
		}
}
