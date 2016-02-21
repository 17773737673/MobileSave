package com.example.reviewmobile.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public   class ServiceStateUtils {
	
	//当前进程
	public static boolean isServiceRunning(Context context,String packageName){
		
		/**
		 * ActivtiyManager
		 */
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		//获取当前服务数
		
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();// 获取服务的名称
			// System.out.println(className);
			if (className.equals(packageName)) {// 服务存在
				return true;
			}
		}
		return false;
	}
}
