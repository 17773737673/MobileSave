package com.example.reviewmobile.view;

import com.nineoldandroids.view.ViewHelper;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 自定义viewGroup 1.onMeasure测量 决定内部View的框和高，以及自己的宽和高 2.onLayout 决定View的放置位置
 * 3.onTouchEvent
 * 
 * @author dell
 * 
 */
public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper; // 滚动view里面会有一个layout
	private ViewGroup mMenu; // 菜单layout
	private ViewGroup mContent;// 内容layout
	private int screenWidth; // 屏幕宽度
	private int menuRightPadding;// menu右侧最大距离
	private int mMenuWidth; // menu宽
	private String namespace = "http://schemas.android.com/apk/res/com.example.reviewmobile";
	private boolean once; // onMesure可能会被多次调用
	private boolean isOpen;//按钮判断当前menu是否打开
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		String value = attrs.getAttributeValue(namespace, "rightpadding");//自定义属性，屏幕预留宽
		
		//获取屏幕宽
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;

		// 将dp转化为50dx
		menuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, Integer.parseInt(value), context.getResources()
						.getDisplayMetrics());
	}

	/**
	 * 设置子view的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		// 防止多次调用
		if (!once) {
			// 拿到mWapper
			mWapper = (LinearLayout) getChildAt(0);// 一个元素
			mMenu = (ViewGroup) mWapper.getChildAt(0); // 拿到mWapper的第一元素
			mContent = (ViewGroup) mWapper.getChildAt(1);// 拿到mWapper的第二个元素

			// 设置kuan
			mMenuWidth = mMenu.getLayoutParams().width = screenWidth
					- menuRightPadding; // 通过视图属性设置menu的宽度等于屏幕宽度-右边距宽度
			mContent.getLayoutParams().width = screenWidth; // 内容view的宽度
			// mWapper.getLayoutParams().width = screenWidth;
			// //wapper的宽度等于menu+content的宽度
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 通过设置偏移量，将menu隐藏
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		super.onLayout(changed, l, t, r, b);

		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:			//滚动view会自动处理按下和触摸事件，，所以这里只需要处理松开事件 
			
			//隐藏左边宽度，，判断当前menu松开时是否大于总宽的一半
			int scrollX = getScrollX();
			if(scrollX>=mMenuWidth/2){
				this.smoothScrollTo(mMenuWidth, 0); //慢慢的隐藏
				isOpen=false;
			}else{
				 this.smoothScrollTo(0, 0);
				 isOpen=true;
			}
			return true;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	//打开菜单
	public void openMenu(){
		if(isOpen)return;			//当menu当前为打开状态，return
		this.smoothScrollTo(0, 0);
		isOpen=true;
	}
	//关闭
	public void closeMenu(){
		if(!isOpen)return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen=false;
	}
	//打开关闭
	public void toggle(){
		if(isOpen){
			closeMenu();
		}else{
			openMenu();
		}
	}
	
	
	//滚动发生时
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		super.onScrollChanged(l, t, oldl, oldt);
		
		float scale = l*1.0f/mMenuWidth;
		float rightScale = 0.7f+0.3f*scale;//内容区缩放
		float leftScale = 1.0f-scale*0.3f;	//menu缩放
		float leftAlpha = 0.6f+0.4f*(1-scale);//menu透明度
		//调用属性动画，引入属性动画包   
		
		
		//调用属性动画
		ViewHelper.setTranslationX(mMenu, mMenuWidth*scale*0.8f);//抽屉式效果
		
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		//设置content缩放的中心点
		//内容区缩放
		ViewHelper.setPivotX(mContent,0);
		ViewHelper.setPivotY(mContent,mContent.getHeight()/2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}
}
