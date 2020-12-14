package com.example.train_shadowlinedemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.adapter.SpotChooseAdapter;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.entity.RouteSpot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChooseSpotActivity extends AppCompatActivity {
    private List<Place> places = new ArrayList<>();
    private List<Integer> spotHasChosen = new ArrayList<>();
    private SpotChooseAdapter spotChooseAdapter;
    private OkHttpClient okHttpClient;
    private CheckBox allIn;
    String cityId="";
    int userId=LoginActivity.user.getUser_id();
    private List<RouteSpot> routeSpots=new ArrayList<>();
    private Button like;
    private Button choose;
    private List<Place> chosenPlaces;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String str= (String) msg.obj;
                    if(str.equals("1")){
                        //Glide.with(this).load(R.drawable.like1).into(like);
                        spotChooseAdapter.changeCheckState();
                        routeSpots.clear();
                        if(allIn.isChecked()){
                            allIn.setText("全选");
                            allIn.setChecked(false);
                        }

                        Log.e("routeSpots的清空的内容",routeSpots.toString());
                        Toast.makeText(ChooseSpotActivity.this,"路线添加成功",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent=new Intent(ChooseSpotActivity.this,PlanningRouteActivity.class);
                    Gson gson=new Gson();
                    String string=gson.toJson(chosenPlaces);
                    intent.putExtra("places",string);
                    if(string!=null&&!string.equals("[]")) {
                        startActivity(intent);
                    }
                    break;
                case 2:
                    String str0= (String) msg.obj;
                    Log.e("change的spotHasChosen",spotHasChosen.toString()+"%%"+spotHasChosen.size());
                    spotChooseAdapter.changeCheckState();
                    if(allIn.isChecked()){
                        allIn.setText("全选");
                        allIn.setChecked(false);
                    }
                    if(str0.equals("1")){
                        //Glide.with(this).load(R.drawable.like1).into(like);
                        Toast.makeText(ChooseSpotActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChooseSpotActivity.this,"已被收藏",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    Log.e("places",places.toString());
                    initView();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_spot);
//        final SharedPreferences sharedPreferences=getSharedPreferences("loginInfo",MODE_PRIVATE);
//        userId=sharedPreferences.getString("id","");
        okHttpClient=new OkHttpClient();
        Intent i=getIntent();

        cityId=i.getStringExtra("cityId");
        if(cityId==null){
            Gson gson=new Gson();
            String str=i.getStringExtra("placeList");
            Type type=new TypeToken<List<Place>>(){}.getType();
            places=gson.fromJson(str,type);
            Message msg = new Message();
            //设置Message对象的参数
            msg.what = 3;
            msg.obj = "str";
            //发送Message
            handler.sendMessage(msg);

        }else{
            getCitySpotSync();

        }
        like=findViewById(R.id.likeSpot);
        choose=findViewById(R.id.route_btn);
        allIn=findViewById(R.id.allIn);
        allIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allIn.getText().toString().equals("全选")){
                    spotChooseAdapter.allCheckTrue();
                    allIn.setText("全不选");
                }else{
//                    spotHasChosen.clear();
                    spotChooseAdapter.changeCheckState();
                    allIn.setText("全选");
                }
            }
        });
        //加入路线
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // noRepeat(spotHasChosen);
                chosenPlaces=new ArrayList<>();
                for(Integer i:spotHasChosen){
                    for (int j=0;j<places.size();j++){
                        if(places.get(j).getPlaceId()==i){
                            chosenPlaces.add(places.get(j));
                            break;
                        }
                    }
                    RouteSpot routeSpot=new RouteSpot(i,userId);
                    routeSpots.add(routeSpot);
                    Log.e("placeId",routeSpot.getPlaceId()+"");
                }
                upRouteSync();
//                spotHasChosen.clear();
            }
        });
        //收藏
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=userId;
                //noRepeat(spotHasChosen);
                Log.e("spotHasChosen",spotHasChosen.toString());
                for(Integer i:spotHasChosen){
                    RouteSpot routeSpot=new RouteSpot(i,id);
                    routeSpots.add(routeSpot);
                }
                upPlaceLikeSync();
                // spotHasChosen.clear();//?
                //怎么取消选中状态

            }
        });

    }
    public void upPlaceLikeSync(){
        //2.创建request请求对象
        Log.e("spotHasChosen的内容",spotHasChosen.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),new Gson().toJson(spotHasChosen));
//        routeSpots.clear();
//        Log.e("routeSpots的内容",routeSpots.toString());
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR + "SpotsLikeServlet?userId="+userId)
                .build();
        //4.创建Call对象，发送请求，并接受响应
        final Call call = okHttpClient.newCall(request);
        //如果使用同步请求 需要使用多线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                Log.e("收藏结果",str);
//                        EventBus.getDefault().postSticky(flag);
                Message msg = new Message();
                //设置Message对象的参数
                msg.what = 2;
                msg.obj = str;
                //发送Message
                handler.sendMessage(msg);
            }
        });


    }
    public void upRouteSync(){
        //2.创建request请求对象
        Log.e("routeSpots的内容",routeSpots.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),new Gson().toJson(routeSpots));
        routeSpots.clear();
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR + "RouteServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        final Call call = okHttpClient.newCall(request);
        //如果使用同步请求 需要使用多线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功时回调
                //Log.e("异步请求的结果",response.body().string());
                //不能直接修改UI，如果需要修改UI，需要使用Handler或者EventBus
                //Log.e("收藏结果",response.body().string());
                String str=response.body().string();
                Log.e("收藏结果",str);
                Message msg = new Message();
                //设置Message对象的参数
                msg.what = 1;
                msg.obj = str;
                //发送Message
                handler.sendMessage(msg);
            }
        });

    }


    public void initView(){
        spotChooseAdapter = new SpotChooseAdapter(places,this,
                R.layout.spot_choose_item,spotHasChosen,allIn);//看下都有的数据吧
        ListView stuListView = findViewById(R.id.spot_route_list);
        stuListView.setAdapter(spotChooseAdapter);
    }
    public void getCitySpotSync(){
        //2.创建request请求对象
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"CitySpotServlet?cityId="+cityId)//请求的地址
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
                        // Log.e("响应数据",response.body().string());
                        //再去看下和gson
                        String placeJson=response.body().string();
                        Log.e("响应数据",placeJson);
                        Type type=new TypeToken<List<Place>>(){}.getType();
                        Gson gson=new Gson();
                        List<Place> spots=gson.fromJson(placeJson,type);
                        //修改数据源
                        places.addAll(spots);
                        // EventBus.getDefault().postSticky(true);
//                        EventBus.getDefault().postSticky(true);
                        Message msg = new Message();
                        //设置Message对象的参数
                        msg.what = 3;
                        msg.obj = "str";
                        //发送Message
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }





}