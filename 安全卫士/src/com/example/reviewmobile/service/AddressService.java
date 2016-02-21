package com.example.reviewmobile.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reviewmobile.R;
import com.example.reviewmobile.db.dao.AddressDao;

public class AddressService extends Service {

	private WindowManager mWM;
	private OutCallReceiver receiver;
	private View view;
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private SharedPreferences pref;
	private WindowManager.LayoutParams params;

	private int winWidth;
	private int winHeight;
	private int startX;
	private int startY;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		pref = getSharedPreferences("config", MODE_PRIVATE);
		
		//来电监听
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		//去电监听
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);

	}

	/*
	 * 电话状态监听 (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */

	class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// TODO弹出浮窗

				String address = AddressDao.getAddress(incomingNumber.trim());
				showWindowToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				// TODO 关闭浮窗

				if (mWM != null && view != null) {
					mWM.removeView(view);
					view=null;
				}
				break;

			}

			super.onCallStateChanged(state, incomingNumber);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);// 关闭广播
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// 关闭电话状态监听
	}

	/**
	 * 去电广播
	 * 
	 * @author dell
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// 获取去电号码
			String number = getResultData().trim();
			System.out.println(number);
			String phone = AddressDao.getAddress(number);
			showWindowToast(phone);

		}

	}

	private void showWindowToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);// 获取窗口管理器
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//				|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;// 电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
		params.gravity = Gravity.LEFT + Gravity.TOP;// 将重心位置设置为左上方,
													// 也就是(0,0)从左上方开始,而不是默认的重心位置
		params.setTitle("Toast");
//
//		// 初始化坐标
		int lastX = pref.getInt("lastX", 0);
		int lastY = pref.getInt("lastY", 0);

		// 设置浮窗的位置, 基于左上方的偏移量
		params.x = lastX;
		params.y = lastY;
		
		view = View.inflate(AddressService.this, R.layout.window_toast, null);

		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = pref.getInt("address_style", 0);

		view.setBackgroundResource(bgs[style]);


		TextView mText = (TextView) view.findViewById(R.id.tv_win_address);
		mText.setText(text);
		mWM.addView(view, params);

		/**
		 * 为view添加触摸事件
		 */

		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					/**
					 * 初始化坐标
					 */

					startX = (int) arg1.getRawX();
					startY = (int) arg1.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) arg1.getRawX();
					int endY = (int) arg1.getRawY();

					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;

					// 更新浮窗位置
					params.x += dx;
					params.y += dy;
					
					
					//防止偏移
					if (params.x < 0) {
						params.x = 0;
					}

					if (params.y < 0) {
						params.y = 0;
					}

					// 防止坐标偏离屏幕
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}

					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}

					mWM.updateViewLayout(view, params);
					// 重新初始化起点坐标
					startX = (int) arg1.getRawX();
					startY = (int) arg1.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					/**
					 * 保存坐标
					 */
					// 记录坐标点
					Editor edit = pref.edit();
					edit.putInt("lastX", params.x);
					edit.putInt("lastY", params.y);
					edit.commit();
					break;

				}
				return true;
			}
		});
	}
}
