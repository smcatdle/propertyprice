package com.company.propertyprice.fragments;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.geo.Coordinate;
import com.company.geo.GraphPoint;
import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;
import com.company.propertyprice.MarkerStorage;
import com.company.propertyprice.TouchableWrapper.UpdateMapAfterUserInterection;
import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.dao.PropertyListItemDAO;
import com.company.propertyprice.model.GridAveragesWrapper;
import com.company.propertyprice.model.GridWrapper;
import com.company.propertyprice.model.android.PropertyListItem;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertyprice.network.EndPointManager;
import com.company.propertyprice.network.GetGridAveragesTask;
import com.company.propertyprice.network.GetPropertiesInViewportTask;
import com.company.propertyprice.processor.GetPropertiesDiscriminator;
import com.company.propertyprice.util.RemoteLog;
import com.company.propertyprice.views.MiniGraphView;
import com.company.propertypricetest.R;
import com.company.utils.DateUtils;
import com.company.utils.PerformanceTimer;
import com.company.utils.PriceUtils;
import com.company.widget.RangeSeekBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import android.widget.Toast;


public class MapFragment extends Fragment implements OnCameraChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
		UpdateMapAfterUserInterection,
		LocationListener {

	public static final int VISIBLE_MARKER_GRID = 2;
	public static final int MINIMUM_ZOOM_LEVEL_FOR_MARKER_UPDATES = 14;
	public static final int REFRESH_DB_GRID_PERIOD_IN_DAYS = 4;
	public static final int SCROLLING_AT_REST_PERIOD = 500;
	public static final int VIEWPORT_UPDATE_INTERVAL_GAP = 2000;
	public static final int INITIAL_MIN_PRICE_VALUE = 0;
	public static final int INITIAL_MAX_PRICE_VALUE = 10000000;
	public static final String INITIAL_MIN_DATE_VALUE = "2010-01-01";

	public static final String REQUEST_PARAM_KEYS = "keys";

	private View fragView = null;
	private MainActivity mcontext = null;
	private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient =null;
    private TextView mCameraTextView;
	private ViewPortGridDivisor viewPortGridDivisor = null;
	private Map gridReferenceIdCache = null;
	boolean mapInitialised = false;
	private boolean slidersInitialised = false;
	private TextView priceRangeText = null;
	private TextView dateRangeText = null;
	private TableLayout tableLayout = null;
	private LinearLayout markerWindowLayout = null;
	private long lastViewPortUpdateDate = 0;
	private RangeSeekBar<Integer> priceSeekBar = null;
	private RangeSeekBar<Long> seekBar = null;
	private ViewGroup layout = null;
	private ViewGroup gridPanelLayout = null;
	private TextView averagePriceText = null;
	private TextView averagePriceLabel = null;
	private TextView averageYearText = null;
	private TextView averageYearLabel = null;
	private MiniGraphView miniGraphView = null;
	private IconGenerator iconFactory = null;
	private Button streetViewButton = null;
	private Button mapViewButton = null;
	private Button saveButton = null;
	private TextView addressText = null;
	private TextView priceText = null;
	private TextView dateText = null;
	private ImageView markerWindowViewImage = null;
	private boolean userPan = true;
    private Gson gson = null;


	public MapFragment() {
		super();
		
		viewPortGridDivisor = new ViewPortGridDivisor(ViewPort.VIEWPORT_IRELAND);
		gridReferenceIdCache = new HashMap();
		
		gson = new Gson();


        // Get tracker.
		/*
		 * Tracker t = ((PropertyPriceApplication)
		 * getActivity().getApplication()).getTracker( TrackerName.APP_TRACKER);
		 * // Set screen name. // Where path is a String representing the screen
		 * name. t.setScreenName("MapFragment"); // Send a screen view.
		 * t.send(new HitBuilders.AppViewBuilder().build());
		 */
	}

	@Override
	public void onAttach(Activity activity) {
		mcontext = (MainActivity) activity;
		mcontext.setMapCallback(this);
		iconFactory = new IconGenerator(this.getActivity());
		
		super.onAttach(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		// TODO: Check how to save the state when creating activity

		super.onActivityCreated(savedInstanceState);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder((FragmentActivity) getActivity())
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (fragView == null) {
			fragView = inflater.inflate(R.layout.map_content_tab, container,
					false);

			gridPanelLayout = (ViewGroup) fragView
					.findViewById(R.id.grid_panel_layout);
			miniGraphView = (MiniGraphView) fragView
					.findViewById(R.id.minigraphview);
			averagePriceLabel = (TextView) fragView
					.findViewById(R.id.grid_panel_average_lab);
			averagePriceText = (TextView) fragView
					.findViewById(R.id.grid_panel_average);
			averageYearLabel = (TextView) fragView
					.findViewById(R.id.grid_panel_year);
			averageYearText = (TextView) fragView
					.findViewById(R.id.grid_panel_empty);
			
			averagePriceLabel.setTextColor(Color.BLUE);
			averagePriceText.setTextColor(Color.BLUE);
			averageYearLabel.setTextColor(Color.BLUE);
			averageYearText.setTextColor(Color.BLUE);
			
			addressText = (TextView) fragView
					.findViewById(R.id.address_text);
			priceText = (TextView) fragView
					.findViewById(R.id.price_text);
			dateText = (TextView) fragView
					.findViewById(R.id.date_text);
			
			markerWindowViewImage = (ImageView) fragView
					.findViewById(R.id.marker_window_image);
			
			markerWindowLayout = (LinearLayout) fragView
			.findViewById(R.id.property_item);
			markerWindowLayout.setVisibility(android.view.View.INVISIBLE);
			
			
			// Show the marker window 'Street' button
		    streetViewButton = (Button) markerWindowLayout
				    .findViewById(R.id.streetmap_property_button);
		    streetViewButton.setText("Street");   
		    streetViewButton.setOnClickListener(new View.OnClickListener() {
			    	
					public void onClick(View v) {
			
						mcontext.addViewTabIfRequired();
						mcontext.setSelectedTab(1);
					}
					
			    });
		    
		    
			// Show the marker window 'Save' button
		    saveButton = (Button) markerWindowLayout
				    .findViewById(R.id.save_property_button);
		    saveButton.setText("Save");   
		    saveButton.setOnClickListener(new View.OnClickListener() {
			    	
					public void onClick(View v) {
			
					    PropertyListItemDAO dao = PropertyListItemDAO
							    .getInstance(mcontext);
						    PropertyMDO property = mcontext.getSelectedProperty();
						    PropertyListItem item = new PropertyListItem();
						    item.setPropertysaleid(property.getId());
						    item.setJson(gson.toJson(property));
						    dao.onAddPropertyListItem(item);
						    Toast.makeText(mcontext, "Property Saved", Toast.LENGTH_LONG).show();
					}
					
			    });
		    
		    
		    
		 // Hide the marker window 'Map' button
		    mapViewButton = (Button) fragView
				    .findViewById(R.id.map_property_button);
		    mapViewButton.setVisibility(android.view.View.INVISIBLE);
		    
			
		}

		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((TouchableSupportMapFragment) ((FragmentActivity) getActivity())
					.getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			mMap.setBuildingsEnabled(true);
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setCompassEnabled(false);


			// Override the default behaviour of the info window
			mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

				public boolean onMarkerClick(Marker marker) {

					PropertyMDO property = MarkerStorage.getMarker(marker);
					
					displayPropertyInfoWindow(property);
					
					return true;
				}

			});
			    
			
			// Set the callback on the custom TouchableSupportMapFragment (for
			// 'ACTION_UP' events).
			((TouchableSupportMapFragment) ((FragmentActivity) getActivity())
					.getSupportFragmentManager().findFragmentById(R.id.map))
					.setTouchableSupportCallback(this);

			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}


		// Set the MapFragment callback on the
		if (!slidersInitialised) {

			try {
				// add RangeSeekBar to pre-defined layout
				// layout = (ViewGroup) fragView.findViewById(R.id.slider);

				/*
				 * priceRangeText = new TextView(mcontext);
				 * 
				 * // Price Range Slider priceSeekBar = new
				 * RangeSeekBar<Integer>(0, 10000000, mcontext);
				 * priceSeekBar.setOnRangeSeekBarChangeListener(new
				 * OnRangeSeekBarChangeListener<Integer>() {
				 * 
				 * @Override public void
				 * onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer
				 * minValue, Integer maxValue) { // handle changed range values
				 * Log.i("RangeSeekBar", "User selected new range values: MIN="
				 * + minValue + ", MAX=" + maxValue);
				 * priceRangeText.setText("Price from  �" + minValue + "  to  �"
				 * + maxValue); mcontext.setPriceFilter(minValue, maxValue); }
				 * });
				 * 
				 * 
				 * // Date Range Slider Date minDate; dateRangeText = new
				 * TextView(mcontext);
				 * 
				 * minDate = new
				 * SimpleDateFormat("yyyy-MM-dd").parse("2010-01-01");
				 * 
				 * Date maxDate = new Date(); seekBar = new
				 * RangeSeekBar<Long>(minDate.getTime(), maxDate.getTime(),
				 * mcontext); seekBar.setOnRangeSeekBarChangeListener(new
				 * OnRangeSeekBarChangeListener<Long>() {
				 * 
				 * @Override public void
				 * onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long
				 * minValue, Long maxValue) { // handle changed range values
				 * Log.i("RangeSeekBar", "User selected new date range: MIN=" +
				 * new Date(minValue) + ", MAX=" + new Date(maxValue));
				 * dateRangeText.setText("Sale Date from  " +
				 * DateUtils.getString(new Date(minValue)) + " To  " +
				 * DateUtils.getString(new Date(maxValue)));
				 * mcontext.setDateFilter(new Date(minValue), new
				 * Date(maxValue)); } });
				 */

				/*tableLayout = new TableLayout(mcontext);
				TableRow tableRow1 = new TableRow(mcontext);
				TableRow tableRow2 = new TableRow(mcontext);
				LayoutParams params = new LayoutParams();
				params.weight = (float) 0.8;
				params.height = LayoutParams.FILL_PARENT;
				params.weight = LayoutParams.FILL_PARENT;
				tableLayout.setLayoutParams(params);
				tableLayout.addView(tableRow1);
				tableLayout.addView(tableRow2);

				tableRow1.addView(priceRangeText);
				tableRow2.addView(priceSeekBar);

				TableRow tableRow3 = new TableRow(mcontext);
				TableRow tableRow4 = new TableRow(mcontext);
				tableLayout.addView(tableRow3);
				tableLayout.addView(tableRow4);

				tableRow3.addView(dateRangeText);
				tableRow4.addView(seekBar);

				layout.addView(tableLayout);*/

				/*
				 * if (mcontext.getStartDateFilter() == null ||
				 * mcontext.getEndDateFilter() == null) {
				 * Toast.makeText(mcontext, "DateFilter returned null",
				 * Toast.LENGTH_LONG).show(); }
				 * //priceRangeText.setText("Price from  �" + 0 + "  to  �" +
				 * 10000000); dateRangeText.setText("Sale Date from  " +
				 * DateUtils.getString(new Date()) + " To  " +
				 * DateUtils.getString(new Date()));
				 */

			} catch (Exception ex) {
				RemoteLog.error(ex);
			}

			slidersInitialised = true;
		}

		// Reset the 'toggle me' button on create
		mcontext.resetToggleFollowMe();
		// displaySliderWindow(false);

		mcontext.setPriceFilter(0, 10000000);
		mcontext.setDateFilter(DateUtils.getDate("01/01/2010"), new Date());

		/*mMap.setInfoWindowAdapter(new PropertyInfoWindowAdapter(inflater,
				container.getContext()));*/
		

		// Disabled snap panning when click marker
		// NB: Need to find a way to position info window relative to marker
		// first (else can't see it!)
		/*
		 * mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		 * 
		 * // Since we are consuming the event this is necessary to // manage
		 * closing opened markers before opening new ones Marker lastOpened =
		 * null;
		 * 
		 * public boolean onMarkerClick(Marksurrsurrer marker) { // Check if there is an
		 * open info window if (lastOpened != null) { // Close the info window
		 * lastOpened.hideInfoWindow();
		 * 
		 * // Is the marker the same marker that was already open if
		 * (lastOpened.equals(marker)) { // Nullify the lastOpened object
		 * lastOpened = null; // Return so that the info window isn't opened
		 * again return true; } }
		 * 
		 * // Open the info window for the marker marker.showInfoWindow(); //
		 * Re-assign the last opened such that we can close it later lastOpened
		 * = marker;
		 * 
		 * // Event was handled by our code do not launch default behaviour.
		 * return true; } });
		 */

		// Display the property info window if a property is selected
		if (mcontext.getSelectedProperty() != null) {
			this.displayPropertyInfoWindow(mcontext.getSelectedProperty());
		}

		// setUpMap();
		return fragView;
	}
	
	  
	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	public void onStart() {
		super.onStart();
        mGoogleApiClient.connect();

	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	public void onStop() {
		// Disconnecting the client invalidates it.
		super.onStop();
        mGoogleApiClient.disconnect();

	}

	/**
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {

		double lat = 53.349803;
		double lng = -6.260309;

        // FIXME: Get the location details from the MainActivity
        /*Location loc;
        loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();
		}*/

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),
				10.0f));
		// Construct a CameraPosition and animate the
		// camera to that position.
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, lng)) // Sets the center of the map to
				.zoom(17) // Sets the zoom
				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));

		mMap.setOnCameraChangeListener(this);

		mapInitialised = true;

	}

	/*
	 * public void displaySliderWindow(boolean show) {
	 * 
	 * if (show) {
	 * 
	 * layout.setVisibility(android.view.View.VISIBLE);
	 * 
	 * //priceSeekBar.buildLayer(); //priceSeekBar.bringToFront();
	 * //layout.buildLayer();
	 * 
	 * 
	 * } else { layout.setVisibility(android.view.View.INVISIBLE); } }
	 */

	@Override
	public void onCameraChange(final CameraPosition position) {

	}

	public void mapPositionChanged(final CameraPosition position) {

		try {

			// The user must have just stopped panning or zoomed above
			// 'MINIMUM_ZOOM_LEVEL_FOR_MARKER_UPDATES'

			// The database will be checked for a grid match first, unless a
			// matching grid found with last refresh greater than
			// 'REFRESH_GRID_PERIOD_IN_DAYS'
			float cameraZoomLevel = position.zoom;

			// Hide the marker window
			markerWindowLayout.setVisibility(android.view.View.INVISIBLE);
			
			// Only fetch the markers for this position if the zoom level is
			// above 'MINIMUM_ZOOM_LEVEL_FOR_MARKER_UPDATES'
			if (checkIfViewPortUpdateRequired(cameraZoomLevel)) {

				if (position != null && position.target != null
						&& position.target.latitude != 0
						&& position.target.longitude != 0) {

					Coordinate currentLocation = new Coordinate(
							(float) position.target.latitude,
							(float) position.target.longitude);

					// What grid cell is the centre of the camera in?
					int selectedGridReferenceId = viewPortGridDivisor
							.getGridReferenceId(currentLocation);

					// Did we already retrieve this gridReferenceId?
					/*
					 * if (selectedGridReferenceId != 0 && !gridReferenceIdCache
					 * .containsKey(selectedGridReferenceId)) {
					 */

					// Cache this gridReferenceId so we don't retrieve it
					// again (until the app is "re-opened" ?)
					/*
					 * gridReferenceIdCache.put(selectedGridReferenceId,
					 * selectedGridReferenceId);
					 */

					// TODO: Retrieve the markers from the database (if
					// within refresh period or no network connection).
					ViewPort cellViewPort = viewPortGridDivisor
							.getCellViewPort(selectedGridReferenceId);

					// Add the markers for this grid
					if (cellViewPort != null
							&& cellViewPort.getBottomRightCoord() != null
							&& cellViewPort.getTopLeftCoord() != null) {

						// If grid refreshed more than
						// 'REFRESH_GRID_PERIOD_IN_DAYS' ago, then delete
						// all markers in db grid and retrieve from server.
						/*
						 * Date lastUpdatedDate =
						 * getLastUpdateForGrid(selectedGridReferenceId); long
						 * todaysDateInSeconds = new Date().getTime(); long
						 * lastUpdatedDateInSeconds = lastUpdatedDate != null ?
						 * lastUpdatedDate .getTime() +
						 * (REFRESH_DB_GRID_PERIOD_IN_DAYS * 24 * 60 * 60 *
						 * 1000) : 0;
						 */

						clearAllMarkers();

						// if (lastUpdatedDate == null
						// || todaysDateInSeconds > lastUpdatedDateInSeconds) {
						updateVisibleMapWithMarkers(cellViewPort,
								selectedGridReferenceId);

						
						// FIXME: Add check when user pans away from current grid (and possibly predict the direction the user is going in).
						/*if (userPan) {
							Intent intent = new Intent(getActivity(), GridPathPredictionService.class);
							intent.putExtra("selectedGridReferenceId", 521300);
							intent.putExtra("radius", 2);
							getActivity().startService(intent);
							userPan = false;
						}*/
						
						// TODO: Add manager to switch back to main server if
						// outside peak ViewPort or failover.

						/*
						 * } else { updateMarkersFrom (selectedGridReferenceId);
						 * }
						 */

						// NB: markers in remote areas of the map will also
						// need to be cleared to improve performance
						// (basically anything 3 grids away).
						// ClearRemoteMarkersTask clearRemoteMarkersTask =
						// new ClearRemoteMarkersTask(cellViewPort,
						// viewPortGridDivisor, gridReferenceIdCache,
						// mcontext);
						// clearRemoteMarkersTask.execute();

					}
				}
				
				// Garbage Collect
				System.gc();
			}

			/*
			 * } else { // Clear all markers when camera above certain zoom
			 * level // clearAllMarkers(); }
			 */

		} catch (Exception ex) {
			RemoteLog.error(ex);
		}

	}

	// TODO: Clear all markers more than 3 grids away
	public void clearAllMarkers() {

		try {
			mMap.clear();
			MarkerStorage.removeAllMarkers();
			gridReferenceIdCache.clear();
		} catch (Exception ex) {
			RemoteLog.error(ex);
		}

	}

	// Location Based services

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {

		// Display the connection status
		// Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

		/*
		 * if (mLocationClient != null && mLocationClient.isConnected()) {
		 * Location mCurrentLocation = mLocationClient.getLastLocation();
		 * 
		 * if (mCurrentLocation != null) { lat = mCurrentLocation.getLatitude();
		 * lng = mCurrentLocation.getLongitude(); } }
		 */

		/*
		 *  * mCameraTextView.setText("Location : " + lat + "." + lng); } }
		 * 
		 * mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,
		 * lng), 10.0f)); // Construct a CameraPosition focusing on Mountain
		 * View and animate the camera to that position. CameraPosition
		 * cameraPosition = new CameraPosition.Builder() .target(new LatLng(lat,
		 * lng)) // Sets the center of the map to Mountain View .zoom(17) //
		 * Sets the zoom .bearing(90) // Sets the orientation of the camera to
		 * east .tilt(30) // Sets the tilt of the camera to 30 degrees .build();
		 * // Creates a CameraPosition from the builder
		 * mMap.animateCamera(CameraUpdateFactory
		 * .newCameraPosition(cameraPosition));
		 * 
		 * 
		 * Log.e("propertyprice : ", "Added markers");
		 */

        /*mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);*/
	}

    @Override
    public void onConnectionSuspended(int i) {

    }

	@Override
	public void onLocationChanged(Location location) {

	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			/*
			 * try { // Start an Activity that tries to resolve the error
			 * connectionResult.startResolutionForResult( this,
			 * CONNECTION_FAILURE_RESOLUTION_REQUEST);
			 * 
			 * } catch (IntentSender.SendIntentException e) { // Log the error
			 * e.printStackTrace(); }
			 */
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			// showErrorDialog(connectionResult.getErrorCode());
		}
	}

	public synchronized void updateMarkers(GridWrapper gridWrapper,
			int selectedGridReferenceId) {
		PerformanceTimer perfTimer = PerformanceTimer.getInstance();
		// perfTimer.getTime("updateMarkers", "");

		List<PropertyMDO> properties = gridWrapper.getP();
		Map<String, GraphPoint> graphPoints = gridWrapper.getG();

		/*
		 * if (properties == null || properties.size() == 0) {
		 * gridReferenceIdCache.remove(selectedGridReferenceId); }
		 */

		// Display all the markers
		for (PropertyMDO property : properties) {

			// Check the sale is within the date filter range and price range
			// if (property.getDateOfSale().after(mcontext.getStartDateFilter())
			// && property.getDateOfSale().before(mcontext.getEndDateFilter())
			// && (property.getPrice() >= mcontext.getStartPriceFilter()) &&
			// (property.getPrice() <= mcontext.getEndPriceFilter())) {

			Marker marker = mMap
					.addMarker(new MarkerOptions()
							.position(
									new LatLng(property.getL(), property
											.getO()))
							.title(property.getA1() + ","
									+ property.getA2())
							.icon(BitmapDescriptorFactory
									.defaultMarker(setMarkerColour(property
											.getP()))));
			
			// Disable the marker info window (as its too annoying).
			marker.hideInfoWindow();
			MarkerStorage.addMarker(marker, property);
			// }
		}
		

		// Update the average house price text for the visible map region
		updateGridPanelData(graphPoints);

		// Toast.makeText(mcontext, "Updated [" + counter + "] markers",
		// Toast.LENGTH_LONG).show();

		// perfTimer.getTime("updateMarkers", "");

		// AddMarkersToDatabaseTask task = new AddMarkersToDatabaseTask(sales,
		// selectedGridReferenceId);
		// task.execute();
	}

	public void updateGridIcons(List<ViewPort> viewPorts,
			List<GridAveragesWrapper> gridAveragesWrapperList) {

		Coordinate centre = null;
		ViewPort viewPort = null;
		GraphPoint graphPoint = null;
		int counter = 0;

		// Display all the grid average icons
		if (gridAveragesWrapperList != null) {

			for (GridAveragesWrapper gridAveragesWrapper : gridAveragesWrapperList) {

				try {

					viewPort = viewPorts.get(counter++);

					RemoteLog.log("viewPort [" + viewPort.getKey() + "]");

					centre = viewPort.getCenter();

					RemoteLog.log("viewPort.getCenter() ["
							+ viewPort.getBottomRightCoord().getLat() + "]");

					Map<String, GraphPoint> graphPoints = gridAveragesWrapper
							.getG();

					if (graphPoints != null) {

						graphPoint = graphPoints.get("2014");

						if (graphPoint != null) {

							RemoteLog.log("graphPoint [" + graphPoint.getA()
									+ "]");

							iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
							addIcon(iconFactory,
									PriceUtils.formatShortPrice(graphPoint
											.getA())
											+ "  ("
											+ PriceUtils.roundNumber(graphPoint
													.getQ()) + ")", new LatLng(
											centre.getLat(), centre.getLng()));
						}

						// Update the average house price text for the visible
						// map region
						updateGridPanelData(graphPoints);

					}

				} catch (Exception ex) {
					RemoteLog.error(ex);
				}

				graphPoint = null;
			}
		}
	}

	/*
	 * public Date getLastUpdateForGrid(int selectedGridReferenceId) {
	 * 
	 * Date lastUpdatedDate = null;
	 * 
	 * PropertySaleDAO dao = PropertySaleDAO.getInstance(mcontext);
	 * List<PropertyMDO> propertySales = dao
	 * .onGetPropertySales(selectedGridReferenceId);
	 * 
	 * if (propertySales != null && propertySales.size() > 0) { lastUpdatedDate
	 * = propertySales.get(0).getDateUpdated(); }
	 * 
	 * return lastUpdatedDate;
	 * 
	 * }
	 */

	/*
	 * public void updateMarkersFromDB(int selectedGridReferenceId) {
	 * 
	 * PropertySaleDAO dao = PropertySaleDAO.getInstance(mcontext);
	 * List<PropertySale> propertySales = dao
	 * .onGetPropertySales(selectedGridReferenceId);
	 * 
	 * AddressDAO addressDAO = AddressDAO.getInstance(mcontext); List<Address>
	 * addresses = addressDAO .onGetAddresss(selectedGridReferenceId);
	 * 
	 * GeoCodeDAO geoCodeDAO = GeoCodeDAO.getInstance(mcontext); List<GeoCode>
	 * geocodes = geoCodeDAO .onGetGeoCodes(selectedGridReferenceId);
	 * 
	 * for (int i = 0; i < geocodes.size(); i++) {
	 * 
	 * PropertySale propertySale = propertySales.get(i);
	 * propertySale.setAddress(addresses.get(i));
	 * propertySale.getAddress().setGeocode(geocodes.get(i));
	 * 
	 * // Check the sale is within the date filter range if
	 * (propertySale.getDateOfSale().after(mcontext.getStartDateFilter()) &&
	 * propertySale.getDateOfSale().before(mcontext.getEndDateFilter()) &&
	 * (propertySale.getPrice() >= mcontext.getStartPriceFilter()) &&
	 * (propertySale.getPrice() <= mcontext.getEndPriceFilter())) {
	 * 
	 * Marker marker = mMap.addMarker(new MarkerOptions() .position( new
	 * LatLng(geocodes.get(i).getLatitude(), geocodes .get(i).getLongitude()))
	 * .title(addresses.get(i).getAddressLine1() + "," +
	 * addresses.get(i).getAddressLine2()) .icon(BitmapDescriptorFactory
	 * .defaultMarker(setMarkerColour(propertySales.get(i) .getPrice()))));
	 * 
	 * MarkerStorage.addMarker(marker, propertySale); } }
	 * 
	 * Toast.makeText(mcontext, "Retrieved DB Sales " + propertySales.size(),
	 * Toast.LENGTH_SHORT).show(); }
	 */

	public void findLocationOnMap(PropertyMDO property) {

		try {

			int zoomLevel = getZoomLevel(property);

			// Construct a CameraPosition focusing and animate the camera to
			// that position.
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(property.getL(), property
							.getO())).zoom(zoomLevel) // Sets the zoom
					.bearing(90) // Sets the orientation of the camera to east
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		} catch (Exception ex) {
			RemoteLog.error(ex);
		}

	}

	private float setMarkerColour(double price) {

		if (price < 100000) {
			return BitmapDescriptorFactory.HUE_BLUE;
		} else if (price < 150000) {
			return 220;
		} else if (price < 200000) {
			return 200;
		} else if (price < 250000) {
			return BitmapDescriptorFactory.HUE_CYAN;
		} else if (price < 300000) {
			return 160;
		} else if (price < 350000) {
			return 140;
		} else if (price < 400000) {
			return BitmapDescriptorFactory.HUE_GREEN;
		} else if (price < 450000) {
			return 105;
		} else if (price < 500000) {
			return 95;
		} else if (price < 550000) {
			return 80;
		} else if (price < 600000) {
			return BitmapDescriptorFactory.HUE_YELLOW;
		} else if (price < 650000) {
			return 45;
		} else if (price < 700000) {
			return BitmapDescriptorFactory.HUE_ORANGE;
		} else if (price < 750000) {
			return 20;
		} else if (price < 800000) {
			return BitmapDescriptorFactory.HUE_MAGENTA;
		} else if (price < 850000) {
			return 310;
		} else if (price < 900000) {
			return BitmapDescriptorFactory.HUE_ROSE;
		} else if (price < 1000000) {
			return 345;
		} else {
			return BitmapDescriptorFactory.HUE_RED;
		}

	}

	/*@Override
	public void onInfoWindowClick(Marker marker) {

		PropertyMDO property = MarkerStorage.getMarker(marker);

		// Bring the user to the 'View' tab
		mcontext.setSelectedProperty(property);
		mcontext.addViewTabIfRequired();
		mcontext.setSelectedTab(1);
	}*/

	private int getZoomLevel(PropertyMDO property) {
		int zoomLevel = 17;

		return zoomLevel;
	}

	public void setCameraBearing(float bearing) {

		if (mapInitialised) {

			double lat = 53.349803;
			double lng = -6.260309;

            //LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(lat, lng)) // Sets the center of
															// the map to
							.zoom(17) // Sets the zoom
							.bearing(bearing) // Sets the orientation of the
												// camera to east
							.tilt(50) // Sets the tilt of the camera to 30
										// degrees
							.build(); // Creates a CameraPosition from the
										// builder
					mMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));

		}
	}

	private boolean checkIfViewPortUpdateRequired(float cameraZoomLevel) {
		boolean updateRequired = false;

		if (cameraZoomLevel > MINIMUM_ZOOM_LEVEL_FOR_MARKER_UPDATES) {

			long currentDate = new Date().getTime();

			// Only update the ViewPort when the user has stopped scrolling for
			// SCROLLING_AT_REST_PERIOD
			// and with the VIEWPORT_UPDATE_INTERVAL_GAP between updates.
			if ((currentDate - lastViewPortUpdateDate) > VIEWPORT_UPDATE_INTERVAL_GAP) {

				// Update the lastViewPortUpdateDate as we are updating the
				// ViewPort.
				lastViewPortUpdateDate = new Date().getTime();

				return true;
			}
		}

		return updateRequired;
	}

	private void updateVisibleMapWithMarkers(ViewPort cellViewPort,
			int selectedGridReferenceId) {

        try {
            GetPropertiesInViewportTask task = null;
            int discriminator = GetPropertiesDiscriminator.getDiscriminator(viewPortGridDivisor,cellViewPort);
            String endpoint = EndPointManager.getEndPoint(EndPointManager.GET_PROPERTIES_IN_VIEWPORT, discriminator, 0);

            // Update the grid on the center of the visible map
            task = new GetPropertiesInViewportTask(selectedGridReferenceId,
                    gridReferenceIdCache);
            task.setCallback(this);
            task.execute(endpoint + GetPropertiesInViewportTask.REQUEST_PARAM_KEY + "="
                    + cellViewPort.getKey());

            // TODO: Use a ValueAnimator to display a radius animation around the map focal point
            PolygonOptions rectOptions = new PolygonOptions().add(new LatLng(
                    cellViewPort.getTopLeftCoord().getLat(), cellViewPort
                            .getTopLeftCoord().getLng()), new LatLng(cellViewPort
                    .getTopLeftCoord().getLat(), cellViewPort.getBottomRightCoord()
                    .getLng()), new LatLng(cellViewPort.getBottomRightCoord()
                    .getLat(), cellViewPort.getBottomRightCoord().getLng()),
                    new LatLng(cellViewPort.getBottomRightCoord().getLat(),
                            cellViewPort.getTopLeftCoord().getLng()), new LatLng(
                            cellViewPort.getTopLeftCoord().getLat(), cellViewPort
                                    .getTopLeftCoord().getLng()));

            Polygon polygon = mMap.addPolygon(rectOptions
            .strokeColor(Color.BLUE));

            // Then get all the surrounding grids
            /*List<ViewPort> viewPorts = viewPortGridDivisor
                    .getSurroundingGrids(cellViewPort);

            // Fetch the grid averages
            GetGridAveragesTask getGridAveragesTask = new GetGridAveragesTask(this);
            getGridAveragesTask.setViewPorts(viewPorts);
            getGridAveragesTask.execute(EndPointManager.getEndPoint(EndPointManager.GET_GRID_AVERAGES, 0) + REQUEST_PARAM_KEYS + "=");*/

        } catch (Exception ex) {
            gridPanelLayout.setVisibility(ViewGroup.INVISIBLE);
            RemoteLog.error(ex);
        }


	}

	public void onUpdateMapAfterUserInterection() {
		mapPositionChanged(mMap.getCameraPosition());
	}

	private void updateGridPanelData(Map<String, GraphPoint> graphPoints) {

		double averagePrice = 0.0;
		double quantitySold = 0.0;

		try {
			// Calculate the average property price for this grid
            // TODO: Calculate the 12 month moving average.
			GraphPoint graphPoint = graphPoints.get("2015");

			// If there is no grid panel data, hide the grid panel
			if (graphPoint == null) {
				gridPanelLayout.setVisibility(ViewGroup.INVISIBLE);
			} else {

				// Display the grid panel
				gridPanelLayout.setVisibility(ViewGroup.VISIBLE);
				gridPanelLayout.bringToFront();

				averagePrice = graphPoint.getT() / graphPoint.getQ();
				quantitySold = graphPoint.getQ();
				averagePriceText.setText(PriceUtils.formatShortPrice(averagePrice) + "  (" + PriceUtils.roundNumber(quantitySold) + ")");
				averagePriceLabel.setText("Avg:");

				quantitySold = graphPoint.getQ();
				averageYearText.setText(graphPoint.getD());
				averageYearLabel.setText("Year:");

				miniGraphView.setData(graphPoints);

			}	
		} catch (Exception ex) {
			gridPanelLayout.setVisibility(ViewGroup.INVISIBLE);
			RemoteLog.error(ex);
		}

	}

	private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
		MarkerOptions markerOptions = new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(iconFactory
						.makeIcon(text))).position(position)
				.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

		mMap.addMarker(markerOptions);
	}

	public CameraPosition getCameraPosition() {

		return mMap.getCameraPosition();

	}

	public void hideMarkerWindow() {
		
		// Hide the marker window
		markerWindowLayout.setVisibility(android.view.View.INVISIBLE);
	}
	
	
	public void displayPropertyInfoWindow(PropertyMDO property) {

		Toast.makeText(mcontext, "displayPropertyInfoWindow " + property.getA1(), Toast.LENGTH_LONG).show();
		
		Picasso.with(mcontext)
				.load("http://maps.googleapis.com/maps/api/streetview?size="
						+ URLEncoder.encode("160x160") + "&location="
						+ property.getL() + "," + property.getO() + "&pitch=10")
				.into(markerWindowViewImage);
		

		addressText.setText(property.getAddress().formatAddress());
		priceText.setText(PriceUtils.formatPrice(property.getP()));
		dateText.setText(DateUtils.getString(property.getD()));

		// Bring the user to the 'View' tab
		mcontext.setSelectedProperty(property);

		// Display the marker window
		markerWindowLayout.setVisibility(android.view.View.VISIBLE);
		markerWindowLayout.bringToFront();

		streetViewButton.setText("Street");
		streetViewButton.bringToFront();

	}
	
	
}
