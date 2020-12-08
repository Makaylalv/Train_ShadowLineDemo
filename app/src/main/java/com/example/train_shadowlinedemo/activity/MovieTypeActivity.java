package com.example.train_shadowlinedemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.SearchResult;
import com.example.train_shadowlinedemo.view.MovieShow.SearchAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.SelectFilmAdapter;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieTypeActivity extends AppCompatActivity {
    private RecyclerView rvIsnewFilm;
    private RecyclerView rvOutTime;
    private RecyclerView rvOutCountry;
    private RecyclerView rvFilmType;
    private ListView lvFilmResult;
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<String> list4;
    private List<SearchResult> results=new ArrayList<>();
    private String isNew=null;
    private String outTime=null;
    private String outCountry=null;
    private String filmType=null;
    private OkHttpClient okHttpClient;
    private SearchAdapter searchAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_type);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        //注册
        EventBus.getDefault().register(this);
        findView();
        initView();
        getResultData("全部","全部","全部",type);
        isNew="全部";
        outTime="全部";
        outCountry="全部";
        filmType=type;
        Log.e("MovieTypeActivity",type);
//        LinearLayout linearLayout=findViewById(R.id.linear);
        SelectFilmAdapter adapter= (SelectFilmAdapter) rvFilmType.getAdapter();
        Log.e("1111111",""+adapter.getItemCount());
        rvIsnewFilm.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view=rvIsnewFilm.getLayoutManager().findViewByPosition(0);
                TextView textView=view.findViewById(R.id.tv_select_film);
                textView.setBackgroundColor(Color.BLACK);
                textView.setTextColor(Color.WHITE);
                rvIsnewFilm.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        rvOutTime.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view=rvOutTime.getLayoutManager().findViewByPosition(0);
                TextView textView=view.findViewById(R.id.tv_select_film);
                textView.setBackgroundColor(Color.BLACK);
                textView.setTextColor(Color.WHITE);
                rvOutTime.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        rvOutCountry.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view=rvOutCountry.getLayoutManager().findViewByPosition(0);
                TextView textView=view.findViewById(R.id.tv_select_film);
                textView.setBackgroundColor(Color.BLACK);
                textView.setTextColor(Color.WHITE);
                rvOutCountry.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }

        });
        rvFilmType.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = rvFilmType.getWidth();
                int height = rvFilmType.getHeight();
                if (width > 0 && height > 0) {
                    rvFilmType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if(rvFilmType!=null&&adapter.getItemCount()>0){
                    for(int i=0;i<adapter.getItemCount();i++){
                        if(adapter.getmDatas().get(i).equals(type)){
                            Log.e("333",width+"");
                            if(i>5){
                                rvFilmType.scrollBy(width*2,0);
                            }
                            View childView =rvFilmType.getLayoutManager().findViewByPosition(i);
                            if(childView!=null){
                                TextView textView=childView.findViewById(R.id.tv_select_film);
                                textView.setBackgroundColor(Color.BLACK);
                                textView.setTextColor(Color.WHITE);
                            }
//                            rvFilmType.smoothScrollToPosition(i+2);
                            break;
                        }
                    }
                }

            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(String str){
        if(str.equals("getSelectResult")){
            searchAdapter.notifyDataSetChanged();
        }
    }
    //获取筛选的数据
    private void getResultData(String isNew,String time,String country,String type) {
        FormBody formBody=new FormBody.Builder()
                .add("isNew",isNew)
                .add("time",time)
                .add("country",country)
                .add("type",type).build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientGetSelectFilms")
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
                List<SearchResult> list=new Gson().fromJson(json,type);
                //修改数据源
                if(results.size()>0){
                    results.clear();
                }
                results.addAll(list);
                EventBus.getDefault().post("getSelectResult");
            }
        });



    }

    private void initView() {
        LinearLayoutManager linearLayoutManager1;
        //设置布局管理器，水平设置LinearLayoutManager.HORIZONTAL
        linearLayoutManager1=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager3=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager linearLayoutManager4=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //创建适配器，将数据传递给适配器
        SelectFilmAdapter adapter1=new SelectFilmAdapter(this,list1,R.layout.item_select_film);
        SelectFilmAdapter adapter2=new SelectFilmAdapter(this,list2,R.layout.item_select_film);
        SelectFilmAdapter adapter3=new SelectFilmAdapter(this,list3,R.layout.item_select_film);
        SelectFilmAdapter adapter4=new SelectFilmAdapter(this,list4,R.layout.item_select_film);
        //固定RecycleView大小,当知道adapter内item的改变不会影响RecycleView宽高的时候设置
        rvIsnewFilm.setHasFixedSize(true);
        rvOutTime.setHasFixedSize(true);
        rvFilmType.setHasFixedSize(true);
        rvOutCountry.setHasFixedSize(true);
        //设置布局管理器
        rvIsnewFilm.setLayoutManager(linearLayoutManager1);
        rvOutTime.setLayoutManager(linearLayoutManager2);
        rvFilmType.setLayoutManager(linearLayoutManager3);
        rvOutCountry.setLayoutManager(linearLayoutManager4);


        searchAdapter=new SearchAdapter(this,results,R.layout.item_search_result);
        lvFilmResult.setAdapter(searchAdapter);
        adapter1.setOnItemClickLitener(new SelectFilmAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view;
                isNew=tv.getText().toString().trim();
                getResultData(isNew,outTime,outCountry,filmType);
            }
        });
        adapter2.setOnItemClickLitener(new SelectFilmAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view;
                outTime=tv.getText().toString().trim();
                getResultData(isNew,outTime,outCountry,filmType);
            }
        });
        adapter3.setOnItemClickLitener(new SelectFilmAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view;
                outCountry =tv.getText().toString().trim();
                getResultData(isNew,outTime,outCountry,filmType);
            }
        });
        adapter4.setOnItemClickLitener(new SelectFilmAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv=(TextView)view;
                filmType=tv.getText().toString().trim();
                getResultData(isNew,outTime,outCountry,filmType);
            }
        });

        //设置适配器adapter
        rvIsnewFilm.setAdapter(adapter1);
        rvOutTime.setAdapter(adapter2);
        rvOutCountry.setAdapter(adapter3);
        rvFilmType.setAdapter(adapter4);
    }

    //获取控件
    private void findView() {
        okHttpClient=new OkHttpClient();
        rvIsnewFilm=findViewById(R.id.rv_film_isnew);
        rvOutTime=findViewById(R.id.rv_film_outtime);
        rvOutCountry=findViewById(R.id.rv_film_outcountry);
        rvFilmType=findViewById(R.id.rv_film_type);
        lvFilmResult=findViewById(R.id.lv_film_result);
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        list3=new ArrayList<>();
        list4=new ArrayList<>();
        list1.add("全部");
        list1.add("新片");
        list1.add("经典");
        list2.add("全部");
        list2.add("2020");
        list2.add("2019");
        list2.add("2018");
        list2.add("2017");
        list2.add("2016");
        list2.add("2015");
        list2.add("2014-2011");
        list2.add("2010-2000");
        list2.add("1999-1900");
        list3.add("全部");
        list3.add("中国大陆");
        list3.add("中国香港");
        list3.add("美国");
        list3.add("英国");
        list3.add("韩国");
        list3.add("日本");
        list4.add("全部");
        list4.add("爱情");
        list4.add("喜剧");
        list4.add("科幻");
        list4.add("动漫");
        list4.add("惊悚");
        list4.add("青春");
        list4.add("犯罪");
        list4.add("悬疑");
        list4.add("文艺");
        list4.add("动作");
        list4.add("励志");
    }
}