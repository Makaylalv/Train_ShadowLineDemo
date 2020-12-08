package com.example.train_shadowlinedemo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.view.SlideView.SlideBottomLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class PlaceDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private LinearLayout linearLayoutBottom;
    private LinearLayout linearLayoutTop;
    private MapView mapView;
    private BaiduMap baiduMap;
    private TextView placeNameTV;
    private TextView placePositionTV;
    private TextView placePhoneTV;
    private TextView placeFilmDescribeTV;
    private TextView placeBigNameTV;
    private TextView placeBigPositionTV;
    private TextView placeTypeTV;
    private TextView placeBigPhone;
    private ImageView placeImgIV;
    private ImageView placeBigImgIV;
    private ImageView placeMapImgIV;
    private SlideBottomLayout slideBottomLayout;
    private OkHttpClient okHttpClient;
    private ImageView placeIsCollectionIV;
    private boolean isCollection;
    private Place place;
    private int userId=LoginActivity.user.getUser_id();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    isCollection=true;
                    placeIsCollectionIV.setImageResource(R.drawable.star_select);
                    break;
                case 2:
                    isCollection=false;
                    placeIsCollectionIV.setImageResource(R.drawable.star_normal);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        //得到控件引用
        getView();
        //初始化bar
        initBar();
        //初始化控件
        initView();
        //初始化地图
        initMap();

        isCollectionPlace(place.getPlaceId(),userId);

    }

    private void initMap() {
        double moveLongitude=place.getPlaceLongitude();
        double moveLatitude=place.getPlaceLatitude();
        LatLng movePoint=new LatLng(moveLatitude,moveLongitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(movePoint);
        //移动地图界面
        baiduMap.animateMapStatus(update);
        BitmapDescriptor descriptorSelect= BitmapDescriptorFactory.fromResource(R.drawable.marker_select);
        MarkerOptions optionsSelect=new MarkerOptions().position(movePoint).icon(descriptorSelect);
        baiduMap.addOverlay(optionsSelect);
        

    }

    private void initView() {
        Intent intent=getIntent();
        String jsonStr=intent.getStringExtra("place");
        Gson gson=new Gson();
        place=new Place();
        place = gson.fromJson(jsonStr,Place.class);
        placeNameTV.setText(place.getPlaceName());
        placePositionTV.setText(place.getPlacePosition());
        placePhoneTV.setText(place.getPlacePhone());
        placeBigNameTV.setText(place.getPlaceName());
        placeFilmDescribeTV.setText(place.getPlaceDescribe());
        placeBigPositionTV.setText(place.getPlacePosition());
        placeTypeTV.setText(place.getPlaceType());
        placeBigPhone.setText(place.getPlacePhone());
        placeImgIV=findViewById(R.id.place_img);
        placeBigImgIV=findViewById(R.id.place_big_img);
        placeMapImgIV=findViewById(R.id.place_big_map);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
                .error(R.drawable.glide_error)//请求失败时显示
                .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示
        Glide.with(PlaceDetailActivity.this)
                .load(ConfigUtil.SERVER_ADDR+place.getPlaceFalseImg())
                .apply(options)//应用请求选项
                .into(placeImgIV);
        Glide.with(PlaceDetailActivity.this)
                .load(ConfigUtil.SERVER_ADDR+place.getPlaceReallyImg())
                .apply(options)//应用请求选项
                .into(placeBigImgIV);
        Glide.with(PlaceDetailActivity.this)
                .load(ConfigUtil.SERVER_ADDR+place.getPlaceMapImg())
                .apply(options)//应用请求选项
                .into(placeMapImgIV);
    }

    /**
     * 得到控件引用
     */
    private void getView() {
        mapView=findViewById(R.id.map);
        baiduMap=mapView.getMap();
        placeNameTV=findViewById(R.id.place_name);
        placePositionTV=findViewById(R.id.place_position);
        placePhoneTV=findViewById(R.id.place_phone);
        placeBigNameTV=findViewById(R.id.place_top_name);
        placeImgIV=findViewById(R.id.place_img);
        placeFilmDescribeTV=findViewById(R.id.place_dec);
        placeBigImgIV=findViewById(R.id.place_big_img);
        placeBigPositionTV=findViewById(R.id.place_big_position);
        placeIsCollectionIV=findViewById(R.id.isCollection);
        placeTypeTV=findViewById(R.id.place_type);
        placeBigPhone=findViewById(R.id.place_big_phone);
        placeMapImgIV=findViewById(R.id.place_big_map);
        linearLayoutBottom=findViewById(R.id.layout_bottom);
        linearLayoutTop=findViewById(R.id.layout_top);
        slideBottomLayout=findViewById(R.id.slideLayout);
        slideBottomLayout.setLinearLayoutBottom(linearLayoutBottom);
        slideBottomLayout.setLinearLayoutTop(linearLayoutTop);

        placeIsCollectionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollectionPlace(place.getPlaceId(),userId);
            }
        });

    }

    /**
     * 初始化bar
     */
    private void initBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.custom_bar);  //绑定自定义的布局：actionbar_layout.xml
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


    private void isCollectionPlace(int placeId,int userId) {
        //2.创建RequestBody（请求体）对象

        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),placeId+"&"+userId);
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"IsCollectionPlaceServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        OkHttpClient okHttpClient=new OkHttpClient();
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
                //Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
                String str=response.body().string();
                Message message=new Message();
                if(str.equals("已收藏")){
                    message.what=1;
                    handler.sendMessage(message);
                }else {
                    message.what=2;
                    handler.sendMessage(message);
                }

            }
        });

    }



    private void addCollectionPlace(int placeId,int userId){
        if(isCollection==false){
            //2.创建RequestBody（请求体）对象
            RequestBody requestBody = RequestBody.create(MediaType.parse(
                    "text/plain;charset=utf-8"),placeId+"&"+userId);
            //3.创建请求对象
            Request request = new Request.Builder()
                    .post(requestBody)//请求方式为POST
                    .url(ConfigUtil.SERVER_ADDR+"AddCollectionPlaceServlet")
                    .build();
            //4.创建Call对象，发送请求，并接受响应
            OkHttpClient okHttpClient=new OkHttpClient();
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
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"已收藏此片场",Toast.LENGTH_SHORT).show();
        }
    }
}