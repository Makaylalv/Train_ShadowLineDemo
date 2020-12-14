package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.activity.PlanningRouteActivity;
import com.example.train_shadowlinedemo.entity.Place;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteCollectionDetail extends AppCompatActivity {
    private int id;
    private String name;
    private List<Place> places = new ArrayList<>();
    private ListView route_detail;
    private TextView fname;
    private RouteCollectionDetailAdapter adapter;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_collection_detail);
        //1.创建OkHttpClient对象,获取数据
        okHttpClient = new OkHttpClient();
        route_detail = findViewById(R.id.route_detail);
        fname = findViewById(R.id.route_place_filmname);

        Intent idIntent = getIntent();
        id = idIntent.getIntExtra("route_id",0);
        name = idIntent.getStringExtra("name");
        getData();
    }
    public void getData(){
        //2.创建Request请求对象(默认使用get请求)
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR +
                        "GetRouteDetailServlet?route_id=" + id)//请求的地址
                .build();
        //3.创建Call对象，发送请求，并接受响应
        Call call = okHttpClient.newCall(request);
        //4.异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String message = response.body().string();
                    Log.e("detail", message);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(message);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Place place = new Place();
                                    place.setPlaceId(jsonObject.getInt("placeId"));
                                    place.setPlaceName(jsonObject.getString("placeName"));
                                    place.setPlaceEnglishname(jsonObject.getString("placeEnglishname"));
                                    place.setPlaceLatitude(jsonObject.getDouble("placeLatitude"));
                                    place.setPlaceLongitude(jsonObject.getDouble("placeLongitude"));
                                    place.setPlacePhone(jsonObject.getString("placePhone"));
                                    place.setPlaceReallyImg(jsonObject.getString("placeReallyImg"));
                                    place.setPlaceFalseImg(jsonObject.getString("placeFalseImg"));
                                    place.setPlaceDescribe(jsonObject.getString("placeDescribe"));
                                    place.setPlaceFilmDescribe(jsonObject.getString("placeFilmDescribe"));
                                    place.setPlacePosition(jsonObject.getString("placePosition"));
                                    place.setPlaceTime(jsonObject.getString("placeTime"));
                                    place.setPlaceType(jsonObject.getString("placeType"));
                                    place.setPlaceMapImg(jsonObject.getString("placeMapImg"));
                                    places.add(place);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("places", places.toString());
                            adapter = new RouteCollectionDetailAdapter(RouteCollectionDetail.this, R.layout.item_route_detail, places);
                            route_detail.setAdapter(adapter);
                            route_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.e("places",(Serializable) places+"");
                                    Gson gson = new Gson();
                                    Log.e("places",gson.toJson(places));
                                    Intent intent = new Intent();
                                    intent.setClass(RouteCollectionDetail.this, PlanningRouteActivity.class);
                                    intent.putExtra("places", gson.toJson(places));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}