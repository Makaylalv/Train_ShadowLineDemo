package com.example.train_shadowlinedemo.fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.MovieDetailActivity;
import com.example.train_shadowlinedemo.activity.SearchActivity;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.view.MovieShow.ImageBannerFramLayout;
import com.example.train_shadowlinedemo.view.MovieShow.NewFilmAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilmFragment  extends Fragment implements ImageBannerFramLayout.FramLayoutLisenner{
    private View root;
    private int[] bannerImages;
    private ImageBannerFramLayout mGroup;
    private RecyclerView rvNewFilmList;
    private List<Film> bannerList=new ArrayList<>();
    private List<Film> newFilmList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root=inflater.inflate(R.layout.fragment_film,container,false);
        }
        Button btnSearch=root.findViewById(R.id.btn_search);
        bannerImages=new int[]{
                R.drawable.movie_banner_pic1,
                R.drawable.movie_banner_pic2,
                R.drawable.movie_banner_pic3,
        };
        autoBannerImages();
        initNewFilmData();
        findNewFilmList();
        getBannerFilm();

        MyOnClickListener myListenter=new MyOnClickListener();
        btnSearch.setOnClickListener(myListenter);
        return root;
    }

    //获取轮播图内电影内容
    private void getBannerFilm() {


    }

    //初始化最新更新电影的列表
    private void initNewFilmData() {

    }

    //初始化RecyclerView
    private void findNewFilmList() {
        rvNewFilmList=root.findViewById(R.id.rv_horizontal_list);
        LinearLayoutManager linearLayoutManager;
        NewFilmAdapter adapter;
        //设置布局管理器，垂直设置LinearLayoutManager.VERTICAL
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        //创建适配器，将数据传递给适配器
        adapter=new NewFilmAdapter(getContext(),newFilmList,R.layout.item_newfilm);
        //固定RecycleView大小,当知道adapter内item的改变不会影响RecycleView宽高的时候设置
        rvNewFilmList.setHasFixedSize(true);
        //设置布局管理器
        rvNewFilmList.setLayoutManager(linearLayoutManager);
        //设置适配器adapter
        rvNewFilmList.setAdapter(adapter);
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_search:
                    //点击搜索按钮跳转到搜索页面
                    Intent intent=new Intent(getContext(), SearchActivity.class);
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
        //点击轮播图跳转到详情界面
        Intent intent=new Intent(getContext(), MovieDetailActivity.class);

    }
}
