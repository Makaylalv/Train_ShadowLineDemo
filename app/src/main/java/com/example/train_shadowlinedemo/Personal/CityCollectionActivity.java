package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.CityDetailActivity;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.activity.SearchActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CityCollectionActivity extends AppCompatActivity {
    private List<CityCollection> cityCollections = new ArrayList<>();
    private List<CityCollection> checkedLists = new ArrayList<>();
    private List<CityCollection> unCheckedList = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private ListView lvCity;
    private ImageView no_city;
    private ImageView back;
    private TextView tv_edit;
    private boolean isOP;
    int i = 0;
    private Handler handler = new Handler();
    private CityCollectionAdapter cityCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city_collection);
        lvCity = findViewById(R.id.li_city);
        tv_edit = findViewById(R.id.tv_edit);
        no_city = findViewById(R.id.nocity);
        back = findViewById(R.id.lv_city_title);
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
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CityCollection cityCollection=cityCollections.get(i);
                Intent intent=new Intent(CityCollectionActivity.this, CityDetailActivity.class);
                intent.putExtra("id",cityCollection.getCity_id()+"");
                startActivity(intent);
            }
        });
    }
    public void getAsync() {
        //2.创建Request请求对象(默认使用get请求)
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+
                        "GetCityCollectionServlet?user_id="+ LoginActivity.user.getUser_id())//请求的地址
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(message);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CityCollection collection = new CityCollection();
                                    collection.setCity_name(jsonObject.getString("city_name"));
                                    collection.setCity_english(jsonObject.getString("city_english"));
                                    collection.setCity_id(jsonObject.getInt("city_id"));
                                    collection.setImg(jsonObject.getString("img"));
                                    cityCollections.add(collection);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            cityCollectionAdapter = new CityCollectionAdapter(CityCollectionActivity.this, cityCollections, R.layout.item_city_collection);
                            lvCity = findViewById(R.id.li_city);
                            lvCity.setAdapter(cityCollectionAdapter);
                            TextView tv_count = findViewById(R.id.city_count);
                            if (cityCollectionAdapter.getCount()==0){
                                no_city.setVisibility(View.VISIBLE);
                                cityCollectionAdapter.notifyDataSetChanged();
                                lvCity.setVisibility(View.GONE);
                            }
                            tv_count.setText("收藏城市(" + cityCollectionAdapter.getCount() + ")");

                        }
                    });
                }
            }
        });
    }

    //编辑
    public void cityClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_edit:
                isOP = !isOP;

                if (isOP) {
                    tv_edit.setText("完成");
                    LinearLayout linearLayout = findViewById(R.id.lv_delete);
                    linearLayout.setVisibility(View.VISIBLE);

                } else {
                    tv_edit.setText("编辑");
                    LinearLayout linearLayout = findViewById(R.id.lv_delete);
                    linearLayout.setVisibility(View.GONE);

                }
                cityCollectionAdapter.setOP(isOP);
                cityCollectionAdapter.notifyDataSetChanged();

                break;
        }
    }

    public void delete(View view) {
        switch (view.getId()){
            case R.id.all_checked:
                //全选
                if (i==0) {
                    for (CityCollection cityCollection : cityCollections) {
                        cityCollection.setChecked(true);
                        i=1;
                    }
                }else {
                    for (CityCollection cityCollection : cityCollections) {
                        cityCollection.setChecked(false);
                        i=0;
                    }
                }
                cityCollectionAdapter.notifyDataSetChanged();
                break;
            case R.id.delete:
                //删除
                checkedLists = new ArrayList<CityCollection>();
                unCheckedList = new ArrayList<CityCollection>();
                for (CityCollection cityCollection : cityCollections) {
                    if (cityCollection.isChecked()) {
                        checkedLists.add(cityCollection);
                    } else {
                        unCheckedList.add(cityCollection);
                    }
                }
                cityCollections.clear();
                cityCollections.addAll(unCheckedList);
                deleteData();
                cityCollectionAdapter.notifyDataSetChanged();
            break;
        }
    }
    public void deleteData() {
        for (CityCollection cityCollection : checkedLists){
            Request request = new Request.Builder()
                    .url( ConfigUtil.SERVER_ADDR +
                            "DeleteCityCollectionServlet?city_id="+cityCollection.getCity_id())//请求的地址
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
                    //不能直接修改UI，如果需要修改UI，需要使用Handler或者EventBus
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = findViewById(R.id.city_count);
                            textView.setText("收藏城市(" + unCheckedList.size() + ")");
                            if (unCheckedList.size()==0){
                                no_city.setVisibility(View.VISIBLE);
                                cityCollectionAdapter.notifyDataSetChanged();
                                lvCity.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }

    }

}