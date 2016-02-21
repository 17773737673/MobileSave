package com.example.reviewmobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.reviewmobile.R;
import com.example.reviewmobile.service.AddressService;
import com.example.reviewmobile.service.BlackNumberService;
import com.example.reviewmobile.utils.ServiceStateUtils;
import com.example.reviewmobile.utils.Utils;
import com.example.reviewmobile.view.SettingAddressStyle;
import com.example.reviewmobile.view.SettingView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

public class SettingCenterActivity extends Activity {
	private static final String packageName = "com.example.reviewmobile.service.AddressService";
	private static final String packageName2 = "com.example.reviewmobile.service.BlackNumberService";
	private SharedPreferences pref;
	private SettingView sv_Setting;
	private SettingView sv_address;
	private SettingView sv_black_number;
	private SettingAddressStyle sas_style;
	private SettingAddressStyle sas_location;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_center_activity);
		ViewUtils.inject(this);
		Utils.start(stv);

		pref = getSharedPreferences("config", MODE_PRIVATE);

		address_show();// 归属地浮窗

		updata_setting();// 自动更新

		address_window_style();// 窗口风格颜色

		address_window_lacation();// 窗口位置

		black_number(); // 黑名单拦截
	}

	private void black_number() {
		sv_black_number = (SettingView) findViewById(R.id.sv_blacknumber);
		/**
		 * 当进程在勾选状态就为勾
		 */
		
		boolean black_number = pref.getBoolean("black_number", false);

		if (black_number) {
			sv_black_number.setCheck(true);
		} else {
			sv_black_number.setCheck(false);
		}
		if (ServiceStateUtils.isServiceRunning(SettingCenterActivity.this,
				packageName2)) {
			
			sv_black_number.setCheck(true);
			pref.edit().putBoolean("black_number", false).commit();
		}

		sv_black_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (sv_black_number.isCheck()) {
					sv_black_number.setCheck(false);
					stopService(new Intent(SettingCenterActivity.this,
							BlackNumberService.class));
				} else {
					sv_black_number.setCheck(true);
					startService(new Intent(SettingCenterActivity.this,
							BlackNumberService.class));
				}
			}
		});
	}

	private void address_window_lacation() {
		sas_location = (SettingAddressStyle) findViewById(R.id.sas_location);
		sas_location.setTitle("归属地提示框显示位置");
		sas_location.setDesc("归属地提示框显示位置");
		sas_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sas_location.setImageView(R.drawable.address__style);
				startActivity(new Intent(SettingCenterActivity.this,
						SetWindowLocationActivity.class));
			}
		});
	}

	private void address_window_style() {
		final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		sas_style = (SettingAddressStyle) findViewById(R.id.sas_style);
		int desc = pref.getInt("address_style", 0);
		sas_style.setDesc(items[desc]);
		sas_style.setTitle("归属地浮窗风格设置");
		sas_style.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sas_style.setImageView(R.drawable.address__style);
				showColorDialog();
			}

			private void showColorDialog() {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						SettingCenterActivity.this);
				dialog.setTitle("归属窗风格");
				int style = pref.getInt("address_style", 0);
				dialog.setSingleChoiceItems(items, style,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								sas_style.setDesc(items[arg1]);
								pref.edit().putInt("address_style", arg1)
										.commit();
								arg0.dismiss();
							}
						});
				dialog.setNegativeButton("取消", null);
				dialog.create().show();
			}
		});
	}

	private void address_show() {
		// TODO Auto-generated method stub
		sv_address = (SettingView) findViewById(R.id.sv_location);
		/**
		 * 当进程在勾选状态就为勾
		 */
		boolean address_show = pref.getBoolean("address_show", false);

		if (address_show) {
			sv_address.setCheck(true);
		} else {
			sv_address.setCheck(false);
		}
		if (ServiceStateUtils.isServiceRunning(SettingCenterActivity.this,
				packageName)) {
			sv_address.setCheck(true);
			pref.edit().putBoolean("address_show", false).commit();
		}

		sv_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (sv_address.isCheck()) {
					sv_address.setCheck(false);
					stopService(new Intent(SettingCenterActivity.this,
							AddressService.class));
				} else {
					sv_address.setCheck(true);
					startService(new Intent(SettingCenterActivity.this,
							AddressService.class));
				}
			}
		});
	}

	private void updata_setting() {
		sv_Setting = (SettingView) findViewById(R.id.sv_updata);
		boolean updataVersion = pref.getBoolean("updata_version", false);

		if (updataVersion) {
			sv_Setting.setCheck(true);
		} else {
			sv_Setting.setCheck(false);
		}
		sv_Setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (sv_Setting.isCheck()) {
					sv_Setting.setCheck(false);
					sv_Setting.setDesc("自动更新已关闭");
					pref.edit().putBoolean("updata_version", false).commit();
				} else {
					sv_Setting.setCheck(true);
					sv_Setting.setDesc("自动更新已开启");
					pref.edit().putBoolean("updata_version", true).commit();
				}
			}
		});
	}
}
