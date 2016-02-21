/**
 * 
 */
/**
 * @author dell
 *
 */
package com.example.reviewmobile.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class MyAdapter<T> extends BaseAdapter{
	
	public List<T> lists;
	public Context mContext;
	
	
	public MyAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyAdapter(List<T> lists,Context mContext) {
		this.lists = lists;
		this.mContext=mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
}