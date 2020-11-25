package com.example.train_shadowlinedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailMapActivity extends AppCompatActivity {
    private ImageView imageView;
    private ArrayList<Place> places;
    private ArrayList<LatLng> latLngs;
    private MapView mapView;
    private BaiduMap baiduMap;
    private TextView placeNameTV;
    private TextView placePositionTV;
    private TextView placeFilmDescribeTV;
    private ImageView placeImgIV;
    private List<Marker> markerList;
    private OverlayOptions optionsNormal;
    private OverlayOptions optionsSelect;
    private List<OverlayOptions> overlayOptionsList;
    private BitmapDescriptor descriptorNormal;
    private BitmapDescriptor descriptorSelect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_map);
        //得到控件引用
        getView();
        //初始化bar
        initBar();
        //初始化地图
        initMap();

    }


    /**
     * 得到控件引用
     */
    private void getView() {
        placeNameTV=findViewById(R.id.place_name);
        placePositionTV=findViewById(R.id.place_position);
        placeFilmDescribeTV=findViewById(R.id.place_film_describe);
        placeImgIV=findViewById(R.id.place_img);
    }


    /**
     * 初始化地图覆盖物
     */
    private void initMap() {
        markerList=new ArrayList<>();
        mapView=findViewById(R.id.map);
        baiduMap=mapView.getMap();

        Intent intent=getIntent();
        String jsonStr=intent.getStringExtra("places");
        Gson gson=new Gson();
        Type type = new TypeToken<ArrayList<Place>>(){}.getType();
        places = gson.fromJson(jsonStr,type);
        double moveLongitude=places.get(0).getPlaceLongitude();
        double moveLatitude=places.get(0).getPlaceLatitude();
        LatLng movePoint=new LatLng(moveLatitude,moveLongitude);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(movePoint);
        //移动地图界面
        baiduMap.animateMapStatus(update);
        descriptorNormal= BitmapDescriptorFactory.fromResource(R.drawable.marker_normal);
        descriptorSelect= BitmapDescriptorFactory.fromResource(R.drawable.marker_normal);
        for(int i=0;i<places.size();i++){
            double longitude=places.get(i).getPlaceLongitude();
            double latitude=places.get(i).getPlaceLatitude();
            LatLng point=new LatLng(latitude,longitude);
            //在当前位置添加标注覆盖物
            Bundle bundle = new Bundle();
            bundle.putInt("placeId",i);
            optionsNormal=new MarkerOptions().position(point).icon(descriptorNormal);
            Marker marker= (Marker) baiduMap.addOverlay(optionsNormal);
            marker.setExtraInfo(bundle);
            overlayOptionsList.add(optionsNormal);
        }

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle idBundle=marker.getExtraInfo();
                LatLng latLng1=marker.getPosition();
                baiduMap.clear();
                int latitude1=new Double(latLng1.latitude*10000).intValue();
                int longitude1=new Double(latLng1.longitude*10000).intValue();
                for(int i=0;i<places.size();i++){
                    Bundle bundle = new Bundle();
                    bundle.putInt("placeId",i);
                    double longitude=places.get(i).getPlaceLongitude();
                    double latitude=places.get(i).getPlaceLatitude();
                    LatLng point=new LatLng(latitude,longitude);
                    int latitude2=new Double(places.get(i).getPlaceLatitude()*10000).intValue();
                    int longitude2=new Double(places.get(i).getPlaceLongitude()*10000).intValue();
                    if((Math.abs(latitude1-latitude2)<=1&&Math.abs(longitude1-longitude2)<=1)){
                        optionsSelect=new MarkerOptions().position(point).icon(descriptorSelect);
                        Marker markerNew= (Marker) baiduMap.addOverlay(optionsNormal);
                        markerNew.setExtraInfo(bundle);
                    }else {
                        //在当前位置添加标注覆盖物
                        optionsNormal=new MarkerOptions().position(point).icon(descriptorNormal);
                        Marker markerNew= (Marker) baiduMap.addOverlay(optionsNormal);
                        markerNew.setExtraInfo(bundle);
                    }
                }
                int index =idBundle.getInt("placeId");
                Place place=new Place();
                place=places.get(index);
                placeNameTV.setText(place.getPlaceName());
                placePositionTV.setText(place.getPlacePosition());
                placeFilmDescribeTV.setText(place.getPlaceDescribe());
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
                        .error(R.drawable.glide_error)//请求失败时显示
                        .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示
                Glide.with(DetailMapActivity.this)
                        .load("http://192.168.43.128:8080/ShadowLine/"+place.getPlaceFalseImg())
                        .apply(options)//应用请求选项
                        .into(placeImgIV);
                return false;
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
}
