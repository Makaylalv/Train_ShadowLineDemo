package com.example.train_shadowlinedemo.fragment.cityDetailsFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.MovieDetailActivity;
import com.example.train_shadowlinedemo.adapter.MovieListAdapter;
import com.example.train_shadowlinedemo.entity.Film;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieFragment extends Fragment {
    //private List<ShowOrderItem> orderItems=new ArrayList<>();
    private List<Film> films=new ArrayList<>();
    private View root;
    private OkHttpClient okHttpClient;
    //private String userId;
    private String cityId;
    private MovieListAdapter movieListAdapter;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initView();
                    break;
                case 2:

                    break;
            }
        }
    };
    public MovieFragment(String cityId){
        this.cityId=cityId;
    }
    public MovieFragment(){

    }
    //private List<String> picStrs = new ArrayList<>();
    public void initView(){
        movieListAdapter = new MovieListAdapter(films,getContext(),
                R.layout.city_movie_item);
        ListView stuListView =root.findViewById(R.id.city_movie_list);
        stuListView.setAdapter(movieListAdapter);
        stuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson=new Gson();
                Film film=films.get(position);
                String str=gson.toJson(film);
                Intent intent=new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("film",str);
                startActivity(intent);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_movie,container,false);


        okHttpClient=new OkHttpClient();

        getCityMovieSync();
        return root;
    }
    public void getCityMovieSync(){
        //2.创建request请求对象
        Log.e("地址","cityId="+cityId);
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"CityMovieServlet?cityId="+cityId)//请求的地址
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
                        //Log.e("响应数据",response.body().string());
                        //再去看下和gson
                        String filmJson=response.body().string();
                        Log.e("响应数据",filmJson);
                        Type type=new TypeToken<List<Film>>(){}.getType();
                        Gson gson=new Gson();
                        List<Film> movies=gson.fromJson(filmJson,type);
                        //修改数据源
                        films.addAll(movies);
                        Message msg = new Message();
                        //设置Message对象的参数
                        msg.what = 1;
                        msg.obj = movies;
                        //发送Message
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    //注意设置sticky属性 默认是false
//    public void onMessage(Boolean flag){
////        Toast.makeText(this,flag,Toast.LENGTH_LONG).show();
//        initView();
//    }
}
