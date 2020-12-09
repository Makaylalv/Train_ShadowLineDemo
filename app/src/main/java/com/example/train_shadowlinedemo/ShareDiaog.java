package com.example.train_shadowlinedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;



public class ShareDiaog implements View.OnClickListener {
    private Context context;
    private AlertDialog alertDialog;

    private LinearLayout ll_share_wechat;
    private LinearLayout ll_share_pyq;
    private LinearLayout ll_share_qq;
    private LinearLayout ll_share_qzone;
    private RelativeLayout rl_menu_cancle;

    public ShareDiaog(Context context) {
        this.context = context;
    }
    public ShareDiaog builder() {
        alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.popwindow_anim_style)).create();
        alertDialog.show();
        Window win = alertDialog.getWindow();
       //win.setWindowAnimations(R.style.mystyle);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        win.setAttributes(lp);
        win.setContentView(R.layout.dialog_share);

        rl_menu_cancle=win.findViewById(R.id.rl_menu_cancle);
        ll_share_wechat=win.findViewById(R.id.ll_share_wechat);
        ll_share_pyq=win.findViewById(R.id.ll_share_pyq);
        ll_share_qq=win.findViewById(R.id.ll_share_qq);
        ll_share_qzone=win.findViewById(R.id.ll_share_qzone);

        rl_menu_cancle.setOnClickListener(this);
        ll_share_wechat.setOnClickListener(this);
        ll_share_pyq.setOnClickListener(this);
        ll_share_qq.setOnClickListener(this);
        ll_share_qzone.setOnClickListener(this);

        return this;
    }
    public void show(){
        alertDialog.show();
    }
    public void cancle(){
        alertDialog.cancel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_menu_cancle:
                cancle();
                break;
            case R.id.ll_share_wechat:
                cancle();
                if(shareClickListener!=null)shareClickListener.shareWechat();
                break;
            case R.id.ll_share_pyq:
                cancle();
                if(shareClickListener!=null)shareClickListener.sharePyq();
                break;
            case R.id.ll_share_qq:
                cancle();
                if(shareClickListener!=null)shareClickListener.shareQQ();
                break;
            case R.id.ll_share_qzone:
                cancle();
                if(shareClickListener!=null)shareClickListener.shareQzone();
                break;
        }
    }
    public ShareClickListener shareClickListener;
    public interface ShareClickListener{
        void shareWechat();
        void sharePyq();
        void shareQQ();
        void shareQzone();
    }
    public void setShareClickListener(ShareClickListener shareClickListener){
        this.shareClickListener=shareClickListener;
    }
}