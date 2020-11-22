package com.example.train_shadowlinedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Fragment currentFragment=new Fragment();
    private ImageView ivFilm;
    private ImageView ivCity;
    private ImageView ivShare;
    private ImageView ivMine;
    private TextView tvFilm;
    private TextView tvCity;
    private TextView tvShare;
    private TextView tvMine;
    //fragment
    private FilmFragment filmFragment;
    private CityFragment cityFragment;
    private ShareFragment shareFragment;
    private PersonalFragment personalFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        ivFilm=findViewById(R.id.iv_tab_film);
        ivCity=findViewById(R.id.iv_tab_city);
        ivShare=findViewById(R.id.iv_tab_share);
        ivMine=findViewById(R.id.iv_tab_mine);
        tvFilm=findViewById(R.id.tv_tab_film);
        tvCity=findViewById(R.id.tv_tab_city);
        tvShare=findViewById(R.id.tv_tab_share);
        tvMine=findViewById(R.id.tv_tab_mine);
        //选择电影列表为主界面
        filmFragment=new FilmFragment();
        cityFragment=new CityFragment();
        shareFragment=new ShareFragment();
        personalFragment=new PersonalFragment();
        changeTab(filmFragment);
        changeColor("film");
        currentFragment=filmFragment;




    }
    private void changeTab(Fragment fragment) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        if(currentFragment!=fragment){
            if(!fragment.isAdded()){
                transaction.add(R.id.tab_content,fragment);
            }
            transaction.hide(currentFragment);
            transaction.show(fragment);
            transaction.commit();
            currentFragment=fragment;

        }

    }

    public void fragmentOnClicked(View view) {
        switch (view.getId()){
            case R.id.tab_film:
                changeTab(filmFragment);
                changeColor("film");
                break;
            case R.id.tab_city:
                changeTab(cityFragment);
                changeColor("city");
                break;
            case R.id.tab_share:
                changeTab(shareFragment);
                changeColor("share");
                break;
            case R.id.tab_mine:
                changeTab(personalFragment);
                changeColor("mine");
                break;
        }
    }

    private void changeColor(String str) {
        switch (str){
            case "film":
                tvFilm.setTextColor(getResources().getColor(R.color.mainColor));
                tvCity.setTextColor(getResources().getColor(R.color.black));
                tvShare.setTextColor(getResources().getColor(R.color.black));
                tvMine.setTextColor(getResources().getColor(R.color.black));
                ivFilm.setImageResource(R.drawable.tab_film2);
                ivCity.setImageResource(R.drawable.tab_city);
                ivShare.setImageResource(R.drawable.tab_share);
                ivMine.setImageResource(R.drawable.tab_mine);
                break;
            case "city":
                tvFilm.setTextColor(getResources().getColor(R.color.black));
                tvCity.setTextColor(getResources().getColor(R.color.mainColor));
                tvShare.setTextColor(getResources().getColor(R.color.black));
                tvMine.setTextColor(getResources().getColor(R.color.black));
                ivFilm.setImageResource(R.drawable.tab_film);
                ivCity.setImageResource(R.drawable.tab_city2);
                ivShare.setImageResource(R.drawable.tab_share);
                ivMine.setImageResource(R.drawable.tab_mine);
                break;
            case "share":
                tvFilm.setTextColor(getResources().getColor(R.color.black));
                tvCity.setTextColor(getResources().getColor(R.color.black));
                tvShare.setTextColor(getResources().getColor(R.color.mainColor));
                tvMine.setTextColor(getResources().getColor(R.color.black));
                ivFilm.setImageResource(R.drawable.tab_film);
                ivCity.setImageResource(R.drawable.tab_city);
                ivShare.setImageResource(R.drawable.tab_share2);
                ivMine.setImageResource(R.drawable.tab_mine);
                break;
            case "mine":
                tvFilm.setTextColor(getResources().getColor(R.color.black));
                tvCity.setTextColor(getResources().getColor(R.color.black));
                tvShare.setTextColor(getResources().getColor(R.color.black));
                tvMine.setTextColor(getResources().getColor(R.color.mainColor));
                ivFilm.setImageResource(R.drawable.tab_film);
                ivCity.setImageResource(R.drawable.tab_city);
                ivShare.setImageResource(R.drawable.tab_share);
                ivMine.setImageResource(R.drawable.tab_mine2);
                break;
        }

    }
}