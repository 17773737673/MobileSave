package com.example.reviewmobile.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public   class ServiceStateUtils {
	
	//��ǰ����
	public static boolean isServiceRunning(Context context,String packageName){
		
		/**
		 * ActivtiyManager
		 */
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		//��ȡ��ǰ������
		
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();// ��ȡ���������
			// System.out.println(className);
			if (className.equals(packageName)) {// �������
				return true;
			}
		}
		return false;
	}
}
