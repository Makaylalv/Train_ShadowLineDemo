package com.example.train_shadowlinedemo.fragment.movieDetailsFragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.PlaceDetailActivity;
import com.example.train_shadowlinedemo.activity.PlanningRouteActivity;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.entity.MyRoute;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.entity.RouteSpot;
import com.example.train_shadowlinedemo.view.MovieDetail.PlaceRecyclerViewAdapter;
import com.example.train_shadowlinedemo.view.MovieDetail.RouteRecyclerViewAdapter;
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

public class FragmentCity extends androidx.fragment.app.Fragment {
    private ArrayList<MyRoute> myRoutes= new ArrayList<>();
    private List<Place> places=new ArrayList<>();
    private List<Place> ps=new ArrayList<>();
    private List<List<Integer>> ids=new ArrayList<List<Integer>>();
    private View view;
    private String filmId;
    private RecyclerView recyclerView;
    private OkHttpClient okHttpClient;
    private RouteRecyclerViewAdapter routeRecyclerViewAdapter;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    for(MyRoute i:myRoutes){
                        String str=i.getPlaceIds();
                        String[] s=str.split("&");
                        List<Integer> is=new ArrayList<>();
                        for(String ss:s){
                            is.add(Integer.parseInt(ss));
                        }
                        ids.add(is);

                    }
                    initView();
                    break;
                case 2:

                    break;
            }
        }
    };
    public FragmentCity(String filmId){
        this.filmId=filmId;
    }

    public FragmentCity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_movie_detail_undetermined, container, false);
        recyclerView =view.findViewById(R.id.mrecyclerview);
        return view;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        okHttpClient=new OkHttpClient();
        getAllPlace();
        getCityMovieSync();

    }

    public void initView(){
        routeRecyclerViewAdapter = new RouteRecyclerViewAdapter(myRoutes,getContext());
        routeRecyclerViewAdapter.setOnItemClickListener(e);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(routeRecyclerViewAdapter);

    }
    private void getAllPlace() {
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),filmId+"");
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"GetAllPlaceServlet")
                .build();
        okHttpClient=new OkHttpClient();
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
                String str=response.body().string();
                if(!str.equals("[]")&&str!=null&&!str.equals("")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Place>>() {
                    }.getType();

                    places = gson.fromJson(str, type);
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        });
    }
    RouteRecyclerViewAdapter.OnItemClickListener e=new RouteRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, RouteRecyclerViewAdapter.ViewName viewName, int position) {
            switch (v.getId()){
                default:
                    for(Place p:places){
                        for (int j=0;j<ids.get(position).size();j++){
                            if(p.getPlaceId()==ids.get(position).get(j).intValue()){
                                if(!ps.contains(p.getPlaceId())) {
                                    ps.add(p);
                                    break;
                                }
                            }
                        }
                    }
                    Gson gson=new Gson();
                    String placeJson=gson.toJson(ps);
                    ps.clear();
                    Intent intent=new Intent();
                    intent.setClass(getContext(), PlanningRouteActivity.class);
                    intent.putExtra("places",placeJson);
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {
        }
    };

    public void getCityMovieSync(){
        //2.创建request请求对象
        Log.e("ccc","filmId="+filmId);
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"ClientGetSysroute?filmId="+filmId)//请求的地址
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
                        String filmJson=response.body().string();
                        Log.e("响应数据",filmJson);
                        Type type=new TypeToken<List<MyRoute>>(){}.getType();
                        Gson gson=new Gson();
                        List<MyRoute> films =gson.fromJson(filmJson,type);
                        //修改数据源
                        myRoutes.addAll(films);
                        Message msg = new Message();
                        //设置Message对象的参数
                        msg.what = 1;
                        msg.obj = films;
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
