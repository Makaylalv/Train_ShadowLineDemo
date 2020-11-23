package com.example.train_shadowlinedemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.train_shadowlinedemo.MovieDetail.view.MovieDetailsViewPager;
import com.example.train_shadowlinedemo.MovieDetail.view.MovieDetailsViewPagerAdapter;
import com.example.train_shadowlinedemo.MovieDetailsFragment.FragmenPlace;
import com.example.train_shadowlinedemo.MovieDetailsFragment.FragmentCity;
import com.example.train_shadowlinedemo.entity.Film;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MovieDetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private MovieDetailsViewPager viewPager;
    private ArrayList<Fragment> fragments = new  ArrayList<>();
    private ArrayList<String>  listTitle = new ArrayList<>();
    private TextView barTextView;
    private TextView filmChinesehNametextView;
    private TextView filmEnglishNametextView;
    private ImageView filmImgView;
    private TextView filmInfoTV;
    private TextView filmLocationNumTV;
    private TextView filmTypeTV;
    private TextView filmCountryTV;
    private TextView filmTimeTV;
    private Button button;
    private ImageView filmMapImgView;
    private Timer timer = new Timer();
    private Film film;
    private ArrayList<ActivityTouchListener> myActivityTouchListeners = new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case 1:
                    float a=barTextView.getAlpha();
                    if(a>=1){
                        timer.cancel();
                        timer.purge();
                    }else {
                        a= (float) (a+0.02);
                        barTextView.setAlpha(a);
                    }
                    break;
                case 2:
                    TimerTask task1 = new TimerTask() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    };
                    timer = new Timer();
                    barTextView.setText("少年的你");
                    barTextView.setAlpha(0);
                    timer.schedule(task1, 0, 30);
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //初始化控件
        initView();
        //初始化bar
        initBar();
        //给activity注册滑动事件
        (this).registerFragmentTouchListener(touchListener);

    }


    /**
     * 初始化bar
     */
    private void initBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.custom_bar);  //绑定自定义的布局：actionbar_layout.xml
            barTextView=actionBar.getCustomView().findViewById(R.id.film_name);

        }else {
            Log.e("actionbar","is null");
        }
    }


    /**
     *初始化控件
     */
    private void initView() {
        Intent intent=getIntent();
        String filmJsonStr=intent.getStringExtra("film");
        Gson gson = new GsonBuilder()
                .serializeNulls()//允许序列化空值
                .setPrettyPrinting()//格式化输出
                .setDateFormat("yyyy-MM-dd")//设置日期输出格式
                .create();
        film=gson.fromJson(filmJsonStr,Film.class);

        //获取控件
        filmChinesehNametextView=findViewById(R.id.film_Chinese_name);
        filmEnglishNametextView=findViewById(R.id.film_English_name);
        filmImgView=findViewById(R.id.film_img);
        filmLocationNumTV=findViewById(R.id.location_num);
        filmCountryTV=findViewById(R.id.film_country);
        filmInfoTV=findViewById(R.id.film_info);
        filmTimeTV=findViewById(R.id.film_release_time);
        filmMapImgView=findViewById(R.id.film_map_img);
        filmTypeTV=findViewById(R.id.film_type);
        tabLayout = findViewById(R.id.tab_main);
        viewPager = findViewById(R.id.viewpager);
        button=findViewById(R.id.more);
        fragments.add(new FragmenPlace());
        fragments.add(new FragmentCity());
        listTitle.add("片场"); //标题
        listTitle.add("片场");//标题
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new MovieDetailsViewPagerAdapter(getSupportFragmentManager(),fragments,listTitle));

        //赋值
        filmChinesehNametextView.setText(film.getFilmName());
        filmEnglishNametextView.setText(film.getFilmEnglishname());
        filmCountryTV.setText(film.getFilmProducercountry());
        filmInfoTV.setText(film.getFilmInfo());
        filmTimeTV.setText(film.getFilmReleasetime());
        filmTypeTV.setText(film.getFilmType());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
                .error(R.drawable.glide_error)//请求失败时显示
                .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示
        Glide.with(this)
                .load("http://192.168.116.1:8080/ShadowLine/imgs/"+film.getFilmImg())
                .apply(options)//应用请求选项
                .into(filmImgView);
        Glide.with(this)
                .load("http://192.168.116.1:8080/ShadowLine/imgs/"+film.getFlimMapImg())
                .apply(options)//应用请求选项
                .into(filmMapImgView);
    }

    //自定义滑动监听
    private MovieDetailActivity.ActivityTouchListener touchListener = new MovieDetailActivity.ActivityTouchListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    int[] location = new int[2];
                    filmEnglishNametextView.getLocationOnScreen(location);
                    if(location[1]<=0&&!barTextView.getText().toString().equals(film.getFilmName())){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                    if(location[1]>=0&&!barTextView.getText().toString().equals("")){
                        barTextView.setText("");
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {
                    int[] location = new int[2];
                    filmEnglishNametextView.getLocationOnScreen(location);
                    if(location[1]<=0&&!barTextView.getText().toString().equals(film.getFilmName())){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                    if(location[1]>=0&&!barTextView.getText().toString().equals("")){
                        barTextView.setText("");
                    }


                }
                break;
                case MotionEvent.ACTION_MOVE:
                {
                    int[] location = new int[2];
                    filmEnglishNametextView.getLocationOnScreen(location);
                    if(location[1]<=0&&!barTextView.getText().toString().equals(film.getFilmName())){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                    if(location[1]>=0&&!barTextView.getText().toString().equals("")){
                        barTextView.setText("");
                    }

                }
                break;

            }
            return false;
        }
    };

    /**
     *注册滑动事件
     */
    public void registerFragmentTouchListener(ActivityTouchListener listener) {
        myActivityTouchListeners.add(listener);
    }

    /**注销滑动事件
     */
    public void unRegisterFragmentTouchListener(ActivityTouchListener listener) {
        myActivityTouchListeners.remove(listener);
    }

    /**
     *分配滑动事件(不被子控件吃掉)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        for (ActivityTouchListener listener : myActivityTouchListeners) {
            listener.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    /**定义滑动接口
     */
    public interface ActivityTouchListener {

        boolean onTouchEvent(MotionEvent event);
    }

}
