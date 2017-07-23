package com.bye.hey.coordinator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by master on 22/7/17.
 */

public class CustomLayout extends FrameLayout {
    private static final String TAG = CustomLayout.class.getName();
    private ScrollView scrollview;
    private float yPrec;
    private int mTouchSlop;
    private boolean mIsScrolling;
    private float dX, dY;
    private float currentY;
    private boolean isAnimating;
    private LinearLayout backview;

    public CustomLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yPrec = ev.getY();
                dY = scrollview.getY() - ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float dy = ev.getY() - yPrec;
                if (dy < mTouchSlop) {
                    Log.d(TAG, "onInterceptTouchEvent: scrolling down");
                } else {
                    Log.d(TAG, "onInterceptTouchEvent: scrolling up");
                    if (scrollview.getScrollY() == 0) {
                        Log.d(TAG, "onInterceptTouchEvent: scrollview is at the top");
                        return true;
                    }
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: called");


        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: action_move");
                dragView(event);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: action_up");
                reboundView();
                break;
        }
        return super.onTouchEvent(event);

    }

    private void reboundView() {
        Log.d(TAG, "reboundView: " + scrollview.getY());
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(scrollview.getY(), 0);
        valueAnimator.setDuration(450);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                backview.setScaleX(0.7f);
                backview.setScaleY(0.7f);
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
                super.onAnimationStart(animation);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                scrollview.setY((Float) valueAnimator.getAnimatedValue());
                float a = (float) valueAnimator.getAnimatedValue();
                backview.setScaleX((float) (((0.3 / 400.0) * (a)) + 0.7));
                backview.setScaleY((float) (((0.3 / 400.0) * (a)) + 0.7));
            }
        });
        valueAnimator.start();
    }

    private void dragView(MotionEvent event) {
        if ((event.getRawY() + dY) < 400)
            scrollview.setY(event.getRawY() + dY);

        if ((event.getRawY() + dY) > 0 && (event.getRawY() + dY) < 400) {
            backview.setScaleX((float) (((0.3 / 400.0) * (event.getRawY() + dY)) + 0.7));
            backview.setScaleY((float) (((0.3 / 400.0) * (event.getRawY() + dY)) + 0.7));
        }

    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getId() == R.id.sss) {
            scrollview = ((ScrollView) child);

        } else if (child.getId() == R.id.aa) {
            backview = ((LinearLayout) child);
        }
    }


}
