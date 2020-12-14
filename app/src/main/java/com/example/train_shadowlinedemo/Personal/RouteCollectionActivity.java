package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteCollectionActivity extends AppCompatActivity {
    private List<RouteCollection> routeCollections = new ArrayList<>();
    private List<RouteCollection> checkedLists = new ArrayList<>();
    private List<RouteCollection> unCheckedList = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private boolean isOP;
    private ImageView no_route;
    private RouteCollectionAdapter routeCollectionAdapter;
    private ListView lvRoute;
    private ImageView iv_route_back;
    private TextView tv_route_edit;

    int i = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_route_collection);
        //1.创建OkHttpClient对象,获取数据
        okHttpClient = new OkHttpClient();
        tv_route_edit = findViewById(R.id.tv_route_edit);
        lvRoute = findViewById(R.id.list_route);
        no_route = findViewById(R.id.noroute);

        iv_route_back = findViewById(R.id.iv_route_back);
        iv_route_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    public void initData() {
        //2.创建Request请求对象(默认使用get请求)
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR +
                        "GetRouteInfoServlet?user_id=" + LoginActivity.user.getUser_id())//请求的地址
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
                    Log.e("routemessage", message);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(message);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    RouteCollection collection = new RouteCollection();
                                    collection.setId(jsonObject.getInt("route_id"));
                                    collection.setName(jsonObject.getString("name"));
                                    collection.setTag(jsonObject.getInt("tag"));
                                    routeCollections.add(collection);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("routeCollections", routeCollections.toString());
                            routeCollectionAdapter = new RouteCollectionAdapter(RouteCollectionActivity.this, R.layout.item_route_collection, routeCollections);
                            lvRoute.setAdapter(routeCollectionAdapter);
                            TextView tv_count = findViewById(R.id.route_count);
                            //收藏数目为0
                            if (routeCollectionAdapter.getCount() == 0) {
                                Log.e("getCount为", routeCollectionAdapter.getCount() + "");
                                lvRoute.setVisibility(View.GONE);
                                no_route.setVisibility(View.VISIBLE);
                                routeCollectionAdapter.notifyDataSetChanged();
                            }
                            tv_count.setText("收藏路线(" + routeCollectionAdapter.getCount() + ")");
                            lvRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Log.e("点击了",routeCollections.get(i).getName()+"");
                                    Intent intent = new Intent();
                                    intent.setClass(RouteCollectionActivity.this,RouteCollectionDetail.class);
                                    intent.putExtra("route_id",routeCollections.get(i).getId());
                                    intent.putExtra("name",routeCollections.get(i).getName());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }


    public void deleteRoute(View view) {
        switch (view.getId()) {
            case R.id.route_all_checked:
                //全选
                if (i == 0) {
                    for (RouteCollection routeCollection : routeCollections) {
                        routeCollection.setChecked(true);
                        i = 1;
                    }
                } else {
                    for (RouteCollection routeCollection : routeCollections) {
                        routeCollection.setChecked(false);
                        i = 0;
                    }
                }
                routeCollectionAdapter.notifyDataSetChanged();
                break;
            case R.id.routedelete:
                //删除
                Log.e("delete", "delete");
                checkedLists = new ArrayList<RouteCollection>();
                unCheckedList = new ArrayList<RouteCollection>();
                for (RouteCollection routeCollection : routeCollections) {
                    if (routeCollection.isChecked()) {
                        checkedLists.add(routeCollection);
                    } else {
                        unCheckedList.add(routeCollection);
                    }
                }
                routeCollections.clear();
                routeCollections.addAll(unCheckedList);
                deleteData();

                routeCollectionAdapter.notifyDataSetChanged();
                Log.e("", "check list:" + checkedLists.size());
                Log.e("", "uncheck list:" + unCheckedList.size());
                break;
        }
    }

    public void deleteData() {
        for (RouteCollection routeCollection : checkedLists) {
            Request request = new Request.Builder()
                    .url(ConfigUtil.SERVER_ADDR +
                            "DeleteRouteCollectionServlet?route_id=" + routeCollection.getId())//请求的地址
                    .build();
            //3.创建Call对象，发送请求，并接受响应
            Call call = okHttpClient.newCall(request);
            //异步网络请求（不需要创建子线程）
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //请求失败时回调
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //请求成功时回调
                    Log.e("异步请求的结果", response.body().string());
                    //不能直接修改UI，如果需要修改UI，需要使用Handler或者EventBus
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = findViewById(R.id.route_count);
                            textView.setText("收藏路线(" + unCheckedList.size() + ")");
                            //收藏数目为0
                            if (unCheckedList.size() == 0) {
                                Log.e("现有收藏数目为", unCheckedList.size() + "");
                                lvRoute.setVisibility(View.GONE);
                                no_route.setVisibility(View.VISIBLE);
                                routeCollectionAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }

    }

    public void routeClicked(View view) {
        Log.e("edit", "edit");
        isOP = !isOP;
        if (isOP) {
            tv_route_edit.setText("完成");
            LinearLayout linearLayout = findViewById(R.id.route_delete);
            linearLayout.setVisibility(View.VISIBLE);

        } else {
            tv_route_edit.setText("编辑");
            LinearLayout linearLayout = findViewById(R.id.route_delete);
            linearLayout.setVisibility(View.GONE);
        }
        routeCollectionAdapter.setOP(isOP);
        routeCollectionAdapter.notifyDataSetChanged();
    }
}