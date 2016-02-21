package com.example.reviewmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.reviewmobile.R;

public class SettingAddressStyle extends RelativeLayout {

	private TextView title;
	private TextView desc;
	private ImageView iv_state;

	public SettingAddressStyle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public SettingAddressStyle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingAddressStyle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init(){
		View view = View.inflate(getContext(), R.layout.setting_address_style, this);
		title = (TextView) findViewById(R.id.tv_title);
		desc = (TextView) findViewById(R.id.tv_desc);
		iv_state = (ImageView) findViewById(R.id.iv_state);
	}
	
	//暴露勾选框接口，可让勾选框在别的类也能被操作
	public void setTitle(String tv_title){
		title.setText(tv_title);
	}
	
	public void setDesc(String tv_desc){
		desc.setText(tv_desc);
	}
	public void setImageView(int resId){
		iv_state.setImageResource(resId);
	}
}
