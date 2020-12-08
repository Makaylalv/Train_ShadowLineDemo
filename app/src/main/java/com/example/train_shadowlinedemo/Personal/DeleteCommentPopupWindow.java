package com.example.train_shadowlinedemo.Personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.train_shadowlinedemo.R;

public class DeleteCommentPopupWindow extends PopupWindow {
    private View mView;//PopupWindow的菜单布局
    private Context mContext;
    private View.OnClickListener mConfirmListener;//确定按钮的点击监听器
    private View.OnClickListener mCancelListener;//取消按钮的点击监听器

    public DeleteCommentPopupWindow(Activity context, View.OnClickListener mConfirmListener, View.OnClickListener mCancelListener) {
        super(context);
        this.mContext = context;
        this.mConfirmListener = mConfirmListener;
        this.mCancelListener = mCancelListener;
        Init();
    }

    /*
      设置布局以及点击事件
     */
    private void Init(){
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView=inflater.inflate(R.layout.delete_popupwindow,null);
        TextView tvConfirm=mView.findViewById(R.id.tv_deletecommentconfirm);
        TextView tvCancel=mView.findViewById(R.id.tv_deletecommentcancle);
        tvConfirm.setOnClickListener(mConfirmListener);
        tvCancel.setOnClickListener(mCancelListener);
        //导入布局
        this.setContentView(mView);
        this.setAnimationStyle(R.style.popwindow_anim_style);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置可触
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(dw);
        // 单击弹出窗以外处 关闭弹出窗
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mView.findViewById(R.id.ll_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
