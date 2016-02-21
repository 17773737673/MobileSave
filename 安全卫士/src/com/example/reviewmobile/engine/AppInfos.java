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
 * ��ȡapp������
 * 
 * @author dell
 * 
 */
public class AppInfos {
	public static List<AppInfo> getAppInfos(Context context) {
		PackageManager packageManager = context.getPackageManager();
		ArrayList<AppInfo> info = new ArrayList<AppInfo>();
		
		List<PackageInfo> installedPackages = packageManager
				.getInstalledPackages(0);// ��ȡ������
		
		for (PackageInfo packageInfo : installedPackages) { // ����
			
			AppInfo appMsg = new AppInfo();
			
			Drawable loadIcon = packageInfo.applicationInfo
					.loadIcon(packageManager);// ��ȡ��app��ͼ��
			appMsg.setIcon(loadIcon);
			
			
			String loadLabel = packageInfo.applicationInfo
					.loadLabel(packageManager).toString();// ��ȡapp����
			appMsg.setApkName(loadLabel);
			
			
			String packageName = packageInfo.packageName;//Ӧ�ó������
			appMsg.setApkPackageName(packageName);
			
			int flags = packageInfo.applicationInfo.flags; //��ȡ����װ����ı��
			if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
				appMsg.setUserApk(true);		//��ʾ�û�app
			}else{
				appMsg.setUserApk(false);		//��ʾϵͳapp
			}
			
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				appMsg.setRom(false);	//��ʾ��sd��
			}else{
				appMsg.setRom(true);	//��ʾ���ֻ��ڴ�
			}
			
			String sourceDir = packageInfo.applicationInfo.sourceDir; 
			File appDir =new File(sourceDir);
			long appSize = appDir.length();
			String fileSize = Formatter.formatFileSize(context, appSize);	//���򳤶�
			appMsg.setApkSize(fileSize);
			info.add(appMsg);

		}
		return info;
	}
}