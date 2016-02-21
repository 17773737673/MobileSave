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

		// ��ȡ��Sim�����к�
		SharedPreferences pref = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		boolean flag = pref.getBoolean("protected", false);
		System.out.println("������");
		if (flag) {
			
			String sim = pref.getString("sim", "");
			if (!TextUtils.isEmpty(sim)) {
				// ��ȡ��ǰsim���к�
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String simSerialNumber = tm.getSimSerialNumber() + "111";
				System.out.println("��ǰsim" + simSerialNumber + "----sim" + sim);

				if (!sim.equals(simSerialNumber)) {
					System.out.println("������Ϣ");
					// ��ȡ����ϵ�˺��뷢�Ͷ���
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
