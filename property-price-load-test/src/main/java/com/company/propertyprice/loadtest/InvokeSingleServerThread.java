package com.company.propertyprice.loadtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InvokeSingleServerThread extends Thread {
	
	public static final String proxyUrl = "http://sales-propertyprod.rhcloud.com/property-price-server/?service=getPropertiesWithinViewport&topLeftX=53.1404580000&topLeftY=-6.2278832000&bottomRightX=53.3004582000&bottomRightY=-6.1";
	public static final long TOTAL_ITERATIONS = 1;	
	
	private final Logger logger = Logger.getLogger(InvokeSingleServerThread.class.getName());
	private Result result = null;
	
	public InvokeSingleServerThread(Result result) {
		this.result = result;
	}
	
	
	public void run() {
		
		String line = null;
		URL url = null;
		long iterationCount = 0;
		
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 2000));
		
		try {
			
			long startIterationsTime = System.currentTimeMillis();
			
			while (iterationCount++ < TOTAL_ITERATIONS) {
				
				Double random = Math.random()*1000000;
				int randomDigits = random.intValue();
				
				logger.log(Level.INFO, "Executing client thread : [" + Thread.currentThread().getName() + "] and random rumber : [" + randomDigits + "]");
				
				url = new URL(proxyUrl + randomDigits);
	
	
				HttpURLConnection uc;
					uc = (HttpURLConnection)url.openConnection();
					long startTime = System.currentTimeMillis();
					uc.connect(); 
				
				StringBuffer tmp = new StringBuffer();
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	
				String resultString = result.getValue();
				
				while ((line = in.readLine()) != null) {
					resultString = resultString + line;
				}
				
				long endTime = System.currentTimeMillis();
				
				uc.disconnect();
				
				logger.log(Level.INFO, "Latency is [" + (endTime - startTime) + "] milliseconds.");
				
				result.setValue(resultString);
				resultString= null;
				in = null;
				tmp = null;
				uc = null;
				url = null;
				
				//logger.log(Level.INFO, "Retrieved result : [" + resultString + "] from thread [" + Thread.currentThread().getName() + "]");
			}
			
			long endIterationsTime = System.currentTimeMillis();
			float totalTimeAllTransactionsInSeconds = (endIterationsTime - startIterationsTime)/1000;
			logger.log(Level.INFO, "Total time for [" + TOTAL_ITERATIONS + "] is [" + totalTimeAllTransactionsInSeconds + "] seconds.");
			float transPerSecond = TOTAL_ITERATIONS/totalTimeAllTransactionsInSeconds;
			float averageLatency = totalTimeAllTransactionsInSeconds/TOTAL_ITERATIONS;
			logger.log(Level.INFO, "Transactions per second : [" + transPerSecond + "]");
			logger.log(Level.INFO, "Average Latency per transaction : [" + averageLatency + "] seconds");
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static void main (String[] args) {
		
		Result threadResult = new Result("");
		
		InvokeSingleServerThread thread = null;
		
		thread = new InvokeSingleServerThread(threadResult);
		thread.start();

		
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
