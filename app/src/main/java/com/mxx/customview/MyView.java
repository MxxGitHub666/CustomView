package com.mxx.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by mxx on 2017/9/18.
 */

public class MyView extends View {

    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;
    private Scroller scroller;


    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    private VelocityTracker velocityTracker;

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;


    public MyView(Context context) {
        this(context,null);
//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTitleText = randomText();
//                Log.e("09090","00000");
//                postInvalidate();
//            }
//        });
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

/**
 * 获取我们所定义的自定义样式属性
 * */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MyView,defStyle,0);
        int n = a.getIndexCount();
        for(int i= 0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.MyView_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.MyView_titleTextColor:
//默认颜色为Black
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;

                case R.styleable.MyView_titleTextSize:
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

/**
 * 获取绘制文本的宽与高
 * */
        mPaint  = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound  = new Rect();
/**
 * @param text  String to measure and return its bounds 要测量的文字
 * @param start Index of the first char in the string to measure    测量的起始位置
 * @param end   1 past the last char in the string measure  测量的最后一个字符串的位置
 * @param bounds Returns the unioned bounds of all the text. Must be    rect对象
 *               allocated by the caller.
 */
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                Log.e("09090","00000");
                postInvalidate();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode  = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width ;
        int height;

        if(widthMode == MeasureSpec.EXACTLY){
            width  = widthSize;
        }else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
            float textWidth = mBound.width();
            int desired  = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop()+textHeight + getPaddingBottom());
            height = desired;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //获取到自定义的view宽高
        final int height  = getHeight();
        final int width = getWidth();

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);


//绘制小圆点
        int [] point;
        for(int i = 0;i<100;i++){
            point = getPoint(height,width);
/**
 * drawCircle (float cx, float cy, float radius, Paint paint)
 * float cx：圆心的x坐标。
 * float cy：圆心的y坐标。
 * float radius：圆的半径。
 * Paint paint：绘制时所使用的画笔。
 */
            mPaint.setColor(Color.BLACK);
            canvas.drawCircle(point[0],point[1],2,mPaint);
        }

        int [] line;
        for(int i = 0; i < 100; i ++) {
            line = getLine(height, width);
/**
 * startX：起始端点的X坐标。
 *startY：起始端点的Y坐标。
 *stopX：终止端点的X坐标。
 *stopY：终止端点的Y坐标。
 *paint：绘制直线所使用的画笔。
 */
            canvas.drawLine(line[0], line[1], line[2], line[3], mPaint);
        }
    }

    public  int[] getLine(int height, int width)
    {
        int [] tempCheckNum = {0,0,0,0};
        for(int i = 0; i < 4; i+=2)
        {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    private int[] getPoint(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }


    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }

    /**
     * 去除该方法，则变成点击获取验证码；
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        if (velocityTracker == null) {
//            velocityTracker = VelocityTracker.obtain();
//        }
//        velocityTracker.addMovement(event);

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

       switch (event.getAction()){
           case MotionEvent.ACTION_DOWN:
               break;
           case MotionEvent.ACTION_MOVE:
               int deltaX = x-mLastX;
               int deltaY =y-mLastY;
               int translationX = (int) (ViewHelper.getTranslationX(this)+deltaX);
               int translationY = (int) (ViewHelper.getTranslationY(this)+deltaY);

               ViewHelper.setTranslationX(this,translationX);
               ViewHelper.setTranslationY(this,translationY);
               break;
           case MotionEvent.ACTION_UP:
               break;
       }
        mLastX = x;
        mLastY = y;

        return true;
    }

}
