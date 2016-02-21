package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;
import com.example.reviewmobile.db.dao.AddressDao;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class Search_Activity extends Activity {
	private EditText search_number;
	private TextView showAddress;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ViewUtils.inject(this);
		Utils.start(stv);
		showAddress = (TextView) findViewById(R.id.show_address);
		search_number = (EditText) findViewById(R.id.et_search_number);
		if(TextUtils.isEmpty(search_number.getText().toString())){
			
			Animation loadAnimation = AnimationUtils.loadAnimation(Search_Activity.this, R.anim.shake);
			search_number.startAnimation(loadAnimation);
		}
		
		search_number.addTextChangedListener(new TextWatcher() {      
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(search_number.getText().toString())){
					
					String address = AddressDao.getAddress(s.toString());
					showAddress.setText(address);
				}
				if(search_number.getText().length()>11&&showAddress.getText().equals("Î´ÖªºÅÂë")){
					Animation loadAnimation = AnimationUtils.loadAnimation(Search_Activity.this, R.anim.shake);
					search_number.startAnimation(loadAnimation);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.onback_in, R.anim.onback_out);
	}
}
