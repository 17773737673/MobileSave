package com.example.reviewmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

public class SetUp1 extends BaseActivity {
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup1);
		
		ViewUtils.inject(this);
	 	Utils.start(stv);
		LinearLayout setup = (LinearLayout) findViewById(R.id.ll_setup);
		setup.startAnimation(Utils.anim());
	}

	@Override
	public void showNextPakage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(SetUp1.this,SetUp2.class));
		finish();
	}

	@Override
	public void showPrviousPakage() {
		// TODO Auto-generated method stub
		
	}
}
