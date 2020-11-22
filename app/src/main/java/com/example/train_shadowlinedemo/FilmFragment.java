package com.example.train_shadowlinedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.MovieShow.view.ImageBannerFramLayout;

import java.util.ArrayList;
import java.util.List;

public class FilmFragment  extends Fragment implements ImageBannerFramLayout.FramLayoutLisenner{
    private View root;
    private int[] bannerImages;
    private ImageBannerFramLayout mGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root=inflater.inflate(R.layout.fragment_film,container,false);
        }
        bannerImages=new int[]{
                R.drawable.movie_banner_pic1,
                R.drawable.movie_banner_pic2,
                R.drawable.movie_banner_pic3,
        };
        autoBannerImages();
        Button btnSearch=root.findViewById(R.id.btn_search);
        MyOnClickListener myListenter=new MyOnClickListener();
        btnSearch.setOnClickListener(myListenter);
        return root;
    }
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_search:
                    //点击搜索按钮跳转到搜索页面
                    Intent intent=new Intent(getContext(),SearchActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    //轮播图初始化
    private void autoBannerImages(){
        //计算当前手机宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        mGroup = root.findViewById(R.id.image_group);
        mGroup.setLisenner(this);
        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < bannerImages.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),bannerImages[i]);
            list.add(bitmap);
        }
        mGroup.addBitmaps(list);
    }

    @Override
    public void chickImageIndex(int pos) {

    }
}
