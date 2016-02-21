/**
 * 
 */
/**
 * @author dell
 *
 */
package com.example.reviewmobile.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class AddressDao {
	final static String path = "data/data/com.example.reviewmobile/files/address.db";

	

	public static String getAddress(String phone) {
		// 初始化返回值
		String str = "未知号码";

		// sql查询语句
		String sql = "select location from data2 where id=(select outkey from data1 where id=?)";

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		/**
		 * 对数据库文件进行查询，，用正则做判断
		 * 
		 * "^1[3-8]\\d{9}$"第一位数是 1，第二数是 3到8之间的数，为数字，之后可以有9位数，加起来11位，结束
		 */
		if (phone.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = db.rawQuery(sql,
					new String[] { phone.substring(0, 7) }); // 截取当前号码前7位
			if (cursor.moveToNext()) { // 如果不为真就查下一个，
				str = cursor.getString(0);
			}
			cursor.close();
		} else if (phone.matches("^\\d+$")) {
			switch (phone.length()) {
			case 3:
				str = "报警电话";
				break;
			case 4:
				str = "模拟器";
				break;
			case 5:
				str = "客服电话";
				break;
			case 7:
			case 8:
				str = "本地电话";
				break;
			}
		} else if (phone.startsWith("0") && phone.length() > 10) {// 当电话号码以0开始，长度大于10位
			// 可以判断为是长途号码再次查询数据库
			Cursor cursor = db.rawQuery(
					"select location from data2 where area =?",
					new String[] { phone.substring(1, 4) });// 获取号码前4位去查询

			if (cursor.moveToNext()) {
				str = cursor.getString(0);
			} else {
				cursor.close();

				cursor = db.rawQuery(
						"select location from data2 where area =?",
						new String[] { phone.substring(1, 3) });// 获取号码前4位去查询

				if (cursor.moveToNext()) {
					str = cursor.getString(0);
				}
				cursor.close();
			}
		}
		db.close();
		return str;
	}
}