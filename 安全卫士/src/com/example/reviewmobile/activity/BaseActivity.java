package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		detector = new GestureDetector(this, new SimpleOnGestureListener() {
			   
			/*
			 * 手动重写方法， e1代表滑动起始点，e2代表滑动结束点 (non-Javadoc)
			 * 
			 * @see
			 * android.view.GestureDetector.SimpleOnGestureListener#onFling(
			 * android.view.MotionEvent, android.view.MotionEvent, float, float)
			 */
			
			/**
			 * 基于整个屏幕的 x点，getRawX表示整个屏幕的滑动距离 getX与getRawX的区别，getRawX表示整个屏幕的坐标点
			 * getX是获取屏幕某个区域的坐标点
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				/*
				 * 设置Y坐标范围
				 */
				
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 350) {
					return true;
				}
				
				/*
				 * 滑动速度
				 */
				if (Math.abs(velocityX) < 150) {
					return true;
				}
				
				/*
				 * 向右滑,上一页
				 * 
				 * 如果 起始坐标为100，结束坐标为300 可以判断出用户是在向右滑
				 * e2-e1大于120个像素点，规定只有滑动距离超过120个像素点才能进入执行里面的程序
				 */
				if ((e2.getRawX() - e1.getRawX()) > 100) {
					overridePendingTransition(R.anim.back_in, R.anim.back_out);
					showPrviousPakage();
					return true;
				}
				
				/*
				 * 向左滑，下一页 同理，e1坐标大于e2坐标时，可以判断用户向左在滑
				 */
				if ((e1.getRawX() - e2.getRawX()) > 100) {
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					showNextPakage();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/*
	 * 已实现滑动原理还不行 必须还要给定触摸监听 onTouchEvent，给GestureDetector传入一个事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	// 按钮监听，调用方法
	public void next(View v) {

		showNextPakage();
		// 两个界面切换的动画，一个进一个出
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	// 按钮监听，调用方法
	public void previous(View v) {
		showPrviousPakage();
		// 两个界面切换的动画，一个进一个出
		overridePendingTransition(R.anim.back_in, R.anim.back_out);
	}

	// 每个继承此类的都要实现这两个个方法，提高解耦性，
	public abstract void showNextPakage();

	public abstract void showPrviousPakage();

}
