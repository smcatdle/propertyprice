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
import com.company.propertyprice.network.GetPropertiesInViewportTask;
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
public class UpdateMRUGridsService extends Service {

	/* The purpose of this service is provide grid data through the database, to speed up responsiveness/accuracy.
	 * This service retrieves MRU grids (grids with update-required flag set) from the DB
	 * Grid checksums should be sent to server, and only grids with new checksums retrieved.
	 * 
	 * e.g. when a user pans over a grid;
	 *    - the DB is first checked for a matching entry with gridId   (a further step of first checking memory cache could be added)
	 *    - a matching grid is found but it hasn't been updated in a week
	 *    - the DB grid is displayed, but this grid is marked for update   (Improve by fetching from server once off?????)
	 *    - the UpdateMRUPropertiesService updates the MRU grids
	 * 
	 * 
	 * This service should start when battery charge is high, and wi-fi enabled (i.e. Android wakeup alarm when wi-fi enabled????).
	 * 
	 * The grids should be retrieved in batch concurrent mode, to save battery and network latency.
	 *   e.g. multiple grids retrieved per HTTP request, using multiple concurrent requests.
	 *    
	 *    
	 */
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		
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

			try {
				
				// Get checksum for this grid and only retrieve grids that have been updated.
				Adler32 adler32 = new Adler32();
				adler32.update("grid json".getBytes());
				long checksum = adler32.getValue();

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
