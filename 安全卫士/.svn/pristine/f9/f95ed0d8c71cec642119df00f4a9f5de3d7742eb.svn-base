package com.example.reviewmobile.activity;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.sax.TextElementListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reviewmobile.R;
import com.example.reviewmobile.adapter.MyAdapter;
import com.example.reviewmobile.bean.BlackNumberBean;
import com.example.reviewmobile.db.dao.BlackNumberDao;

public class BlackNunberActivity extends BaseActivity {
	private static final int SENDMESSAGE = 4;
	private ListView lv;
	private List<BlackNumberBean> findAllNumber;
	private static final String MSG = "3";
	private static final String PHONE = "2";
	private static final String MSGNUMBER = "1";
	private LinearLayout ll;
	private LinearLayout rl;
	private TextView tv_page_number;
	// 当前页数
	private int mCurrentPage = 0;
	// 一页展示信息数
	private int pageNumber = 20;
	private int total_page;
	private BlackNumberDao dao;
	private CallSafeAdapter callSafeAdapter;

	/**
	 * 发送消息
	 */
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			ll.setVisibility(View.INVISIBLE);// 隐藏
			rl.setVisibility(View.VISIBLE);
			callSafeAdapter = new CallSafeAdapter(findAllNumber,
					BlackNunberActivity.this);
			lv.setAdapter(callSafeAdapter);
			tv_page_number.setText(mCurrentPage + "/" + total_page);
		}
	};

	private EditText et_page_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black);

		initUI(); // 初始化界面
		initData();// 初始化数据
	}

	/**
	 * 初始化数据添加adapter
	 */
	private void initData() {
		/**
		 * 数据多的话开线程加载
		 */
		new Thread() {

			public void run() {
				dao = new BlackNumberDao(BlackNunberActivity.this);
				// 动态变化页面数据
				total_page = dao.getTotal() / pageNumber;

				// 分页查询
				findAllNumber = dao.findpar(mCurrentPage, pageNumber);
				handler.sendEmptyMessage(SENDMESSAGE);
			}
		}.start();
	}

	/**
	 * 初始化ui
	 */
	private void initUI() {

		// ListView
		ll = (LinearLayout) findViewById(R.id.ll_bar);
		// 默认展示
		ll.setVisibility(View.VISIBLE);
		rl = (LinearLayout) findViewById(R.id.rt_button);
		rl.setVisibility(View.INVISIBLE);
		lv = (ListView) findViewById(R.id.lv_list);
		// lv.setEnabled(false);
		tv_page_number = (TextView) findViewById(R.id.tv_page_number);

	}

	/**
	 * listView适配器
	 */

	class CallSafeAdapter extends MyAdapter<BlackNumberBean> {

		public CallSafeAdapter(List<BlackNumberBean> lists, Context mContext) {
			super(lists, mContext);
		}

		/**
		 * 获取所有数据
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Houdler mHoudler = null;
			if (convertView == null) {
				convertView = View.inflate(BlackNunberActivity.this,
						R.layout.item_black_view, null);

				mHoudler = new Houdler();

				mHoudler.black_number = (TextView) convertView
						.findViewById(R.id.tv_black_number);

				mHoudler.black_mode = (TextView) convertView
						.findViewById(R.id.tv_black_mode);

				mHoudler.delete = (ImageView) convertView
						.findViewById(R.id.iv_delete);
				convertView.setTag(mHoudler);
			} else {
				mHoudler = (Houdler) convertView.getTag();
			}
			mHoudler.black_number.setText(lists.get(position).getNumber());

			String mode = lists.get(position).getMode();

			if (mode.equals(MSGNUMBER)) {
				mHoudler.black_mode.setText("拦截电话+短信");
			} else if (mode.equals(PHONE)) {
				mHoudler.black_mode.setText("拦截电话");
			} else if (mode.equals(MSG)) {
				mHoudler.black_mode.setText("拦截短信");
			}

			/**
			 * 获取当前item值
			 * 
			 */
			final BlackNumberBean blackNumberBean = lists.get(position);
			mHoudler.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 获取当前号码
					String number = blackNumberBean.getNumber();
					boolean delete = dao.delete(number);
					if (delete) {
						lists.remove(blackNumberBean);
						Toast.makeText(BlackNunberActivity.this, "删除成功",
								Toast.LENGTH_SHORT).show();
						callSafeAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(BlackNunberActivity.this, "删除失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			return convertView;
		}
	}

	class Houdler {
		TextView black_number;
		TextView black_mode;
		ImageView delete;
	}

	// 下一页
	@Override
	public void showNextPakage() {
		// TODO Auto-generated method stub
		if (mCurrentPage >= total_page) {
			Toast.makeText(BlackNunberActivity.this, "已经是最后页了",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mCurrentPage++;
		initData();
	}

	// 上一页
	@Override
	public void showPrviousPakage() {
		if (mCurrentPage <= 0) {
			Toast.makeText(BlackNunberActivity.this, "已经是第一页了",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mCurrentPage--;
		initData();
	}

	// 跳转
	public void change(View v) {
		et_page_number = (EditText) findViewById(R.id.et_changeText);
		String text = et_page_number.getText().toString();
		if (TextUtils.isEmpty(text)) {
			Toast.makeText(BlackNunberActivity.this, "不能为空", Toast.LENGTH_SHORT)
					.show();
		} else {
			int number = Integer.parseInt(text);
			if (number >= 0 && number <= total_page) {
				mCurrentPage = Integer.parseInt(text);
				initData();
			}
		}
	}
}
