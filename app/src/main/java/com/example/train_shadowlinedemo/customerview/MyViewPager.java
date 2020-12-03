package com.example.train_shadowlinedemo.customerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    public static final int STATE_DOWN = MotionEvent.ACTION_DOWN;
    public static final int STATE_UP = MotionEvent.ACTION_UP;
    public static final int STATE_MOVE = MotionEvent.ACTION_MOVE;

    int state = STATE_UP;

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
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
