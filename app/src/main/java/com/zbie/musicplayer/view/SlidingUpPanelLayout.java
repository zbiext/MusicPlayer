package com.zbie.musicplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.zbie.musicplayer.R;

/**
 * Created by 涛 on 2016/11/16 0016.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.view
 * 创建时间         2016/11/16 21:00
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class SlidingUpPanelLayout extends ViewGroup {

    private static final String TAG = "dingUpPanelLayout";

    /** 默认attributes */
    private static final int[] DEFAULT_ATTRS = new int[]{android.R.attr.gravity};

    /** 主视图的默认视差距离 */
    private static final int        DEFAULT_PARALAX_OFFSET      = 0;
    /** 当折叠(面板)时，侧滑面板的默认offset */
    private static final int        DEFAULT_SLIDE_PANEL_OFFSET  = 0;
    /** 默认直接偏移 的标志位 */
    private static final boolean    DEFAULT_DIRECT_OFFSET_FLAG  = false;
    /** fling动作被检测到的最小滑动速度 */
    private static final int        DEFAULT_MIN_FLING_VELOCITY  = 400; // 每秒的dips
    /** 如果没给出淡出颜色，默认设置为80%灰色 */
    private static final int        DEFAULT_FADE_COLOR          = 0x99000000;
    /** 是否 抓住 拖动view的点击状态 */
    private static final boolean    DEFAULT_DRAG_VIEW_CLICKABLE = true;
    /** 默认设置为false，因为这就是它是如何写的 */
    private static final boolean    DEFAULT_OVERLAY_FLAG        = false;
    /** 默认固定点的高度 */
    private static final float      DEFAULT_ANCHOR_POINT        = 1.0f; // In relative %
    /** 组件初始化默认状态 */
    private static       SlideState DEFAULT_SLIDE_STATE         = SlideState.COLLAPSED;
    /** 探出面板的默认高度 */
    private static final int DEFAULT_PANEL_HEIGHT = 68; // dp;
    /** 探出面板之上的阴影的默认高度 */
    private static final int DEFAULT_SHADOW_HEIGHT = 4; // dp;

    /** 在面板之间画阴影 */
    private final Drawable       mShadowDrawable;
    private final ViewDragHelper mDragHelper;

    /** 折叠面板是否被向上拽拖 标志位 */
    private boolean mIsSlidingUp;

    /** 突出部分的像素高度(px) */
    private int        mPanelHeight       = -1;
    /** 当侧滑面板展开时的宽度(px) */
    private int        mSlidePanelOffset  = 0;
    /** 影子的像素高度(px) */
    private int        mShadowHeight      = -1;
    /** 视差距离 */
    private int        mParallaxOffset    = -1;
    /** 夹主视图给侧滑视图 */
    private boolean    mDirectOffset      = false;
    /** fling动作的最小滑动速度 */
    private int        mMinFlingVelocity  = DEFAULT_MIN_FLING_VELOCITY;
    /** 面板覆盖滑块的淡出颜色 0表示不淡出 */
    private int        mCoveredFadeColor  = DEFAULT_FADE_COLOR;
    /** 如果提供了，面板只能被这个(提供的)view拖拽；否则，整个面板都可以被拖拽 */
    private int        mDragViewResId     = -1;
    /** 点击拖动视图是否可以展开/折叠 */
    private boolean    mDragViewClickable = DEFAULT_DRAG_VIEW_CLICKABLE;
    /** 面板覆盖下面的窗口，而不是把它放在它(窗口)下面 TODO 理解一下 */
    private boolean    mOverlayContent    = DEFAULT_OVERLAY_FLAG;
    private float      mAnchorPoint       = 1.f;
    private SlideState mSlideState        = SlideState.COLLAPSED;
    /** 滑动特性启用/禁用的指示标志 */
    private boolean mIsSlidingEnabled;

    /** 在面板view锁在内部scrolling 或 另外一个条件阻止拖拽(preventing a drag) */
    private boolean mIsUnableToDrag;

    public SlidingUpPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            mShadowDrawable = null;
            mDragHelper = null;
            return;
        }

        if (attrs != null) {
            TypedArray defAttrs = context.obtainStyledAttributes(attrs, DEFAULT_ATTRS);
            if (defAttrs != null) {
                int gravity = defAttrs.getInt(0, Gravity.NO_GRAVITY);
                if (gravity != Gravity.TOP && gravity != Gravity.BOTTOM) {
                    throw new IllegalArgumentException("gravity must be set to either Gravity.TOP or Gravity.BOTTOM");
                }
                mIsSlidingUp = gravity == Gravity.BOTTOM;
            }
            defAttrs.recycle();
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout);
            if (ta != null) {
                mPanelHeight = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_panelHeight, -1);
                mSlidePanelOffset = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_slidePanelOffset, DEFAULT_SLIDE_PANEL_OFFSET);
                mShadowHeight = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_shadowHeight, -1);
                mParallaxOffset = ta.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_paralaxOffset, -1);
                mDirectOffset = ta.getBoolean(R.styleable.SlidingUpPanelLayout_directOffset, DEFAULT_DIRECT_OFFSET_FLAG);

                mMinFlingVelocity = ta.getInt(R.styleable.SlidingUpPanelLayout_flingVelocity, DEFAULT_MIN_FLING_VELOCITY);
                mCoveredFadeColor = ta.getColor(R.styleable.SlidingUpPanelLayout_fadeColor, DEFAULT_FADE_COLOR);

                mDragViewResId = ta.getResourceId(R.styleable.SlidingUpPanelLayout_dragView, -1);
                mDragViewClickable = ta.getBoolean(R.styleable.SlidingUpPanelLayout_dragViewClickable, DEFAULT_DRAG_VIEW_CLICKABLE);

                mOverlayContent = ta.getBoolean(R.styleable.SlidingUpPanelLayout_overlay, DEFAULT_OVERLAY_FLAG);

                mAnchorPoint = ta.getFloat(R.styleable.SlidingUpPanelLayout_anchorPoint, DEFAULT_ANCHOR_POINT);

                mSlideState = SlideState.values()[ta.getInt(R.styleable.SlidingUpPanelLayout_initialState, DEFAULT_SLIDE_STATE.ordinal())];
            }

            ta.recycle();
        }

        final float density = context.getResources().getDisplayMetrics().density;
        if (mPanelHeight == -1) {
            mPanelHeight = (int) (DEFAULT_PANEL_HEIGHT * density + 0.5f);
        }
        if (mShadowHeight == -1) {
            mShadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);
        }
        if (mParallaxOffset == -1) {
            mParallaxOffset = (int) (DEFAULT_PARALAX_OFFSET * density);
        }
        // If the shadow height is zero, don't show the shadow
        if (mShadowHeight > 0) {
            if (mIsSlidingUp) {
                mShadowDrawable = ContextCompat.getDrawable(context, R.drawable.above_shadow);
            } else {
                mShadowDrawable = ContextCompat.getDrawable(context, R.drawable.below_shadow);
            }
        } else {
            mShadowDrawable = null;
        }
        setWillNotDraw(false);

        mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        mDragHelper.setMinVelocity(mMinFlingVelocity * density);
        mIsSlidingEnabled = true;


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /** 侧滑view的当前状态 */
    private enum SlideState {
        EXPANDED, // 展开状态
        COLLAPSED, // 折叠状态
        ANCHORED, // 固定状态
        HIDDEN, // 隐藏状态
        DRAGGING // 拖动状态
    }

    // TODO: 2016/11/16 0016 好多地方待完善
    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (mIsUnableToDrag) {
                return false;
            }
            return child == mSlideableView;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                mSlideOffset = computeSlideOffset(mSlideableView.getTop());

                if (mSlideOffset == 1) {
                    if (mSlideState != SlideState.EXPANDED) {
                        updateObscuredViewVisibility();
                        mSlideState = SlideState.EXPANDED;
                        dispatchOnPanelExpanded(mSlideableView);
                    }
                } else if (mSlideOffset == 0) {
                    if (mSlideState != SlideState.COLLAPSED) {
                        mSlideState = SlideState.COLLAPSED;
                        dispatchOnPanelCollapsed(mSlideableView);
                    }
                } else if (mSlideOffset < 0) {
                    mSlideState = SlideState.HIDDEN;
                    mSlideableView.setVisibility(View.GONE);
                    dispatchOnPanelHidden(mSlideableView);
                } else if (mSlideState != SlideState.ANCHORED) {
                    updateObscuredViewVisibility();
                    mSlideState = SlideState.ANCHORED;
                    dispatchOnPanelAnchored(mSlideableView);
                }
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            setAllChildrenVisible();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(top);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int target = 0;

            // direction is always positive if we are sliding in the expanded direction
            float direction = mIsSlidingUp ? -yvel : yvel;

            if (direction > 0) {
                // swipe up -> expand
                target = computePanelTopPosition(1.0f);
            } else if (direction < 0) {
                // swipe down -> collapse
                target = computePanelTopPosition(0.0f);
            } else if (mAnchorPoint != 1 && mSlideOffset >= (1.f + mAnchorPoint) / 2) {
                // zero velocity, and far enough from anchor point => expand to the top
                target = computePanelTopPosition(1.0f);
            } else if (mAnchorPoint == 1 && mSlideOffset >= 0.5f) {
                // zero velocity, and far enough from anchor point => expand to the top
                target = computePanelTopPosition(1.0f);
            } else if (mAnchorPoint != 1 && mSlideOffset >= mAnchorPoint) {
                target = computePanelTopPosition(mAnchorPoint);
            } else if (mAnchorPoint != 1 && mSlideOffset >= mAnchorPoint / 2) {
                target = computePanelTopPosition(mAnchorPoint);
            } else {
                // settle at the bottom
                target = computePanelTopPosition(0.0f);
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), target);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mSlideRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int collapsedTop = computePanelTopPosition(0.f);
            final int expandedTop = computePanelTopPosition(1.0f);
            if (mIsSlidingUp) {
                return Math.min(Math.max(top, expandedTop), collapsedTop);
            } else {
                return Math.min(Math.max(top, collapsedTop), expandedTop);
            }
        }
    }

}
