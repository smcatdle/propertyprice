package com.company.propertyprice.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InitiatePPREngineTask {
	
	public static final String[] serverEndpoints = {"http://sales-propertyprod.rhcloud.com/property-price-server/PPREngineBatchServlet"};	
	
	private static final Logger logger = Logger.getLogger(InitiatePPREngineTask.class.getName());
	
	public static void main(String[] args) {
		
		try {
		
		    // Send the 'InitiatePPREngineTask' to each master server
		    for (int i=0; i<serverEndpoints.length; i++) {
			sendMessage(serverEndpoints[i]);
		    }
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());
		}
	}
	
	
	public static void sendMessage(String serverEndpoint) {
		
		try {
			
			String line = null;
			URL url = null;
			String resultString = "";
			
			url = new URL(serverEndpoint);
			
			logger.log(Level.INFO, "Initiating PPR Engine Task for endpoint [" + serverEndpoint + "]");
	
			// Wait 60 seconds before firing out the request to the server (to allow the server to startup)
			Thread.sleep(60000);
			
			HttpURLConnection uc;
				uc = (HttpURLConnection)url.openConnection();
				long startTime = System.currentTimeMillis();
				uc.connect(); 
			
			StringBuffer tmp = new StringBuffer();
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			
			while ((line = in.readLine()) != null) {
				resultString = resultString + line;
			}
			
			long endTime = System.currentTimeMillis();
			
			uc.disconnect();
	
			in = null;
			tmp = null;
			uc = null;
			url = null;
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
