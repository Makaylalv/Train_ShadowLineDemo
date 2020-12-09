package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class PlaceCollectionActivity extends AppCompatActivity {

    private List<PlaceCollection> collections = new ArrayList<>();
    private List<PlaceCollection> checkedLists = new ArrayList<>();
    private List<PlaceCollection> unCheckedList = new ArrayList<>();
    private TextView place_tv_edit;
    private ImageView no_cut;
    private ImageView back;
    private boolean isOP;
    int i = 0;
    private PlaceCollectionAdapter placeCollectionAdapter;
    private OkHttpClient okHttpClient;
    private ListView lvPlace;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_place_collection);
        place_tv_edit = findViewById(R.id.place_tv_edit);
        lvPlace = findViewById(R.id.li_place);
        no_cut = findViewById(R.id.nocut);
        back = findViewById(R.id.lv_cut_title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //1.创建OkHttpClient对象,获取数据
        okHttpClient = new OkHttpClient();
        //获取数据
        getAsync();
    }
    //初始化数据
    //异步的get请求
    public void getAsync() {
        //2.创建Request请求对象(默认使用get请求)
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR  +
                        "GetPlaceCollectionServlet?user_id="+ LoginActivity.user.getUser_id())//请求的地址
                .build();
        //3.创建Call对象，发送请求，并接受响应
        Call call = okHttpClient.newCall(request);
        //4.异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功时回调
                //不能直接修改UI，如果需要修改UI，需要使用Handler或者EventBus
                if (response.isSuccessful()) {
                    String message = response.body().string();
                    Log.e("message", message);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(message);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    PlaceCollection collection = new PlaceCollection();
                                    collection.setPlace_id(jsonObject.getInt("place_id"));
                                    collection.setPlace_name(jsonObject.getString("place_name"));
                                    collection.setPlace_english(jsonObject.getString("place_english"));
                                    collection.setPlace_position(jsonObject.getString("place_position"));
                                    collection.setFilm(jsonObject.getString("film"));
                                    collections.add(collection);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("collections", collections.toString());
                            placeCollectionAdapter = new PlaceCollectionAdapter(PlaceCollectionActivity.this, collections, R.layout.item_place_collection);
                            lvPlace = findViewById(R.id.li_place);
                            lvPlace.setAdapter(placeCollectionAdapter);
                            TextView tv_count = findViewById(R.id.place_count);
                            //收藏数目为0
                            if (placeCollectionAdapter.getCount()==0){
                                Log.e("getCount为",placeCollectionAdapter.getCount()+"");
                                lvPlace.setVisibility(View.GONE);
                                no_cut.setVisibility(View.VISIBLE);
                                placeCollectionAdapter.notifyDataSetChanged();
                            }
                            tv_count.setText("收藏片场(" + placeCollectionAdapter.getCount() + ")");
                            lvPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void placeClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_place_edit:
                Log.e("edit", "edit");
                isOP = !isOP;

                if (isOP) {
                    place_tv_edit.setText("完成");
                    LinearLayout linearLayout = findViewById(R.id.place_delete);
                    linearLayout.setVisibility(View.VISIBLE);

                } else {
                    place_tv_edit.setText("编辑");
                    LinearLayout linearLayout = findViewById(R.id.place_delete);
                    linearLayout.setVisibility(View.GONE);

                }
                placeCollectionAdapter.setOP(isOP);
                placeCollectionAdapter.notifyDataSetChanged();

                break;
        }
    }

    public void deletePlace(View view) {
        switch (view.getId()){
            case R.id.place_all_checked:
                //全选
                if (i==0) {
                    for (PlaceCollection placeCollection : collections) {
                        placeCollection.setChecked(true);
                        i=1;
                    }
                }else {
                    for (PlaceCollection placeCollection : collections) {
                        placeCollection.setChecked(false);
                        i=0;
                    }
                }
                placeCollectionAdapter.notifyDataSetChanged();
                break;
            case R.id.placedelete:
                //删除
                Log.e("delete","delete");
                checkedLists = new ArrayList<PlaceCollection>();
                unCheckedList = new ArrayList<PlaceCollection>();
                for (PlaceCollection placeCollection : collections) {
                    if (placeCollection.isChecked()) {
                        checkedLists.add(placeCollection);
                    } else {
                        unCheckedList.add(placeCollection);
                    }
                }
                collections.clear();
                collections.addAll(unCheckedList);
                deleteData();

                placeCollectionAdapter.notifyDataSetChanged();
                Log.e("","check list:" + checkedLists.size());
                Log.e("","uncheck list:" + unCheckedList.size());
                break;
        }
    }
    public void deleteData() {
        for (PlaceCollection placeCollection : checkedLists){
            Request request = new Request.Builder()
                    .url(ConfigUtil.SERVER_ADDR +
                            "DeletePlaceCollectionServlet?place_id="+placeCollection.getPlace_id())//请求的地址
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
                    Log.e("异步请求的结果",response.body().string());
                    //不能直接修改UI，如果需要修改UI，需要使用Handler或者EventBus
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = findViewById(R.id.place_count);
                            textView.setText("收藏片场(" + unCheckedList.size() + ")");
                            //收藏数目为0
                            if (unCheckedList.size()==0){
                                Log.e("现有收藏数目为",unCheckedList.size()+"");
                                lvPlace.setVisibility(View.GONE);
                                no_cut.setVisibility(View.VISIBLE);
                                placeCollectionAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }

    }
}