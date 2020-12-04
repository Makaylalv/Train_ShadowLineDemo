package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieCollectionActivity extends AppCompatActivity {
    private List<FilmCollection> collections = new ArrayList<>();
    private List<FilmCollection> checkedLists = new ArrayList<>();
    private List<FilmCollection> unCheckedList = new ArrayList<>();
    private OkHttpClient okHttpClient;
    private TextView movie_tv_edit;
    private ListView lvFilm;
    private boolean isOP;
    int i = 0;
    private MovieCollectionAdapter movieCollectionAdapter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_collection);
        movie_tv_edit = findViewById(R.id.movie_tv_edit);
        //1.创建OkHttpClient对象,获取数据
        okHttpClient = new OkHttpClient();
        //获取数据
        getAsync();


    }
    //初始化数据
    //异步的get请求
    public void getAsync(){
        //2.创建Request请求对象(默认使用get请求)
        Request request = new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR +
                        "GetFilmCollectionsServlet?user_id="+ LoginActivity.user.getUser_id())//请求的地址
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
                if (response.isSuccessful()){
                    String message = response.body().string();
                    Log.e("message",message.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(message);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    FilmCollection collection = new FilmCollection();
                                    collection.setFilm_name(jsonObject.getString("name"));
                                    collection.setFilm_city(jsonObject.getString("city"));
                                    collection.setFilm_english(jsonObject.getString("englishName"));
                                    collection.setFilm_type(jsonObject.getString("type"));
                                    collection.setFilm_place(jsonObject.getString("place"));
                                    collection.setFilm_id(jsonObject.getInt("film_id"));
                                    collections.add(collection);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("collections",collections.toString());
                            movieCollectionAdapter = new MovieCollectionAdapter(MovieCollectionActivity.this,collections,R.layout.item_movie_collection);
                            lvFilm = findViewById(R.id.li_movie);
                            lvFilm.setAdapter(movieCollectionAdapter);
                            TextView tv_count = findViewById(R.id.movie_count);
                            tv_count.setText("收藏电影("+movieCollectionAdapter.getCount()+")");
                            lvFilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    public void movieClicked(View view) {
        switch (view.getId()) {
            case R.id.movie_edit:
                Log.e("edit", "edit");
                isOP = !isOP;

                if (isOP) {
                    movie_tv_edit.setText("完成");
                    LinearLayout linearLayout = findViewById(R.id.movie_delete);
                    linearLayout.setVisibility(View.VISIBLE);

                } else {
                    movie_tv_edit.setText("编辑");
                    LinearLayout linearLayout = findViewById(R.id.movie_delete);
                    linearLayout.setVisibility(View.GONE);

                }
                movieCollectionAdapter.setOP(isOP);
                movieCollectionAdapter.notifyDataSetChanged();

                break;
        }
    }
    public void deleteMovie(View view) {
        switch (view.getId()){
            case R.id.movie_all_checked:
                //全选
                if (i==0) {
                    for (FilmCollection filmCollection : collections) {
                        filmCollection.setChecked(true);
                        i=1;
                    }
                }else {
                    for (FilmCollection filmCollection : collections) {
                        filmCollection.setChecked(false);
                        i=0;
                    }
                }
                movieCollectionAdapter.notifyDataSetChanged();
                break;
            case R.id.moviedelete:
                //删除
                Log.e("delete","delete");
                checkedLists = new ArrayList<FilmCollection>();
                unCheckedList = new ArrayList<FilmCollection>();
                for (FilmCollection filmCollection : collections) {
                    if (filmCollection.isChecked()) {
                        checkedLists.add(filmCollection);
                    } else {
                        unCheckedList.add(filmCollection);
                    }
                }
                collections.clear();
                collections.addAll(unCheckedList);
                deleteData();

                movieCollectionAdapter.notifyDataSetChanged();
                Log.e("","check list:" + checkedLists.size());
                Log.e("","uncheck list:" + unCheckedList.size());
                break;
        }

    }
    public void deleteData() {
        for (FilmCollection filmCollection : checkedLists){
            Request request = new Request.Builder()
                    .url(ConfigUtil.SERVER_ADDR +
                            "DeleteFilmCollectionServlet?film_id="+filmCollection.getFilm_id())//请求的地址
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
                            TextView textView = findViewById(R.id.movie_count);
                            textView.setText("收藏电影(" + unCheckedList.size() + ")");
                        }
                    });
                }
            });
        }
    }

}