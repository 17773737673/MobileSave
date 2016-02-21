package com.example.reviewmobile.engine;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable icon; //appͼ��
	private String apkName;//app����
	private String apkSize; //app��С
	private boolean userApk;//�û�app��ϵͳapp
	private boolean isRom;	//����λ��
	private String apkPackageName;
	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", apkName=" + apkName + ", apkSize="
				+ apkSize + ", userApk=" + userApk + ", isRom=" + isRom
				+ ", apkPackageName=" + apkPackageName + "]";
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getApkSize() {
		return apkSize;
	}
	public void setApkSize(String apkSize) {
		this.apkSize = apkSize;
	}
	public boolean isUserApk() {
		return userApk;
	}
	public void setUserApk(boolean userApk) {
		this.userApk = userApk;
	}
	public boolean isRom() {
		return isRom;
	}
	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}
	public String getApkPackageName() {
		return apkPackageName;
	}
	public void setApkPackageName(String apkPackageName) {
		this.apkPackageName = apkPackageName;
	}
	
	
}
