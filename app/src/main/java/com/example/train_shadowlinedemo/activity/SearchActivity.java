package com.example.train_shadowlinedemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.entity.SearchResult;
import com.example.train_shadowlinedemo.view.MovieShow.CustomSearchView;
import com.example.train_shadowlinedemo.view.MovieShow.SearchAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.SearchFlowLayout;
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
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements CustomSearchView.SearchViewListener {

    private Spinner searchType;
    private Button btnDeletehistory;
    private CustomSearchView customSearchView;
    private SearchFlowLayout flowLayout;
    private List<String> histories=new ArrayList<>();//搜索记录
    private ListView lvResults; //搜索结果列表
    private ArrayAdapter<String> hintAdapter;//热搜框列表adapter
    private ArrayAdapter<String> autoCompleteAdapter;//自动补全
    private List<SearchResult> resultData; //搜索结果数据
    private List<SearchResult> dbData=new ArrayList<>();//数据库的结果

    private OkHttpClient okHttpClient;

    //搜索结果列表adapter
    private SearchAdapter resultAdapter;
    //热搜版数据
    private List<String> hintData;
    //搜索过程中自动补全数据
    private List<String> autoCompleteData;
    //默认提示框显示项的个数
    private static int DEFAULT_HINT_SIZE = 5;

    //提示框显示项的个数
    private static int hintSize = DEFAULT_HINT_SIZE;
     //设置提示框显示项的个数
    public static void setHintSize(int hintSize) {
        SearchActivity.hintSize = hintSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchType=findViewById(R.id.spinner_search);
        btnDeletehistory=findViewById(R.id.btn_delete_history);
        customSearchView=findViewById(R.id.custom_searchview);
        okHttpClient=new OkHttpClient();
        //注册事件订阅者
        EventBus.getDefault().register(this);
        //删除历史记录
        btnDeletehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllHistory();
            }
        });

        searchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //当选择时触发该方法，当选择的item和选择前是一样的不会触发该方法
                Log.e("onItemSelected",searchType.getSelectedItem().toString());
                getDbData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getHistoryData();//获取历史记录
        initChildViews();//初始化历史搜索记录
        initData();//初始化数据
        initViews();//初始化控件
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //处理跳转点击事件
                SearchResult result=resultData.get(position);
                String searchType=result.getType();
                switch (searchType){
                    case "电影":
                        getFilmByid(result.getId());
                        break;
                    case "地点":
                        //跳转到地点详情
                        getPlaceByid(result.getId());
                        break;
                    case "城市":
                        //跳转到城市详情

                        Intent intent=new Intent(SearchActivity.this,CityDetailActivity.class);
                        intent.putExtra("id",result.getId()+"");
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void getPlaceByid(int id) {
        FormBody formBody=new FormBody.Builder()
                .add("id",id+"")
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientGetPlaceById")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String json=response.body().string();
                Type type=new TypeToken<Place>(){}.getType();//获取实际类型
                Place place=fromToJson(json,type);
                //修改数据源
                EventBus.getDefault().post(place);
            }
        });
    }

    //删除历史搜索记录
    private void deleteAllHistory() {
        //传入userid
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),"1");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"deleteAllHistory")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","deleteAllHistory发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String json=response.body().string();
                if (json.equals("delete")){
                    histories.clear();
                    EventBus.getDefault().post("delete");
                }
            }
        });
    }
    //处理事件的方法 强制要求在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSearchResult(String result){
        if(result.equals("result")){
            //更新搜索结果
            resultAdapter.notifyDataSetChanged();
        }else if (result.equals("history")){
            initChildViews();
        }else if(result.equals("delete")){
            flowLayout.removeAllViews();
        } else{
            MarginLayoutParams lp = new MarginLayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            lp.topMargin = 10;
            lp.bottomMargin = 10;

            TextView view = new TextView(this);
            view.setText(result);
            view.setTextColor(Color.BLACK);
            view.setPadding(10, 10, 10, 10);
            view.setBackgroundResource(R.drawable.history_textview);
            flowLayout.addView(view,0,lp);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleJump(Film film){
        Intent intent=new Intent(this,MovieDetailActivity.class);
        intent.putExtra("film",new Gson().toJson(film));
        startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleJumpToPlace(Place place){
        Intent intent=new Intent(this,PlaceDetailActivity.class);
        intent.putExtra("place",new Gson().toJson(place));
        startActivity(intent);
    }

    //注销订阅者
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void initViews() {
        lvResults = (ListView) findViewById(R.id.search_results);
        //设置监听
        customSearchView.setSearchViewListener(this);
        //设置adapter
        customSearchView.setTipsHintAdapter(hintAdapter);
        customSearchView.setAutoCompleteAdapter(autoCompleteAdapter);

    }

    private void getFilmByid(int id) {
        FormBody formBody=new FormBody.Builder()
                .add("id",id+"")
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientGetFilmById")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","searchDataBySelectedType发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String json=response.body().string();
                Type type=new TypeToken<List<Film>>(){}.getType();//获取实际类型
                List<Film> list=fromToJson(json,type);
                //修改数据源
                EventBus.getDefault().post(list.get(0));
            }
        });

    }

    //初始化搜索数据
    private void initData() {
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据  
        getAutoCompleteData(null);
        //初始化搜索结果数据  
        getResultData(null);
    }

    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if(!text.equals("")&&text!=null){
                    if (dbData.get(i).getName().contains(text.trim())) {
                        resultData.add(dbData.get(i));
                    }
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_search_result);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            autoCompleteData = new ArrayList<>(hintSize);

//             根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).getName().contains(text.trim())|| dbData.get(i).getEnglishName().contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i).getName());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.clear();
            autoCompleteAdapter.addAll(autoCompleteData);
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    private void getHintData() {
        hintData = new ArrayList<>(hintSize);
        hintData.add("少年的你");
        hintData.add("重庆");
        hintData.add("泰坦尼克号");
        hintData.add("大话西游之大圣娶亲");
        hintData.add("北京");
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    private void getDbData() {
        if(dbData!=null&&dbData.size()>0){
            dbData.clear();
        }
        if(searchType.getSelectedItem().toString().equals("全部")){
            searchDataByall();//可以根据全部搜索出不同内容
        }else{
            searchDataBySelectedType(searchType.getSelectedItem().toString());//可以根据类型选择字段
        }
    }
    private void searchDataBySelectedType(String type) {
        FormBody formBody=new FormBody.Builder()
                .add("type",type)
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientGetDataByType")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","searchDataBySelectedType发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String json=response.body().string();
                Type type=new TypeToken<List<SearchResult>>(){}.getType();//获取实际类型
                List<SearchResult> list=fromToJson(json,type);
                //修改数据源
                dbData.addAll(list);
            }
        });
    }

    private void searchDataByall() {
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"ClientGetAllData")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","searchDataByall发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String json=response.body().string();
                Type type=new TypeToken<List<SearchResult>>(){}.getType();//获取实际类型
                List<SearchResult> list=fromToJson(json,type);
                //修改数据源
                dbData.addAll(list);
            }
        });
    }

    //根据泛型返回解析制定的类型
    public  <T> T fromToJson(String json,Type listType){
        Gson gson = new Gson();
        T t = null;
        t = gson.fromJson(json,listType);
        return t;
    }
    //搜索后加入历史记录
    private void addHistoryData(String text) {
        String userId=1+"";
        FormBody formBody=new FormBody.Builder()
                .add("userId",userId)
                .add("history",text)
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientAddHistory")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","banner发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String str=response.body().string();
                if(str.equals("insert")){
                    EventBus.getDefault().post(text);
                }
            }
        });

    }
    private void getHistoryData() {
        //传入userid
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),"1");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientSearchHistories")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","banner发生错误");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取轮播图列表数据
                String json=response.body().string();
                Type type=new TypeToken<List<String>>(){}.getType();//获取实际类型
                List<String> list=new Gson().fromJson(json,type);
                //修改数据源
                histories.addAll(list);
                //使用EventBus 发布事件更新adapter
                EventBus.getDefault().post("history");
            }
        });
    }

    //初始化历史搜索记录数据view
    private void initChildViews() {
        flowLayout= (SearchFlowLayout) findViewById(R.id.flowlayout);
        MarginLayoutParams lp = new MarginLayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;

        for(int i = 0; i < histories.size(); i ++){
            TextView view = new TextView(this);
            view.setText(histories.get(i));
            view.setTextColor(Color.BLACK);
            view.setPadding(10,10,10,10);
            view.setBackgroundResource(R.drawable.history_textview);
            String text=histories.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSearch(text);
                }
            });
            flowLayout.addView(view,lp);
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     * @param text
     */
    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
        if(resultData!=null&&resultData.size()>0){
            customSearchView.setmResultAdapter(resultAdapter);
            Toast.makeText(this, "完成搜索", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "未搜索到", Toast.LENGTH_SHORT).show();
            lvResults.setVisibility(View.GONE);
        }
        addHistoryData(text);

    }

    @Override
    public void onSearchAgain(String text) {
        if (text.equals("")){
            if (resultAdapter!=null){
                resultData.clear();
                resultAdapter.notifyDataSetChanged();
                lvResults.setVisibility(View.GONE);
            }
        }
    }
}