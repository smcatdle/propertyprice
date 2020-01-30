package com.company.propertyprice.util;

import com.company.propertyprice.model.UserCrash;
import com.company.propertyprice.network.LogUserCrashTask;

public class RemoteLog {

	private static boolean debugEnabled = false;
	private static boolean networkStatsEnabled = true;
	private static boolean crashReportEnabled = true;
	
	public static synchronized void log(String value) {
		
		if (debugEnabled) {
			UserCrash userCrash = new UserCrash();
			userCrash.setData(value);
			LogUserCrashTask task = new LogUserCrashTask();
			task.setUserCrash(userCrash);
			task.execute();
		}
	}
	
	public static synchronized void error(Exception exception) {
		
		if (crashReportEnabled) {
			UserCrash userCrash = new UserCrash();
			userCrash.setException(exception);
			LogUserCrashTask task = new LogUserCrashTask();
			task.setUserCrash(userCrash);
			task.execute();
		}
	}

	public static synchronized void network(String value) {
		
		if (networkStatsEnabled) {
			UserCrash userCrash = new UserCrash();
			userCrash.setData(value);
			LogUserCrashTask task = new LogUserCrashTask();
			task.setUserCrash(userCrash);
			task.execute();
		}
	}
	
	public static boolean isDebugEnabled() {
		return debugEnabled;
	}

	public static void setDebugEnabled(boolean debugEnabled) {
		RemoteLog.debugEnabled = debugEnabled;
	}

	public static boolean isCrashReportEnabled() {
		return crashReportEnabled;
	}

	public static void setCrashReportEnabled(boolean crashReportEnabled) {
		RemoteLog.crashReportEnabled = crashReportEnabled;
	}

	public static boolean isNetworkStatsEnabled() {
		return networkStatsEnabled;
	}

	public static void setNetworkStatsEnabled(boolean networkStatsEnabled) {
		RemoteLog.networkStatsEnabled = networkStatsEnabled;
	}
	
	
}
