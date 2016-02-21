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
 * �������������
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
		//�Զ��������������׼
		Criteria cr = new Criteria();
		cr.setCostAllowed(true); 	//�����Ƿ������շ�
		cr.setAccuracy(Criteria.ACCURACY_FINE);//���þ�ȷ��Ϊ����
		String bestProvider = lm.getBestProvider(cr, true);	//��ȡ���λ���ṩ��
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		
	}
	class MyLocationListener implements LocationListener{
		
		//λ�÷����仯ʱ
		@Override
		public void onLocationChanged(Location location) {
			// ʹ�þ�γ��ƫ�ƾ�����ȡ��γ��
			String jingweidu = ModifyOffset.jingweidu(location.getLongitude(), location.getLatitude());
			System.out.println(jingweidu);
			
			//�ѻ�ȡ���ľ�γ�ȱ���
			pref.edit().putString("location", jingweidu).commit();
		}
		
		//���û��ر�gpsʱ
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//���û���gpsʱ
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		//״̬�����仯ʱ
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//������ر�ʱ
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(listener);
	}
}
