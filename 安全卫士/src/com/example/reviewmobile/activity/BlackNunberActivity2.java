package com.example.reviewmobile.activity;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dialog.anim.Effectstype;
import com.dialog.anim.NiftyDialogBuilder;
import com.example.reviewmobile.R;
import com.example.reviewmobile.activity.ContactList.OnActivityMsg;
import com.example.reviewmobile.adapter.MyAdapter;
import com.example.reviewmobile.bean.BlackNumberBean;
import com.example.reviewmobile.db.dao.BlackNumberDao;
import com.example.reviewmobile.metallview.MetaballView;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.TwirlEffect;

public class BlackNunberActivity2 extends Activity {
	private static final String MSG = "3";
	private static final String MSGNUMBER = "1";
	private static final String PHONE = "2";
	private static final int SENDMESSAGE = 4;
	private static final int SENDCONTCAT = 0;
	private TextView tvContractPhone;
	private EditText addPhone;
	private Button addContact;
	private CallSafeAdapter callSafeAdapter;
	private CheckBox cbPhone;
	private CheckBox cbSms;
	private int countIndex = 10;
	private BlackNumberDao dao;
	private List<BlackNumberBean> findAllNumber;
	private JazzyListView lv;
	private PopupWindow popupWindow;
	private int startIndex = 0;
	private int total_page;
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
	private boolean once;
	private SharedPreferences pref;
	private MetaballView mvm;
	private PopupWindow popup;
	// 消息处理器
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			BlackNunberActivity2.this.lv
					.setAdapter(BlackNunberActivity2.this.callSafeAdapter);
			lv.setTransitionEffect(new TwirlEffect());
		}
	};

	// onCreate
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_black2);
		pref = getSharedPreferences("config", MODE_PRIVATE);
		once = pref.getBoolean("once", true);
		if (once) {
			add(); // 测试数据
			once = false;
			pref.edit().putBoolean("once", false).commit();
			Toast.makeText(BlackNunberActivity2.this, "测试数据= =", 1).show();
		}
		initUI();
	}

	public void add() {
		BlackNumberDao dao = new BlackNumberDao(BlackNunberActivity2.this);

		for (int i = 0; i < 20; i++) {
			Random random = new Random();
			Long number = 13012348321l + i;
			dao.add(number + "", String.valueOf(random.nextInt(3) + 1));
		}
	}

	// dismiss浮窗
	private void dismissPop() {
		if ((this.popupWindow != null) && (this.popupWindow.isShowing())) {
			this.popupWindow.dismiss();
			this.popupWindow = null;
		}
	}

	private void dismissprogress() {
		if (popup != null) {
			this.popup.dismiss();
			this.popup = null;
		}
	}

	// 初始化数据
	private void initData() {
		this.dao = new BlackNumberDao(this);
		this.total_page = this.dao.getTotal();
		System.out.println(total_page + "-------");
		this.findAllNumber = this.dao
				.findpar2(this.startIndex, this.countIndex);
		this.callSafeAdapter = new CallSafeAdapter(this.findAllNumber, this);
	}

	// 初始化ui
	private void initUI() {
		this.lv = ((JazzyListView) findViewById(R.id.lv_list));

		ViewUtils.inject(this);
		Utils.start(stv);
		initData();
		this.handler.sendEmptyMessage(SENDMESSAGE);
		// 滚动监听
		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			// 滚动状态变化时
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				if (scrollState == SCROLL_STATE_IDLE) {
					if (lv.getLastVisiblePosition() == findAllNumber.size() - 1
							&& total_page != lv.getLastVisiblePosition() + 1) {
						addprogress();
					} else {
						dismissprogress();
					}
				}
			}
			@Override
			// 滚动实时
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (lv.getLastVisiblePosition() != findAllNumber.size() - 1) {
					dismissprogress();
				}
				BlackNunberActivity2.this.handler.postDelayed(new Runnable() {
					public void run() {
						if (BlackNunberActivity2.this.lv
								.getLastVisiblePosition() == BlackNunberActivity2.this.findAllNumber
								.size() - 1) { // 当前item最后一条数据等于显示的最大数据时
							if (nextPage()) {
								dismissprogress();
								callSafeAdapter.notifyDataSetChanged();
							}
						}
						;
						// 延时8秒毫秒调用下一页
					}
				}, 2000L);
			}
		});

		// list——item项点击监听
		this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> paramAnonymousAdapterView,
					View paramAnonymousView, int paramAnonymousInt,
					long paramAnonymousLong) {
				// item点击监听
				BlackNumberBean localBlackNumberBean = (BlackNumberBean) BlackNunberActivity2.this.lv
						.getItemAtPosition(paramAnonymousInt);
				BlackNunberActivity2.this.updataItem(localBlackNumberBean);
			}
		});
	}

	// 分页加载
	private boolean nextPage() {
		int i = this.callSafeAdapter.getCount();
		if (i <= this.total_page) {
			if (this.findAllNumber == null) {
				this.findAllNumber = this.dao.findpar2(startIndex,
						this.countIndex);
				return true;
			} else {
				startIndex += countIndex;
				this.findAllNumber.addAll(this.dao.findpar2(startIndex,
						this.countIndex));
			}
			return true;
		}
		return false;
	}


	// 添加黑名单浮窗
	public void addprogress() {
		// 1.填充
		View view = getLayoutInflater().inflate(R.layout.waitout, null);
		MetaballView mv = (MetaballView) view.findViewById(R.id.metaball);
		mv.setPaintMode(0);
		popup = new PopupWindow(view, -2, -2);
		// 3.显示位置
		popup.showAtLocation(mv, Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL, 0,
				0);
	}

	// 添加黑名单dialog
	public void addItem(View v) {
		//-------------------------------------------
		// 初始化弹出框
		View localView = View.inflate(this, R.layout.add_dialog, null);
		this.addPhone = ((EditText) localView.findViewById(R.id.et_add_phone));

		ContactList.SetActivityMsg(new OnActivityMsg() {

			@Override
			public void contactPhone(String phone) {
				phone = phone.replaceAll("-", "").replaceAll(" ", "");
				addPhone.setText(phone);
			}
		});

		this.addPhone.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.shake1));
		this.cbPhone = ((CheckBox) localView
				.findViewById(R.id.cb_phone_checked));
		this.cbSms = ((CheckBox) localView.findViewById(R.id.cb_sms_checked));
		ShimmerTextView stv = (ShimmerTextView) localView
				.findViewById(R.id.tv_app_title);
		Utils.start(stv);
		addContact = (Button) localView.findViewById(R.id.bu_sys_contact);
		
		addContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BlackNunberActivity2.this,
						ContactList.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", "blackNumber");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 添加事件
		
		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		Effectstype effect = Effectstype.Newspager;

		dialogBuilder
				.withTitle(null)
				// .withTitle(null) no title
				// // .withTitle(null) no title
				.withTitleColor("#FFFFFF")
				// def
				.withDividerColor("#11000000")
				// def
				.withMessage(null)
				// .withMessage(null) no Msg
				.withMessageColor("#FFFFFFFF")
				// def | withMessageColor(int resid)
				.withDialogColor("#FFE74C3C")
				// def | withDialogColor(int resid) //def
				.withIcon(getResources().getDrawable(R.drawable.icon))
				.isCancelableOnTouchOutside(true) // 外部点击离开
				.withDuration(500) // 动画时长
				.withEffect(effect) // 动画
				.withButton1Text("添加") // def gone
				.withButton2Text("取消") // def gone
				.setCustomView(localView, BlackNunberActivity2.this) // .setCustomView(View
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 获取添加号码
						String number = BlackNunberActivity2.this.addPhone
								.getText().toString().trim();
						// 判断号码规则
						if (TextUtils.isEmpty(number)) {
							Toast.makeText(BlackNunberActivity2.this,
									"请输入电话号码", 0).show();
							return;
						}
						String mode;
						if ((BlackNunberActivity2.this.cbPhone.isChecked())
								&& (BlackNunberActivity2.this.cbSms.isChecked())) {
							mode = MSGNUMBER;
						} else if ((BlackNunberActivity2.this.cbPhone
								.isChecked())) {
							mode = PHONE;
						} else if ((BlackNunberActivity2.this.cbSms.isChecked())) {
							mode = MSG;
						} else {
							Toast.makeText(BlackNunberActivity2.this,
									"请选择拦截模式", 0).show();
							return;
						}

						// 数据添加
						BlackNumberBean localBlackNumberBean = new BlackNumberBean();
						localBlackNumberBean.setNumber(number);
						localBlackNumberBean.setMode(mode);
						BlackNunberActivity2.this.findAllNumber.add(0,
								localBlackNumberBean);
						BlackNunberActivity2.this.dao.add(number, mode);

						if (BlackNunberActivity2.this.callSafeAdapter == null) {
							BlackNunberActivity2.this.callSafeAdapter = new BlackNunberActivity2.CallSafeAdapter(
									BlackNunberActivity2.this.findAllNumber,
									BlackNunberActivity2.this);     
							BlackNunberActivity2.this.lv
									.setAdapter(BlackNunberActivity2.this.callSafeAdapter);
						} else {
							BlackNunberActivity2.this.callSafeAdapter
									.notifyDataSetChanged();
						}
						dialogBuilder.dismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
		
	}

	/**
	 * 修改拦截模式
	 * 
	 * @param paramBlackNumberBean
	 */
	public void updataItem(final BlackNumberBean paramBlackNumberBean) {

		View localView = View.inflate(BlackNunberActivity2.this,
				R.layout.updata_dialog, null);
		this.tvContractPhone = ((TextView) localView
				.findViewById(R.id.et_add_phone));

		this.tvContractPhone.setText(paramBlackNumberBean.getNumber());
//		this.tvContractPhone.setAnimation(AnimationUtils.loadAnimation(this,
//				R.anim.shake1));
		this.cbPhone = ((CheckBox) localView
				.findViewById(R.id.cb_phone_checked));
		this.cbSms = ((CheckBox) localView.findViewById(R.id.cb_sms_checked));
		ShimmerTextView stv = (ShimmerTextView) localView
				.findViewById(R.id.tv_app_title);
		Utils.start(stv);

		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		Effectstype effect = Effectstype.Fall;

		dialogBuilder
				.withTitle(null)
				// .withTitle(null) no title
				// // .withTitle(null) no title
				.withTitleColor("#FFFFFF")
				// def
				.withDividerColor("#11000000")
				// def
				.withMessage(null)
				// .withMessage(null) no Msg
				.withMessageColor("#FFFFFFFF")
				// def | withMessageColor(int resid)
				.withDialogColor("#FFE74C3C")
				// def | withDialogColor(int resid) //def
				.withIcon(getResources().getDrawable(R.drawable.icon))
				.isCancelableOnTouchOutside(true) // 外部点击离开
				.withDuration(500) // 动画时长
				.withEffect(effect) // 动画
				.withButton1Text("确定") // def gone
				.withButton2Text("取消") // def gone
				.setCustomView(localView, BlackNunberActivity2.this) // .setCustomView(View
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String mode;
						if ((BlackNunberActivity2.this.cbPhone.isChecked())
								&& (BlackNunberActivity2.this.cbSms.isChecked())) {
							mode = MSGNUMBER;
						} else if ((BlackNunberActivity2.this.cbPhone
								.isChecked())) {
							mode = PHONE;
						} else if ((BlackNunberActivity2.this.cbSms.isChecked())) {
							mode = MSG;
						} else {
							Toast.makeText(BlackNunberActivity2.this,
									"请选择拦截模式", 0).show();
							return;
						}
						// 数据修改
						paramBlackNumberBean.setMode(mode);
						BlackNunberActivity2.this.dao.modeChange(
								paramBlackNumberBean.getNumber(), mode);

						if (BlackNunberActivity2.this.callSafeAdapter == null) {
							BlackNunberActivity2.this.callSafeAdapter = new BlackNunberActivity2.CallSafeAdapter(
									BlackNunberActivity2.this.findAllNumber,
									BlackNunberActivity2.this);
							BlackNunberActivity2.this.lv
									.setAdapter(BlackNunberActivity2.this.callSafeAdapter);
						} else {
							BlackNunberActivity2.this.callSafeAdapter
									.notifyDataSetChanged();
						}
						dialogBuilder.dismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();

		// /-------------------------------------------------------
	}

	// 适配器
	class CallSafeAdapter extends MyAdapter<BlackNumberBean> {

		public CallSafeAdapter(List<BlackNumberBean> lists, Context mContext) {
			super(lists, mContext);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lists.size();
		}
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			Houdler localHoudler = null;
			String str;
			if (paramView == null) {
				paramView = View.inflate(BlackNunberActivity2.this,
						R.layout.item_black_view, null);
				localHoudler = new BlackNunberActivity2.Houdler();
				localHoudler.black_number = ((TextView) paramView
						.findViewById(R.id.tv_black_number));
				localHoudler.black_mode = ((TextView) paramView
						.findViewById(R.id.tv_black_mode));
				localHoudler.delete = ((ImageView) paramView
						.findViewById(R.id.iv_delete));
				paramView.setTag(localHoudler);

			} else {
				localHoudler = (BlackNunberActivity2.Houdler) paramView
						.getTag();
			}
			// 初始化当电话
			localHoudler.black_number.setText(((BlackNumberBean) this.lists
					.get(paramInt)).getNumber());

			// 初始化拦截模式
			str = ((BlackNumberBean) this.lists.get(paramInt)).getMode();
			if (str.equals("1"))
				// break;
				localHoudler.black_mode.setText("拦截电话+短信");
			if (str.equals("2"))
				localHoudler.black_mode.setText("拦截电话");
			else if (str.equals("3"))
				localHoudler.black_mode.setText("拦截短信");

			final BlackNumberBean localBlackNumberBean = (BlackNumberBean) this.lists
					.get(paramInt);
			localHoudler.delete.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					String str = localBlackNumberBean.getNumber();// 删除
					if (BlackNunberActivity2.this.dao.delete(str)) {
						BlackNunberActivity2.CallSafeAdapter.this.lists
								.remove(localBlackNumberBean);
						callSafeAdapter.notifyDataSetChanged();
						Toast.makeText(BlackNunberActivity2.this, "删除成功", 0)
								.show();
						return;
					}
					Toast.makeText(BlackNunberActivity2.this, "删除失败", 0).show();
				}
			});
			return paramView;
		}
	}

	// 优化id
	class Houdler {
		TextView black_mode;
		TextView black_number;
		ImageView delete;
	}

}
