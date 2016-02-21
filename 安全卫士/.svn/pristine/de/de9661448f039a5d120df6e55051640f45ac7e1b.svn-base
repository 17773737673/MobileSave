package com.example.reviewmobile.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.example.reviewmobile.R;
import com.example.reviewmobile.activity.SetUp4;
import com.example.reviewmobile.service.LocationService;
import com.example.reviewmobile.utils.Utils;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences pref;
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	public void onReceive(Context context, Intent intent) {
		pref = context.getSharedPreferences("config", context.MODE_PRIVATE);
		String phone = pref.getString("phone_save", "");
//		
//		//获取系统实例与设备管理器
		mDPM = mDPM = Utils.getInstance(context);
		mDeviceAdminSample = Utils.getCname(context);
//		
//		//激活设备管理器
//		Intent intents = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        intents.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
//        intents.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                "哈哈哈，我们有了超级管理器");
//        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intents);
//		
		// 获取短信
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			// 用短信pdu拿到一个字节数组
			SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);

			// 拿到来电短信和内容
			String phone_num = msg.getOriginatingAddress(); // 来电号码
			String body = msg.getMessageBody(); // 短信内容

			if ("#*alarm*#".equals(body)) {
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);// 播放音量
				player.setLooping(true);// 循环播放
				player.start();// 开启播放
				// 播放报警音乐
				abortBroadcast();// 拦截短信
			} else if ("#*location*#".equals(body)) {
				// 当信息内容为location时，启动服务获取经纬度、
				context.startService(new Intent(context, LocationService.class));
				// 获取安全号码，并发送信息回去
				String location = pref.getString("location", "");
				if (!TextUtils.isEmpty(location)) {
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(phone, null, location, null, null);
				}
				abortBroadcast();// 拦截短信
			}else if("#*wipedata*#".equals(body)){
				if(mDPM.isAdminActive(mDeviceAdminSample)){
		    		mDPM.wipeData(0);//currently 0 and WIPE_EXTERNAL_STORAGE 清楚数据
		    	}
				abortBroadcast();
			}else if("#*lockscreen*#".equals(body)){
				if(mDPM.isAdminActive(mDeviceAdminSample)){
					mDPM.lockNow();
					mDPM.resetPassword("110", 0);
				}
				abortBroadcast();
			}	   
		}
	}

}
