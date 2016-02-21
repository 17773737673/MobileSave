package com.example.reviewmobile.activity;

import com.example.reviewmobile.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SetWindowLocationActivity extends Activity {
	private ImageView iv_drag;
	private TextView tv_top;
	private TextView tv_bottom;
	private SharedPreferences pref;

	private int x;
	private int y;
	long[] mHits = new long[2];// 数组长度表示要点击的次数

	/**
	 * 实现图标拽动效果
	 * 
	 * 1.获取当前布局，和布局宽高，初始化组件位置
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.window_location);
		iv_drag = (ImageView) findViewById(R.id.iv_drag);
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_bottom = (TextView) findViewById(R.id.tv_bottom);
		pref = getSharedPreferences("config", MODE_PRIVATE);

		// 获取当前屏幕宽高
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		final int height = getWindowManager().getDefaultDisplay().getHeight();

		// 获取最后保存的坐标
		int lastX = pref.getInt("lastX", 0);
		int lastY = pref.getInt("lastY", 0);

		if (lastY > height / 2) {// 上边显示,下边隐藏
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.INVISIBLE);
		} else {
			tv_top.setVisibility(View.INVISIBLE);
			tv_bottom.setVisibility(View.VISIBLE);
		}
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_drag
				.getLayoutParams();// 获取布局对象
		layoutParams.leftMargin = lastX;// 设置左边距
		layoutParams.topMargin = lastY;// 设置top边距

		iv_drag.setLayoutParams(layoutParams);// 重新设置位置

		iv_drag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 把图片居中
					iv_drag.layout(width / 2 - iv_drag.getWidth() / 2,
							iv_drag.getTop(), width / 2 + iv_drag.getWidth()
									/ 2, iv_drag.getBottom());
					//记录当前位置
					Editor ed = pref.edit();
					ed.putInt("lastX", iv_drag.getLeft());
					ed.putInt("lastY", iv_drag.getTop());
					ed.commit();

				}
			}
		});
		
		//拖拽事件
		iv_drag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = (int) arg1.getRawX(); // 初始化坐标
					y = (int) arg1.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endx = (int) arg1.getRawX(); // 移动后的坐标
					int endy = (int) arg1.getRawY();

					// 计算移动偏移量
					int dx = endx - x; // 得出移动后的像素距离
					int dy = endy - y;

					// 更新
					int l = iv_drag.getLeft() + dx;
					int r = iv_drag.getRight() + dx;

					int t = iv_drag.getTop() + dy;
					int b = iv_drag.getBottom() + dy;

					// 判断是否超出边界
					// 当x小于0或者大于屏幕宽或者高小于0大于屏幕宽时
					if (l < 0 || r > width || t < 0 || b > height - 40) {
						break;
					}

					// 已屏幕中间为界，判断textview显示与隐藏
					// 根据图片位置,决定提示框显示和隐藏
					if (t > height / 2) {// 上边显示,下边隐藏
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}
					iv_drag.layout(l, t, r, b);

					x = (int) arg1.getRawX(); // 重新初始化坐标
					y = (int) arg1.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					// 按下时获取当前左和上保存
					Editor ed = pref.edit();
					ed.putInt("lastX", iv_drag.getLeft());
					ed.putInt("lastY", iv_drag.getTop());
					ed.commit();
					break;

				default:
					break;
				}
				return false; // 要向下传递事件
			}
		});
	}
}
