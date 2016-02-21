package com.example.reviewmobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.reviewmobile.R;

public class SettingView extends RelativeLayout {

	private TextView title;
	private TextView desc;
	private CheckBox cb_state;
	private String namespace = "http://schemas.android.com/apk/res/com.example.reviewmobile";
	private String mtitle;
	private String desc_on;
	private String desc_off;
	private String color_off;
	private String color_on;

	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mtitle = attrs.getAttributeValue(namespace, "title");
		desc_on = attrs.getAttributeValue(namespace, "desc_on");
		desc_off = attrs.getAttributeValue(namespace, "desc_off");
		init();
	}

	public SettingView(Context context) {
		super(context,null);
		// TODO Auto-generated constructor stub
		init();
	}

	public void init(){
		View view = View.inflate(getContext(), R.layout.setting_view, this);
		title = (TextView) findViewById(R.id.tv_title);
		desc = (TextView) findViewById(R.id.tv_desc);
		cb_state = (CheckBox) findViewById(R.id.cb_state);
		title.setText(mtitle);
		desc.setText(desc_off);
	}
	
	//暴露勾选框接口，可让勾选框在别的类也能被操作
	public void setTitle(String tv_title){
		title.setText(tv_title);
	}
	
	public void setDesc(String tv_desc){
		desc.setText(tv_desc);
	}
	//修改当前字体颜色
	public void setDescColor(int color){
		desc.setTextColor(color);
	}
	//当前勾选框点击状态
	public boolean isCheck(){
		return cb_state.isChecked();
	}
	
	//设置勾选框
	public void setCheck(boolean check){
		cb_state.setChecked(check);
		if(check){
			setDesc(desc_on);
			setDescColor(getResources().getColor(R.color.wirte));
			
		}else{
			setDesc(desc_off);
			setDescColor(getResources().getColor(R.color.black));
		}
	}
}
