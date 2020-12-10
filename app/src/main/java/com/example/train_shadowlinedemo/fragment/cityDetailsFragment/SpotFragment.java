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
import com.example.train_shadowlinedemo.activity.PlaceDetailActivity;
import com.example.train_shadowlinedemo.adapter.SpotListAdapter;
import com.example.train_shadowlinedemo.entity.Place;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotFragment extends Fragment {
    private List<Place> places=new ArrayList<>();
    private View root;
    private ListView listView;
    private OkHttpClient okHttpClient;
    // String userId;
    private String cityId;
//    private SpotFragment spotFragment;
    private SpotListAdapter spotListAdapter;
    public SpotFragment(String cityId){
        this.cityId=cityId;
    }
    public SpotFragment(){

    }
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initView();
                    EventBus.getDefault().postSticky(places);
                    break;
                case 2:

                    break;
            }
        }
    };
    public void initView(){
        spotListAdapter = new SpotListAdapter(places,getContext(),
                R.layout.city_spot_item);
        listView =root.findViewById(R.id.city_spot_list);
        listView.setAdapter(spotListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Place place=new Place();
                place=places.get(position);
                Gson gson=new Gson();
                String str=gson.toJson(place);
                Intent intent=new Intent(getActivity(), PlaceDetailActivity.class);
                intent.putExtra("place",str);
                startActivity(intent);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_spot,container,false);
//        final SharedPreferences sharedPreferences=getContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
//        userId=sharedPreferences.getString("id","");
        EventBus.getDefault().register(this);
        okHttpClient=new OkHttpClient();
        getCitySpotSync();

        //downloadStr(ConfigUtil.SERVER_ADDR + "DownUserOrderServlet");
        return root;

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
                        //Log.e("响应数据",response.body().string());
                        //再去看下和gson
                        String placeJson=response.body().string();
                        Type type=new TypeToken<List<Place>>(){}.getType();
                        Gson gson=new Gson();
                        List<Place> spots=gson.fromJson(placeJson,type);
                        //修改数据源
                        places.addAll(spots);
                       // EventBus.getDefault().postSticky(true);
                        Message msg = new Message();
                        //设置Message对象的参数
                        msg.what = 1;
                        msg.obj = places;
                        //发送Message
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    //注意设置sticky属性 默认是false
    public void onMessage(Boolean flag){
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
    
}
