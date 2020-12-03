package com.example.train_shadowlinedemo.fragment;

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
import com.example.train_shadowlinedemo.activity.CityDetailActivity;
import com.example.train_shadowlinedemo.adapter.CityListAdapter;
import com.example.train_shadowlinedemo.entity.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CityFragment extends Fragment {
    private View root;
    private List<City> citiesC=new ArrayList<>();
    private List<City> citiesA=new ArrayList<>();
    private CityListAdapter cityListAdapter;
    private OkHttpClient okHttpClient;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(msg.obj.toString().equals("innitchina")){
                        initView(citiesC, R.id.chinese_city_list);
                    }
                    if(msg.obj.toString().equals("innitasia")){
                        initView(citiesA,R.id.aisa_city_list);
                    }
                    break;
                case 2:

                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root=inflater.inflate(R.layout.city_fragment,container,false);
        }
        okHttpClient=new OkHttpClient();
        // initView();
        getOceanSync("china",citiesC);
        getOceanSync("asia",citiesA);
        return root;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        okHttpClient=new OkHttpClient();
//        // initView();
//        getOceanSync("china",citiesC);
//        getOceanSync("asia",citiesA);
//    }
    public void initView(List<City> cities,int layoutId){
        cityListAdapter = new CityListAdapter(getContext(),
                R.layout.city_item0,R.layout.city_item1,cities);//看下都有的数据吧
        ListView stuListView = root.findViewById(layoutId);//R.id.chinese_city_list
        stuListView.setAdapter(cityListAdapter);
        stuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), CityDetailActivity.class);
                intent.putExtra("id",cities.get(position).getCityId());
                startActivity(intent);

            }
        });
    }
    public void getOceanSync(String ocean,List<City> cities){
        //2.创建request请求对象
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR +"CityOceanServlet?ocean="+ocean)//请求的地址
                .build();
        //3.创建call对象，发送请求，并接收响应
        final Call call=okHttpClient.newCall(request);
        //如果使用同步请求 需要使用多线程

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
                Type type=new TypeToken<List<City>>(){}.getType();
                Gson gson=new Gson();
                List<City> citys=gson.fromJson(cityJson,type);
//                cities=gson.fromJson(cityJson,type);
                //修改数据源
                cities.addAll(citys);
                // pageNum++;//页码加1
//                EventBus.getDefault().post("innit"+ocean);
                Message msg = new Message();
                //设置Message对象的参数
                msg.what = 1;
                msg.obj = "innit"+ocean;
                //发送Message
                handler.sendMessage(msg);
            }
        });

    }

    public void btnOnclicked(View view) {
    }
}
