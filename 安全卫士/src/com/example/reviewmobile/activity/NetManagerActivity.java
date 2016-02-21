package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.os.Bundle;

public class NetManagerActivity extends Activity {
	
//	@ViewInject(R.id.tv_app_title)
//	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_net);
//		ViewUtils.inject(this);
//		Utils.start(stv);
	}
}
