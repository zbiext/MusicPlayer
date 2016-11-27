package com.zbie.musicplayer.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * Created by 涛 on 2016/11/22 0022.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.widgets
 * 创建时间         2016/11/22 21:55
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            一种音乐可视化动画工具(随机数据)
 */

public class MusicVisualizer extends View {

    private Random mRandom = new Random();
    private Paint mPaint = new Paint();

    private Runnable mAnimateView = new Runnable() {
        @Override
        public void run() {
            postDelayed(this, 150);
            invalidate();
        }
    };

    public MusicVisualizer(Context context) {
        super(context);
        new MusicVisualizer(context, null);
    }

    public MusicVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 开始mAnimateView
        removeCallbacks(mAnimateView);
        post(mAnimateView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置mPaint的style为Style.FILL，将会填充这个颜色
        // 而设置styles为Style.STROKE，将会stroke这个颜色
        // mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(getDimensionInPixel(0), getHeight() - (20 + mRandom.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(7), getHeight(), mPaint);
        canvas.drawRect(getDimensionInPixel(10), getHeight() - (20 + mRandom.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(17), getHeight(), mPaint);
        canvas.drawRect(getDimensionInPixel(20), getHeight() - (20 + mRandom.nextInt((int) (getHeight() / 1.5f) - 19)), getDimensionInPixel(27), getHeight(), mPaint);
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    /**
     * 为使view适配不同分辨率，先获得每一寸像素值
     *
     * @param dip
     * @return
     */
    private int getDimensionInPixel(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

}
