package com.example.train_shadowlinedemo.fragment.ShareChildrenFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.adapter.CustomerDynamicAdapter;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DynamicFragment extends Fragment {
    private View view;
    private FloatingActionButton fabAddDynamic;
    private List<Dynamic> dynamics=new ArrayList<Dynamic>();
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Gson gson=new Gson();
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    String imginfo=msg.obj.toString();
                    dynamics=gson.fromJson(imginfo,new TypeToken<List<Dynamic>>(){}.getType());
                    Log.e("dynamics",dynamics.toString());
                    CustomerDynamicAdapter customerDynamicAdapter=new CustomerDynamicAdapter(getContext(),dynamics,R.layout.item_dynamic);
                    ListView lvDynamics=view.findViewById(R.id.lv_dynamics);
                    lvDynamics.setAdapter(customerDynamicAdapter);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_shared_dynamic_fragment,container,false);
        initView();
        setOnClickListener();
        initData();
//        CustomerDynamicAdapter customerDynamicAdapter=new CustomerDynamicAdapter(getContext(),dynamics,R.layout.item_dynamic);
//        ListView lvDynamics=view.findViewById(R.id.lv_dynamics);
//        lvDynamics.setAdapter(customerDynamicAdapter);
        return view;
    }
    //初始化布局控件
    private void initView(){
        fabAddDynamic=view.findViewById(R.id.fab_add_dynamic);
    }
    //为布局控件设置点击事件
    private void setOnClickListener(){
        //添加动态设置点击事件
        fabAddDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到编辑动态界面
                Intent intent=new Intent();
                intent.putExtra("skipdynamic","skipdynamic");
                intent.setClass(getContext(), EditDynamicActivity.class);
                startActivity(intent);
            }
        });
    }
    //初始化数据
    private void initData(){
      Dynamic dynamic=new Dynamic();
      dynamic.setUserImg("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      dynamic.setUserName("马大爷");
      dynamic.setDynamicTime("今天15:36");
      dynamic.setDynamicContent("今天有点小难过");
      List<String> users=new ArrayList<>();
      users.add("懒羊羊");
      users.add("西门庆");
      users.add("迪迦奥特曼");
      dynamic.setLikeUser(users);
      List<String> imgs=new ArrayList<>();
      imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      dynamic.setDynamicImgs(imgs);
      getAllDynamics();
     // dynamics.add(dynamic);
    }
    private void getAllDynamics(){
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),"aa");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"GetAllDynamicsServlet")
                .build();
        //创建Call对象,发送请求，并接受响应
        Call call=okHttpClient.newCall(request);
        //异步网络请求(不需要创建子线程)
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Log.e("登录失败","发布失败");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                    }
                }.start();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String imginfo=response.body().string();

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message msg=handler.obtainMessage();
                        msg.what=100;

                        msg.obj=imginfo;

                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });

    }
}
