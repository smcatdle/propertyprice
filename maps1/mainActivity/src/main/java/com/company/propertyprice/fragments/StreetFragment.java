package com.company.propertyprice.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.company.propertyprice.IrishPropertyApplication.TrackerName;
import com.company.propertyprice.activities.MainActivity;
import com.company.propertyprice.dao.PropertyListItemDAO;
import com.company.propertyprice.model.android.PropertyListItem;
import com.company.propertyprice.model.mdo.PropertyMDO;
import com.company.propertypricetest.R;
import com.company.utils.DateUtils;
import com.company.utils.PriceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.StreetViewPanorama.OnStreetViewPanoramaChangeListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLink;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StreetFragment extends Fragment implements OnMarkerDragListener, OnStreetViewPanoramaChangeListener {

    /**
     * The amount in degrees by which to scroll the camera
     */
    private static final int PAN_BY_DEG = 30;

    private static final float ZOOM_BY = 0.5f;

    private StreetViewPanorama svp;
    private View fragView = null;
    private MainActivity mcontext = null;
    private ImageView image = null;
    private Gson gson = null;
    /*private boolean slidersInitialised = false;
    private TextView priceRangeText = null;
    private TextView dateRangeText = null;*/

    @Override
    public void onAttach(Activity activity) {
		mcontext = (MainActivity) activity;
		mcontext.setViewFragmentCallback(this);

		super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	    
		if (fragView == null) {
			
			gson = new Gson();
			
		    fragView = inflater.inflate(R.layout.property_content_tab,
			    container, false);
		    
		    PropertyMDO property = mcontext.getSelectedProperty();
		    
		    if (property != null && property.getAddress() != null) {

				TextView addressText = (TextView) fragView
					.findViewById(R.id.address_text);
		
				TextView priceText = (TextView) fragView
					.findViewById(R.id.price_text);
		
				TextView dateText = (TextView) fragView
					.findViewById(R.id.date_text);

		
				addressText.setText(property.getAddress().concatAddress());
				priceText.setText(PriceUtils.formatPrice(property.getP()));
				dateText.setText(DateUtils.getString(property.getD()));
				
				setUpStreetViewPanoramaIfNeeded(savedInstanceState);
		    }
	
		    final Button button = (Button) fragView
			    .findViewById(R.id.add_property_button);
		    
		    button.setOnClickListener(new View.OnClickListener() {
		    	
				public void onClick(View v) {
		
				    PropertyListItemDAO dao = PropertyListItemDAO
					    .getInstance(mcontext);
				    PropertyMDO property = mcontext.getSelectedProperty();
				    PropertyListItem item = new PropertyListItem();
				    item.setPropertysaleid(property.getId());
				    item.setJson(gson.toJson(property));
				    dao.onAddPropertyListItem(item);
				    Toast.makeText(mcontext, "Property Saved", Toast.LENGTH_LONG).show();
				    mcontext.setSelectedTab(1);
				}
				
		    });

		    
			setUpStreetViewPanoramaIfNeeded(savedInstanceState);
		}

		// Get tracker.
		Tracker t = ((com.company.propertyprice.IrishPropertyApplication) getActivity().getApplication()).getTracker(
		    TrackerName.GLOBAL_TRACKER);

		// Set screen name.
		t.setScreenName("ViewFragment");

		// Send a screen view.
		t.send(new HitBuilders.AppViewBuilder().build());
		
		
		return fragView;
    }

    
    
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Fragment#onViewCreated(android.view.View,
     * android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
	
		PropertyMDO property = mcontext.getSelectedProperty();
	
		if (property != null && property.getAddress() != null) {

		    String formattedAddress = property.getAddress().formatAddress();
	
		    TextView addressText = (TextView) fragView
			    .findViewById(R.id.address_text);
	
		    TextView priceText = (TextView) fragView
			    .findViewById(R.id.price_text);
	
		    TextView dateText = (TextView) fragView
			    .findViewById(R.id.date_text);
	
		    addressText.setText(property.getAddress().formatAddress());
		    priceText.setText(PriceUtils.formatPrice(property.getP()));
		    dateText.setText(DateUtils.getString(property.getD()));
	
		}

    }

    class GetStreetViewImageTask extends AsyncTask {

		private MainActivity callback = null;
	
		@Override
		protected Bitmap doInBackground(Object... urls) {
	
		    // params comes from the execute() call: params[0] is the url.
		    try {
				String url = (String) urls[0];
				Bitmap bitmap = downloadUrl(url);
		
				return bitmap;
		    } catch (IOException e) {
		    	return null;
		    }
		}
	
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(Object result) {
	
		    try {
			Bitmap bitmap = (Bitmap) result;
	
			if (bitmap != null && bitmap.getByteCount() > 0) {
	
			    if (bitmap != null) {
	
					Toast.makeText(mcontext, "decoded", Toast.LENGTH_LONG)
						.show();
					image.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
						300, 300, false));
		
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
		private Bitmap downloadUrl(String myurl) throws IOException {
		    InputStream is = null;
	
		    Log.e("downloadUrl : ", myurl);
	
		    try {
				URL url = new URL(myurl);
				HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
		
				Log.e("onPostExecute : ", "Opened connection to url : ["
					+ myurl + "]");
		
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				// Starts the query
				conn.connect();
				int response = conn.getResponseCode();
				Log.d("GetPropertiesInViewportTask", "The response is: "
					+ response);
				is = conn.getInputStream();
		
				Bitmap bitmap = BitmapFactory.decodeStream(is);
		
				return bitmap;
		
				// Makes sure that the InputStream is closed after the app is
				// finished using it.
		    } finally {
		    	is.close();
		    }
		}

    }

    
	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	public void onStart() {
		
		super.onStart();

		PropertyMDO property = mcontext.getSelectedProperty();
		
		if (svp != null && property != null) {
	        svp.setPosition(new LatLng(property.getL(),property.getO()));
		}
	}
	
    private void setUpStreetViewPanoramaIfNeeded(Bundle savedInstanceState) {
        if (svp == null ) {
            svp = ((SupportStreetViewPanoramaFragment)
        	    ((FragmentActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.streetmap))
                    .getStreetViewPanorama();
            if (svp != null) {
                if (savedInstanceState == null) {
                    PropertyMDO property = mcontext.getSelectedProperty();
                    svp.setPosition(new LatLng(property.getL(),property.getO()));
                }
            }
        }
        
        // TODO: Modify JS code at bottom to place "property objects" on each property on street view, e.g. the price.
        // See https://github.com/rbejar/StreetView3DOverlay/blob/master/src/streetviewoverlay.js
    }
    
    /**
     * When the panorama is not ready the PanoramaView cannot be used. This should be called on
     * all entry points that call methods on the Panorama API.
     */
    private boolean checkReady() {
        if (svp == null) {
            Toast.makeText(mcontext, "Not Ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Called when the Go To Street Position is clicked.
     */
    public void onGoStreetPosition() {
        if (!checkReady()) {
            return;
        }
        PropertyMDO property = mcontext.getSelectedProperty();
        svp.setPosition(new LatLng(property.getL(),property.getO()));
    }


    public void onRequestPosition(View view) {
        if (!checkReady()){
            return;
        }
        if (svp.getLocation() != null) {
          Toast.makeText(view.getContext(), svp.getLocation().position.toString(),
              Toast.LENGTH_SHORT).show();
        }
    }

    public void onMovePosition(View view) {
        StreetViewPanoramaLocation location = svp.getLocation();
        StreetViewPanoramaCamera camera = svp.getPanoramaCamera();
        if (location != null && location.links != null) {
            StreetViewPanoramaLink link = findClosestLinkToBearing(location.links, camera.bearing);
            svp.setPosition(link.panoId);
        }
    }

    public static StreetViewPanoramaLink findClosestLinkToBearing(StreetViewPanoramaLink[] links,
        float bearing) {
        float minBearingDiff = 360;
        StreetViewPanoramaLink closestLink = links[0];
        for (StreetViewPanoramaLink link : links) {
            if (minBearingDiff > findNormalizedDifference(bearing, link.bearing)) {
                minBearingDiff = findNormalizedDifference(bearing, link.bearing);
                closestLink = link;
            }
        }
        return closestLink;
    }

    // Find the difference between angle a and b as a value between 0 and 180
    public static float findNormalizedDifference(float a, float b) {
        float diff = a - b;
        float normalizedDiff = diff - (360.0f * FloatMath.floor(diff / 360.0f));
        return (normalizedDiff < 180.0f) ? normalizedDiff : 360.0f - normalizedDiff;
    }
    
    @Override
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation location) {
        if (location != null) {
          //marker.setPosition(location.position);
        }
    }
    
    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        svp.setPosition(marker.getPosition(), 150);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }
    
    
}


/*
 

function StreetViewOverlay() {
    var SVO = {};    
    
    function streetViewPOVChangeListener() {    
        SVO.camera.rotation.x = SVO.streetViewPano.getPov().pitch * SVO.DEG2RAD;
        SVO.camera.rotation.y = - SVO.streetViewPano.getPov().heading * SVO.DEG2RAD;
    }
    
    // Fixed set of panoramas to choose
    var panoramaPos = [[41.684196,-0.888992],[41.685296,-0.888992],
                       [41.684196,-0.887992],[41.684196,-0.889992]];
    var currShownPano = 0;
    
    SVO.PANO_HEIGHT = 3; // For instance...

    SVO.DEFAULT_FOCAL_LENGTH = 25; // Will be using the default 35 mm for frame size
    SVO.STREETVIEW_FOCAL_LENGTH_MULTIPLIER = 15; // Discovered experimentally. Imprecise but
    // a reasonable approximation I think
    // 12 gives a horizontal FOV of 1.57 rads (aprox 90 degrees). With that value,
    // vertically the objects do not fit very well.. ??
    // Besides this, the 3d objects positioning is sligthly different in Firefox and Chromium
    // (now more precise in Firefox...) ?? 
    SVO.STREETVIEW_ZOOM_CONSTANT = 50; // Discovered experimentally. Imprecise. 
    SVO.STREETVIEW_DIV_ID = 'streetviewpano';
    SVO.THREEJS_DIV_ID = 'container';
    SVO.DEG2RAD = Math.PI / 180;
    SVO.RAD2DEG = 180 / Math.PI;
    
    SVO.objects3Dmaterial = null;
    
    SVO.currentPanorama = null;
    SVO.scene = new THREE.Scene();
    
    SVO.cameraParams = {focalLength: SVO.DEFAULT_FOCAL_LENGTH};
    SVO.camera = new THREE.PerspectiveCamera( 
            1, 16/9, 1, 1100 ); // When I initalize the camera, I will change fov and aspect
    
    // A spot light
    SVO.light = new THREE.SpotLight(0xffffbb);
    SVO.light.position.set( 200, 400, 400 ); // The position is chosen to be roughly
    // "compatible" with the sun in the panoramas we use
    SVO.light.castShadow = true; // only spotligths cast shadows in ThreeJS (I think...)
        
    SVO.renderer = null;
    
    SVO.$container = null;
    SVO.container = null;    
            
    SVO.dragView = {draggingView: false, mouseDownX: 0, mouseDownY: 0};
            
    SVO.$streetViewPano = null;
    SVO.streetViewPano = null;            
  
    SVO.currentStreetViewZoom = 1;           
                    
    SVO.showing = {streetView: true, objects3D: false};
    
    SVO.mesh = null;
    
    SVO.load = function(showing, mesh, lat, lon) {
        $(document).ready(function(){
            SVO.showing= $.extend(SVO.showing, showing);
            SVO.mesh = mesh;
            
                      
            if (SVO.showing.webGL) {                
                if (Detector.webgl) {
                    SVO.renderer = new THREE.WebGLRenderer();
                } else {                    
                    SVO.renderer = new THREE.CanvasRenderer();
                    $("#help").append('<p>WebGL not supported. A slower and less pretty version is shown.</p>');
                    $("#help").append('<p><a target="_blank" href="http://www.khronos.org/webgl/wiki_1_15/index.php/Getting_a_WebGL_Implementation">Click here to find out how to activate WebGL support</a></p>');
                }                   
            } else {
                SVO.renderer = new THREE.CanvasRenderer();
                $("#help").append('<p>WebGL not even tried. A slower and less pretty version is shown.</p>');
                $("#help").append('<p><a target="_blank" href="http://www.khronos.org/webgl/wiki_1_15/index.php/Getting_a_WebGL_Implementation">Click here to find out how to activate WebGL support</a></p>');
            }
            SVO.renderer.setClearColor(0x000000, 0); // TRANSPARENT BACKGROUND        
            SVO.renderer.shadowMapEnabled = true; // For shadows to be shown
            
            SVO.$container = $('#' + SVO.THREEJS_DIV_ID);
            SVO.container = SVO.$container.get(0);
            SVO.container.appendChild(SVO.renderer.domElement);
            
            SVO.$streetViewPano = $('#'+SVO.STREETVIEW_DIV_ID);
            SVO.streetViewPano = new google.maps.StreetViewPanorama($('#'+SVO.STREETVIEW_DIV_ID).get(0),
                             {disableDefaultUI: true, scrollwheel: false, clickToGo: false});
            
            // In order to show and make responsive the links that Google adds to every
            // Street View panorama (link to Google maps, terms of use etc.) I have
            // made the threejs container smaller than the streetview panorama, so the 
            // bottom of the panorama is not covered. This works, but creates a problem: if
            // the user drags the panorama from the bottom of the window, the threejs
            // container does not receive the event, so the panorama changes but the 3D model
            // does not. I have to listen to the pano_changed event of the street view
            // panorama to make up for this:            
            google.maps.event.addListener(SVO.streetViewPano, 'pov_changed', streetViewPOVChangeListener);
  

                                
            if (SVO.showing.objects3D) {
                SVO.scene.add(SVO.mesh);
            }
       
            SVO.scene.add(SVO.light);
            SVO.attachEventHandlers(); 
            // Obtain real panorama position (the closest one in Street View to
            // lat,lon and call init to start   
            SVO.realPanoPos(lat,lon, SVO.init); 
        });
    };
  
    SVO.init = function(lat, lon) {
        var i;

        var panoPos = latLon2ThreeMeters(lat, lon);
        
        SVO.currentPanorama = {};
        SVO.currentPanorama.position = new THREE.Vector3(panoPos.x, panoPos.y, panoPos.z); 
        SVO.currentPanorama.position.y += SVO.PANO_HEIGHT;
        SVO.currentPanorama.heading = 0;           
        SVO.currentPanorama.pitch = 0;         
                
        if (SVO.showing.streetView) {
            SVO.cameraParams.focalLength = SVO.streetViewFocalLenght();
            SVO.initStreetView(lat, lon);
        }              
        
        SVO.camera.aspect = SVO.$container.width() / SVO.$container.height();
        SVO.camera.setLens(SVO.cameraParams.focalLength); 
                              
        SVO.camera.position = SVO.currentPanorama.position;
                       
        // Changing rotation order is necessary. As rotation is relative to the position
        // of the camera, if it rotates first in the X axis (by default), the Y axis
        // will not be "up" anymore. If I rotate first in the Y axis, rotation in X is
        // not affected so I can rotate in X later.  
        SVO.camera.rotation.order = 'YXZ';
        SVO.camera.rotation.x = SVO.currentPanorama.pitch * SVO.DEG2RAD;
        SVO.camera.rotation.y = -SVO.currentPanorama.heading * SVO.DEG2RAD;
                
        SVO.renderer.setSize(SVO.$container.width(), SVO.$container.height());
        SVO.animate();        
    };
    
    SVO.initStreetView = function(lat, lon) {                    
        var panoPos = new google.maps.LatLng(lat,lon);                
        var myPOV = {heading:SVO.currentPanorama.heading, 
                     pitch:SVO.currentPanorama.pitch, zoom:1};        
        SVO.streetViewPano.setPosition(panoPos);
        SVO.streetViewPano.setPov(myPOV);
    };
    
    SVO.updateStreetView = function() {
        // If I am calling this function, I do not need the streetViewPano
        // to generate events when pov changes
        google.maps.event.clearListeners(SVO.streetViewPano, 'pov_changed');   
                            
        var myPOV = {heading: SVO.currentHeading(), 
                     pitch:SVO.currentPitch(), zoom:SVO.currentStreetViewZoom};                   
        SVO.streetViewPano.setPov(myPOV);
          
        // After updating pov, it can generate pov change events again
        google.maps.event.addListener(SVO.streetViewPano, 'pov_changed', streetViewPOVChangeListener);
    };      
    
    SVO.realPanoPos = function(lat, lon, callBackFun) {                                                                
        var givenPanoPos = new google.maps.LatLng(lat, lon);                

        function processSVData(data, status) {
           if (status === google.maps.StreetViewStatus.OK) {
               callBackFun(data.location.latLng.lat(),
                           data.location.latLng.lng());               
           } else {
               throw new Error("Panorama not found");     
           }          
        }
        var sv = new google.maps.StreetViewService();
        // With a radius of 50 or less, this call returns information
        // about the closest streetview panorama to the given position.
        // In the callback function processSVData, the data
        // parameter can give us the TRUE position of the panorama.
        // This is necessary because the StreetViewPanorama object position
        // is the one we give to it, no the TRUE position of that panorama.
        sv.getPanoramaByLocation(givenPanoPos, 50, processSVData);                                  
    };
    
    // Returns a focal length "compatible" with a Google Street View background
    // given the current size of the renderer and the given zoomLevel
    SVO.streetViewFocalLenght = function(zoomLevel) {
        if (!zoomLevel || zoomLevel < 1) {
            zoomLevel = 1;
        }
        if (SVO.$container.width() > 0) {            
            return (SVO.STREETVIEW_FOCAL_LENGTH_MULTIPLIER * SVO.$container.width() / SVO.$container.height()) 
                   + SVO.STREETVIEW_ZOOM_CONSTANT * (zoomLevel - 1);
            
        } else {
            // Just in case innerHeight is 0. If that happens, it does not matter which
            // focal length we return, because nothing will be shown
            return SVO.DEFAULT_FOCAL_LENGTH;
        }
    };
    

    SVO.currentHeading = function() {
      return -(SVO.camera.rotation.y * SVO.RAD2DEG);    
    };
    
    SVO.currentPitch = function() {
      return SVO.camera.rotation.x * SVO.RAD2DEG;    
    };
    
    
    SVO.attachEventHandlers = function() {    
        function onMouseWheel(event) {
            event.preventDefault();           
            event.stopPropagation();

            // Zooming could be more "progressive", but for now this is enough
            if (event.deltaY > 0) {            
                SVO.currentStreetViewZoom += 1;
                SVO.cameraParams.focalLength = SVO.streetViewFocalLenght(SVO.currentStreetViewZoom);
            } else {
                if (SVO.currentStreetViewZoom > 1) {
                    SVO.currentStreetViewZoom -= 1;
                    SVO.cameraParams.focalLength = SVO.streetViewFocalLenght(SVO.currentStreetViewZoom);
                }
            }            
            
            
            SVO.camera.setLens(SVO.cameraParams.focalLength);
            SVO.camera.updateProjectionMatrix();
            
            SVO.updateStreetView();
            
            SVO.render();
        };  
        
        function onMouseDown(event) {
            event.preventDefault();           
            event.stopPropagation();
                
            SVO.dragView.draggingView = true;

            SVO.dragView.mouseDownX = event.clientX;
            SVO.dragView.mouseDownY = event.clientY;                                               
        };
        
        function onMouseUp(event) {
            event.preventDefault();           
            event.stopPropagation();
            SVO.dragView.draggingView = false;
        };
        
        function onMouseMove(event) {
            event.preventDefault();           
            event.stopPropagation();
            
            var horizontalMovement, verticalMovement;     
            
            var aspect = SVO.$container.width() / SVO.$container.height();
            // horizontal FOV. Formula from <https://github.com/mrdoob/three.js/issues/1239>            
            var hFOV = 2 * Math.atan( Math.tan( (SVO.camera.fov * SVO.DEG2RAD) / 2 ) * aspect );            
            //console.log("focal lenght zoom 1="+SVO.STREETVIEW_FOCAL_LENGTH_MULTIPLIER * SVO.$container.width() / SVO.$container.height());
            //console.log("SVO.camera.fov="+SVO.camera.fov);
            //console.log("hFOV="+hFOV);
            
            if (SVO.dragView.draggingView) {
                horizontalMovement = SVO.dragView.mouseDownX  - event.clientX;
                verticalMovement  = SVO.dragView.mouseDownY  - event.clientY;
                
                // The /N is to adjust the "responsiveness" of the panning. 
                // This needs a rewriting but for now it works...                
                SVO.camera.rotation.y = (SVO.camera.rotation.y - ((horizontalMovement/4) * hFOV / SVO.$container.width())) % (2 * Math.PI);                
                SVO.camera.rotation.x = SVO.camera.rotation.x + ((verticalMovement/4) *  (SVO.camera.fov * SVO.DEG2RAD) / SVO.$container.height());
                SVO.camera.rotation.x = Math.max(-Math.PI/2, Math.min( Math.PI/2, SVO.camera.rotation.x));                
                
                SVO.updateStreetView();
            }
        };
        
        function onWindowResize() {
            SVO.camera.aspect = SVO.$container.width() / SVO.$container.height();            
            
            if (SVO.showing.streetView) {
              SVO.cameraParams.focalLength = SVO.streetViewFocalLenght();
              SVO.camera.setLens(SVO.cameraParams.focalLength);    
            }                                    
            SVO.camera.updateProjectionMatrix();            
            
            SVO.renderer.setSize(SVO.$container.width(), SVO.$container.height());
        };
        
        function onKeyUp(event) {            
            currShownPano = (currShownPano + 1) % panoramaPos.length; 
            
            switch (event.which) {
                case 13: // return
                case 32: // space         
                    event.preventDefault();
                    event.stopPropagation();
                    // change to the next panorama in panoramaPos
                    SVO.realPanoPos(panoramaPos[currShownPano][0],
                                    panoramaPos[currShownPano][1], SVO.init);                          
                default:
                    // Nothing. It is important not to interfere at all 
                    // with keys I do not use.
            }
        };
      
        SVO.$container.mousewheel(onMouseWheel);
        SVO.$container.mousedown(onMouseDown);
        SVO.$container.mouseup(onMouseUp);
        SVO.$container.mousemove(onMouseMove);
        $(window).resize(onWindowResize);
        $(document).on("keyup", onKeyUp);
        
    };

    SVO.animate = function() {
        requestAnimationFrame(SVO.animate);
        SVO.render();
    };

    SVO.render = function() {        
        SVO.renderer.render(SVO.scene, SVO.camera );
    };
    
    return SVO;
};
*/
