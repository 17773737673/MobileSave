package com.example.reviewmobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reviewmobile.R;
import com.example.reviewmobile.engine.AppInfo;
import com.example.reviewmobile.engine.AppInfos;
import com.example.reviewmobile.metallview.MetaballView;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.TiltEffect;

public class SoftWareActivity extends Activity implements OnClickListener {
	private static final int SENDMSG = 0;
	private static final int DISSMISSPRORESS = 1;
	private static final int ADDPROGRESS = 2;
	
	// 注解把当前
	@ViewInject(R.id.lv_softs)
	private JazzyListView soft;
	@ViewInject(R.id.tv_rom)
	private TextView tv_rom;
	@ViewInject(R.id.tv_sdcard)
	private TextView tv_sd;
	@ViewInject(R.id.tv_app_count_title)
	private TextView tv_count;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	AppManagerAdapter adapter; // 适配器
	private List<AppInfo> appInfos;// 所有应用
	private List<AppInfo> useApp;// 个人应用
	private List<AppInfo> sysApp;// 系统应用
	private PopupWindow window;// 弹窗
	private View inflate; // 填充到窗口的ciew
	private AppInfo click; // 点击后item获取的对象
	private UnInstallPackageReceiver receiver;
	private PopupWindow popup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();// 初始化界面
		initData();
	}

	// 消息处理器
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case SENDMSG:
				soft.setAdapter(adapter);
				dismissprogress();				//修复由于加载数据耗时listview空白bug
				soft.setTransitionEffect(new TiltEffect());
				break;
			case ADDPROGRESS:
				addprogress();					//修复由于加载数据耗时listview空白bug
				break;
			}

		}
	};

	// 初始化数据
	private void initData() {
		new Thread() {
			public void run() {
				appInfos = AppInfos.getAppInfos(SoftWareActivity.this); // 获取手机上所有应用信息

				useApp = new ArrayList<AppInfo>(); // 用户app

				sysApp = new ArrayList<AppInfo>(); // 系统app

				for (AppInfo appinfo : appInfos) { // 把一个大集合分成分类成两个小集合
					if (appinfo.isUserApk()) {
						sysApp.add(appinfo);
					} else {
						useApp.add(appinfo);
					}
				}
				adapter = new AppManagerAdapter();
				handler.sendEmptyMessage(SENDMSG);
			}
		}.start();
	}

	public void addprogress() {
		// 1.填充
		View view = getLayoutInflater().inflate(R.layout.waitout, null);
		MetaballView mv = (MetaballView) view.findViewById(R.id.metaball);
		mv.setPaintMode(0);
		popup = new PopupWindow(view, -2, -2);
		// 3.显示位置
		popup.showAtLocation(mv, Gravity.CENTER, 0,
				0);
	}  
	private void dismissprogress() {
		if (popup != null) {
			this.popup.dismiss();
			this.popup = null;
		}
	}
	/**
	 * 卸载广播
	 * 
	 * @author dell
	 * 
	 */
	class UnInstallPackageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Uri data = intent.getData();
			Toast.makeText(SoftWareActivity.this,
					"二狗说:你的" + click.getApkName() + "卸载成功", 0).show();
		}

	}

	// 初始化ui
	private void initUI() {
		setContentView(R.layout.activity_software);
		ViewUtils.inject(SoftWareActivity.this); // xUtils视图工具类
		Utils.start(stv);
		handler.sendEmptyMessage(ADDPROGRESS);
		// 卸载
		receiver = new UnInstallPackageReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);

		long romFreeSpace = Environment.getDataDirectory().getFreeSpace();

		long sdFreeSpace = Environment.getExternalStorageDirectory()
				.getFreeSpace();

		tv_rom.setText("内存可用:"
				+ Formatter.formatFileSize(SoftWareActivity.this, romFreeSpace));
		tv_sd.setText("SD卡可用:"
				+ Formatter.formatFileSize(SoftWareActivity.this, sdFreeSpace));

		soft.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPop();
				if (useApp != null && sysApp != null) {
					if (firstVisibleItem > useApp.size()) {
						tv_count.setText("系统程序(" + sysApp.size() + ")");
					} else {
						tv_count.setText("用户程序(" + useApp.size() + ")");
					}
				}
			}
		});

		// ListView item点击事件
		soft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object atPosition = soft.getItemAtPosition(position);
				System.out.println("被电击了" + position);
				// 获取当前item点击的对象d
				if (position <= useApp.size()) {
					if (atPosition != null && atPosition instanceof AppInfo) {

						click = (AppInfo) atPosition;// 点击获取的对象

						inflate = getLayoutInflater().inflate(
								R.layout.popup_item, null);

						LinearLayout unInstall = (LinearLayout) inflate
								.findViewById(R.id.ll_uninstall);
						LinearLayout launcher = (LinearLayout) inflate
								.findViewById(R.id.ll_icluncher);
						LinearLayout fenxiang = (LinearLayout) inflate
								.findViewById(R.id.ll_fenxiang);
						LinearLayout detail = (LinearLayout) inflate
								.findViewById(R.id.ll_detail);

						unInstall.setOnClickListener(SoftWareActivity.this);
						launcher.setOnClickListener(SoftWareActivity.this);
						fenxiang.setOnClickListener(SoftWareActivity.this);
						detail.setOnClickListener(SoftWareActivity.this);

						dismissPop();
						// 当不为空已经在展示时在点击做的处理

						window = new PopupWindow(inflate,
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						// 弹窗宽高

						window.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						// 需要给window设置一个背景颜色才能显示动画

						int location[] = new int[2];
						// 窗体的xy坐标

						view.getLocationInWindow(location);
						// 获取view展示到窗体上面d位置

						window.showAtLocation(parent, Gravity.LEFT
								+ Gravity.TOP, 70, location[1]);
						// 显示到屏幕位置

						ScaleAnimation scaleAnimation = new ScaleAnimation(
								0.5f, 1.0f, 0.5f, 1.0f,
								Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f);
						scaleAnimation.setDuration(300);
						inflate.startAnimation(scaleAnimation);
						// 动画
					}
				} else {
					AppInfo click = (AppInfo) atPosition;// 点击获取的对象
					Intent detail_intent = new Intent();
					detail_intent
							.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
					detail_intent.setData(Uri.parse("package:"
							+ click.getApkPackageName()));
					startActivity(detail_intent);
					Toast.makeText(SoftWareActivity.this, "二狗说:系统组件,不建议对其进行操作",
							0).show();
				}
			}

		});

	}

	// 关闭弹窗
	private void dismissPop() {

		if (window != null && window.isShowing()) {
			window.dismiss();

			window = null;
		}
	}


	// 适配器
	// ---------------------------------------------------------------------

	class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return useApp.size() + sysApp.size() + 1;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/*********************
		 * 此处要把分开的集合返回到item，
		 */
		@Override
		public Object getItem(int position) {
			if (position == useApp.size()) {
				return null;
			}

			AppInfo appInfo;
			if (position < useApp.size()) {
				appInfo = useApp.get(position);// 拿到所有的系统app，索引为，当前索引剪掉用户索引
			} else {
				appInfo = sysApp.get(position - useApp.size() - 1); // 添加所有的用户app，减去不属于集合的条目
			}
			return appInfo;
		}

		/**
		 * 填充视图
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/**
			 * 添加区分条
			 */
			if (position == useApp.size()) {
				TextView tv_sysApp = new TextView(SoftWareActivity.this);
				tv_sysApp.setTextColor(Color.WHITE);
				tv_sysApp.setBackgroundResource(R.drawable.call_locate_white);
				tv_sysApp.setText("系统程序(" + sysApp.size() + ")");
				return tv_sysApp;
			}

			/**
			 * 分开添加
			 */
			AppInfo appInfo;
			if (position < useApp.size()) {
				appInfo = useApp.get(position);// 拿到所有的系统app，索引为，当前索引剪掉用户索引
			} else {
				appInfo = sysApp.get(position - useApp.size() - 1); // 添加所有的用户app，减去不属于集合的条目
			}
			/**
			 * 填充listview
			 */
			MyHoudler houdler; // 当view不为空，包含布局时  
			if (convertView != null && convertView instanceof LinearLayout) {

				houdler = (MyHoudler) convertView.getTag();
			} else {
				convertView = getLayoutInflater().inflate(
						R.layout.soft_list_item, null);
				houdler = new MyHoudler();
				houdler.appIcon = (ImageView) convertView
						.findViewById(R.id.iv_app_icon);
				houdler.appTitle = (TextView) convertView
						.findViewById(R.id.tv_app_title);
				houdler.appLocation = (TextView) convertView
						.findViewById(R.id.tv_app_location);
				houdler.appSize = (TextView) convertView
						.findViewById(R.id.tv_app_size);
				convertView.setTag(houdler);
			}

			/**
			 * 优化listview
			 */
			houdler.appIcon.setImageDrawable(appInfo.getIcon());
			houdler.appTitle.setText(appInfo.getApkName());
			houdler.appSize.setText(appInfo.getApkSize());

			/**
			 * 判断存储位置
			 */
			if (appInfo.isRom()) {
				houdler.appLocation.setText("手机内存");
			} else {
				houdler.appLocation.setText("SD内存");
			}
			return convertView;
		}

	}

	class MyHoudler {
		ImageView appIcon;
		TextView appTitle;
		TextView appLocation;
		TextView appSize;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		dismissprogress();
		dismissPop();
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_uninstall:
			Intent uninstall_localIntent = new Intent(
					"android.intent.action.DELETE", Uri.parse("package:"
							+ click.getApkPackageName()));
			startActivity(uninstall_localIntent);
			dismissPop();
			break;

		case R.id.ll_icluncher:
			// 获取所有运行的报名，通过点击获取的对象拿到当前对象报名
			System.out.println(click.getApkPackageName());
			Intent launchIntentForPackage = getPackageManager()
					.getLaunchIntentForPackage(click.getApkPackageName());
			Intent intent = this.getPackageManager().getLaunchIntentForPackage(
					click.getApkPackageName());
			this.startActivity(intent);
			dismissPop();

			break;
		case R.id.ll_fenxiang:

			Intent share_localIntent = new Intent("android.intent.action.SEND");
			share_localIntent.setType("text/plain");
			share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
			share_localIntent.putExtra("android.intent.extra.TEXT",
					"Hi！推荐您使用软件：" + click.getApkName() + "下载地址:"
							+ "https://play.google.com/store/apps/details?id="
							+ click.getApkPackageName());
			this.startActivity(Intent.createChooser(share_localIntent, "分享"));
			dismissPop();
			break;
		case R.id.ll_detail:
			Intent detail_intent = new Intent();
			detail_intent
					.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
			detail_intent.setData(Uri.parse("package:"
					+ click.getApkPackageName()));
			startActivity(detail_intent);
			dismissPop();
		}
	}
}
