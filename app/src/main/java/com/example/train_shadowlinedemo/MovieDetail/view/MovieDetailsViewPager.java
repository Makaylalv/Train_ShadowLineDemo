package com.example.train_shadowlinedemo.MovieDetail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class MovieDetailsViewPager extends ViewPager {
    public static final int STATE_DOWN = MotionEvent.ACTION_DOWN;
    public static final int STATE_UP = MotionEvent.ACTION_UP;
    public static final int STATE_MOVE = MotionEvent.ACTION_MOVE;

    int state = STATE_UP;

    public MovieDetailsViewPager(@androidx.annotation.NonNull Context context) {
        super(context);
    }

    public MovieDetailsViewPager(@androidx.annotation.NonNull Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mOnTouchListener != null)
            mOnTouchListener.onTouch(ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    OnTouchListener mOnTouchListener;

    interface OnTouchListener {
        void onTouch(int state);
    }

}
