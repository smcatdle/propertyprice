package com.company.propertyprice.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KeepTestSystemAliveTask {
	
	public static final String taskRequestUrl = "http://sales-propertyprice.rhcloud.com/property-price-server/PropertyServlet?service=getPropertiesWithinViewport&topLeftX=53.1404580000&topLeftY=-6.2078832000&bottomRightX=53.2004582000&bottomRightY=-6.1978834000";	
	
	private static final Logger logger = Logger.getLogger(KeepTestSystemAliveTask.class.getName());
	
	
	public static void main(String[] args) {
		
		try {
			
			String line = null;
			URL url = null;
			String resultString = "";
			
			url = new URL(taskRequestUrl);
			
			logger.log(Level.INFO, "Initiating Keep Test System Alive Task...");
	
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
