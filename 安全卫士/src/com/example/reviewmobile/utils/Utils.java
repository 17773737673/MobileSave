package com.example.reviewmobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.reviewmobile.receiver.AdminReceiver;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.Shimmer;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

public class Utils {
	public static String getStream(InputStream is) throws IOException {
		// json解析流并返回
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int len = 0;
		byte[] b = new byte[1024];

		while ((len = is.read(b)) != -1) {
			out.write(b, 0, len);
		}

		String result = out.toString();
		is.close();
		out.close();
		return result;
	}

	//渐变动画
	public static AlphaAnimation anim() {
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
		anim.setDuration(2000);
		return anim;
	}

	//系统管理器单例
	private static DevicePolicyManager mDPM = null;

	public static DevicePolicyManager getInstance(Context context) {

		if (mDPM == null) {
			mDPM = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);
		}
		return mDPM;
	}

	//谈吐司方法
	public static void showToast(final Activity context,final String msg){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(context, msg, 1).show();
		}else{
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, msg, 1).show();
				}
			});
		}
	}
	
	//系统管理器
	private static ComponentName mDeviceAdminSample = null;

	public static ComponentName getCname(Context context) {

		if (mDeviceAdminSample == null) {
			mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
		}
		return mDeviceAdminSample;
	}
	
	//标题闪烁
	public static void start(ShimmerTextView v){
		  Shimmer s = new Shimmer();
	       s.start(v);
	}
}
