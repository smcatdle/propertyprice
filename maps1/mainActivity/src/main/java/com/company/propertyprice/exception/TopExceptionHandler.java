package com.company.propertyprice.exception;


import com.company.propertyprice.config.Config;
import com.company.propertyprice.model.UserCrash;
import com.company.propertyprice.network.LogUserCrashTask;

import android.app.Activity;

public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {

	private Thread.UncaughtExceptionHandler defaultUEH;

	private Activity app = null;

	//TODO: Update this to send error back to server.
	public TopExceptionHandler(Activity app) {
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		this.app = app;
	}

	public void uncaughtException(Thread t, Throwable e) {
		
		try {
			
			Config config = Config.getInstance();
			UserCrash userCrash = new UserCrash();
			userCrash.setException(e);
			
			
			//userCrash.setDataType(UserCrash.DATA_TYPE_JSON);
			userCrash.setDevice(config.getDeviceManufacturer() + " " + config.getDeviceModel());
			//userCrash.setIpAddress(ipAddress);
			//userCrash.setEventType(UserCrash.CRASH_TYPE_UNEXPECTED);
			//userCrash.setDateCreated(new Date());
			
			//Toast.makeText(app, "TopExceptionHandler 1 ...",
					//Toast.LENGTH_SHORT).show();
			
			
			LogUserCrashTask task = new LogUserCrashTask();
			task.setUserCrash(userCrash);
			task.execute();
			
		} catch (Exception ioe) {
			// ...
		}

		defaultUEH.uncaughtException(t, e);
		
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}