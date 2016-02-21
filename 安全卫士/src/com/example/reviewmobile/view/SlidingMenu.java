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
 * �Զ���viewGroup 1.onMeasure���� �����ڲ�View�Ŀ�͸ߣ��Լ��Լ��Ŀ�͸� 2.onLayout ����View�ķ���λ��
 * 3.onTouchEvent
 * 
 * @author dell
 * 
 */
public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper; // ����view�������һ��layout
	private ViewGroup mMenu; // �˵�layout
	private ViewGroup mContent;// ����layout
	private int screenWidth; // ��Ļ���
	private int menuRightPadding;// menu�Ҳ�������
	private int mMenuWidth; // menu��
	private String namespace = "http://schemas.android.com/apk/res/com.example.reviewmobile";
	private boolean once; // onMesure���ܻᱻ��ε���
	private boolean isOpen;//��ť�жϵ�ǰmenu�Ƿ��
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		String value = attrs.getAttributeValue(namespace, "rightpadding");//�Զ������ԣ���ĻԤ����
		
		//��ȡ��Ļ��
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;

		// ��dpת��Ϊ50dx
		menuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, Integer.parseInt(value), context.getResources()
						.getDisplayMetrics());
	}

	/**
	 * ������view�Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		// ��ֹ��ε���
		if (!once) {
			// �õ�mWapper
			mWapper = (LinearLayout) getChildAt(0);// һ��Ԫ��
			mMenu = (ViewGroup) mWapper.getChildAt(0); // �õ�mWapper�ĵ�һԪ��
			mContent = (ViewGroup) mWapper.getChildAt(1);// �õ�mWapper�ĵڶ���Ԫ��

			// ����kuan
			mMenuWidth = mMenu.getLayoutParams().width = screenWidth
					- menuRightPadding; // ͨ����ͼ��������menu�Ŀ�ȵ�����Ļ���-�ұ߾���
			mContent.getLayoutParams().width = screenWidth; // ����view�Ŀ��
			// mWapper.getLayoutParams().width = screenWidth;
			// //wapper�Ŀ�ȵ���menu+content�Ŀ��
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * ͨ������ƫ��������menu����
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
		case MotionEvent.ACTION_UP:			//����view���Զ������ºʹ����¼�������������ֻ��Ҫ�����ɿ��¼� 
			
			//������߿�ȣ����жϵ�ǰmenu�ɿ�ʱ�Ƿ�����ܿ��һ��
			int scrollX = getScrollX();
			if(scrollX>=mMenuWidth/2){
				this.smoothScrollTo(mMenuWidth, 0); //����������
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
	
	//�򿪲˵�
	public void openMenu(){
		if(isOpen)return;			//��menu��ǰΪ��״̬��return
		this.smoothScrollTo(0, 0);
		isOpen=true;
	}
	//�ر�
	public void closeMenu(){
		if(!isOpen)return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen=false;
	}
	//�򿪹ر�
	public void toggle(){
		if(isOpen){
			closeMenu();
		}else{
			openMenu();
		}
	}
	
	
	//��������ʱ
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		super.onScrollChanged(l, t, oldl, oldt);
		
		float scale = l*1.0f/mMenuWidth;
		float rightScale = 0.7f+0.3f*scale;//����������
		float leftScale = 1.0f-scale*0.3f;	//menu����
		float leftAlpha = 0.6f+0.4f*(1-scale);//menu͸����
		//�������Զ������������Զ�����   
		
		
		//�������Զ���
		ViewHelper.setTranslationX(mMenu, mMenuWidth*scale*0.8f);//����ʽЧ��
		
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		//����content���ŵ����ĵ�
		//����������
		ViewHelper.setPivotX(mContent,0);
		ViewHelper.setPivotY(mContent,mContent.getHeight()/2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
	}
}
