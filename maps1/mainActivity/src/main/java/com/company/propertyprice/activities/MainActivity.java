/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.company.propertyprice.activities;


import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.company.propertyprice.config.Config;
import com.company.propertyprice.dao.AddressDAO;
import com.company.propertyprice.dao.GeoCodeDAO;
import com.company.propertyprice.dao.PropertyListItemDAO;
import com.company.propertyprice.dao.PropertySaleDAO;
import com.company.propertyprice.exception.TopExceptionHandler;
import com.company.propertyprice.fragments.HelpFragment;
import com.company.propertyprice.fragments.MapFragment;
import com.company.propertyprice.fragments.StreetFragment;
import com.company.propertyprice.fragments.SavedPropertyFragment;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertyprice.network.EndPointManager;
import com.company.propertyprice.network.GetPropertyByUUIDTask;
import com.company.propertyprice.processor.GetPropertiesDiscriminator;
import com.company.propertyprice.util.RemoteLog;
import com.company.propertypricetest.R;
import com.company.utils.DateUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static String INITIALIZATION_PREFERENCE_FILE = "Initialization";
    private static String INITIALIZED = "INITIALIZED";

    public static String APP_UUID = "APP_UUID";
    public static final String TAB_MAP = "Map";
    public static final String TAB_STREET = "Street";
    public static final String TAB_SAVED = "Saved";
    public static final String TAB_GRAPH = "Graph";

    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */
    private ActionBar ab = null;
    private SearchView searchView = null;
    private MapFragment mapFragmentCallback = null;
    private StreetFragment viewFragmentCallback = null;
    private SavedPropertyFragment savedFragmentCallback = null;
    private PropertyMDO selectedProperty = null;
    private SearchResultsActivity searchResultsActivity = null;
    private SensorManager sensorManager = null;
    private Sensor sensorMagneticField = null;
    private Sensor sensorAccelerometer = null;
    private float[] valuesMagneticField = null;
    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    private float[] outR;
    private float[] valuesAccelerometer;
    private int sensorTicker = 95;
    private boolean followMeEnabled = false;
    private Date startDate = null;
    private Date endDate = null;
    private int startPrice = 0;
    private int endPrice = 1000000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	// Catch any app crashes and log to file
    	Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
    	Config config = Config.getInstance();
    	
    	/*try {
    		String errorString = "";
    		FileInputStream trace = this.openFileInput("stack.trace");
            BufferedReader dis = new BufferedReader(new InputStreamReader(trace));
            String s;
            while ((s = dis.readLine()) != null) {
            	errorString = errorString + s;
            }
            trace.close();
    		
		    Toast.makeText(this, "Crash Error : " + errorString , Toast.LENGTH_LONG)
		    .show();
            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
		/*********************************************************/
		/*  INITIALIZE THE APP IF INSTALLED FOR THE FIRST TIME   */
		/*********************************************************/
		
		intializeApp();
		
		/*********************************************************/
	
		
		
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory("com.scanshop.TABS");
	
		// setContentView(R.layout.main);
		ab = getActionBar();
		ab.setTitle("graphPointsbar"
				+ " Property Price");
		ab.setDisplayShowHomeEnabled(true);
	
		valuesMagneticField = new float[3];
		valuesAccelerometer = new float[3];
		matrixR = new float[16];
		matrixI = new float[16];
		matrixValues = new float[3];
		

		startDate = DateUtils.getDate("01/01/2010");
		endDate = new Date();
		startPrice = 0;
		endPrice = 10000000;

		
		// TODO: Try TYPE_ROTATION_VECTOR, from demo https://www.youtube.com/watch?v=_oZiK_NJuG8&index=3&list=PLOU2XLYxmsIJmnYVrcyd_ipO7NBeWCxgw seems easier to use.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorMagneticField = sensorManager
			.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorAccelerometer = sensorManager
			.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	
		/*List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
		
		if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
		    Toast.makeText(this, "Sensor : [" + "TYPE_MAGNETIC_FIELD" + "]",
		     Toast.LENGTH_SHORT).show();
		    }
		  else {
		    // Failure! No magnetometer.
		    }*/
	
		// mCameraTextView = (TextView) findViewById(R.id.map_text);
	
		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		// mLocationClient = new LocationClient(this, this, this);
	
		// setUpMapIfNeeded();

	    Toast.makeText(this, "Device : [" + config.getDeviceModel() + ":" + config.getDeviceManufacturer() + "]",
			     Toast.LENGTH_LONG).show();
	    
		Intent intent = getIntent();
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            
	        final List<String> segments = intent.getData().getPathSegments();
	        
	        // Load the sale if the url has the correct number of segments
	        if (segments != null && segments.size() > 4)  loadPPRSiteSale(segments.get(5));

		}


        // Create a GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /*@Override
    protected void onResume() {
	super.onResume();
	
	if (followMeEnabled) {
        	sensorManager.registerListener(this,
        		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        		SensorManager.SENSOR_DELAY_FASTEST);
        	sensorManager.registerListener(this,
        		sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
    		SensorManager.SENSOR_DELAY_FASTEST);
	}

    }*/

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    /*@Override
    protected void onDestroy() {
		super.onDestroy();
		
		if (followMeEnabled) {
		    sensorManager.unregisterListener(this);
		}

    }*/

    public void onToggleTabs(View v) {
		final ActionBar bar = getActionBar();
	
		if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
		    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		    //bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE,
			    //ActionBar.DISPLAY_SHOW_TITLE);
		} else {
		    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		    //bar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_TITLE);
		}
		
		bar.setTitle("Irish Property Price");
		bar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
	
		// Add three tabs
		this.onAddTabs(super.getCurrentFocus());
		this.onToggleTabs(super.getCurrentFocus());
	
		// Need to use MenuItemCompat to retrieve the Action Provider
		/*
		 * ShareActionProvider mActionProvider = (ShareActionProvider)
		 * MenuItemCompat.getActionProvider(searchItem);
		 */
	
		return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

		// getMenuInflater().inflate(R.menu.activity_main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_websearch);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setSubmitButtonEnabled(true);
		searchView.setIconified(true);
		searchResultsActivity = new SearchResultsActivity(searchView,
			mapFragmentCallback);
		searchView.setOnQueryTextListener(searchResultsActivity);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager
			.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);
		return super.onPrepareOptionsMenu(menu);
    }

    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_toggle_follow:
	            // Toggle the "Follow Me" functionality
	            //toggleFollowMe();
	            return true;
	        default:
	    }
	            
	    return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	
		// TODO: Try using 'Offset Camera Target' for zooming to search location
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		    String query = intent.getStringExtra(SearchManager.QUERY);
		    /*Toast.makeText(this, "onNewIntent()" , Toast.LENGTH_SHORT)
			    .show();*/
		    searchView.setQuery(query, false);
		    /*
		     * searchView.setIconified(true); searchView.clearFocus();
		     */
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            
	        final List<String> segments = intent.getData().getPathSegments();
	        
	        // Load the sale if the url has the correct number of segments
	        if (segments != null && segments.size() > 4)  loadPPRSiteSale(segments.get(5));

		}
    }

    public void onAddTabs(View v) {
		Log.e("onAddTab", "onAddTab");
		final ActionBar bar = getActionBar();
		bar.addTab(bar
			.newTab()
			.setText(TAB_MAP)
			.setTabListener(
				new TabListener<MapFragment>(this, TAB_MAP,
					MapFragment.class)));
		bar.addTab(bar
				.newTab()
				.setText(TAB_STREET)
				.setTabListener(
					new TabListener<StreetFragment>(this, TAB_STREET,
						StreetFragment.class)));
		bar.addTab(bar
			.newTab()
			.setText(TAB_SAVED)
			.setTabListener(
				new TabListener<SavedPropertyFragment>(this, TAB_SAVED,
					SavedPropertyFragment.class)));
		/*bar.addTab(bar
				.newTab()
				.setText(TAB_GRAPH)
				.setTabListener(
					new TabListener<GraphFragment>(this, TAB_GRAPH,
							GraphFragment.class)));*/

    }

    public void addViewTabIfRequired() {
    	
		/*final ActionBar bar = getActionBar();
		int tabCount = bar.getTabCount();
		
		// Assume 'View' tab already added if count is 3
		if (tabCount == 3) return;

		bar.addTab(bar
			.newTab()
			.setText(TAB_VIEW)
			.setTabListener(
				new TabListener<ViewFragment>(this, TAB_VIEW,
					ViewFragment.class)));*/
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private class TabListener<T extends Fragment> implements
	    ActionBar.TabListener {
	private Fragment mFragment;
	private final Activity mActivity;
	private final String mTag;
	private final Class<T> mClass;

	public TabListener(Activity activity, String tag, Class<T> clz) {
	    mActivity = activity;
	    mTag = tag;
	    mClass = clz;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    // TODO Auto-generated method stub

	    // Check if the fragment is already initialized
	    if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
	
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
	    }

	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    if (mFragment != null) {
			// Detach the fragment, because another one is being attached
			ft.detach(mFragment);
	    	}
		}

    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
		super.onStart();
		// Connect the client.
        mGoogleApiClient.connect();
    }
 

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();

		// Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();

    }

    public void setSelectedTab(int index) {
    	getActionBar().setSelectedNavigationItem(index);
    }

    public void setSelectedProperty(PropertyMDO property) {
    	this.selectedProperty = property;
    }

    public PropertyMDO getSelectedProperty() {
    	return this.selectedProperty;
    }

    public void setMapCallback(Fragment callback) {
		this.mapFragmentCallback = (MapFragment) callback;
		this.searchResultsActivity.setCallback((MapFragment) callback);
    }
    
    public void setViewFragmentCallback(Fragment callback) {
    	this.viewFragmentCallback = (StreetFragment) callback;
    }

    public void setSavedFragmentCallback(Fragment callback) {
    	this.savedFragmentCallback = (SavedPropertyFragment) callback;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    /*@Override
    protected void onPause() {
    	super.onPause();
	
		if (followMeEnabled) {
		    sensorManager.unregisterListener(this);
		}
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
	// TODO Auto-generated method stub

    }

    @Override
    public synchronized void onSensorChanged(SensorEvent event) {
	
	if (sensorTicker++ > 100) {

		//Toast.makeText(this, "onSensorChanged ...",
			//Toast.LENGTH_SHORT).show();
		
        	// TODO Auto-generated method stub
        	switch (event.sensor.getType()) {
        	case Sensor.TYPE_ACCELEROMETER:
        
        	    for (int i = 0; i < 3; i++) {
        		valuesAccelerometer[i] = event.values[i];
        	    }
        	    break;
        	case Sensor.TYPE_MAGNETIC_FIELD:
        	    for (int i = 0; i < 3; i++) {
        		valuesMagneticField[i] = event.values[i];
        	    }
        	    break;
        	}
        
        	boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
        		valuesAccelerometer, valuesMagneticField);
        
        	//SensorManager.remapCoordinateSystem(matrixR, 
        		      //SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, outR); 
        	if (success) {
        	    SensorManager.getOrientation(matrixR, matrixValues);
        	    //float magHeading = SensorManager.getInclination(matrixI);
        	    
        	    double azimuth = Math.toDegrees(matrixValues[0]);
        	    //double heading = Math.toDegrees(magHeading);
        	    
    			Toast.makeText(this, "azimuth : [" + azimuth + "] heading : [" + 0 + "]",
			 Toast.LENGTH_SHORT).show();
    		
        	    if (mapFragmentCallback != null) {
        		mapFragmentCallback.setCameraBearing((float)azimuth);
        	    }

        	}
	    	sensorTicker = 0;
	}
    }*/
    
    public void intializeApp() {
	

		// TMP: Test the UpdatePropertiesToDBService
		/*Intent intent = new Intent(this, GridPathPredictionService.class);
		intent.putExtra("selectedGridReferenceId", 521300);
		intent.putExtra("radius", 2);
		startService(intent);*/
		
	    SharedPreferences preferences = this.getSharedPreferences(INITIALIZATION_PREFERENCE_FILE, Context.MODE_PRIVATE);
	    boolean initialized = preferences.getBoolean(INITIALIZED, Boolean.FALSE);
	    
	    if (!initialized) {
		
	    	// Generate a UUID for this app instance
	    	UUID uuid = UUID.randomUUID();
	    	
			Toast.makeText(this, "Initializing ...[" + uuid.toString() + "]",
				Toast.LENGTH_LONG).show();
			
			// Create database tables
			PropertySaleDAO propertySaleDAO = PropertySaleDAO.getInstance(this);
			AddressDAO addressDAO = AddressDAO.getInstance(this);
			GeoCodeDAO geoCodeDAO = GeoCodeDAO.getInstance(this);
			PropertyListItemDAO propertyListItemDAO = PropertyListItemDAO.getInstance(this);
			
			propertySaleDAO.initialize();
			addressDAO.initialize();
			geoCodeDAO.initialize();
			propertyListItemDAO.initialize();
			
			Editor editor = this.getSharedPreferences(INITIALIZATION_PREFERENCE_FILE, Context.MODE_PRIVATE).edit();
			editor.putBoolean(INITIALIZED, Boolean.TRUE);
			editor.putString(APP_UUID, uuid.toString());
			editor.commit();
	    }
    }
    
    /*public void toggleFollowMe() {
	
		if (!followMeEnabled) {
		   	//sensorManager.unregisterListener(this);
		    followMeEnabled = true;
		    mapFragmentCallback.displaySliderWindow(followMeEnabled);
		} else {
			sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
			sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_FASTEST);
			followMeEnabled = false;
			mapFragmentCallback.displaySliderWindow(followMeEnabled);
		}
    }*/
    
    public void resetToggleFollowMe() {
    	followMeEnabled = false;
    }
    
    public void setPriceFilter(int startPrice, int endPrice) {
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		mapFragmentCallback.clearAllMarkers();
    }
    
    public void setDateFilter(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		mapFragmentCallback.clearAllMarkers();
    }
    
    public int getStartPriceFilter() {
    	return this.startPrice;
    }
    
    public int getEndPriceFilter() {
    	return this.endPrice;
    }
    
    public Date getStartDateFilter() {
    	return this.startDate;
    }
    
    public Date getEndDateFilter() {
    	return this.endDate;
    }
    
    private void loadPPRSiteSale(String urlSegment) {

        try {
            // Parse the urlSegment to retrieve the UUID for the sale.
            String[] urlSegmentSplit1 = urlSegment.split("-");
            String[] urlSegmentSplit2 = urlSegmentSplit1[1].split("\\?");

            String uuid = urlSegmentSplit2[0];

            GetPropertyByUUIDTask task = new GetPropertyByUUIDTask();
            String endpoint = EndPointManager.getEndPoint(EndPointManager.GET_PROPERTIES_BY_UUID, 0, 0);
            task.setCallback(this);
            task.execute(endpoint+ "uuid=" + uuid);
        } catch (Exception ex) {
            RemoteLog.error(ex);
        }
    }
    
    public void viewProperty(PropertyMDO property) {
        
    	this.setSelectedProperty(property);
    	this.addViewTabIfRequired();
    	this.setSelectedTab(0);
    }
    
    /** Use this method to check the memory available for the app to use.
     *  It can be used to check how powerful the phone is and allow more in-memory caching!!!
     * 
     */
    public void getMemoryInfo() {
	    ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
	    MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
	    activityManager.getMemoryInfo(memoryInfo);
	
	    /*Log.i(TAG, " memoryInfo.availMem " + memoryInfo.availMem + "\n" );
	    Log.i(TAG, " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n" );
	    Log.i(TAG, " memoryInfo.threshold " + memoryInfo.threshold + "\n" );
	    Log.i("memory free", "" + memoryInfo.availMem);
	    
	    Runtime.getRuntime().totalMemory();*/
	    
	    
    }
    
}
