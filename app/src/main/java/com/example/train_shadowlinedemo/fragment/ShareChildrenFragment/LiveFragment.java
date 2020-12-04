package com.example.train_shadowlinedemo.fragment.ShareChildrenFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LivingRoomActivity;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.activity.StartLivingActivity;
import com.example.train_shadowlinedemo.adapter.CustomerDynamicAdapter;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.entity.Room;
import com.example.train_shadowlinedemo.view.LiveRoom.LiveRoomRecyclerViewAdapter;
import com.example.train_shadowlinedemo.view.MovieDetail.PlaceRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

public class LiveFragment extends Fragment {
    private View view;
    private ArrayList<Room> roomList;
    //下拉刷新时调用
    private ArrayList<Room> newRoomList;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private LiveRoomRecyclerViewAdapter myAdapter;
    private Button startButton;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    recyclerView = view.findViewById(R.id.rv_rooms);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    myAdapter =new LiveRoomRecyclerViewAdapter(roomList,getContext());
                    myAdapter.setOnItemClickListener(onItemClickListener);

                    recyclerView.setAdapter(myAdapter);
                    refreshLayout.finishRefresh();
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_live,container,false);
        refreshLayout=view.findViewById(R.id.srl);
        setListener();
        startButton=view.findViewById(R.id.start_living_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiving();
                Intent intent=new Intent();
                intent.setClass(getActivity(), StartLivingActivity.class);
                startActivity(intent);
            }
        });
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        getLivingRoom();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        Log.e("1111111","111111");
        getLivingRoom();
        super.onResume();
    }

    private void getLivingRoom() {

        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),"获取正在直播的room");
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"GetAllLivingRoomServlet")
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
                Log.e("11111111",str);
                roomList=new ArrayList<>();
                //请求成功时回调
                if(!str.equals("无人直播")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Room>>() {}.getType();
                    roomList = gson.fromJson(str, type);
                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });

    }

    LiveRoomRecyclerViewAdapter.OnItemClickListener onItemClickListener=new LiveRoomRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, LiveRoomRecyclerViewAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()){
                default:
                    Intent intent=new Intent();
                    intent.putExtra("roomId",roomList.get(position).getRoomId());
                    intent.setClass(getActivity(), LivingRoomActivity.class);
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {
        }
    };

    private void setListener(){
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getLivingRoom();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //上拉加载更多时调用

            }
        });
    }



    private void startLiving(){
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),""+ LoginActivity.user.getUser_id());
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"StartRoomServlet")
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
                Log.e("11111",str);
            }
        });

    }
}
