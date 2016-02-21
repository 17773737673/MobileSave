package com.example.reviewmobile.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.reviewmobile.bean.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;

public class TaskProcesses {

	public static List<TaskInfo> getTaskInfo(Context context) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager packageManager = context.getPackageManager();

		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

		List<TaskInfo> TaskInfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			
			try {
				TaskInfo info = new TaskInfo();
				String processName = runningAppProcessInfo.processName;
				info.setPackageName(processName);

				PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
				Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
				info.setIcon(icon);
				
				String label = packageInfo.applicationInfo.loadLabel(packageManager).toString();
				info.setAppName(label);

				int flags = packageInfo.applicationInfo.flags; // ��ȡ����װ����ı��
				if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					info.setUserApp(false); // ��ʾϵͳapp
				} else {
					info.setUserApp(true); // ��ʾ�û�app
				}
				//��С
//				
//				MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[runningAppProcessInfo.pid]);
//				int s = memoryInfo[0].getTotalPrivateDirty()*1024;
				String sourceDir = packageInfo.applicationInfo.sourceDir; 
				File appDir =new File(sourceDir);
				long appSize = appDir.length();
				//String fileSize = Formatter.formatFileSize(context, appSize);	//���򳤶�
				info.setMemorySize(appSize);
				TaskInfos.add(info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return TaskInfos;
	}
}
