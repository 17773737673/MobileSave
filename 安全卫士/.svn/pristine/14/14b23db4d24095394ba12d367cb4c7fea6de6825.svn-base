package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.SmsUtils;
import com.example.reviewmobile.utils.SmsUtils.BackUpCallBackSms;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AtoolsActivity extends Activity {
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		ViewUtils.inject(this);
		Utils.start(stv);
	}

	public void atool(View v) {
		switch (v.getId()) {    
		case R.id.tv_searchs:
			startActivity(new Intent(AtoolsActivity.this, Search_Activity.class));
			overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
			break;
		// 备份短信
		// 1.点击获取当前手机短信数据 通过内容提供者
		// 2.通过生成xml文件，
		// io流复制到内存卡根目录
		// 提示备份成功
		case R.id.tv_sms_safe:
			// 初始化进度条
			final ProgressDialog pb = new ProgressDialog(AtoolsActivity.this);
			pb.setTitle("提示");
			pb.setMessage("正在备份中...");
			pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 横向风格
			pb.show();
			new Thread() {
				@Override
				public void run() {

					boolean result = SmsUtils.backUp(AtoolsActivity.this, new BackUpCallBackSms() {

						@Override
						public void onBackUpSms(int process) {
							// 当前进度
							pb.setProgress(process);  
						}
						@Override    
						public void befor(int count) {
							// TODO Auto-generated method stub
							pb.setMax(count);
						}  
					});

					// 备份成功
					if (result) {
						Utils.showToast(AtoolsActivity.this, "备份成功");

					} else {
						Utils.showToast(AtoolsActivity.this, "备份失败");
					}
					pb.dismiss();
				}
			}.start();
			break;
		}
	}
}
