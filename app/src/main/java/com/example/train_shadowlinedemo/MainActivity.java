package com.example.train_shadowlinedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.train_shadowlinedemo.activity.MovieDetailActivity;
import com.example.train_shadowlinedemo.activity.PlanningRouteActivity;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.fragment.CityFragment;
import com.example.train_shadowlinedemo.fragment.FilmFragment;
import com.example.train_shadowlinedemo.fragment.PersonalFragment;
import com.example.train_shadowlinedemo.fragment.ShareFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Fragment currentFragment = new Fragment();
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
        /*Intent intent=new Intent();
        Film film=new Film();
        film.setFilmId(1);
        film.setFilmDirector("曾国祥");
        film.setFilmEnglishname("Young you");
        film.setFilmName("少年的你");
        film.setFilmInfo("一场高考前夕的校园意外，改变了两个少年的命运。陈念(周冬雨)是学校里的优等生，考上好大学是她唯一的念头。同班同学的意外坠楼牵扯出的故事，陈念也被一点点卷入其中…在她最孤独的时刻，一个叫小北（易烊千玺）的少年闯入了她的世界。");
        film.setFilmTostar("周冬雨&易烊千玺");
        film.setFilmReleasetime("2019");
        film.setFilmProducercountry("中国");
        film.setFilmType("爱情/犯罪/剧情");
        film.setFilmImg("film/filmphoto/img1.jpg");
        film.setFlimMapImg("film/map/img1.jpg");
        Gson gson = new GsonBuilder()
                .serializeNulls()//允许序列化空值
                .setPrettyPrinting()//格式化输出
                .setDateFormat("yyyy-MM-dd")//设置日期输出格式
                .create();
        intent.putExtra("film", gson.toJson(film));
        intent.setClass(this, MovieDetailActivity.class);
        startActivity(intent);
        finish();*/
       /* Intent intent=new Intent(this, PlanningRouteActivity.class);
        startActivity(intent);*/
        //获取控件
        ivFilm = findViewById(R.id.iv_tab_film);
        ivCity = findViewById(R.id.iv_tab_city);
        ivShare = findViewById(R.id.iv_tab_share);
        ivMine = findViewById(R.id.iv_tab_mine);
        tvFilm = findViewById(R.id.tv_tab_film);
        tvCity = findViewById(R.id.tv_tab_city);
        tvShare = findViewById(R.id.tv_tab_share);
        tvMine = findViewById(R.id.tv_tab_mine);
        //选择电影列表为主界面
        filmFragment = new FilmFragment();
        cityFragment = new CityFragment();
        shareFragment = new ShareFragment();
        personalFragment = new PersonalFragment();
        changeTab(filmFragment);
        changeColor("film");
        currentFragment = filmFragment;
        onclickReceive();

    }

    private void changeTab(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment != fragment) {
            if (!fragment.isAdded()) {
                transaction.add(R.id.tab_content, fragment);
            }
            transaction.hide(currentFragment);
            transaction.show(fragment);
            transaction.commit();
            currentFragment = fragment;
        }

    }

    public void fragmentOnClicked(View view) {
        switch (view.getId()) {
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
        switch (str) {
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

    public void onclickReceive() {
        Intent intent = getIntent();
        if (intent != null) {
            String skipDynamic = intent.getStringExtra("skipDynamic");
            if (skipDynamic != null) {
                if (skipDynamic.equals("skipDynamic")) {
                    shareFragment = new ShareFragment();
                    changeTab(shareFragment);
                    changeColor("share");

                }
            }

        }

    }
}