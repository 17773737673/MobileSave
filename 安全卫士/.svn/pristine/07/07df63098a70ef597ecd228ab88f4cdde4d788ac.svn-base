package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;
import com.example.reviewmobile.receiver.AdminReceiver;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetUp4 extends BaseActivity {

	
	private CheckBox cb_protected;
	private SharedPreferences pref;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4);
		ViewUtils.inject(this);
		Utils.start(stv);
		cb_protected = (CheckBox) findViewById(R.id.cb_protected);
		pref = getSharedPreferences("config", MODE_PRIVATE);
		
		boolean flag = pref.getBoolean("protected", false);
		if(flag){
			cb_protected.setChecked(true);
			cb_protected.setText("防盗保护已开启");
			cb_protected.setTextColor(getResources().getColor(R.color.wirte));
		}else{
			cb_protected.setChecked(false);
			cb_protected.setText("防盗保护未开启");
			cb_protected.setTextColor(getResources().getColor(R.color.black));
		}
		
		//复选框监听
		cb_protected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
					//获取系统实例与设备管理器
					mDPM = Utils.getInstance(SetUp4.this);
					mDeviceAdminSample = Utils.getCname(SetUp4.this);
					
					//激活设备管理器
					Intent intents = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			        intents.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
			        intents.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			                "系统重要配置");
			       // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        startActivity(intents);
			        
			        cb_protected.setText("防盗保护已开启");
					cb_protected.setTextColor(getResources().getColor(R.color.black));
					pref.edit().putBoolean("protected", true).commit();
				}else{
					cb_protected.setText("防盗保护未开启");
					cb_protected.setTextColor(getResources().getColor(R.color.red));
					pref.edit().putBoolean("protected", false).commit();
				}
			}
		});
	}
	@Override
	public void showNextPakage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(SetUp4.this,FindMobileActivity.class));
		pref.edit().putBoolean("set", true).commit();
		finish();
	}

	@Override
	public void showPrviousPakage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(SetUp4.this,SetUp3.class));
		finish();
	}

}
