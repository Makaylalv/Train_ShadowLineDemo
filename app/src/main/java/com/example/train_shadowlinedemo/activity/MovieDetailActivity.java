package com.example.train_shadowlinedemo.activity;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.fragment.movieDetailsFragment.FragmenPlace;
import com.example.train_shadowlinedemo.fragment.movieDetailsFragment.FragmentCity;
import com.example.train_shadowlinedemo.view.MovieDetail.MovieDetailsViewPager;
import com.example.train_shadowlinedemo.view.MovieDetail.MovieDetailsViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MovieDetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private MovieDetailsViewPager viewPager;
    private ArrayList<Fragment> fragments = new  ArrayList<>();
    private ArrayList<String>  listTitle = new ArrayList<>();
    private FragmenPlace fragmenPlace;
    private FragmentCity fragmentCity;
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
    private ImageView imageView;
    private ArrayList<Place> places= new ArrayList<>();
    private OkHttpClient okHttpClient;
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
                    barTextView.setText(film.getFilmName());
                    barTextView.setAlpha(0);
                    timer.schedule(task1, 0, 30);
                    break;
                case 3:
                    fragmenPlace.setData(places);

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

    @Override
    protected void onResume() {
        super.onResume();
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
            imageView=actionBar.getCustomView().findViewById(R.id.back);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            Log.e("actionbar","is null");
        }
    }

    private void getAllPlace() {
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),film.getFilmId()+"");
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"GetAllPlaceServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        final Call call = okHttpClient.newCall(request);
        //异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功时回调
                Gson gson=new Gson();
                Type type = new TypeToken<ArrayList<Place>>(){}.getType();
                places = gson.fromJson(response.body().string(),type);
                Message message=new Message();
                message.what=3;
                handler.sendMessage(message);
                //打印测试
                for(Place i:places){
                    System.out.println(i.getPlaceMapImg());
                }
            }
        });
    }


    /**
     *初始化控件
     */
    private void initView() {
        okHttpClient=new OkHttpClient();
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
        button=findViewById(R.id.collection);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionFilm();
            }
        });


        listTitle.add("片场"); //标题
        listTitle.add("片场");//标题
        fragmenPlace=new FragmenPlace();
        fragmentCity=new FragmentCity();
        fragments.add(fragmenPlace);
        fragments.add(fragmentCity);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new MovieDetailsViewPagerAdapter(getSupportFragmentManager(),fragments,listTitle));
        getAllPlace();
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
                .load(ConfigUtil.SERVER_ADDR+"imgs/"+film.getFilmImg())
                .apply(options)//应用请求选项
                .into(filmImgView);
        Glide.with(this)
                .load(ConfigUtil.SERVER_ADDR+"imgs/"+film.getFlimMapImg())
                .apply(options)//应用请求选项
                .into(filmMapImgView);
        filmMapImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("11111111111","11111111");
                Intent intent1=new Intent();
                Gson gson=new Gson();
                String str=gson.toJson(places);
                intent1.putExtra("places",str);
                intent1.setClass(MovieDetailActivity.this,DetailMapActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void collectionFilm() {
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),film.getFilmId()+"&"+"1");
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"CollectionFilmServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        final Call call = okHttpClient.newCall(request);
        //异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
                Log.e("请求成功",response.body().string());
            }
        });

    }

    //自定义滑动监听
    private ActivityTouchListener touchListener = new ActivityTouchListener() {
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
