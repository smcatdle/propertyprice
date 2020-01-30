/**
 * 
 */
package com.company.propertyprice.service;

import java.util.Date;
import java.util.List;
import java.util.zip.Adler32;

import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;
import com.company.propertyprice.dao.GridDAO;
import com.company.propertyprice.model.android.Grid;
import com.company.propertyprice.network.EndPointManager;
import com.company.propertyprice.network.GetPropertiesInViewportTask;
import com.company.propertyprice.processor.GetPropertiesDiscriminator;
import com.company.utils.network.HttpUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

/**
 * @author smcardle
 *
 */
public class GridPathPredictionService extends Service {

	/* The purpose of this service is to predict grids the user will pan over and cache these in the database, to speed up responsiveness.
	 * One example is retrieving a broad path of grids out from the current panning location.
	 * 
	 * This service should start when;
	 * 
	 *   - user first installs (retrieve all properties within a given radius of the users current location) (wi-fi and battery high).
	 *   - user Wi-Fi is enabled and battery high (large radius of current location).
	 *   - user is panning on map (small radius from pan location).
	 *   
	 *   The service should get all grids from the server,
	 *    	- within a given radius in the direction the user is panning
	 *    
	 *    
	 *   The grids should be retrieved in batch concurrent mode, to save battery and network latency.
	 *   e.g. multiple grids retrieved per HTTP request, with multiple concurrent request.
	 *   GZIP should compress data sent/retrieved over the network to reduce user data usage.
	 *    
	 *    
	 *  NB:  Grid retrieval performance should be monitored and tweaked according to "real world" user patterns.
	 *  The performance data should be sent to the server in batches (when wi-fi enabled and battery high).
	 *  
	 *  NB: DO NOT UPLOAD ANY DATA WHICH CAN IDENTIFY THE USER, OR THEIR LOCATION. ONLY GENERIC PERFORMANCE STRATEGY INFO.
	 *  
	 *  
	 */
	
	
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private ViewPortGridDivisor viewPortGridDivisor = null;
	private int selectedGridReferenceId = 0;
	private int radius = 0;
	// Fast checksum generation.
	Adler32 adler32 = null;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

		selectedGridReferenceId = intent.getIntExtra("selectedGridReferenceId", 0);
		radius = intent.getIntExtra("radius", 3);
		
		viewPortGridDivisor = new ViewPortGridDivisor(ViewPort.VIEWPORT_IRELAND);
		
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return this.START_NOT_STICKY;
	}

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			
			String result = null;
			String url = null;
			Grid grid = null;
			Grid existingGrid = null;
			long checksum = 0;
			
			try {
				
				// Get the current view port
				ViewPort cellViewPort = viewPortGridDivisor
						.getCellViewPort(selectedGridReferenceId);
				
				//Toast.makeText(getBaseContext(), PropertyMDODAO.getInstance(getBaseContext()).onGetPropertySales(520300).get(0), Toast.LENGTH_SHORT).show();
				
				
				// Retrieve all the grids at a certain radius out from current grid
				List<ViewPort> viewPorts = viewPortGridDivisor.getRadialGrids(cellViewPort, radius);

				for (ViewPort viewPort : viewPorts) {
					
					int gridId = viewPortGridDivisor.getGridReferenceId(viewPort.getCenter());
					
					// Only get grids that have been updated on the server.
					List<Grid> grids = GridDAO.getInstance(getBaseContext()).onGetGrids(gridId);
					if (grids != null && grids.size() > 0) {
						existingGrid = grids.get(0);
						
						// Generate checksum for this grid and only retrieve grids that have been updated.
						adler32.update(existingGrid.getJson().getBytes());
						checksum = adler32.getValue();
					}
					
					//TODO: Send multiple threaded batch HTTP requests (using pool of connections) (similar to grid averages reducing latency)
                    int discriminator = GetPropertiesDiscriminator.getDiscriminator(viewPortGridDivisor, cellViewPort);
                    String endpoint = EndPointManager.getEndPoint(EndPointManager.GET_PROPERTIES_IN_VIEWPORT, discriminator, 0);
					url = endpoint + GetPropertiesInViewportTask.REQUEST_PARAM_KEY + "="
							+ viewPort.getKey() + ":" + checksum;
		
					result = HttpUtils.httpRequest(url, HttpUtils.METHOD_GET, null, null);
					
					if (result != null && !"".equals(result) && !"null".equals(result)) {
						
						// Save the grid to the DB
						grid = new Grid();
						grid.setGridId(gridId);
						grid.setUpdate(false);
						grid.setJson(result);
						grid.setDateUpdated(new Date());
						grid.setDateLastUsed(new Date());
						GridDAO.getInstance(getBaseContext()).onAddGrid(grid);
						
					}

				}
				
				Toast.makeText(getBaseContext(), "service ended", Toast.LENGTH_LONG).show();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				grid = null;
				existingGrid = null;
			}
			
			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}
	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		adler32 = new Adler32();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

}
