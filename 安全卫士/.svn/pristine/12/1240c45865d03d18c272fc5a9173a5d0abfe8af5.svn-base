package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;
import com.example.reviewmobile.view.SettingView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SetUp2 extends BaseActivity {  
	private SettingView sv;
	private SharedPreferences pref;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2);
		ViewUtils.inject(this);
		Utils.start(stv);
		sv = (SettingView) findViewById(R.id.sv);
		pref = getSharedPreferences("config", MODE_PRIVATE);
		
		String sim_serial = pref.getString("sim", "");
		if(!TextUtils.isEmpty(sim_serial)){
			sv.setCheck(true);
		}else{
			sv.setCheck(false);
		}
		
	
		//设置整个框架可点击，
		sv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sv.isCheck()){
					sv.setCheck(false);
					pref.edit().remove("sim").commit();
				}else{
					sv.setCheck(true);
					//获取手机卡序列号
					TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					pref.edit().putString("sim", simSerialNumber).commit();
					
//					// 获取绑定联系人号码发送短信
//					SmsManager sms = SmsManager.getDefault();   
//					sms.sendTextMessage("17773737673", null, "sim card changed", null,
//							null);  
				}  
			}
		});
		
	}

	@Override
	public void showNextPakage() {
		String sim_serial = pref.getString("sim", "");
		if(TextUtils.isEmpty(sim_serial)){
			Toast.makeText(SetUp2.this, "请绑定Sim卡", Toast.LENGTH_SHORT).show();
			return;
		}
		//Toast.makeText(SetUp2.this, "绑定完成", 0).show();
		startActivity(new Intent(SetUp2.this,SetUp3.class));
		finish();
	}

	@Override
	public void showPrviousPakage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(SetUp2.this,SetUp1.class));
		finish();
	}
}
