package com.example.train_shadowlinedemo.fragment.movieDetailsFragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.PlaceDetailActivity;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.view.MovieDetail.PlaceRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmenPlace extends androidx.fragment.app.Fragment {
    private ArrayList<Place> places= new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private OkHttpClient okHttpClient;
    private int filmId;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    recyclerView = view.findViewById(R.id.recyclerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    PlaceRecyclerViewAdapter myAdapter =new PlaceRecyclerViewAdapter(places,getContext());
                    myAdapter.setOnItemClickListener(onItemClickListener);
                    recyclerView.setAdapter(myAdapter);
                    break;
            }

        }
    };

    public FragmenPlace(int filmId) {
        this.filmId = filmId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place, container, false);
        getAllPlace();
        return view;
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
                    message.what = 1;
                    handler.sendMessage(message);
                    //打印测试
                    for (Place i : places) {
                        System.out.println(i.getPlaceMapImg());
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    PlaceRecyclerViewAdapter.OnItemClickListener onItemClickListener=new PlaceRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, PlaceRecyclerViewAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()){
                default:
                    Gson gson=new Gson();
                    String placeJson=gson.toJson(places.get(position));
                    Intent intent=new Intent();
                    intent.setClass(getContext(), PlaceDetailActivity.class);
                    intent.putExtra("place",placeJson);
                    startActivity(intent);
                    Toast.makeText(getContext(),"你点击了item按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {
        }
    };


}
