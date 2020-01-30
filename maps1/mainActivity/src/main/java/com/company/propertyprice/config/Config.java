package com.company.propertyprice.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Config {
	
	  private static Config instance = null;
	  private String deviceModel = null;
	  private String deviceManufacturer = null;
	  private String ipAddress = null;
		
	  private Config(){

			try {
				deviceModel = android.os.Build.MODEL;
				deviceManufacturer = android.os.Build.MANUFACTURER;
				//ipAddress=InetAddress.getLocalHost().getHostName();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	  }
	  
	  public static Config getInstance() {
	  	
	  	if (instance == null) {
	  		instance = new Config();
	  	}
	  	
	  	return instance;
	  }

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}

	public void setDeviceManufacturer(String deviceManufacturer) {
		this.deviceManufacturer = deviceManufacturer;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	  
	  
}
