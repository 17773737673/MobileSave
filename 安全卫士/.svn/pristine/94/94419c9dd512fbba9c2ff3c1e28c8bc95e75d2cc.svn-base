package com.example.reviewmobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// 获取绑定Sim卡序列号
		SharedPreferences pref = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		boolean flag = pref.getBoolean("protected", false);
		System.out.println("开机了");
		if (flag) {
			
			String sim = pref.getString("sim", "");
			if (!TextUtils.isEmpty(sim)) {
				// 获取当前sim序列号
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String simSerialNumber = tm.getSimSerialNumber() + "111";
				System.out.println("当前sim" + simSerialNumber + "----sim" + sim);

				if (!sim.equals(simSerialNumber)) {
					System.out.println("发送消息");
					// 获取绑定联系人号码发送短信
					SmsManager sms = SmsManager.getDefault();   
					String phone = pref.getString("phone_save", "");
					sms.sendTextMessage(phone, null, "sim card changed1", null,
							null);
				}else{
					
				}
			}
		}
	}

}
