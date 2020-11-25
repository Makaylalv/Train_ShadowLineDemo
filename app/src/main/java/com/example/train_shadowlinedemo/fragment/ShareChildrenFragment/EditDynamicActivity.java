package com.example.train_shadowlinedemo.fragment.ShareChildrenFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.MainActivity;
import com.example.train_shadowlinedemo.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


import java.util.List;

public class EditDynamicActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE=10;
    private Button btnBackDynamic;
    private ImageView ivAddPicture;
    private DisplayMetrics dm;
    private ImageView ivAddImg1;
    private ImageView ivAddImg2;
    private ImageView ivAddImg3;
    private LinearLayout llUserLocation;
    private Button btnConfirmPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dynamic);
        initView();
        setOnclickListener();
    }
    //初始化布局控件
    public void initView(){
        btnBackDynamic=findViewById(R.id.btn_back_dynamic);
        ivAddPicture=findViewById(R.id.iv_add_picture);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        llUserLocation=findViewById(R.id.ll_user_location);
        ivAddImg1=findViewById(R.id.iv_add_img1);
        ivAddImg2=findViewById(R.id.iv_add_img2);
        ivAddImg3=findViewById(R.id.iv_add_img3);
        btnConfirmPublish=findViewById(R.id.btn_share_confirm_publish);

    }
    //为控件设置点击事件
    private void setOnclickListener(){
        //为返回按钮设置点击事件
        btnBackDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("skipDynamic","skipDynamic");
                intent.setClass(EditDynamicActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //为照片图片设置点击事件
        ivAddPicture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Matisse.from(EditDynamicActivity.this)
                        .choose(MimeType.ofAll())
                        .countable(true)//是否有序
                        .maxSelectable(3)//最大图片数量
                        .gridExpectedSize(dm.widthPixels/3)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.Matisse_Zhihu) //主题
                        .capture(true)
                                .captureStrategy(new CaptureStrategy(true,"com.example.train_shadowlinedemo.SharedFragment.MyFileProvider"))
                        .forResult(REQUEST_CODE_CHOOSE);
                    }
                }
        );
        //为确定发布按钮设置点击事件
        btnConfirmPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到动态页面
                Intent intent=new Intent();
                intent.putExtra("skipDynamic","skipDynamic");
                intent.setClass(EditDynamicActivity.this, MainActivity.class);
                startActivity(intent);
                Toast toast=Toast.makeText(EditDynamicActivity.this,"发布成功",Toast.LENGTH_SHORT);

            }
        });

    }
    protected void setnull(){
        ivAddImg1.setImageDrawable(null);
        ivAddImg2.setImageDrawable(null);
        ivAddImg3.setImageDrawable(null);
    }
    //选择图片后的响应操作

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            List<Uri> selected = Matisse.obtainResult(data);
            if(selected.size() == 1) {
                setnull();
                Glide.with(this).load(selected.get(0)).into(ivAddImg1);
            }
            else if(selected.size() == 2) {
                setnull();
                Glide.with(this).load(selected.get(0)).into(ivAddImg1);
                Glide.with(this).load(selected.get(1)).into(ivAddImg2);
            }
            else if(selected.size() == 3) {
                setnull();
                Glide.with(this).load(selected.get(0)).into(ivAddImg1);
                Glide.with(this).load(selected.get(1)).into(ivAddImg2);
                Glide.with(this).load(selected.get(2)).into(ivAddImg2);
            }
        }
    }
}