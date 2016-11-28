package com.zbie.musicplayer.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 涛 on 2016/11/27 0027.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.widgets
 * 创建时间         2016/11/27 19:08
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            播放/暂停按钮
 */

public class PlayPauseButton extends View {

    /**
     * 根号3的值
     */
    private final static double SQRT_3 = Math.sqrt(3);
    /**
     * Animation的播放速率
     */
    private final static int    SPEED  = 1;
    /**
     * 坐标的标准点
     * 中心点(0,0)
     * 右上(1,1) 右下(1,-1) 左上(-1,1) 左下(-1,-1)
     */
    private Point         mPoint;
    /**
     * 画笔
     */
    private Paint         mPaint;
    /**
     * 左半边的Path
     */
    private Path          mLeftPath;
    /**
     * 右半边的Path
     */
    private Path          mRightPath;
    /**
     * 中间的Animator
     */
    private ValueAnimator mCenterEdgeAnimator;
    /**
     * 最左边的Animator
     */
    private ValueAnimator mLeftEdgeAnimator;
    /**
     * 最右边的Animator
     */
    private ValueAnimator mRightEdgeAnimator;
    /**
     * 按钮背景色
     */
    private int mBackgroundColor = Color.BLACK;
    /**
     * 按钮的状态
     */
    private boolean mPlayed;
    /**
     * Animator的更新监听器
     */
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }
    };

    private OnControlStatusChangeListener mListener;

    public PlayPauseButton(Context context) {
        this(context, null, 0);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPoint = new Point();
        initView();
    }

    /**
     * view的初始化
     */
    private void initView() {
        setUpPaint();
        setUpPath();
        setUpAnimator();
    }

    /**
     * Paint的初始化
     */
    private void setUpPaint() {
        mPaint = new Paint();
        mPaint.setColor(mBackgroundColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Path的初始化
     */
    private void setUpPath() {
        mLeftPath = new Path();
        mRightPath = new Path();
    }

    /**
     * Animator
     * 各种Animator的初始化，并开始动画
     */
    private void setUpAnimator() {
        if (mPlayed) {
            mCenterEdgeAnimator = ValueAnimator.ofFloat(1.f, 1.f);
            mLeftEdgeAnimator = ValueAnimator.ofFloat((float) (-0.2f * SQRT_3), (float) (-0.2f * SQRT_3));
            mRightEdgeAnimator = ValueAnimator.ofFloat(1.f, 1.f);
        } else {
            mCenterEdgeAnimator = ValueAnimator.ofFloat(0.5f, 0.5f);
            mLeftEdgeAnimator = ValueAnimator.ofFloat(0.f, 0.f);
            mRightEdgeAnimator = ValueAnimator.ofFloat(0.f, 0.f);
        }

        mCenterEdgeAnimator.start();
        mLeftEdgeAnimator.start();
        mRightEdgeAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        mPoint.setHeight(canvas.getHeight());
        mPoint.setWidth(canvas.getWidth());

        mLeftPath.reset();
        mRightPath.reset();

        // 设置左半边的Path
        mLeftPath.moveTo(mPoint.getX(-0.5 * SQRT_3), mPoint.getY(1.f));
        mLeftPath.lineTo(
                mPoint.getY((Float) mLeftEdgeAnimator.getAnimatedValue()) + 0.7f,
                mPoint.getY((Float) mCenterEdgeAnimator.getAnimatedValue())
                        );
        mLeftPath.lineTo(
                mPoint.getY((Float) mLeftEdgeAnimator.getAnimatedValue()) + 0.7f,
                mPoint.getY(-1 * (Float) mCenterEdgeAnimator.getAnimatedValue())
                        );
        mLeftPath.lineTo(mPoint.getX(-0.5 * SQRT_3), mPoint.getY(-1.f));

        // 设置右半边的Path
        mRightPath.moveTo(
                mPoint.getY(-1 * (Float) mLeftEdgeAnimator.getAnimatedValue()),
                mPoint.getY((Float) mCenterEdgeAnimator.getAnimatedValue())
                         );
        mRightPath.lineTo(
                mPoint.getX(0.5 * SQRT_3),
                mPoint.getY((Float) mRightEdgeAnimator.getAnimatedValue())
                         );
        mRightPath.lineTo(
                mPoint.getX(0.5 * SQRT_3),
                mPoint.getY(-1 * (Float) mRightEdgeAnimator.getAnimatedValue())
                         );
        mRightPath.lineTo(
                mPoint.getY(-1 * (Float) mLeftEdgeAnimator.getAnimatedValue()),
                mPoint.getY(-1 * (Float) mCenterEdgeAnimator.getAnimatedValue())
                         );

        canvas.drawPath(mLeftPath, mPaint);
        canvas.drawPath(mRightPath, mPaint);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.played = isPlayed();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setPlayed(savedState.played);
        setUpAnimator();
        invalidate();
    }

    /**
     * 开始播放动画
     */
    public void startAnimation() {
        mCenterEdgeAnimator = ValueAnimator.ofFloat(1.f, 0.5f);
        mCenterEdgeAnimator.setDuration(100 * SPEED);
        mCenterEdgeAnimator.addUpdateListener(mAnimatorUpdateListener);

        mLeftEdgeAnimator = ValueAnimator.ofFloat((float) (-0.2 * SQRT_3), 0.f);
        mLeftEdgeAnimator.setDuration(100 * SPEED);
        mLeftEdgeAnimator.addUpdateListener(mAnimatorUpdateListener);

        mRightEdgeAnimator = ValueAnimator.ofFloat(1.f, 0.f);
        mRightEdgeAnimator.setDuration(150 * SPEED);
        mRightEdgeAnimator.addUpdateListener(mAnimatorUpdateListener);

        if (!mPlayed) {
            mCenterEdgeAnimator.start();
            mLeftEdgeAnimator.start();
            mRightEdgeAnimator.start();
        } else {
            mCenterEdgeAnimator.reverse();
            mLeftEdgeAnimator.reverse();
            mRightEdgeAnimator.reverse();
        }
    }

    public void setOnControlStatusChangeListener(OnControlStatusChangeListener l) {
        if (l != null) {
            mListener = l;
        } else {
            throw new IllegalArgumentException("OnControlStatusChangeListener isn't allowed to ba null!")
                    ;
        }
    }

    public boolean isPlayed() {
        return mPlayed;
    }

    public void setPlayed(boolean played) {
        if (mPlayed != played) {
            mPlayed = played;
            invalidate();
        }
    }

    public void setColor(int color) {
        mBackgroundColor = color;
        mPaint.setColor(mBackgroundColor);
        invalidate();
    }

    @Override
    public void setBackground(Drawable background) {
    }

    public interface OnControlStatusChangeListener {
        void onStatusChange(View view, boolean state);
    }

    /**
     * 画面开启时，保存状态的类
     */
    private static class SavedState extends BaseSavedState {

        public boolean played;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            played = (Boolean) in.readValue(null);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(played);
        }

    }

    /**
     * 坐标的标准点
     * 中心点(0,0)
     * 右上(1,1) 右下(1,-1) 左上(-1,1) 左下(-1,-1)
     */
    private static class Point {
        private int width;
        private int height;

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public float getX(float x) {
            return (width / 2) * (x + 1);
        }

        public float getY(float y) {
            return (height / 2) * (y + 1);
        }

        public float getX(double x) {
            return getX((float) x);
        }

        public float getY(double y) {
            return getY((float) y);
        }
    }

}
