/**
 * 
 */
/**
 * @author dell
 *
 */
package com.example.reviewmobile.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

/**
 * 获取app工具类
 * 
 * @author dell
 * 
 */
public class AppInfos {
	public static List<AppInfo> getAppInfos(Context context) {
		PackageManager packageManager = context.getPackageManager();
		ArrayList<AppInfo> info = new ArrayList<AppInfo>();
		
		List<PackageInfo> installedPackages = packageManager
				.getInstalledPackages(0);// 获取包集合
		
		for (PackageInfo packageInfo : installedPackages) { // 迭代
			
			AppInfo appMsg = new AppInfo();
			
			Drawable loadIcon = packageInfo.applicationInfo
					.loadIcon(packageManager);// 获取到app的图标
			appMsg.setIcon(loadIcon);
			
			
			String loadLabel = packageInfo.applicationInfo
					.loadLabel(packageManager).toString();// 获取app名称
			appMsg.setApkName(loadLabel);
			
			
			String packageName = packageInfo.packageName;//应用程序包名
			appMsg.setApkPackageName(packageName);
			
			int flags = packageInfo.applicationInfo.flags; //获取到安装程序的标记
			if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
				appMsg.setUserApk(true);		//表示用户app
			}else{
				appMsg.setUserApk(false);		//表示系统app
			}
			
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				appMsg.setRom(false);	//表示在sd卡
			}else{
				appMsg.setRom(true);	//表示在手机内存
			}
			
			String sourceDir = packageInfo.applicationInfo.sourceDir; 
			File appDir =new File(sourceDir);
			long appSize = appDir.length();
			String fileSize = Formatter.formatFileSize(context, appSize);	//程序长度
			appMsg.setApkSize(fileSize);
			info.add(appMsg);

		}
		return info;
	}
}