package com.example.reviewmobile.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.example.reviewmobile.db.dao.BlackNumberDao;

public class BlackNumberService extends Service {

	private BlackNumberDao dao;
	private MyBroadcast receiver;
	private TelephonyManager manager;
	private static String incoming_number = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new BlackNumberDao(this);
		receiver = new MyBroadcast();
		// 注册广播来信息
		IntentFilter filter = new IntentFilter();// 过滤器
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.addAction("android.intent.action.PHONE_STATE");
		filter.setPriority(Integer.MAX_VALUE); // 优先权
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		// manager.listen(listener, PhoneStateListener)
	}

	class MyBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 接受短信
			if (intent.getAction().equals(
					"android.provider.Telephony.SMS_RECEIVED")) {
				Object[] objects = (Object[]) intent.getExtras().get("pdus");
				for (Object object : objects) {
					// 用短信pdu拿到一个字节数组
					SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);

					// 拿到来电短信和内容
					String phone_num = msg.getOriginatingAddress(); // 来电号码
					String body = msg.getMessageBody(); // 短信内容
					String mode = dao.findBlackNumber(phone_num);// 获取拦截模式

					if (mode.equals("1") || mode.equals("3")) {// 拦截电话短信
						abortBroadcast();
					}
				}

			} else {
				TelephonyManager tm =

				(TelephonyManager) context
						.getSystemService(Service.TELEPHONY_SERVICE);

				switch (tm.getCallState()) {

				case TelephonyManager.CALL_STATE_RINGING:

					incoming_number = intent.getStringExtra("incoming_number");

					String mode = dao.findBlackNumber(incoming_number);
					if (mode.equals("1") || mode.equals("2")) {// 拦截电话短信
						Toast.makeText(BlackNumberService.this,
								"来电话了" + incoming_number, 0).show();

						// 内容提供者
						Uri uri = Uri.parse("content://call_log/calls");
						getContentResolver().registerContentObserver(
								uri,
								true,
								new MyContentObserver(new Handler(),
										incoming_number));

						stopCall();
						abortBroadcast();// 截断广播
					}
					break;
				}
			}
		}
	}

	
	class MyContentObserver extends ContentObserver {
		String comingNumber;

		public MyContentObserver(Handler handler, String incoming_number) {
			super(handler);
			this.comingNumber = incoming_number;
			// TODO Auto-generated constructor stub
		}

		// 当数据改变的时候调用
		@Override
		public void onChange(boolean selfChange) {
			getContentResolver().unregisterContentObserver(this);
			deleteCallNumbet(comingNumber);
			super.onChange(selfChange);
		}

	}

	// 删除电话列表黑名单电话
	private void deleteCallNumbet(String comingNumber2) {
		Uri uri = Uri.parse("content://call_log/calls");
		getContentResolver().delete(uri,"number=?",new String[]{comingNumber2});
	}

	/**
	 * 挂机方法
	 */
	public void stopCall() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT); // 静音处理
		ITelephony iTelephony = getITelephony(BlackNumberService.this);
		try {

			if (iTelephony != null) {
				iTelephony.endCall();// 结束电话
			}
		} catch (RemoteException e) {

			e.printStackTrace();

		}

		// 再恢复正常铃声
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}

	/**
	 * 获取反射类实例
	 * 
	 * @param context
	 * @return
	 */
	private static ITelephony getITelephony(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context

		.getSystemService(Context.TELEPHONY_SERVICE);

		Class c = TelephonyManager.class;

		Method getITelephonyMethod = null;

		try {

			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null); // 获取声明的方法

			getITelephonyMethod.setAccessible(true);

		} catch (SecurityException e) {

			e.printStackTrace();

		} catch (NoSuchMethodException e) {

			e.printStackTrace();

		}
		ITelephony iTelephony = null;
		try {

			iTelephony = (ITelephony) getITelephonyMethod.invoke(

			mTelephonyManager, (Object[]) null); // 获取实例

			return iTelephony;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return iTelephony;
	}
}
