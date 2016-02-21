package com.example.reviewmobile.contactlistindex;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.example.reviewmobile.R;



public class FancyIndexer extends View {
	
	
	public interface OnTouchLetterChangedListener {
		public void onTouchLetterChanged(String s);
	}
	
	private static final String TAG = "FancyIndexer";
	
	/////////////////////////////////////////////////////////////////////////
	
	//Properties
	// ����ƫ�ƶ��ٻ��ַ��� default 30
	float mWidthOffset = 30.0f;
	
	// ��С�����С
	int mMinFontSize = 24;
	
	// ��������С
	int mMaxFontSize = 48;
	
	// ��ʾ�����С
	int mTipFontSize = 52;
	
	// ��ʾ�ַ��Ķ���ƫ��
	float mAdditionalTipOffset = 20.0f;
	
	// ���������߿��Ƶĸ߶�
	float mMaxBezierHeight = 150.0f;
	
	// ���������ߵ�����
	float mMaxBezierWidth = 240.0f;
	
	// ���������ߵ���ģ������
	int  mMaxBezierLines = 32;
	
	// �б��ַ���ɫ
	int  mFontColor = 0xffffffff;
	
	// ��ʾ�ַ���ɫ
//	int  mTipFontColor = 0xff3399ff;
	int  mTipFontColor = 0xffd33e48;
	
	/////////////////////////////////////////////////////////////////////////
	
	private OnTouchLetterChangedListener mListener;
	
	private final String[] ConstChar = {"#","A","B","C","D","E","F","G","H","I","J","K","L"
			               ,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	int mChooseIndex = -1;
	Paint mPaint = new Paint();
	PointF mTouch = new PointF();
	
	PointF[] mBezier1;
	PointF[] mBezier2;
	
	float mLastOffset[] = new float[ConstChar.length]; // ��¼ÿһ����ĸ��x����ƫ����, ����<=0
	PointF mLastFucusPostion = new PointF();
	
	Scroller mScroller;
	boolean mAnimating = false;
	float mAnimationOffset;
	
	boolean mHideAnimation = false;
	int mAlpha = 255; 
	
	Handler mHideWaitingHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			if( msg.what == 1 )
			{
//				mScroller.startScroll(0, 0, 255, 0, 1000);
				mHideAnimation = true;
				mAnimating = false;
				FancyIndexer.this.invalidate();
				return;
			}
			super.handleMessage(msg);
		}
	};
	
	public FancyIndexer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context, attrs);
	}

	public FancyIndexer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context, attrs);
	}

	public FancyIndexer(Context context) {
		super(context);
		initData(null, null);
	}
	
	private void initData(Context context, AttributeSet attrs) {
		
		if( context != null && attrs != null ) {
			
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FancyIndexer, 0, 0);
			
			mWidthOffset    = a.getDimension(R.styleable.FancyIndexer_widthOffset, mWidthOffset);
			mMinFontSize    = a.getInteger(R.styleable.FancyIndexer_minFontSize, mMinFontSize);
			mMaxFontSize    = a.getInteger(R.styleable.FancyIndexer_maxFontSize, mMaxFontSize);
			mTipFontSize    = a.getInteger(R.styleable.FancyIndexer_tipFontSize, mTipFontSize);
			mMaxBezierHeight = a.getDimension(R.styleable.FancyIndexer_maxBezierHeight, mMaxBezierHeight);
			mMaxBezierWidth = a.getDimension(R.styleable.FancyIndexer_maxBezierWidth, mMaxBezierWidth);			
			mMaxBezierLines =  a.getInteger(R.styleable.FancyIndexer_maxBezierLines, mMaxBezierLines);
			mAdditionalTipOffset = a.getDimension(R.styleable.FancyIndexer_additionalTipOffset, mAdditionalTipOffset);
			mFontColor = a.getColor(R.styleable.FancyIndexer_fontColor, mFontColor);
			mTipFontColor = a.getColor(R.styleable.FancyIndexer_tipFontColor, mTipFontColor);
			a.recycle();
		}
		mScroller = new Scroller( getContext() );
		mTouch.x = 0;
		mTouch.y = -10*mMaxBezierWidth;
		
		mBezier1 = new PointF[mMaxBezierLines];
		mBezier2 = new PointF[mMaxBezierLines];
		
		calculateBezierPoints();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// �ؼ����
	    int height = getHeight();
	    int width = getWidth();

	    // ������ĸ�߶�
	    float singleHeight = height / (float)ConstChar.length;
	    
	    int workHeight = 0;
	    
	    if( mAlpha == 0 )
	    	return;
	    
	    mPaint.reset();
	    
	    int saveCount = 0;
	    
	    if( mHideAnimation )
	    {
	    	saveCount = canvas.save();
	    	canvas.saveLayerAlpha( 0, 0, width, height, mAlpha, Canvas.ALL_SAVE_FLAG );
	    }
	    
	    for(int i=0;i<ConstChar.length;i++) {
	    	
	       mPaint.setColor(mFontColor);
	       mPaint.setAntiAlias(true);
	       
	       float xPos = width - mWidthOffset;
	       float yPos = workHeight + singleHeight/2;
	       
	       //float adjustX = adjustXPos( yPos, i == mChooseIndex );
	       // ���ݵ�ǰ��ĸy��λ�ü���õ������С
	       int fontSize = adjustFontSize(i, yPos ); 
	       mPaint.setTextSize(fontSize);
    	   
	       // ���һ����ĸ�ĸ߶�
	       workHeight += singleHeight;
	       
	       // ������ĸ
	       drawTextInCenter(canvas, ConstChar[i], xPos + ajustXPosAnimation(i,  yPos )  , yPos );
	       
	       // ���Ƶ���ĸ�͵�ǰ��������һ��, ���ƺ�ɫ��ѡ����ĸ
	       if(i == mChooseIndex) {
	    	   mPaint.setColor( mTipFontColor );
	    	   mPaint.setFakeBoldText(true);
	    	   mPaint.setTextSize( mTipFontSize );
	    	   yPos = mTouch.y;
	    	   
	    	   float pos = 0;
	    	   
	    	   if( mAnimating || mHideAnimation ) {
	    		   pos = mLastFucusPostion.x;
	    		   yPos = mLastFucusPostion.y;
	    	   } else {
	    		   pos = xPos + ajustXPosAnimation(i, yPos ) - mAdditionalTipOffset;
	    		   mLastFucusPostion.x = pos;
	    		   mLastFucusPostion.y = yPos;
	    	   }
	    		   
	    	   drawTextInCenter(canvas, ConstChar[i], pos, yPos );
//	    	   mPaint.setStrokeWidth(5);
//	    	   canvas.drawLine(0, yPos, width, yPos, mPaint);
	       }
	       mPaint.reset();
	    }
	    
	    if( mHideAnimation ) 
	    {
	    	canvas.restoreToCount(saveCount);
	    }
	   
	}
	
	/**
	 * @param canvas ����
	 * @param string �����Ƶ���ĸ
	 * @param xCenter ��ĸ������x����λ��
	 * @param yCenter ��ĸ������y����λ��
	 */
	private void drawTextInCenter(Canvas canvas, String string, float xCenter, float yCenter) {

		FontMetrics fm = mPaint.getFontMetrics();
		//float fontWidth = paint.measureText(string);
		float fontHeight = mPaint.getFontSpacing();
		
		float drawY = yCenter + fontHeight/2 - fm.descent;
		
		if( drawY < -fm.ascent -fm.descent )
			drawY = -fm.ascent -fm.descent;
		
		if( drawY > getHeight() )
			drawY = getHeight() ;
		
		mPaint.setTextAlign(Align.CENTER);
		
		canvas.drawText(string, xCenter, drawY, mPaint);
	}
	
	private int adjustFontSize(int i, float yPos ) {
		
		// ����ˮƽ����ƫ���������һ���Ŵ���ֺ�
		float adjustX = Math.abs(ajustXPosAnimation(i,  yPos ));
		
		int adjustSize =(int)( (mMaxFontSize - mMinFontSize ) * adjustX / (float)mMaxBezierHeight) + mMinFontSize;
		
		return adjustSize;
	}
	
	/**
	 * x ���������ƫ����
	 * @param i	��ǰ��ĸ������
	 * @param yPos y����ĳ�ʼλ��
	 * @return
	 */
	private float ajustXPosAnimation (int i, float yPos ) {

		float offset ;
		if( this.mAnimating || this.mHideAnimation ) {
			// ���ڶ����л��������ض���
			offset = mLastOffset[i];
			if( offset !=0.0f ) {
				offset += this.mAnimationOffset;
				if( offset > 0)
					offset = 0;
			}
		} else {
			
			// ���ݵ�ǰ��ĸy����λ��, ����ˮƽ����ƫ����
			offset = adjustXPos( yPos );
			
			// ��ǰ������x����λ��
			float xPos = mTouch.x  ;
			
			float width = getWidth() - mWidthOffset;
			width = width - 60;
			
			// ��ĸ����ʱ����ƫ���� ��������, offset��Ҫ��<=0��ֵ
			if( offset != 0.0f  && xPos > width )
				offset +=  ( xPos - width );
			if( offset > 0)
				offset = 0;
			
			mLastOffset[i] = offset;
		}
		return offset;
	}
	
	private float adjustXPos(float yPos ) {
	
		float dis = yPos - mTouch.y; // ��ĸy����λ�úʹ���ʱyֵ����Ĳ�ֵ, ����ԽС, �õ���ˮƽ����ƫ��Խ�� 
		if( dis > -mMaxBezierWidth  && dis < mMaxBezierWidth ) {
			// ��2�����������߿�ȷ�Χ���� (һ�����������߿����ָһ��ɽ���һ��)

			// ��һ�� ����
			if( dis > mMaxBezierWidth/4 ) {
				for( int i = mMaxBezierLines-1; i>0 ; i-- ) {
					// ���µ���, �������
					
					if( dis == -mBezier1[i].y ) // ���ڵ���
						return mBezier1[i].x;
					
					// �������dis������������������ģ���֮��, ͨ�����Ǻ�������õ���ǰdis��Ӧ��x����ƫ����
					if( dis > -mBezier1[i].y && dis < -mBezier1[i-1].y ) {
						return (dis + mBezier1[i].y) * ( mBezier1[i-1].x - mBezier1[i].x ) / ( -mBezier1[i-1].y + mBezier1[i].y ) + mBezier1[i].x;
					}
				}
				return mBezier1[0].x;
			}
			
			// ������ ����, �͵�һ�����߶Գ�
			if( dis < -mMaxBezierWidth/4 ) {
				for( int i = 0; i< mMaxBezierLines-1; i++ ) {
					// ���ϵ���
					
					if( dis == mBezier1[i].y ) // ���ڵ���
						return mBezier1[i].x;

					// �������dis������������������ģ���֮��, ͨ�����Ǻ�������õ���ǰdis��Ӧ��x����ƫ����
					if( dis > mBezier1[i].y && dis < mBezier1[i+1].y ) {
						return (dis - mBezier1[i].y )* (mBezier1[i+1].x - mBezier1[i].x ) / ( mBezier1[i+1].y - mBezier1[i].y ) + mBezier1[i].x;
					}
				}
				return mBezier1[mMaxBezierLines-1].x;
			}
			
			// �ڶ��� �嶥����
			for( int i = 0; i< mMaxBezierLines-1; i++ ) {
				
				if( dis == mBezier2[i].y )
					return mBezier2[i].x;

				// �������dis������������������ģ���֮��, ͨ�����Ǻ�������õ���ǰdis��Ӧ��x����ƫ����
				if( dis > mBezier2[i].y && dis < mBezier2[i+1].y ) {
					return ( dis - mBezier2[i].y) * ( mBezier2[i+1].x - mBezier2[i].x ) / (mBezier2[i+1].y - mBezier2[i].y ) + mBezier2[i].x;
				}
			}	
			return mBezier2[mMaxBezierLines-1].x;		

		}
		
		return 0.0f;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
	    final float y = event.getY();
	    final int oldmChooseIndex = mChooseIndex;
	    final OnTouchLetterChangedListener listener = mListener;
	    final int c = (int) (y/getHeight()*ConstChar.length);
	    
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				
				if( this.getWidth() > mWidthOffset ) {
					if ( event.getX() < this.getWidth() - mWidthOffset )
						return false;
				}
				
				mHideWaitingHandler.removeMessages(1);
				
				mScroller.abortAnimation();
				mAnimating = false;
				mHideAnimation = false;
				mAlpha = 255;
				
				mTouch.x = event.getX();
				mTouch.y = event.getY();
				
				if(oldmChooseIndex != c && listener != null){
					if(c > 0 && c< ConstChar.length){
						listener.onTouchLetterChanged(ConstChar[c]);
						mChooseIndex = c;
					}
				}
				invalidate();				
				break;
			case MotionEvent.ACTION_MOVE:
				mTouch.x = event.getX();
				mTouch.y = event.getY();
				invalidate();
				if(oldmChooseIndex != c && listener != null){

					if(c >= 0 && c< ConstChar.length){
						listener.onTouchLetterChanged(ConstChar[c]);
						mChooseIndex = c;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:

				mTouch.x = event.getX();
				mTouch.y = event.getY();

				//this.mChooseIndex = -1;
				
				mScroller.startScroll(0, 0, (int)mMaxBezierHeight, 0, 2000);
				mAnimating = true;
				postInvalidate();
				break;
		}
		return true;
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			if( mAnimating ) {
				float x = mScroller.getCurrX();
				mAnimationOffset = x;				
			} else if( mHideAnimation ) {
				mAlpha = 255 - (int) mScroller.getCurrX();
			}
			invalidate();
		} else if( mScroller.isFinished() ) {
			if( mAnimating ) {
				mHideWaitingHandler.sendEmptyMessage(1);
			} else if( mHideAnimation ) {
				mHideAnimation = false;
				this.mChooseIndex = -1;
				mTouch.x = -10000;
				mTouch.y = -10000;
			}

		}
	}
	public void setOnTouchLetterChangedListener( OnTouchLetterChangedListener listener) {
		this.mListener = listener;
	}

	/**
	 * ��������б����������ϵĵ� 
	 * ����Ϊ mMaxBezierLines * 2 = 64
	 */
	private void calculateBezierPoints() {
		
		PointF mStart = new PointF();   // ��ʼ��
		PointF mEnd = new PointF();		// ������
		PointF mControl = new PointF(); // ���Ƶ�
		
		
		// �����һ�κ�ɫ���� ���������ߵĵ�
		// ��ʼ��
		mStart.x = 0.0f;
		mStart.y = -mMaxBezierWidth;

		// ���Ƶ�
		mControl.x = 0.0f;
		mControl.y = -mMaxBezierWidth/2;
		
		// ������
		mEnd.x = - mMaxBezierHeight / 2;
		mEnd.y = - mMaxBezierWidth / 4;
		
		mBezier1[0] = new PointF();
		mBezier1[mMaxBezierLines-1] = new PointF();
		
		mBezier1[0].set(mStart);
		mBezier1[mMaxBezierLines-1].set(mEnd);
		
		for( int i = 1; i< mMaxBezierLines -1; i++ ) {
			
			mBezier1[i] = new PointF();
			
			mBezier1[i].x = calculateBezier( mStart.x, mEnd.x, mControl.x, i / (float) mMaxBezierLines );
			mBezier1[i].y = calculateBezier( mStart.y, mEnd.y, mControl.y, i / (float) mMaxBezierLines );
			
		}

		// ����ڶ�����ɫ���� ���������ߵĵ�
		mStart.y = -mMaxBezierWidth / 4;
		mStart.x = -mMaxBezierHeight / 2;
		
		mControl.y = 0.0f;
		mControl.x = -mMaxBezierHeight;

		mEnd.y = mMaxBezierWidth / 4;
		mEnd.x = -mMaxBezierHeight / 2;
		
		mBezier2[0] = new PointF();
		mBezier2[mMaxBezierLines-1] = new PointF();

		mBezier2[0].set(mStart);
		mBezier2[mMaxBezierLines-1].set(mEnd);
		
		for( int i = 1; i< mMaxBezierLines -1 ; i++ ) {
			
			mBezier2[i]= new PointF();
			mBezier2[i].x = calculateBezier( mStart.x, mEnd.x, mControl.x,  i / (float) mMaxBezierLines );
			mBezier2[i].y = calculateBezier( mStart.y, mEnd.y, mControl.y,  i / (float) mMaxBezierLines );
		}
	}

	/**
	 * ���������ߺ����㷨
	 * @param start
	 * @param end
	 * @param control
	 * @param val
	 * @return
	 * ��ʽ����ͼ, ά���ٿ�: https://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * ���Ŀɲο�����վ: http://blog.csdn.net/likendsl/article/details/7852658
	 * 
	 */
	private float calculateBezier(float start, float end, float control, float val) {
		
		float t = val;
		float s = 1-t;
		
		float ret = start * s * s + 2 * control * s * t + end * t * t;
		
		return ret;
	}	
}
