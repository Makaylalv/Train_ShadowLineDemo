package com.example.train_shadowlinedemo.MovieShow.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

public class ImageBarnnerViewGroup extends ViewGroup {
    private int children;//我们View Group的子视图总个数
    private int childwidth;//子视图的宽度
    private int childheight;//子视图的高度

    private int x;//此时的x的值代表的是第一次按下的位置的横坐标，每一次移动过的过程中 移动之前的位置横坐标
    private int index = 0;//代表名为每张图片的索引
    private Scroller scroller;

    /**
     * 利用一个单击变量开关进行判断，离开屏幕的一瞬间判断用户的操作是点击
     */
    private boolean isClick;//true的时候点击事件，false的时候不是点击事件
    private ImageBarnnerLister lister;

    private ImageBarnnerViewGroupLisnner barnnerViewGroupLisnner;

    public ImageBarnnerLister getLister() {
        return lister;
    }

    public void setLister(ImageBarnnerLister lister) {
        this.lister = lister;
    }

    public ImageBarnnerViewGroupLisnner getBarnnerViewGroupLisnner() {
        return barnnerViewGroupLisnner;
    }
    public void setBarnnerViewGroupLisnner(ImageBarnnerViewGroupLisnner barnnerViewGroupLisnner) {
        this.barnnerViewGroupLisnner = barnnerViewGroupLisnner;
    }
    public interface ImageBarnnerLister {
        void chickImageIndex(int pos);//pos代表的是我们当前的图片的具体索引值
    }
    /**
     * 实现轮播图底部圆点切换效果
     * 自定义一个继承自FragmenLayou布局，利用FragmeLayout布局特性
     */

    //自动轮播
    private boolean isAuto = true;//默认情况下开启轮播
    private Timer timer = new Timer();
    private TimerTask timerTask;

    @SuppressLint("HandlerLeak")
    private android.os.Handler autohandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://我们需要图片的自动轮播
                    if (++index >= children) {//如果是最后一张图片，从第一张开始
                        index = 0;
                    }
                    scrollTo(childwidth * index,0);
                    barnnerViewGroupLisnner.selectImage(index);
                    break;
                default:
            }
        }
    };

    private void startAuto() {
        isAuto = true;

    }

    private void stopAuto() {
        isAuto = false;
    }

    /**
     * 采用Timer，TimerTask，Handler三者结合的方式来实现自动轮播
     */

    public ImageBarnnerViewGroup(Context context) {
        super(context);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageBarnnerViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initObj();
    }


    private void initObj() {
        scroller = new Scroller(getContext());

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isAuto) {//开启轮播图
                    autohandler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(timerTask,100,3000);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),0);
            invalidate();//重绘
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1.求出子视图的个数.6+
        children = getChildCount();//我们可以知道自试图的个数
        if (0 == children)
        {
            setMeasuredDimension(0,0);
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //此时我们以第一个子视图=为基准，也就是说我们的View Group
            View view = getChildAt(0);
            childwidth = view.getMeasuredWidth();
            childheight = view.getMeasuredHeight();
            int width = view.getMeasuredWidth() * children;
            setMeasuredDimension(width,childheight);
        }
        //2.测量子视图的宽度和高度
        //3.根据子视图的狂赌和高度，求出该ViewGroup的宽度和高度
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
    /**
     * 用两种方式来实现轮播图的手动轮播
     * 1，利用scrollTo，scrollBy 完成轮播图的手动轮播
     * 1，利用Scroller 对象完成轮播图的手动效果
     * @param event
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://表示用户按下的一瞬间
                stopAuto();//停止图片轮播
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                isClick = true;//isClick 标识，是不是点击事件
                x=(int)event.getX();
                break;
            case MotionEvent.ACTION_MOVE://表示用户按下之后在屏幕上移动的过程
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance,0);
                x = moveX;
                isClick = false;
                break;
            case MotionEvent.ACTION_UP://标识的是用户抬起的一瞬间
                int scrollX = getScrollX();
                index = (scrollX + childwidth / 2) / childwidth;
                if (index < 0) {    //已经滑动到了最左边
                    index = 0;
                } else if (index > children - 1) {//说明已经滑动到了最右边
                    index = children - 1;
                }

                if (isClick) {  //点击事件
                    lister.chickImageIndex(index);
                } else {

                    int dx = index * childwidth - scrollX;
                    scroller.startScroll(scrollX,0,dx,0);
                    postInvalidate();
                    barnnerViewGroupLisnner.selectImage(index);
                }
                startAuto();//开启图片轮播
                break;
            default:
        }
        return true;
        //返回true的目的是告诉该View Group容器的父View 我们已经处理好了该事件
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (b) {
            int lefrMargin = 0;
            for (int j = 0; j < children; j++) {
                View view = getChildAt(j);
                view.layout(lefrMargin,0,lefrMargin + childwidth,childheight);
                lefrMargin += childwidth;
            }
        }
    }

    public interface ImageBarnnerViewGroupLisnner{
        void selectImage(int index);
    }

}
