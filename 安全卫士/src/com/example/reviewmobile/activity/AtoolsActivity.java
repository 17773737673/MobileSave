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
		// ���ݶ���
		// 1.�����ȡ��ǰ�ֻ��������� ͨ�������ṩ��
		// 2.ͨ������xml�ļ���
		// io�����Ƶ��ڴ濨��Ŀ¼
		// ��ʾ���ݳɹ�
		case R.id.tv_sms_safe:
			// ��ʼ��������
			final ProgressDialog pb = new ProgressDialog(AtoolsActivity.this);
			pb.setTitle("��ʾ");
			pb.setMessage("���ڱ�����...");
			pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// ������
			pb.show();
			new Thread() {
				@Override
				public void run() {

					boolean result = SmsUtils.backUp(AtoolsActivity.this, new BackUpCallBackSms() {

						@Override
						public void onBackUpSms(int process) {
							// ��ǰ����
							pb.setProgress(process);  
						}
						@Override    
						public void befor(int count) {
							// TODO Auto-generated method stub
							pb.setMax(count);
						}  
					});

					// ���ݳɹ�
					if (result) {
						Utils.showToast(AtoolsActivity.this, "���ݳɹ�");

					} else {
						Utils.showToast(AtoolsActivity.this, "����ʧ��");
					}
					pb.dismiss();
				}
			}.start();
			break;
		}
	}
}
