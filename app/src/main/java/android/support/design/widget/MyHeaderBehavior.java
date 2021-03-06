package android.support.design.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.spearbothy.transitiondemo.App;
import com.spearbothy.transitiondemo.R;

/**
 * Created by mahao on 17-5-19.
 */

public class MyHeaderBehavior extends ViewOffsetBehavior {

    private OverScroller mOverScroller;

    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;


    private boolean isClose = false;
    private CoordinatorLayout mParent;
    private View mChild;


    public MyHeaderBehavior() {
    }

    public MyHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(App.sApp);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = parent;
        mChild = child;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        // 边界条件
        return (nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL) && canScroll(child, 0) && !isClose;
    }


    private boolean canScroll(View child, float pendingDy) {
        int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
        // 计算是否能够滑动 ，滑动
        if (pendingTranslationY >= getHeaderOffsetRange() && pendingTranslationY <= 0) {
            return true;
        }
        return false;
    }

    public boolean isClose() {
        return isClose;
    }

    public void openPager() {
        isClose = false;
        mChild.animate()
                .translationY(0)
                .setDuration(500)
                .start();
    }


    private int getHeaderOffsetRange() {
        // 头部可偏移的最大范围
        return App.sApp.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        Log.i("info", "onNestedPreFling");
        return true;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        //dy>0 scroll up;dy<0,scroll down 向下滚动  dy<0
        float halfOfDis = dy / 4.0f; //消费掉其中的4分之1，不至于滑动效果太灵敏
        if (!canScroll(child, halfOfDis)) {
            child.setTranslationY(halfOfDis > 0 ? getHeaderOffsetRange() : 0);
        } else {
            child.setTranslationY(child.getTranslationY() - halfOfDis);
        }
        //只要开始拦截，就需要把所有Scroll事件消费掉
        consumed[1] = dy;
    }

/*    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }*/

/*    private void changeState(int newState) {
        if (mCurState != newState) {
            mCurState = newState;
        }
    }*/

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        handleActionUp(coordinatorLayout, child);
    }


    private void handleActionUp(CoordinatorLayout parent, final View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new MyHeaderBehavior.FlingRunnable(parent, child);
        if (child.getTranslationY() < getHeaderOffsetRange() / 3.0f) {
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        } else {
            mFlingRunnable.scrollToOpen(DURATION_SHORT);
        }

    }

    private FlingRunnable mFlingRunnable;

    //结束动画的时候调用，并改变状态
    private void onFlingFinished(CoordinatorLayout coordinatorLayout, View layout) {
        if (layout.getTranslationY() == getHeaderOffsetRange()) {
            isClose = true;
        } else {
            isClose = false;
        }
    }


    /**
     * For animation , Why not use {@link android.view.ViewPropertyAnimator } to play animation is of the
     * other {@link android.support.design.widget.CoordinatorLayout.Behavior} that depend on this could not receiving the correct result of
     * {@link View#getTranslationY()} after animation finished for whatever reason that i don't know
     */
    public class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            float dy = getHeaderOffsetRange() - curTranslationY;
            //这里做了些处理，避免有时候会有1-2Px的误差结果，导致最终效果不好
            mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy + 0.1f), duration);
            start();
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
            start();
        }

        private void start() {
            if (mOverScroller.computeScrollOffset()) {
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
                // 惯性滚动完成
                onFlingFinished(mParent, mLayout);
            }
        }

        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    ViewCompat.setTranslationY(mLayout, mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }
}
