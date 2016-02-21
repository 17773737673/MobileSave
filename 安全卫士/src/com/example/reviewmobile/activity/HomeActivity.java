package com.example.reviewmobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dialog.anim.Effectstype;
import com.dialog.anim.NiftyDialogBuilder;
import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;
import com.example.reviewmobile.view.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

public class HomeActivity extends Activity {

	private GridView grid;
	private SharedPreferences pref;
	private SlidingMenu sm;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	String[] names = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", " 联系人", "高级工具", "设置中心" };
	int[] pics = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe,
			R.drawable.home_apps, R.drawable.home_taskmanager,
			R.drawable.home_netmanager, R.drawable.home_trojan,
			R.drawable.home_sysoptimize, R.drawable.home_tools,
			R.drawable.home_settings };

	/**
	 * 弹出菜单栏
	 * 
	 * @param v
	 */
	public void openMenu(View v) {
		sm.toggle();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		   
		ViewUtils.inject(this);
		Utils.start(stv);

		sm = (SlidingMenu) findViewById(R.id.sm_menu);
		RelativeLayout rlMenu = (RelativeLayout) findViewById(R.id.menu);
		LinearLayout llMenu = (LinearLayout) rlMenu
				.findViewById(R.id.ll_ima_change);
		llMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(HomeActivity.this, "正在开发图片选择器,敬请期待", 1).show();
			}
		});
		pref = getSharedPreferences("config", MODE_PRIVATE);

		grid = (GridView) findViewById(R.id.gd_list);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < names.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", names[i]);
			map.put("pic", pics[i]);
			data.add(map);
		}

		grid.setAdapter(new SimpleAdapter(this, data, R.layout.home,
				new String[] { "name", "pic" }, new int[] { R.id.tv_name,
						R.id.iv_icon }));
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					showUpdataDialog();
					break;
				case 1:
					startActivity(new Intent(HomeActivity.this,
							BlackNunberActivity2.class));
					break;
				case 2:
					startActivity(new Intent(HomeActivity.this,
							SoftWareActivity.class));
					break;
				case 3:
					startActivity(new Intent(HomeActivity.this,
							TaskManagerActivity.class));
					break;
				case 4:
					startActivity(new Intent(HomeActivity.this,
							NetManagerActivity.class));
					break;
				case 5:
					startActivity(new Intent(HomeActivity.this,
							TrojanActivity.class));
					break;
				case 6:
					startActivity(new Intent(HomeActivity.this,
							ContactList2.class));
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AtoolsActivity.class));
					break;
				case 8:
					startActivity(new Intent(HomeActivity.this,
							SettingCenterActivity.class));
					break;
				}
			}
		});
	}

	// 判断当前pref文件夹中是否存在设置密码，有则弹出密码登录框，没有则弹密码设置设置框
	protected void showUpdataDialog() {
		// TODO Auto-generated method stub
		String password = pref.getString("password", "");

		if (!TextUtils.isEmpty(password)) {
			showLoadDialog();
		} else {
			showSetPassDialog();
		}
	}

	/**
	 * 设置密码弹出框
	 */
	private void showSetPassDialog() {
		View view = View.inflate(HomeActivity.this, R.layout.setpw, null);
		final EditText pass1 = (EditText) view.findViewById(R.id.et_pw1);
		final EditText pass2 = (EditText) view.findViewById(R.id.et_pws1);
		ShimmerTextView stv = (ShimmerTextView) view
				.findViewById(R.id.tv_app_title);
		Utils.start(stv);
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		Effectstype effect = Effectstype.SlideBottom;

		dialogBuilder.withTitle(null)
				// .withTitle(null) no title
				.withTitleColor("#FFFFFF")
				// def
				.withDividerColor("#11000000")
				// def
				.withMessage(null)
				// .withMessage(null) no Msg
				.withMessageColor("#FFFFFFFF")
				// def | withMessageColor(int resid)
				.withDialogColor("#FFE74C3C")
				// def | withDialogColor(int resid) //def
				.withIcon(getResources().getDrawable(R.drawable.icon))
				.isCancelableOnTouchOutside(true) // 外部点击离开
				.withDuration(700) // 动画时长
				.withEffect(effect) // 动画
				.withButton1Text("确定") // def gone
				.withButton2Text("取消") // def gone
				.setCustomView(view, HomeActivity.this) // .setCustomView(View
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String pass = pass1.getText().toString();
						String pass_affirm = pass2.getText().toString();
						if (!TextUtils.isEmpty(pass)
								&& !TextUtils.isEmpty(pass_affirm)) {
							if (pass.equals(pass_affirm)) {
								pref.edit().putString("password", pass)
										.commit();
								dialogBuilder.dismiss();
								startActivity(new Intent(HomeActivity.this,
										SetUp1.class));
							} else {
								Toast.makeText(HomeActivity.this, "两次密码不相同",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(HomeActivity.this, "密码不能为空",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}

	/**
	 * 登录弹出框
	 */
	private void showLoadDialog() {
		// TODO Auto-generated method stub

		View view = View.inflate(HomeActivity.this, R.layout.loadpw, null);
		final EditText pass = (EditText) view.findViewById(R.id.et_pw2);
		ShimmerTextView stv = (ShimmerTextView) view
				.findViewById(R.id.tv_app_title);
		Utils.start(stv);
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		Effectstype effect = Effectstype.Slidetop;

		dialogBuilder.withTitle(null)
				// .withTitle(null) no title
				.withTitleColor("#FFFFFF")
				// def
				.withDividerColor("#11000000")
				// def
				.withMessage(null)
				// .withMessage(null) no Msg
				.withMessageColor("#FFFFFFFF")
				// def | withMessageColor(int resid)
				.withDialogColor("#FFE74C3C")
				// def | withDialogColor(int resid) //def
				.withIcon(getResources().getDrawable(R.drawable.icon))
				.isCancelableOnTouchOutside(true) // 外部点击离开
				.withDuration(700) // 动画时长
				.withEffect(effect) // 动画
				.withButton1Text("确定") // def gone
				.withButton2Text("取消") // def gone
				.setCustomView(view, HomeActivity.this) // .setCustomView(View
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String password = pass.getText().toString();

						if (!TextUtils.isEmpty(password)) {
							if (password.equals(pref
									.getString("password", null))) {

								dialogBuilder.dismiss();
								startActivity(new Intent(HomeActivity.this,
										FindMobileActivity.class));
							} else {
								Toast.makeText(HomeActivity.this, "密码错误",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(HomeActivity.this, "输入框密码为空",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}
}
