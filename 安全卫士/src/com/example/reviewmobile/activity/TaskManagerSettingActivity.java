package com.example.reviewmobile.activity;


import com.example.reviewmobile.R;
import com.example.reviewmobile.service.KillProcessService;
import com.example.reviewmobile.utils.SharedPreferencesUtils;
import com.example.reviewmobile.utils.SystemInfoUtils;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * ============================================================
 * 
 * �� Ȩ �� �������Ա�������� ��Ȩ���� (c) 2015
 * 
 * �� �� : ��ΰ��
 * 
 * �� �� �� 1.0
 * 
 * �������� �� 2015-3-4 ����10:47:25
 * 
 * �� �� ��
 * 
 *       ��������������ý���
 * �޶���ʷ ��
 * 
 * ============================================================
 **/
public class TaskManagerSettingActivity extends Activity {
	
	private SharedPreferences sp;
	private CheckBox cb_status_kill_process;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		
	}

	private void initUI() {
		setContentView(R.layout.activity_task_manager_setting);
		CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);
		
		ViewUtils.inject(this);
		Utils.start(stv);
		//�����Ƿ�ѡ��
		cb_status.setChecked(SharedPreferencesUtils.getBoolean(TaskManagerSettingActivity.this, "is_show_system", false));
		
		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferencesUtils.saveBoolean(TaskManagerSettingActivity.this, "is_show_system", isChecked);
			}
		});
		
		//��ʱ�������
		
		cb_status_kill_process = (CheckBox) findViewById(R.id.cb_status_kill_process);
		
		final Intent intent = new Intent(this,KillProcessService.class);
		
		cb_status_kill_process.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					startService(intent);
				}else{
					stopService(intent);
				}
			}
		});
	
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(SystemInfoUtils.isServiceRunning(TaskManagerSettingActivity.this, "com.itheima.mobileguard.services.KillProcessService")){
			cb_status_kill_process.setChecked(true);
		}else{
			cb_status_kill_process.setChecked(false);
		}
	}

}
