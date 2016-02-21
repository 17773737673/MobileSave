package com.example.reviewmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

public class FindMobileActivity extends Activity {
	private SharedPreferences pref;
	private ImageView iv_proBox;
	private TextView tv_phone_number;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pref = getSharedPreferences("config", MODE_PRIVATE);
		boolean set = pref.getBoolean("set", false);

		if (set) {
			setContentView(R.layout.findmobile);
		ViewUtils.inject(this);
		Utils.start(stv);
			
			boolean flag = pref.getBoolean("protected", false);
			String phone = pref.getString("phone_save", "");
			
			
			iv_proBox = (ImageView) findViewById(R.id.iv_protect);
			tv_phone_number = (TextView) findViewById(R.id.phone_number);
			
			tv_phone_number.setText(phone);
			if (flag) {
				iv_proBox.setImageResource(R.drawable.lock);
			} else {
				iv_proBox.setImageResource(R.drawable.unlock);
			}
		} else {
			startActivity(new Intent(FindMobileActivity.this, SetUp1.class));
			finish();
		}
	}

	public void reset(View v) {
		startActivity(new Intent(FindMobileActivity.this, SetUp1.class));
		finish();
	}
}
