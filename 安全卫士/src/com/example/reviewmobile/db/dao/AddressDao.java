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
		// ��ʼ������ֵ
		String str = "δ֪����";

		// sql��ѯ���
		String sql = "select location from data2 where id=(select outkey from data1 where id=?)";

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		/**
		 * �����ݿ��ļ����в�ѯ�������������ж�
		 * 
		 * "^1[3-8]\\d{9}$"��һλ���� 1���ڶ����� 3��8֮�������Ϊ���֣�֮�������9λ����������11λ������
		 */
		if (phone.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = db.rawQuery(sql,
					new String[] { phone.substring(0, 7) }); // ��ȡ��ǰ����ǰ7λ
			if (cursor.moveToNext()) { // �����Ϊ��Ͳ���һ����
				str = cursor.getString(0);
			}
			cursor.close();
		} else if (phone.matches("^\\d+$")) {
			switch (phone.length()) {
			case 3:
				str = "�����绰";
				break;
			case 4:
				str = "ģ����";
				break;
			case 5:
				str = "�ͷ��绰";
				break;
			case 7:
			case 8:
				str = "���ص绰";
				break;
			}
		} else if (phone.startsWith("0") && phone.length() > 10) {// ���绰������0��ʼ�����ȴ���10λ
			// �����ж�Ϊ�ǳ�;�����ٴβ�ѯ���ݿ�
			Cursor cursor = db.rawQuery(
					"select location from data2 where area =?",
					new String[] { phone.substring(1, 4) });// ��ȡ����ǰ4λȥ��ѯ

			if (cursor.moveToNext()) {
				str = cursor.getString(0);
			} else {
				cursor.close();

				cursor = db.rawQuery(
						"select location from data2 where area =?",
						new String[] { phone.substring(1, 3) });// ��ȡ����ǰ4λȥ��ѯ

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