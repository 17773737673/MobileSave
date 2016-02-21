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
	
	// ע��ѵ�ǰ
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
	AppManagerAdapter adapter; // ������
	private List<AppInfo> appInfos;// ����Ӧ��
	private List<AppInfo> useApp;// ����Ӧ��
	private List<AppInfo> sysApp;// ϵͳӦ��
	private PopupWindow window;// ����
	private View inflate; // ��䵽���ڵ�ciew
	private AppInfo click; // �����item��ȡ�Ķ���
	private UnInstallPackageReceiver receiver;
	private PopupWindow popup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();// ��ʼ������
		initData();
	}

	// ��Ϣ������
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case SENDMSG:
				soft.setAdapter(adapter);
				dismissprogress();				//�޸����ڼ������ݺ�ʱlistview�հ�bug
				soft.setTransitionEffect(new TiltEffect());
				break;
			case ADDPROGRESS:
				addprogress();					//�޸����ڼ������ݺ�ʱlistview�հ�bug
				break;
			}

		}
	};

	// ��ʼ������
	private void initData() {
		new Thread() {
			public void run() {
				appInfos = AppInfos.getAppInfos(SoftWareActivity.this); // ��ȡ�ֻ�������Ӧ����Ϣ

				useApp = new ArrayList<AppInfo>(); // �û�app

				sysApp = new ArrayList<AppInfo>(); // ϵͳapp

				for (AppInfo appinfo : appInfos) { // ��һ���󼯺Ϸֳɷ��������С����
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
		// 1.���
		View view = getLayoutInflater().inflate(R.layout.waitout, null);
		MetaballView mv = (MetaballView) view.findViewById(R.id.metaball);
		mv.setPaintMode(0);
		popup = new PopupWindow(view, -2, -2);
		// 3.��ʾλ��
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
	 * ж�ع㲥
	 * 
	 * @author dell
	 * 
	 */
	class UnInstallPackageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Uri data = intent.getData();
			Toast.makeText(SoftWareActivity.this,
					"����˵:���" + click.getApkName() + "ж�سɹ�", 0).show();
		}

	}

	// ��ʼ��ui
	private void initUI() {
		setContentView(R.layout.activity_software);
		ViewUtils.inject(SoftWareActivity.this); // xUtils��ͼ������
		Utils.start(stv);
		handler.sendEmptyMessage(ADDPROGRESS);
		// ж��
		receiver = new UnInstallPackageReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);

		long romFreeSpace = Environment.getDataDirectory().getFreeSpace();

		long sdFreeSpace = Environment.getExternalStorageDirectory()
				.getFreeSpace();

		tv_rom.setText("�ڴ����:"
				+ Formatter.formatFileSize(SoftWareActivity.this, romFreeSpace));
		tv_sd.setText("SD������:"
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
						tv_count.setText("ϵͳ����(" + sysApp.size() + ")");
					} else {
						tv_count.setText("�û�����(" + useApp.size() + ")");
					}
				}
			}
		});

		// ListView item����¼�
		soft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object atPosition = soft.getItemAtPosition(position);
				System.out.println("�������" + position);
				// ��ȡ��ǰitem����Ķ���d
				if (position <= useApp.size()) {
					if (atPosition != null && atPosition instanceof AppInfo) {

						click = (AppInfo) atPosition;// �����ȡ�Ķ���

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
						// ����Ϊ���Ѿ���չʾʱ�ڵ�����Ĵ���

						window = new PopupWindow(inflate,
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						// �������

						window.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						// ��Ҫ��window����һ��������ɫ������ʾ����

						int location[] = new int[2];
						// �����xy����

						view.getLocationInWindow(location);
						// ��ȡviewչʾ����������dλ��

						window.showAtLocation(parent, Gravity.LEFT
								+ Gravity.TOP, 70, location[1]);
						// ��ʾ����Ļλ��

						ScaleAnimation scaleAnimation = new ScaleAnimation(
								0.5f, 1.0f, 0.5f, 1.0f,
								Animation.RELATIVE_TO_SELF, 0.5f,
								Animation.RELATIVE_TO_SELF, 0.5f);
						scaleAnimation.setDuration(300);
						inflate.startAnimation(scaleAnimation);
						// ����
					}
				} else {
					AppInfo click = (AppInfo) atPosition;// �����ȡ�Ķ���
					Intent detail_intent = new Intent();
					detail_intent
							.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
					detail_intent.setData(Uri.parse("package:"
							+ click.getApkPackageName()));
					startActivity(detail_intent);
					Toast.makeText(SoftWareActivity.this, "����˵:ϵͳ���,�����������в���",
							0).show();
				}
			}

		});

	}

	// �رյ���
	private void dismissPop() {

		if (window != null && window.isShowing()) {
			window.dismiss();

			window = null;
		}
	}


	// ������
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
		 * �˴�Ҫ�ѷֿ��ļ��Ϸ��ص�item��
		 */
		@Override
		public Object getItem(int position) {
			if (position == useApp.size()) {
				return null;
			}

			AppInfo appInfo;
			if (position < useApp.size()) {
				appInfo = useApp.get(position);// �õ����е�ϵͳapp������Ϊ����ǰ���������û�����
			} else {
				appInfo = sysApp.get(position - useApp.size() - 1); // ������е��û�app����ȥ�����ڼ��ϵ���Ŀ
			}
			return appInfo;
		}

		/**
		 * �����ͼ
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/**
			 * ���������
			 */
			if (position == useApp.size()) {
				TextView tv_sysApp = new TextView(SoftWareActivity.this);
				tv_sysApp.setTextColor(Color.WHITE);
				tv_sysApp.setBackgroundResource(R.drawable.call_locate_white);
				tv_sysApp.setText("ϵͳ����(" + sysApp.size() + ")");
				return tv_sysApp;
			}

			/**
			 * �ֿ����
			 */
			AppInfo appInfo;
			if (position < useApp.size()) {
				appInfo = useApp.get(position);// �õ����е�ϵͳapp������Ϊ����ǰ���������û�����
			} else {
				appInfo = sysApp.get(position - useApp.size() - 1); // ������е��û�app����ȥ�����ڼ��ϵ���Ŀ
			}
			/**
			 * ���listview
			 */
			MyHoudler houdler; // ��view��Ϊ�գ���������ʱ  
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
			 * �Ż�listview
			 */
			houdler.appIcon.setImageDrawable(appInfo.getIcon());
			houdler.appTitle.setText(appInfo.getApkName());
			houdler.appSize.setText(appInfo.getApkSize());

			/**
			 * �жϴ洢λ��
			 */
			if (appInfo.isRom()) {
				houdler.appLocation.setText("�ֻ��ڴ�");
			} else {
				houdler.appLocation.setText("SD�ڴ�");
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
			// ��ȡ�������еı�����ͨ�������ȡ�Ķ����õ���ǰ������
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
			share_localIntent.putExtra("android.intent.extra.SUBJECT", "f����");
			share_localIntent.putExtra("android.intent.extra.TEXT",
					"Hi���Ƽ���ʹ�������" + click.getApkName() + "���ص�ַ:"
							+ "https://play.google.com/store/apps/details?id="
							+ click.getApkPackageName());
			this.startActivity(Intent.createChooser(share_localIntent, "����"));
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
