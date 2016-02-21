package com.example.reviewmobile.service;

import com.example.reviewmobile.utils.ModifyOffset;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;


/**
 * 
 * 创建坐标服务器
 *
 */
public class LocationService extends Service {

	private MyLocationListener listener;
	private LocationManager lm;
	private SharedPreferences pref;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		pref = getSharedPreferences("config", MODE_PRIVATE);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();
		//自定义坐标管理器标准
		Criteria cr = new Criteria();
		cr.setCostAllowed(true); 	//设置是否允许收费
		cr.setAccuracy(Criteria.ACCURACY_FINE);//设置精确度为良好
		String bestProvider = lm.getBestProvider(cr, true);	//获取最佳位置提供者
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		
	}
	class MyLocationListener implements LocationListener{
		
		//位置发生变化时
		@Override
		public void onLocationChanged(Location location) {
			// 使用经纬度偏移纠正获取经纬度
			String jingweidu = ModifyOffset.jingweidu(location.getLongitude(), location.getLatitude());
			System.out.println(jingweidu);
			
			//把获取到的经纬度保存
			pref.edit().putString("location", jingweidu).commit();
		}
		
		//当用户关闭gps时
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//当用户打开gps时
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//状态发生变化时
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//当程序关闭时
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(listener);
	}
}
