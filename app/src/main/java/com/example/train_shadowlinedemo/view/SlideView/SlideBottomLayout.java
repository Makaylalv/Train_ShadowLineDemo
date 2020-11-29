package com.example.train_shadowlinedemo.view.SlideView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.train_shadowlinedemo.R;


/**
 * Created by QingMei on 2017/8/25.
 * desc:
 */
public class SlideBottomLayout extends LinearLayout {
    private LinearLayout linearLayoutBottom;
    private LinearLayout linearLayoutTop;
    private AlphaAnimation appearAnimation;
    private AlphaAnimation disappearAnimationBottom;
    private AlphaAnimation disappearAnimationTop;
    private boolean moving;

    public LinearLayout getLinearLayoutBottom() {
        return linearLayoutBottom;
    }

    public void setLinearLayoutBottom(LinearLayout linearLayoutBottom) {
        this.linearLayoutBottom = linearLayoutBottom;
        linearLayoutBottom.setVisibility(View.VISIBLE);
        appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(1000);


        disappearAnimationBottom = new AlphaAnimation(1, 0);
        disappearAnimationBottom.setDuration(1000);
        disappearAnimationBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(isStopAnimation==true||moving==true) {
                    linearLayoutBottom.clearAnimation();
                    linearLayoutTop.clearAnimation();
                    isStopAnimation = false;
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayoutBottom.setVisibility(View.GONE);
                linearLayoutTop.startAnimation(appearAnimation);
                linearLayoutTop.setVisibility(View.VISIBLE);
                moving=false;
            }
        });

        disappearAnimationTop = new AlphaAnimation(1, 0);
        disappearAnimationTop.setDuration(1000);
        disappearAnimationTop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(isStopAnimation==true||moving==true) {
                    linearLayoutBottom.clearAnimation();
                    linearLayoutTop.clearAnimation();
                    isStopAnimation = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayoutTop.setVisibility(View.GONE);
                linearLayoutBottom.startAnimation(appearAnimation);
                linearLayoutBottom.setVisibility(View.VISIBLE);
                moving=false;

            }
        });
    }

    public LinearLayout getLinearLayoutTop() {
        return linearLayoutTop;
    }

    public void setLinearLayoutTop(LinearLayout linearLayoutTop) {
        this.linearLayoutTop = linearLayoutTop;
        linearLayoutTop.setVisibility(View.GONE);
    }

    public void setShortSlideListener(ShortSlideListener listener) {
        this.shortSlideListener = listener;
    }

    private ShortSlideListener shortSlideListener;

    /**
     * The {@link MotionEvent#ACTION_DOWN} gesture location.
     */
    private int downY;

    /**
     * The {@link MotionEvent#ACTION_MOVE} gesture location.
     */
    private int moveY;

    /**
     * the value of moved distance by the gesture. When the value was modified and not exceed
     * the {@link #movedMaxDis}, then make this ViewGroup move.
     */
    private int movedDis;

    /**
     * The max distance that the {@link com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout} can scroll to, it used to compare with the
     * {@link #downY}, determine whether it can slide by the gesture.
     */
    private int movedMaxDis;

    /**
     * ChildView of the {@link com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout}, you can set a Layout such as the {@link LinearLayout}、
     * {@link android.widget.RelativeLayout} ect.
     * We set the rules that {@link com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout} just can have one child-view, or else get a
     * {@link RuntimeException} at {@link #onFinishInflate()}
     */
    private View childView;

    /**
     * The control {@link com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout} automatically switches the threshold of the state. if
     * this ViewGroup moved distance more than {@link #movedMaxDis} * it, switch the state of
     * {@link #arriveTop} right now.
     * </p>
     * See the {@link #touchActionUp(float)}.
     */
    private float hideWeight = 0.25f;

    public void setHideWeight(float hideWeight) {
        if (hideWeight <= 0 || hideWeight > 1)
            throw new IllegalArgumentException("hideWeight should belong (0f,1f]");
        this.hideWeight = hideWeight;
    }

    private Scroller mScroller;

    /**
     * It means the {@link #childView} is arriving the top of parent or else.
     */
    private boolean arriveTop = false;

    /**
     * the {@link #childView} Initially visible height
     */
    private float visibilityHeight;

    public void setVisibilityHeight(float visibilityHeight) {
        this.visibilityHeight = visibilityHeight;
    }

    public SlideBottomLayout(@NonNull Context context) {
        super(context);
    }

    public SlideBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public SlideBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * Get the config from {@link R.styleable}, then init other configrations{@link #initConfig(Context)}.
     *
     * @param context the {@link Context}
     * @param attrs   the configs in layout attrs.
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideBottomLayout);
        visibilityHeight = ta.getDimension(R.styleable.SlideBottomLayout_handler_height, 0);
        ta.recycle();

        initConfig(context);
    }

    private void initConfig(Context context) {
        if (mScroller == null)
            mScroller = new Scroller(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * It start a judgement for ensure the child-view  be unique in this method，then assgin it
     * to {{@link #childView}.
     * this method will be called before the {@link #onMeasure(int, int)}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0 || getChildAt(0) == null) {
            throw new RuntimeException("there have no child-View in the SlideBottomLayout！");
        }
        if (getChildCount() > 1) {
            throw new RuntimeException("there just alow one child-View in the SlideBottomLayout!");
        }
        childView = getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        movedMaxDis = (int) (childView.getMeasuredHeight() - visibilityHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        childView.layout(0, movedMaxDis, childView.getMeasuredWidth(), childView.getMeasuredHeight() + movedMaxDis);
    }
    private int times=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float dy = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchActionDown(dy))
                    return true;

                break;
            case MotionEvent.ACTION_MOVE:
                if(arriveTop==true){
                    if(times==0){
                        moving=true;
                        TopToBottom();
                        times=1;
                    }
                }else {
                    if(times==0){
                        moving=true;
                        bottomToTop();
                        times=1;
                    }
                }
                if (touchActionMove(dy))
                    return true;
                break;
            case MotionEvent.ACTION_UP:
                times=0;
                if (touchActionUp(dy))
                    return true;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller == null)
            mScroller = new Scroller(getContext());
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * When the touch event is {@link MotionEvent#ACTION_UP},
     * then judge the state of view group and control the {@link Scroller} to scroll.
     * <p>
     * In this ViewGroup, we set the rules that is if this scroll gesture's move distance
     * more than {@link #movedMaxDis} * {@link #hideWeight}(default hideWeight value is 1/4 heights
     * of this ViewGroup), then call {@link #hide()} or {@link #show()} right now. which method will
     * be call depends on {@link #arriveTop}.
     * <p
     * if the scroll gesture's move distance don't reach the goal value, then call the
     * {@link ShortSlideListener#onShortSlide(float)} if you call {@link #setShortSlideListener(ShortSlideListener)}
     * init this ViewGroup. else will call {@link #hide()}.
     *
     * @param eventY The location of trigger
     * @return Be used to determine consume this event or else.
     */
    private boolean isStopAnimation;
    public boolean touchActionUp(float eventY) {
        if (movedDis > movedMaxDis * hideWeight) {
            switchVisible();
        } else {
            if (shortSlideListener != null) {
                isStopAnimation=true;
                linearLayoutBottom.clearAnimation();
                linearLayoutTop.clearAnimation();
                linearLayoutBottom.setVisibility(View.VISIBLE);
                linearLayoutTop.setVisibility(View.GONE);
                shortSlideListener.onShortSlide(eventY);
            } else {
                linearLayoutBottom.clearAnimation();
                linearLayoutTop.clearAnimation();
                linearLayoutBottom.setVisibility(View.VISIBLE);
                linearLayoutTop.setVisibility(View.GONE);
                isStopAnimation=true;
                hide();
            }
        }
        return true;
    }

    /**
     * When the touch event is {@link MotionEvent#ACTION_MOVE},
     * then judge the state of view group and control the {@link Scroller} to scroll.
     * <p>
     * In this ViewGroup, we set the rules that is if this scroll gesture's move distance
     * more than {@link #movedMaxDis} * {@link #hideWeight}(default hideWeight value is 1/4 heights of this ViewGroup),
     * then call {@link #hide()} or {@link #show()} right now.
     * <p>
     *
     * @param eventY The location of trigger
     * @return Be used to determine consume this event or else.
     */

    public boolean touchActionMove(float eventY) {

        moveY = (int) eventY;

        //the dy is sum of the move distance,  the value > 0 means scroll up, the value < 0 means scroll down.
        final int dy = downY - moveY;

        if (dy > 0) {               //scroll up
            movedDis += dy;
            if (movedDis > movedMaxDis)
                movedDis = movedMaxDis;

            if (movedDis < movedMaxDis) {
                scrollBy(0, dy);
                downY = moveY;
                return true;
            }
        } else {                //scroll down
            movedDis += dy;
            if (movedDis < 0) movedDis = 0;
            if (movedDis > 0) {
                scrollBy(0, dy);
            }
            downY = moveY;
            return true;
        }
        return false;
    }

    /**
     * When the touch event is {@link MotionEvent#ACTION_DOWN},
     * Record the location of this action.
     *
     * @param eventY The location of trigger
     * @return Be used to determine consume this event or else.
     */
    public boolean touchActionDown(float eventY) {
        downY = (int) eventY;

        //Whether custom this gesture.
        if (!arriveTop && downY < movedMaxDis) {
            return false;
        } else
            return true;
    }

    /**
     * the extand method for showing {@link com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout}
     */
    public void show() {
//        linearLayoutBottom.clearAnimation();
//        linearLayoutTop.clearAnimation();
//        linearLayoutBottom.setVisibility(View.GONE);
//        linearLayoutTop.setVisibility(View.VISIBLE);
        scroll2TopImmediate();
    }

    /**
     * the extand method for hiding {@link com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout}
     */
    public void hide() {

        scroll2BottomImmediate();
    }

    /**
     * @return The ViewGroup is arrive top or else.
     */
    public boolean switchVisible() {
        if (arriveTop())
            hide();
        else
            show();
        return arriveTop();
    }

    public boolean arriveTop() {
        return this.arriveTop;
    }

    public void scroll2TopImmediate() {
        mScroller.startScroll(0, getScrollY(), 0, (movedMaxDis - getScrollY()));
        invalidate();
        movedDis = movedMaxDis;
        arriveTop = true;
    }

    public void scroll2BottomImmediate() {

        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
        postInvalidate();
        movedDis = 0;
        arriveTop = false;
    }


    private void TopToBottom() {
        linearLayoutTop.startAnimation(disappearAnimationTop);
    }

    private void bottomToTop() {
        linearLayoutBottom.startAnimation(disappearAnimationBottom);
    }
}

