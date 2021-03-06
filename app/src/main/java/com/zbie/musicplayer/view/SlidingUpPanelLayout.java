package com.zbie.musicplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

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
    private static final int        DEFAULT_PANEL_HEIGHT        = 68; // dp;
    /** 探出面板之上的阴影的默认高度 */
    private static final int        DEFAULT_SHADOW_HEIGHT       = 4; // dp;

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
    /** 视差(偏移)距离 */
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
    /**
     * Stores whether or not the pane was expanded the last time it was slideable.
     * If expand/collapse operations are invoked this state is modified. Used by
     * instance state save/restore.
     */
    private boolean mFirstLayout = true;
    /**
     * The child view that can slide, if any.
     */
    private View  mSlideableView;
    /**
     * The main view
     */
    private View mMainView;
    /**
     * How far the panel is offset from its expanded position.
     * range [0, 1] where 0 = collapsed, 1 = expanded.
     */
    private float mSlideOffset;
    private PanelSlideListener mPanelSlideListener;
    /**
     * How far in pixels the slideable panel may move.
     */
    private int mSlideRange;

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

    /**
     * 设置panelSlideListener
     *
     * @param listener
     */
    public void setPanelSlideListener(PanelSlideListener listener) {
        if (listener != null) {
            mPanelSlideListener =listener;
        } else {
            throw  new IllegalArgumentException("PanelSlideListener isn't allowed to be null!");
        }
    }

    /**
     * 检查侧滑面板视图是否全部展开
     *
     * @return 如果侧滑慢板完全展开返回ture
     */
    public boolean isPanelExpanded() {
        return mSlideState == SlideState.EXPANDED;
    }

    /**
     * Collapse the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now collapsed/in the process of collapsing
     */
    public boolean collapsePanel() {
        if (mFirstLayout) {
            mSlideState = SlideState.COLLAPSED;
            return true;
        } else {
            if (mSlideState == SlideState.HIDDEN || mSlideState == SlideState.COLLAPSED)
                return false;
            return collapsePanel(mSlideableView, 0);
        }
    }

    private boolean collapsePanel(View pane, int initialVelocity) {
        return mFirstLayout || smoothSlideTo(0.0f, initialVelocity);
    }

    /**
     * Smoothly animate mDraggingPane to the target X position within its range.
     *
     * @param slideOffset position to animate to
     * @param velocity    initial velocity in case of fling, or 0.
     */
    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!isSlidingEnabled()) {
            // Nothing to do.
            return false;
        }
        int panelTop = computePanelTopPosition(slideOffset);
        if (mDragHelper.smoothSlideViewTo(mSlideableView, mSlideableView.getLeft(), panelTop)) {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }

    public boolean isSlidingEnabled() {
        return mIsSlidingEnabled && mSlideableView != null;
    }

    private int computePanelTopPosition(float slideOffset) {
        return 0;
    }

    /**
     * Computes the slide offset based on the top position of the panel
     */
    private float computeSlideOffset(int topPosition) {
        // Compute the panel top position if the panel is collapsed (offset 0)
        final int topBoundCollapsed = computePanelTopPosition(0);

        // Determine the new slide offset based on the collapsed top position and the new required
        // top position
        return (mIsSlidingUp ? (float) (topBoundCollapsed - topPosition) / mSlideRange : (float) (topPosition - topBoundCollapsed) / mSlideRange);
    }

    private static boolean hasOpaqueBackground(View v) {
        final Drawable bg = v.getBackground();
        return bg != null && bg.getOpacity() == PixelFormat.OPAQUE;
    }

    private void updateObscuredViewVisibility() {
        if (getChildCount() == 0) {
            return;
        }
        final int leftBound   = getPaddingLeft();
        final int rightBound  = getWidth() - getPaddingRight();
        final int topBound    = getPaddingTop();
        final int bottomBound = getHeight() - getPaddingBottom();
        final int left;
        final int right;
        final int top;
        final int bottom;
        if (mSlideableView != null && hasOpaqueBackground(mSlideableView)) {
            left = mSlideableView.getLeft();
            right = mSlideableView.getRight();
            top = mSlideableView.getTop();
            bottom = mSlideableView.getBottom();
        } else {
            left = right = top = bottom = 0;
        }
        View      child              = mMainView;
        final int clampedChildLeft   = Math.max(leftBound, child.getLeft());
        final int clampedChildTop    = Math.max(topBound, child.getTop());
        final int clampedChildRight  = Math.min(rightBound, child.getRight());
        final int clampedChildBottom = Math.min(bottomBound, child.getBottom());
        final int vis;
        if (clampedChildLeft >= left && clampedChildTop >= top &&
                clampedChildRight <= right && clampedChildBottom <= bottom) {
            vis = INVISIBLE;
        } else {
            vis = VISIBLE;
        }
        child.setVisibility(vis);
    }

    private void dispatchOnPanelExpanded(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelExpanded(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    private void dispatchOnPanelCollapsed(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelCollapsed(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    private void dispatchOnPanelHidden(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelHidden(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    private void dispatchOnPanelAnchored(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelAnchored(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    private void dispatchOnPanelSlide(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
        }
    }

    /**
     * 获得当前视差的偏移
     *
     * @return 当前视差的偏移
     */
    public int getCurrentParalaxOffset() {
        if (mParallaxOffset < 0) {
            return 0;
        }
        return (int) (mParallaxOffset * getDirectionalSlideOffset());
    }

    /**
     * get The directional slide offset
     *
     * @return The directional slide offset
     */
    protected float getDirectionalSlideOffset() {
        return mIsSlidingUp ? -mSlideOffset : mSlideOffset;
    }

    @SuppressLint("NewApi")
    private void onPanelDragged(int newTop) {
        mSlideState = SlideState.DRAGGING;
        // Recompute the slide offset based on the new top position
        mSlideOffset = computeSlideOffset(newTop);
        // Update the parallax based on the new slide offset
        if ((mParallaxOffset > 0 || mDirectOffset) && mSlideOffset >= 0) {
            int mainViewOffset = 0;
            if (mParallaxOffset > 0) {
                mainViewOffset = getCurrentParalaxOffset();
            } else {
                mainViewOffset = (int) (getDirectionalSlideOffset() * mSlideRange);
            }
            mMainView.setTranslationY(mainViewOffset);
        }
        // Dispatch the slide event
        dispatchOnPanelSlide(mSlideableView);
        // If the slide offset is negative, and overlay is not on, we need to increase the
        // height of the main content
        if (mSlideOffset <= 0 && !mOverlayContent) {
            // expand the main view
            LayoutParams lp = (LayoutParams) mMainView.getLayoutParams();
            lp.height = mIsSlidingUp ? (newTop - getPaddingBottom()) : (getHeight() - getPaddingBottom() - mSlideableView.getMeasuredHeight() - newTop);
            mMainView.requestLayout();
        }
    }

    /** 侧滑view的当前状态 */
    private enum SlideState {
        EXPANDED, // 展开状态
        COLLAPSED, // 折叠状态
        ANCHORED, // 固定状态
        HIDDEN, // 隐藏状态
        DRAGGING // 拖动状态
    }

    /** 监听侧滑面板的事件 */
    public interface PanelSlideListener {

        /**
         * 当侧滑面板的位置变化
         *
         * @param panel       被移动的子视图
         * @param slideOffset The new offset of this sliding pane within its range，取值在0-1之间
         */
        void onPanelSlide(View panel, float slideOffset);

        /**
         * 当侧滑面板完全被折叠
         *
         * @param panel 子视图滑到折叠的位置
         */
        void onPanelCollapsed(View panel);

        /**
         * 当侧滑面板完全被展开
         *
         * @param panel 子视图滑到展开的位置
         */
        void onPanelExpanded(View panel);

        /**
         * 当侧滑面板完全被固定
         *
         * @param panel 子视图滑到固定的位置
         */
        void onPanelAnchored(View panel);

        /**
         * 当侧滑面板完全被隐藏
         *
         * @param panel 子视图滑到隐藏的位置
         */
        void onPanelHidden(View panel);
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
            final int expandedTop  = computePanelTopPosition(1.0f);
            if (mIsSlidingUp) {
                return Math.min(Math.max(top, expandedTop), collapsedTop);
            } else {
                return Math.min(Math.max(top, collapsedTop), expandedTop);
            }
        }
    }

}
