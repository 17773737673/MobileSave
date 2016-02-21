package com.example.reviewmobile.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.reviewmobile.R;
import com.example.reviewmobile.bean.TaskInfo;
import com.example.reviewmobile.utils.SharedPreferencesUtils;
import com.example.reviewmobile.utils.SystemInfoUtils;
import com.example.reviewmobile.utils.TaskProcesses;
import com.example.reviewmobile.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sdf.jazzylistviewdemo.romainpiel.shimmer.ShimmerTextView;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.CurlEffect;
import com.twotoasters.jazzylistview.effects.FadeEffect;
import com.twotoasters.jazzylistview.effects.ReverseFlyEffect;
import com.twotoasters.jazzylistview.effects.SlideInEffect;
import com.twotoasters.jazzylistview.effects.TiltEffect;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {
	@ViewInject(R.id.tv_app_title)
	ShimmerTextView stv;
//	@ViewInject(R.id.lv_task_list)
//	JazzyListView listTask;
	
	private ActivityManager am;
//	private List<TaskInfo> taskInfos;
//	private List<TaskInfo> useApp;
//	private List<TaskInfo> sysApp;
//	private TaskProcessesAdapter adapter;


	
	
	
	@ViewInject(R.id.tv_task_count)
	private TextView tv_task_process_count;
	@ViewInject(R.id.tv_task_memory)
	private TextView tv_task_memory;
	
	@ViewInject(R.id.lv_task_list)
	JazzyListView listTask;
	
	private long totalMem;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemAppInfos;
	private TaskManagerAdapter adapter;
	private int processCount;
	private long availMem;
//	private SharedPreferences sp;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

//		sp = getSharedPreferences("config", 0);
		
		initUI();
		initData();   

	}
	
	@Override
	protected void onResume() {  
		// TODO Auto-generated method stub
		super.onResume();
		        
		if(adapter != null){  
			adapter.notifyDataSetChanged();
		}
	}

	private class TaskManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			/**
			 * �жϵ�ǰ�û��Ƿ���Ҫչʾϵͳ����
			 * �����Ҫ��ȫ��չʾ
			 * �������Ҫ��չʾ�û�����
			 */
			boolean result = SharedPreferencesUtils.getBoolean(TaskManagerActivity.this, "is_show_system", false);
			if(result){
				return userTaskInfos.size() + 1 + systemAppInfos.size() + 1;
			}else{
				return userTaskInfos.size() + 1;
			}
			
			
		}

		@Override
		public Object getItem(int position) {

			if (position == 0) {
				return null;
			} else if (position == userTaskInfos.size() + 1) {
				return null;
			}

			TaskInfo taskInfo;

			if (position < (userTaskInfos.size() + 1)) {
				// �û�����
				taskInfo = userTaskInfos.get(position - 1);// ����һ��textview�ı�ǩ ��
															// λ����Ҫ-1
			} else {
				// ϵͳ����
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo = systemAppInfos.get(location);
			}
			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				// ��0��λ����ʾ��Ӧ���� �û�����ĸ����ı�ǩ��
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("�û�����" + userTaskInfos.size() + "��");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("ϵͳ����" + systemAppInfos.size() + "��");
				return tv;
			}
			ViewHolder holder;
			View view;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;

				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(TaskManagerActivity.this,
						R.layout.item_task_processes, null);

				holder = new ViewHolder();

				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_processes_icon);

				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_processes_name);

				holder.tv_app_memory_size = (TextView) view
						.findViewById(R.id.tv_processes_memory);

				holder.tv_app_status = (CheckBox) view
						.findViewById(R.id.cb_clean_processes);

				view.setTag(holder);
			}

			TaskInfo taskInfo;

			if (position < (userTaskInfos.size() + 1)) {
				// �û�����
				taskInfo = userTaskInfos.get(position - 1);// ����һ��textview�ı�ǩ ��
															// λ����Ҫ-1
			} else {
				// ϵͳ����
				int location = position - 1 - userTaskInfos.size() - 1;
				taskInfo = systemAppInfos.get(location);
			}
			// ���������ͼƬ�ؼ��Ĵ�С
			// holder.iv_app_icon.setBackgroundDrawable(d)
			// ����ͼƬ����Ĵ�С
			holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());

			holder.tv_app_name.setText(taskInfo.getAppName());

			holder.tv_app_memory_size.setText("�ڴ�ռ��:"+Formatter.formatFileSize(TaskManagerActivity.this, taskInfo.getMemorySize()));

			if (taskInfo.isChecked()) {
				holder.tv_app_status.setChecked(true);
			} else {
				holder.tv_app_status.setChecked(false);
			}
            //�жϵ�ǰչʾ��item�Ƿ����Լ��ĳ�������ǡ��Ͱѳ��������
			if(taskInfo.getPackageName().equals(getPackageName())){
				//����
				holder.tv_app_status.setVisibility(View.INVISIBLE);
			}else{
				//��ʾ
				holder.tv_app_status.setVisibility(View.VISIBLE);
			}
			
			return view;
		}

	}

	static class ViewHolder {
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_app_memory_size;
		CheckBox tv_app_status;
	}

	private void initData() {
		new Thread() {

			public void run() {
				taskInfos = TaskProcesses
						.getTaskInfo(TaskManagerActivity.this);

				userTaskInfos = new ArrayList<TaskInfo>();

				systemAppInfos = new ArrayList<TaskInfo>();

				for (TaskInfo taskInfo : taskInfos) {

					if (taskInfo.isUserApp()) {
						userTaskInfos.add(taskInfo);
					} else {
						systemAppInfos.add(taskInfo);
					}

				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						adapter = new TaskManagerAdapter();
						listTask.setAdapter(adapter);
						listTask.setTransitionEffect(new CurlEffect());
					}
				});

			};
		}.start();

	}

	/**
	 * ����
	 * 
	 * ActivityManager �������(���������)
	 * 
	 * packageManager ��������
	 */

	private void initUI() {
		setContentView(R.layout.activity_task);
		ViewUtils.inject(this);

		processCount = SystemInfoUtils.getProcessCount(this);

		tv_task_process_count.setText("����:" + processCount + "��");

		availMem = SystemInfoUtils.getAvailMem(this);

		totalMem = SystemInfoUtils.getTotalMem(this);

		tv_task_memory.setText("ʣ��/���ڴ�:"
				+ Formatter.formatFileSize(TaskManagerActivity.this, availMem)
				+ "/"
				+ Formatter.formatFileSize(TaskManagerActivity.this, totalMem));

		listTask.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �õ���ǰ���listview�Ķ���
				Object object = listTask.getItemAtPosition(position);

				
				if (object != null && object instanceof TaskInfo) {

					TaskInfo taskInfo = (TaskInfo) object;

					ViewHolder holder = (ViewHolder) view.getTag();
					
					if(taskInfo.getPackageName().equals(getPackageName())){
						return;
					}
					
					// �жϵ�ǰ��item�Ƿ񱻹�ѡ��
					/**
					 * �������ѡ���ˡ���ô�͸ĳ�û�й�ѡ�� ���û�й�ѡ���͸ĳ��Ѿ���ѡ
					 */
					if (taskInfo.isChecked()) {
						taskInfo.setChecked(false);
						holder.tv_app_status.setChecked(false);
					} else {
						taskInfo.setChecked(true);
						holder.tv_app_status.setChecked(true);
					}

				}

			}

		});
	}

	/**
	 * ȫѡ
	 * 
	 * @param view
	 */

	public void selectAll(View view) {

		for (TaskInfo taskInfo : userTaskInfos) {

			// �жϵ�ǰ���û������ǲ����Լ��ĳ���������Լ��ĳ�����ô�Ͱ��ı�������

			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}

			taskInfo.setChecked(true);
		}

		for (TaskInfo taskInfo : systemAppInfos) {
			taskInfo.setChecked(true);
		}
		// һ��Ҫע�⡣һ�����ݷ����ı�һ��Ҫˢ��
		adapter.notifyDataSetChanged();

	}

	/**
	 * ��ѡ
	 * 
	 * @param view
	 */
	public void selectOppsite(View view) {
		for (TaskInfo taskInfo : userTaskInfos) {
			// �жϵ�ǰ���û������ǲ����Լ��ĳ���������Լ��ĳ�����ô�Ͱ��ı�������

			if (taskInfo.getPackageName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setChecked(!taskInfo.isChecked());
		}
		for (TaskInfo taskInfo : systemAppInfos) {
			taskInfo.setChecked(!taskInfo.isChecked());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * �������
	 * 
	 * @param view
	 */
	public void killProcess(View view) {

		// ��ɱ�����̡����ȱ���õ����̹�����

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		// ������̵ļ���
		List<TaskInfo> killLists = new ArrayList<TaskInfo>();

		// ������ܹ��Ľ��̸���
		int totalCount = 0;
		// ����Ľ��̵Ĵ�С
		int killMem = 0;
		for (TaskInfo taskInfo : userTaskInfos) {

			if (taskInfo.isChecked()) {
				killLists.add(taskInfo);
				// userTaskInfos.remove(taskInfo);
				totalCount++;
				killMem += taskInfo.getMemorySize();

			}
		}

		for (TaskInfo taskInfo : systemAppInfos) {

			if (taskInfo.isChecked()) {
				killLists.add(taskInfo);
				// systemAppInfos.remove(taskInfo);
				totalCount++;
				killMem += taskInfo.getMemorySize();
				// ɱ������ ������ʾ����
				activityManager.killBackgroundProcesses(taskInfo
						.getPackageName());
			}
		}
		/**
		 * ע�⣺ �������ڵ�����ʱ�򡣲����޸ļ��ϵĴ�С
		 */
		for (TaskInfo taskInfo : killLists) {
			// �ж��Ƿ����û�app
			if (taskInfo.isUserApp()) {
				userTaskInfos.remove(taskInfo);
				// ɱ������ ������ʾ����
				activityManager.killBackgroundProcesses(taskInfo
						.getPackageName());
			} else {
				systemAppInfos.remove(taskInfo);
				// ɱ������ ������ʾ����
				activityManager.killBackgroundProcesses(taskInfo
						.getPackageName());
			}
		}

		Utils.showToast(
				TaskManagerActivity.this,
				"������"
						+ totalCount
						+ "������,�ͷ�"
						+ Formatter.formatFileSize(TaskManagerActivity.this,
								killMem) + "�ڴ�");
        //processCount ��ʾ�ܹ��ж��ٸ�����
		//totalCount ��ǰ�����˶��ٸ�����
		processCount -= totalCount; 
		tv_task_process_count.setText("����:"+ processCount +"��");
		// 
		tv_task_memory.setText("ʣ��/���ڴ�:"
				+ Formatter.formatFileSize(TaskManagerActivity.this, availMem + killMem)
				+ "/"
				+ Formatter.formatFileSize(TaskManagerActivity.this, totalMem));
		
		// ˢ�½���
		adapter.notifyDataSetChanged();

	}
	/**
	 * �����ý���
	 * @param view
	 */
	public void openSetting(View view){
		Intent intent = new Intent(this,TaskManagerSettingActivity.class);
		startActivity(intent);
	}
}
