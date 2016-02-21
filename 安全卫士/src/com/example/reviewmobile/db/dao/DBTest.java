package com.example.reviewmobile.db.dao;

import java.util.List;
import java.util.Random;

import com.example.reviewmobile.bean.BlackNumberBean;
import com.example.reviewmobile.bean.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.test.AndroidTestCase;

public class DBTest extends AndroidTestCase {
	Context context;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context=getContext();
	}
	
	/**
	 * 
	 * ���
	 */
	
	public void add(){  
		BlackNumberDao dao = new BlackNumberDao(context);
		
		for (int i = 0; i < 30; i++) {
			Random random = new Random();
			Long number = 13012348323l+i;
			dao.add(number+"", String.valueOf(random.nextInt(3)+1));
		}   
	}
	
	/**
	 * ɾ��
	 */
	public void delete(){
		BlackNumberDao dao = new BlackNumberDao(context);
		dao.delete("13012348323");
	}  
	
	/**
	 * ����
	 */
	public void updata(){
		BlackNumberDao dao = new BlackNumberDao(context);
		dao.modeChange("13012348324", "1");
	}
	
	/**
	 * ����
	 */
	public void find(){
		BlackNumberDao dao = new BlackNumberDao(context);
		String number = dao.findBlackNumber("13012348324");
		System.out.println(number);  
	}
	
	/**
	 * ��������
	 */
	public void findall(){
		BlackNumberDao dao = new BlackNumberDao(context);
		List<BlackNumberBean> findAllNumber = dao.findAllNumber();
		for (BlackNumberBean blackNumberBean : findAllNumber) {
			System.out.println(blackNumberBean.getMode()+":"+blackNumberBean.getNumber());
		}
	}
}
