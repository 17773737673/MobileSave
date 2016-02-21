package com.example.reviewmobile.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	protected static final int DIALOG_UPDATA = 0;

	protected static final int URL_ERROR = 1;

	protected static final int IO_ERROR = 2;

	protected static final int JSON_ERROR = 3;

	protected static final int CODE_ENTER_HOME = 4;


	// ������URI

	// ����������
	private String mVersionName;
	private int mVersionCode;
	private String mDesc;
	private String mDownloadUrl;

	// �汾��ʾ�ı�
	private TextView tv_version;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DIALOG_UPDATA:
				showDialog();
				break;
			case URL_ERROR:
				Enter();
				break;
			case IO_ERROR:
				Enter();
				break;
			case JSON_ERROR:
				Enter();
				break;
			case CODE_ENTER_HOME:
				Enter();
				break;
			}
		}
	};

	private SharedPreferences pref;

	private RelativeLayout spalsh;

	private void showDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
		ab.setTitle("���°汾2.0");
		ab.setMessage(mDesc);
		ab.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ����ʹ��xutils�첽���ز���ʾ��װ������ʾ���ؽ�����

			}
		});

		ab.setNegativeButton("�´���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ����������

			}
		});
		ab.create().show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		pref = getSharedPreferences("config", MODE_PRIVATE);

		boolean updataVersion = pref.getBoolean("updata_version", false);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("��ǰ�汾:" + getVersionName());
		spalsh = (RelativeLayout) findViewById(R.id.rl_splash);

		// �������ݿ���data/data/files/
		copyDB("address.db");
		createShortWay();
		if (updataVersion) {
			CheckVersion();
		}else{
			handler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
			
		}
		// ��������Ч��
		spalsh.startAnimation(Utils.anim());  
	}    
	
	
	private void createShortWay() {
Intent intent = new Intent();
		
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//�������Ϊtrue��ʾ���Դ����ظ��Ŀ�ݷ�ʽ
		intent.putExtra("duplicate", false);
		
		/**
		 * 1 ��ʲô����
		 * 2 ���ʲô����
		 * 3�㳤��ʲô����
		 */
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�����ֻ���ʿ");
		//��ʲô����
		/**
		 * ����ط�����ʹ����ʾ��ͼ
		 * ����ʹ����ʽ��ͼ
		 */
		Intent shortcut_intent = new Intent();
		
		shortcut_intent.setAction("aaa.bbb.ccc");
		
		shortcut_intent.addCategory("android.intent.category.DEFAULT");
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut_intent);
		
		sendBroadcast(intent);
	}      
  
	private void copyDB(String dbName) {
		
		File destFile = new File(getFilesDir(), dbName);// Ҫ������Ŀ���ַ
		if (destFile.exists()) {
			return;
		}

		FileOutputStream out = null;
		InputStream in = null;

		try {
			in = getAssets().open(dbName);
			out = new FileOutputStream(destFile);

			int len = 0;
			byte[] buffer = new byte[1024];

			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 1.�����ȡ��ǰ����汾��Ϣ
	private String getVersionName() {
		// TODO Auto-generated method stub
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private int getVersionCode() {
		// TODO Auto-generated method stub
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	// 2.�ӷ������������ݣ��ж��Ƿ����

	private void CheckVersion() {
		// TODO Auto-generated method stub
		// ����һ�����߳����ں�ʱ����
		final String path = "http://172.21.232.3:8080/updata1.json";

		final long time = System.currentTimeMillis();
		new Thread() {

			private HttpURLConnection conn;

			public void run() {
				Message msg = Message.obtain();
				try {
					URL url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
					conn.connect();
					int responseCode = conn.getResponseCode();

					if (responseCode == 200) {
						// System.out.println("ok");
						// ���ӳɹ�ʱ����ȡ������������������
						InputStream inputStream = conn.getInputStream();
						String result = Utils.getStream(inputStream);
						// System.out.println(result);
						// �����õ���json������
						JSONObject js = new JSONObject(result);
						mVersionName = js.getString("versionName");
						mVersionCode = js.getInt("versionCode");
						mDesc = js.getString("dsc");
						mDownloadUrl = js.getString("downloadUri");

						// �ж��Ƿ����
						if (mVersionCode > getVersionCode()) {
							// �����Ի�������ֻ�������߳�ˢ��ui�������﷢��һ����Ϣ֪ͨ������ˢ��
							msg.what = DIALOG_UPDATA;
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// url����
					msg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = IO_ERROR;
					// �������Ӵ���
					e.printStackTrace();
				} catch (JSONException e) {
					// json�����쳣����ʽ����
					msg.what = JSON_ERROR;
					e.printStackTrace();
				} finally {
					long currentTime = System.currentTimeMillis();
					long newTime = currentTime - time;
					if (newTime < 1700) {
						try {
							Thread.sleep(1700 - newTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}

			}
		}.start();
	}

	public void Enter() {
//		ScaleAnimation scaleAnimation = new ScaleAnimation(
//				1.0f, 5.0f, 1.0f, 5.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f,
//				Animation.RELATIVE_TO_SELF, 0.5f);
//		scaleAnimation.setDuration(1000);
//		spalsh.startAnimation(scaleAnimation);

//		AlphaAnimation anim = new AlphaAnimation(1f, 0f);
//		anim.setDuration(1000);
//		spalsh.startAnimation(anim);
//		handler.postDelayed(new Runnable() {
			
//			@Override
//			public void run() {
				
				Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
//			}
//		}, 1000);
//				
	}

}
