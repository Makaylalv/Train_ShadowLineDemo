package com.example.train_shadowlinedemo.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.adapter.ViewPagerAdapter;
import com.example.train_shadowlinedemo.customerview.MyViewPager;
import com.example.train_shadowlinedemo.entity.City;
import com.example.train_shadowlinedemo.fragment.cityDetailsFragment.MovieFragment;
import com.example.train_shadowlinedemo.fragment.cityDetailsFragment.SpotFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityDetailActivity extends AppCompatActivity {
    private String userId="1";
    private String cityId="";
    private ImageView imageView;
    private ImageView imageView1;
    private TextView textViewC;
    private TextView textViewE;
    private TextView textViewI;
    private TextView textViewF;
    private ImageView like;
    private ImageView more;
    private ImageView choose;
    private OkHttpClient okHttpClient;
    TabLayout tabLayout;
    MyViewPager viewPager;


    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    City city= (City) msg.obj;
                    Glide.with(CityDetailActivity.this).load(ConfigUtil.SERVER_ADDR+city.getCityImg()).into(imageView);
                    Log.e("图片地址",ConfigUtil.SERVER_ADDR+city.getCityImg());
                    textViewI.setText(city.getCityInfo());
                    textViewF.setText(city.getCityCountry());
                    textViewE.setText(city.getCityTextEnglish());
                    textViewC.setText(city.getCityTextChinese());
                    Log.e("布尔类型",city.toString());
                    break;
                case 2:
                    String flag=msg.obj.toString();
                    if(flag.equals("1")){
                        Glide.with(CityDetailActivity.this).load(R.drawable.like1).into(like);
                        Toast.makeText(CityDetailActivity.this,"收藏成功",Toast.LENGTH_SHORT);
                    } else if(flag.equals("2")){
                        Glide.with(CityDetailActivity.this).load(R.drawable.like0).into(like);
                        Toast.makeText(CityDetailActivity.this,"取消收藏成功",Toast.LENGTH_SHORT);
                    }
                    break;
            }
        }
    };
    ArrayList<Fragment> fragments = new  ArrayList<>();
    ArrayList<String>  listTitle = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        //初始化bar
        initBar();
        okHttpClient=new OkHttpClient();
        Intent i=getIntent();
        cityId=i.getStringExtra("id");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
        actionBar.setCustomView(R.layout.actionbar_layout);  //绑定自定义的布局：actionbar_layout.xml
        like=actionBar.getCustomView().findViewById(R.id.like);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upCityLikeSync();
            }
        });
        more=actionBar.getCustomView().findViewById(R.id.more0);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CityDetailActivity.this,ChooseSpotActivity.class);
                i.putExtra("cityId",cityId);
                startActivity(i);
            }
        });
        choose=findViewById(R.id.chooseSpot);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CityDetailActivity.this,ChooseSpotActivity.class);
                i.putExtra("cityId",cityId);
                startActivity(i);
            }
        });

        imageView=findViewById(R.id.cityImg);
        textViewC=findViewById(R.id.cityTextChinese);
        textViewE=findViewById(R.id.cityTextEnglish);
        textViewF=findViewById(R.id.cityFrom);
        textViewI=findViewById(R.id.cityIntro);

        tabLayout = findViewById(R.id.tab_main);
        viewPager = findViewById(R.id.viewpager);
        fragments.add(new MovieFragment(cityId));
        fragments.add(new SpotFragment(cityId));
        listTitle.add("电影"); //标题
        listTitle.add("景点");//标题

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments,listTitle));
//
        getCityDetailedSync();
    }
    public void getCityDetailedSync(){
        //2.创建request请求对象
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"CityDetailedServlet?cityId="+cityId+"&userId="+userId)//请求的地址
                .build();
        //3.创建call对象，发送请求，并接收响应
        final Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Log.e("出错了",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String cityJson=response.body().string();
                Log.e("响应数据",cityJson);
//                        Type type=new TypeToken<List<City>>(){}.getType();
                Gson gson=new Gson();
                City city=gson.fromJson(cityJson,City.class);
                //List<Book> books=gson.fromJson(bookJson,type);
                //修改数据源
                //bookList.addAll(books);
                //再去看下和gson
//                        EventBus.getDefault().postSticky(city);
                Message msg = new Message();
                //设置Message对象的参数
                msg.what = 1;
                msg.obj = city;
                //发送Message
                handler.sendMessage(msg);
            }
        });

    }

    public void upCityLikeSync(){
        //2.创建request请求对象
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR +"CityLikeServlet?cityId="+cityId+"&userId="+userId)//请求的地址
                .build();
        //3.创建call对象，发送请求，并接收响应
        final Call call=okHttpClient.newCall(request);
        //如果使用同步请求 需要使用多线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //发送同步请求
                    Response response=call.execute();
                    //判断是否相应成功 根据请求码判断
                    if(response.isSuccessful()){
                        //获取服务端相应的数据 并且是字符串数据
                       // Log.e("收藏结果",response.body().string());
                        String flag=response.body().string();
                        Log.e("收藏结果",flag);
//                        EventBus.getDefault().postSticky(flag);
                        Message msg = new Message();
                        //设置Message对象的参数
                        msg.what = 2;
                        msg.obj = flag;
                        //发送Message
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 初始化bar
     */
    private void initBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.custom_bar);  //绑定自定义的布局：actionbar_layout.xml
            imageView1=actionBar.getCustomView().findViewById(R.id.back);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            Log.e("actionbar","is null");
        }
    }


}