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
	long[] mHits = new long[2];// ���鳤�ȱ�ʾҪ����Ĵ���

	/**
	 * ʵ��ͼ��ק��Ч��
	 * 
	 * 1.��ȡ��ǰ���֣��Ͳ��ֿ�ߣ���ʼ�����λ��
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

		// ��ȡ��ǰ��Ļ���
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		final int height = getWindowManager().getDefaultDisplay().getHeight();

		// ��ȡ��󱣴������
		int lastX = pref.getInt("lastX", 0);
		int lastY = pref.getInt("lastY", 0);

		if (lastY > height / 2) {// �ϱ���ʾ,�±�����
			tv_top.setVisibility(View.VISIBLE);
			tv_bottom.setVisibility(View.INVISIBLE);
		} else {
			tv_top.setVisibility(View.INVISIBLE);
			tv_bottom.setVisibility(View.VISIBLE);
		}
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_drag
				.getLayoutParams();// ��ȡ���ֶ���
		layoutParams.leftMargin = lastX;// ������߾�
		layoutParams.topMargin = lastY;// ����top�߾�

		iv_drag.setLayoutParams(layoutParams);// ��������λ��

		iv_drag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();// ������ʼ�����ʱ��
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// ��ͼƬ����
					iv_drag.layout(width / 2 - iv_drag.getWidth() / 2,
							iv_drag.getTop(), width / 2 + iv_drag.getWidth()
									/ 2, iv_drag.getBottom());
					//��¼��ǰλ��
					Editor ed = pref.edit();
					ed.putInt("lastX", iv_drag.getLeft());
					ed.putInt("lastY", iv_drag.getTop());
					ed.commit();

				}
			}
		});
		
		//��ק�¼�
		iv_drag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = (int) arg1.getRawX(); // ��ʼ������
					y = (int) arg1.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endx = (int) arg1.getRawX(); // �ƶ��������
					int endy = (int) arg1.getRawY();

					// �����ƶ�ƫ����
					int dx = endx - x; // �ó��ƶ�������ؾ���
					int dy = endy - y;

					// ����
					int l = iv_drag.getLeft() + dx;
					int r = iv_drag.getRight() + dx;

					int t = iv_drag.getTop() + dy;
					int b = iv_drag.getBottom() + dy;

					// �ж��Ƿ񳬳��߽�
					// ��xС��0���ߴ�����Ļ����߸�С��0������Ļ��ʱ
					if (l < 0 || r > width || t < 0 || b > height - 40) {
						break;
					}

					// ����Ļ�м�Ϊ�磬�ж�textview��ʾ������
					// ����ͼƬλ��,������ʾ����ʾ������
					if (t > height / 2) {// �ϱ���ʾ,�±�����
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}
					iv_drag.layout(l, t, r, b);

					x = (int) arg1.getRawX(); // ���³�ʼ������
					y = (int) arg1.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					// ����ʱ��ȡ��ǰ����ϱ���
					Editor ed = pref.edit();
					ed.putInt("lastX", iv_drag.getLeft());
					ed.putInt("lastY", iv_drag.getTop());
					ed.commit();
					break;

				default:
					break;
				}
				return false; // Ҫ���´����¼�
			}
		});
	}
}
