package com.example.reviewmobile.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.example.reviewmobile.bean.BlackNumberBean;

public class BlackNumberDao {

	private BlackNumberOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberOpenHelper(context);
	}

	/**
	 * 添加黑名单和类型
	 */
	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		long insert = db.insert("blacknumber", null, values);
		if (insert == -1) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 删除黑名单 通过电话来删除
	 */
	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// 参数1：表名，参数2，删除条件
		int delete = db.delete("blacknumber", "number=?",
				new String[] { number });
		if (delete == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 修改拦截模式 修改电话的拦截模式
	 * 
	 */
	public boolean modeChange(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		// 表名，要修改的值，修改的条件
		int update = db.update("blacknumber", values, "number=?",
				new String[] { number });
		if (update == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 返回一个黑名单拦截模式
	 */
	public String findBlackNumber(String number) {
		String mode = "";
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * 查询所有黑名单
	 */
	public List<BlackNumberBean> findAllNumber() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<BlackNumberBean> arrayList = new ArrayList<BlackNumberBean>();
		Cursor query = db
				.query("blacknumber", new String[] { "number", "mode" }, null,
						null, null, null, null);
		while (query.moveToNext()) {
			BlackNumberBean bean = new BlackNumberBean();
			bean.setMode(query.getString(1));
			bean.setNumber(query.getString(0));
			arrayList.add(bean);
		}
		query.close();
		db.close();
		/**
		 * 睡三秒
		 */
		SystemClock.sleep(1000);
		return arrayList;
	}

	/**
	 * 分页加载 limit限制 offset 从第几条开始
	 */
	public List<BlackNumberBean> findpar(int pageNumber, int pageSize) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from blacknumber limit ? offset ?",
				new String[] { String.valueOf(pageSize),
						String.valueOf(pageSize * pageNumber) });

		List<BlackNumberBean> arrayList = new ArrayList<BlackNumberBean>();
		while (cursor.moveToNext()) {
			BlackNumberBean bean = new BlackNumberBean();
			bean.setMode(cursor.getString(2));
			bean.setNumber(cursor.getString(1));
			arrayList.add(bean);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(1200);
		return arrayList;
	}
	/**
	 * 分页加载 limit限制 offset 从第几条开始
	 */
	public List<BlackNumberBean> findpar2(int startIndex, int countIndex) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from blacknumber limit ? offset ?",
				new String[] { String.valueOf(countIndex),
						String.valueOf(startIndex) });

		List<BlackNumberBean> arrayList = new ArrayList<BlackNumberBean>();
		while (cursor.moveToNext()) {
			BlackNumberBean bean = new BlackNumberBean();
			bean.setMode(cursor.getString(2));
			bean.setNumber(cursor.getString(1));
			arrayList.add(bean);
		}
		cursor.close();
		db.close();
		return arrayList;
	}
	/**
	 * 计算总数页面
	 */
	public int getTotal() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int i = cursor.getInt(0);
		cursor.close();
		db.close();
		return i;
	}
}
