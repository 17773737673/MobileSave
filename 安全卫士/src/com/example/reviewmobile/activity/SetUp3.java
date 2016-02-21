package com.example.reviewmobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reviewmobile.R;
import com.example.reviewmobile.activity.ContactList.OnActivityMsg;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

public class SetUp3 extends BaseActivity{
	private EditText et_phone;
	private SharedPreferences pref;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup3);
		ViewUtils.inject(this);
		Utils.start(stv);
		et_phone = (EditText) findViewById(R.id.et_phone);
		ContactList.SetActivityMsg(new OnActivityMsg() {
			
			@Override
			public void contactPhone(String phone) {
				// TODO Auto-generated method stub
				phone = phone.replaceAll("-", " ").replaceAll(" ", "");
				et_phone.setText(phone);
			}
		});
		pref = getSharedPreferences("config", MODE_PRIVATE);
		String phone = pref.getString("phone_save", "");
		if(!TextUtils.isEmpty(phone)){
			et_phone.setText(phone);
		}
	}
	
	public void contact(View v){
		Intent intent = new Intent(SetUp3.this,ContactList.class);
		Bundle bundle = new Bundle();
		bundle.putString("name", "setup3");
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	
	@Override
	public void showNextPakage() {
		// TODO Auto-generated method stub
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(SetUp3.this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		pref.edit().putString("phone_save", phone).commit();
		//Toast.makeText(SetUp2.this, "绑定完成", 0).show();
		startActivity(new Intent(SetUp3.this,SetUp4.class));
		finish();
		
	}

	@Override
	public void showPrviousPakage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(SetUp3.this,SetUp2.class));
		finish();
	}
}
