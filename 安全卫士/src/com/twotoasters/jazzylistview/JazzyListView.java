/*
 * Copyright (C) 2015 Two Toasters
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twotoasters.jazzylistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.reviewmobile.R;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class JazzyListView extends ListView {

    private final JazzyHelper mHelper;
    
    
    /**
	 * ��󻬶�����
	 */
	private static final float MAX_DELTAY = 150;
	/**
	 * �����ָ��Ķ���ʱ��
	 */
	private static final long SEPARATE_RECOVER_DURATION = 300;
	/**
	 * Ħ��ϵ��
	 */
	private static final float FACTOR = 0.25f;
	/**
	 * ����x�����ű���
	 */
	private static final float SCALEX = 0.98f;
	/**
	 * ����y�����ű���
	 */
	private static final float SCALEY = 0.9f;
	/**
	 * չ��ȫ��
	 */
	private boolean separateAll;
	
	/**
	 * ����߽�ʱ����������ʼλ��
	 */
	private float startY;
	/**
	 * ����ʱ��View
	 */
	private View downView;
	
	private int touchSlop;
	
	private boolean separate = false;
	private boolean showDownAnim;
	
	/**
	 * ԭʼ����λ��(������Item�е�λ��)
	 */
	private int originDownPosition;
	/**
	 * ���µ�λ��(����Ļ�е�λ��)
	 */
	private int downPosition;
	
	/**
	 * �ϴλ�����λ�ã������жϷ���
	 */
	private float preY;
	
	private float deltaY;
	private boolean reachTop,reachBottom,move;
	
    public JazzyListView(Context context) {
        super(context);
        mHelper = init(context, null);
        inits();
    }
    
    public JazzyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = init(context, attrs);
        inits();
    }

    public JazzyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHelper = init(context, attrs);
        inits();
    }

    private JazzyHelper init(Context context, AttributeSet attrs) {
        JazzyHelper helper = new JazzyHelper(context, attrs);
        super.setOnScrollListener(helper);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.PullSeparateListView);
		separateAll = t.getBoolean(R.styleable.PullSeparateListView_separate_all, false);
		showDownAnim = t.getBoolean(R.styleable.PullSeparateListView_showDownAnim, true);
		t.recycle();
		inits();
        return helper;
    }
    private void inits() {
		//��֪����ô��divider��selector��Itemһ���ƶ�������ȥ������Ҫ�Լ��ӷָ���
		this.setDivider(null);
		this.setSelector(new BitmapDrawable());
		
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		super.setOnScrollListener(listener);
	}
    //����Ķ���Ҫ�ܣ����⿪ʼ��д
    @Override
    public final void setOnScrollListener(OnScrollListener l) {
        mHelper.setOnScrollListener(l);
    }

    /**
     * Sets the desired transition effect.
     *
     * @param transitionEffect Numeric constant representing a bundled transition effect.
     */
    public void setTransitionEffect(int transitionEffect) {
        mHelper.setTransitionEffect(transitionEffect);
    }

    /**
     * Sets the desired transition effect.
     *
     * @param transitionEffect The non-bundled transition provided by the client.
     */
    public void setTransitionEffect(JazzyEffect transitionEffect) {
        mHelper.setTransitionEffect(transitionEffect);
    }

    /**
     * Sets whether new items or all items should be animated when they become visible.
     *
     * @param onlyAnimateNew True if only new items should be animated; false otherwise.
     */
    //������item����ʱӦ���еĶ���
    public void setShouldOnlyAnimateNewItems(boolean onlyAnimateNew) {
        mHelper.setShouldOnlyAnimateNewItems(onlyAnimateNew);
    }

    /**
     * If true animation will only occur when scrolling without the users finger on the screen.
     *
     * @param onlyWhenFling
     */
    //�������ڹ���ʱ�Ķ���
    public void setShouldOnlyAnimateFling(boolean onlyWhenFling) {
        mHelper.setShouldOnlyAnimateFling(onlyWhenFling);
    }

    /**
     * Stop animations after the list has reached a certain velocity. When the list slows down
     * it will start animating again. This gives a performance boost as well as preventing
     * the list from animating under the users finger if they suddenly stop it.
     *
     * @param itemsPerSecond, set to JazzyHelper.MAX_VELOCITY_OFF to turn off max velocity.
     *        While 13 is a good default, it is dependent on the size of your items.
     */
    //���ù�������
    public void setMaxAnimationVelocity(int itemsPerSecond) {
        mHelper.setMaxAnimationVelocity(itemsPerSecond);
    }

    /**
     * Enable this if you are using a list with items that should act like grid items.
     *
     * @param simulateGridWithList
     */
    //����ģ��
    public void setSimulateGridWithList(boolean simulateGridWithList) {
        mHelper.setSimulateGridWithList(simulateGridWithList);
        setClipChildren(!simulateGridWithList);
    }

    //---------------------------------------------------------------
    
    /**
	 * �Ƿ�ȫ������
	 * @param separateAll ���Ϊtrue,��ôȫ��������롣����Ļ�������Ƕ���������ֻ�е��λ��֮ǰ��Item�����</br>
	 * 					  ����ǵײ���������ֻ�е��λ��֮���item����롣Ĭ��Ϊfalse
	 */
	public void setSeparateAll(boolean separateAll) {
		this.separateAll = separateAll;
	}
	
	public boolean isSeparateAll() {
		return separateAll;
	}
	
	/**
	 * �����Ƿ���ʾ���µ�Item�Ķ���Ч��
	 * @param showDownAnim Ĭ��Ϊtrue
	 */
	public void setShowDownAnim(boolean showDownAnim) {
		this.showDownAnim = showDownAnim;
	}
	
	public boolean isShowDownAnim() {
		return showDownAnim;
	}
	
	
	//���Ĵ���
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float currentY = ev.getY();
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			float downX = ev.getX();
			float downY = ev.getY();
			//��¼����λ�ã���isSeparateAll()����falseʱ�����õ�
			originDownPosition = pointToPosition((int)downX, (int)downY);
			downPosition = originDownPosition - getFirstVisiblePosition();
			if(showDownAnim){
				performDownAnim(downPosition);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			//��¼���ﶥ����ײ�ʱ��ָ��λ��
			if(!separate){
				startY = currentY;
			}
			deltaY = currentY - startY;
			
			//���ﶥ��
			if(reachTop){
				if(!separateFromTop(currentY)){
					return super.dispatchTouchEvent(ev);
				}
				return false;
			}
			//����ײ�
			if(reachBottom){
				if(!separateFromBottom(currentY)){
					return super.dispatchTouchEvent(ev);
				}
				return false;
			}
			preY = currentY;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			preY = 0;
			recoverDownView();
			if(separate){
				separate = false;
				recoverSeparate();
				//�ƶ�������Ӧ����¼�
				if(move){
					move = false;
					return false;
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private boolean separateFromTop(float currentY){
		//���ܷ����ⲿ�������ڶ�������û��FlingЧ��
		if(deltaY > touchSlop){
			move = true;
		}
		separate = true;
		//������������������룬����ʼλ��������
		if(deltaY > MAX_DELTAY){
			startY = currentY - MAX_DELTAY;
			//����������ʱ������overScrollЧ��//������
			//return super.dispatchTouchEvent(ev);
		}else if(deltaY < 0){ //Ϊ��ֵʱ��˵�������򳬹�����ʼλ��startY����0
			deltaY = 0;
			separate = false;
		}
		
		if(deltaY <= MAX_DELTAY){
			for(int index = 0 ; index < getChildCount() ; index++){
				View child = getChildAt(index);
				int multiple = index;
				if(!separateAll){
					if(index > downPosition){
						multiple = Math.max(1, downPosition);
					}
				}
				float distance = multiple*deltaY*FACTOR;
				child.setTranslationY(distance);
			}
			//����뷽��ķ����򻬶�����λ�û�δ��ԭʱ
			if(deltaY != 0 && currentY - preY < 0){
				return true;
			}
			//deltaY=0��˵��λ���Ѿ���ԭ��Ȼ�󽻸����ദ��
		}
		if(deltaY == 0){
			return false;
		}
		return true;
	}
	
	private boolean separateFromBottom(float currentY) {
		if(Math.abs(deltaY) > touchSlop){
			move = true;
		}
		separate = true;
		//������������������룬����ʼλ��������
		if(Math.abs(deltaY) > MAX_DELTAY){
			startY = currentY + MAX_DELTAY;
			//����������ʱ������overScrollЧ��
			//return super.dispatchTouchEvent(ev);
		}else if(deltaY > 0){ //Ϊ��ֵʱ��˵���������ƶ�������ʼλ��startY������0
			deltaY = 0;
			separate = false;
		}
		if(Math.abs(deltaY) <= MAX_DELTAY){
			int visibleCount = getChildCount();
			for(int inedex = 0 ; inedex < visibleCount ; inedex++){
				View child = getChildAt(inedex);
				int multiple = visibleCount - inedex - 1;
				if(!separateAll){
					if(inedex < downPosition){
						multiple = Math.max(1,visibleCount - downPosition - 1);
					}
				}
				float distance = multiple*deltaY*FACTOR;
				child.setTranslationY(distance);
			}
			//����뷽��ķ����򻬶�����λ�û�δ��ԭʱ
			if(deltaY != 0 && currentY - preY > 0){
				return true;
			}
			//deltaY=0��˵��λ���Ѿ���ԭ��Ȼ�󽻸����ദ��
			if(deltaY == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * �ָ�
	 */
	private void recoverSeparate() {
		for(int i = 0 ; i < getChildCount() ; i++){
			View child = getChildAt(i);
			ViewPropertyAnimator.animate(child)
			.translationY(0).setDuration(SEPARATE_RECOVER_DURATION)
			.setInterpolator(new AccelerateInterpolator());
		}
	}
	
	/**
	 * ���µĶ���
	 * @param downPosition ����Ļ�е�λ��
	 */
	private void performDownAnim(int downPosition) {
		downView = getChildAt(downPosition);
		if(downView != null){
			ViewPropertyAnimator.animate(downView)
			.scaleX(SCALEX).scaleY(SCALEY).setDuration(50)
			.setInterpolator(new AccelerateInterpolator());
		}
	}
	
	/**
	 * �ָ������View
	 */
	private void recoverDownView() {
		if(showDownAnim && downView != null){
			ViewPropertyAnimator.animate(downView)
			.scaleX(1f).scaleY(1f).setDuration(separate ? SEPARATE_RECOVER_DURATION : 100)
			.setInterpolator(new AccelerateInterpolator());
		}
	}
	
	private OnScrollListener listener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(mHelper != null){
				mHelper.onScrollStateChanged(view, scrollState);
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if(mHelper != null){
				mHelper.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
			
			//�Ƿ񵽴ﶥ��
			if(firstVisibleItem == 0){
				View firstView = getChildAt(firstVisibleItem);
				if(firstView != null && (firstView.getTop() + getPaddingTop()) >= 0){
					downPosition = originDownPosition;
					reachTop = true;
				}else{
					reachTop = false;
				}
			}else{
				reachTop = false;
			}
			//�Ƿ񵽴�ײ�
			if(firstVisibleItem + visibleItemCount == getCount()){
				View lastView = getChildAt(visibleItemCount - 1);
				if(lastView != null && (lastView.getBottom() + getPaddingBottom()) <= getHeight() && getCount() > getChildCount()){
					downPosition = originDownPosition - firstVisibleItem;
					reachBottom = true;
				}else{
					reachBottom = false;
				}
			}else{
				reachBottom = false;
			}
		}
	};
	
	/**
	 * �Ƿ񵽴ﶥ��
	 * @return
	 */
	@Deprecated
	protected boolean isReachTopBound() {
		int firstVisPos = getFirstVisiblePosition();
		if(firstVisPos == 0){
			View firstView = getChildAt(firstVisPos);
			if(firstView != null && firstView.getTop() >= 0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * �Ƿ񵽴�ײ�
	 * @return
	 */
	@Deprecated
	protected boolean isReachBottomBound(){
		int lastVisPos = getLastVisiblePosition();
		if(lastVisPos == getCount() - 1){
			View lastView = getChildAt(getChildCount() - 1);
			if(lastView != null && lastView.getBottom() <= getHeight() && getCount() > getChildCount()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
    
    
}
