package com.example.train_shadowlinedemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.MovieDetailActivity;
import com.example.train_shadowlinedemo.activity.MovieTypeActivity;
import com.example.train_shadowlinedemo.activity.SearchActivity;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.view.MovieShow.HotFilmGridViewAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.HotFilmListViewAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.ImageAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.ListViewForScrollView;
import com.example.train_shadowlinedemo.view.MovieShow.NewFilmAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.util.BannerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class FilmFragment  extends Fragment {
    private View root;
    private Banner banner;
    private RecyclerView rvNewFilmList;
    private List<Film> bannerList=new ArrayList<>();//轮播图
    private List<Film> newFilmList=new ArrayList<>();//最新电影
    private List<Film> hotFilmList=new ArrayList<>();//热门电影
    private List<Film> gvHotFilmList=new ArrayList<>();
    private List<Film> lvHotFilmList=new ArrayList<>();
    private ImageView ivHotMovie;
    private TextView tvHotMovieName;
    private TextView tvHotMovieFromCountry;
    private TextView tvHotMovieoutTime;
    private TextView tvHotMovieType;

    private GridView gvHotFilm;
    private ListViewForScrollView lvHotFilm;

    private OkHttpClient okHttpClient;
    private NewFilmAdapter newFilmAdapter;
    private HotFilmGridViewAdapter gridViewAdapter;
    private HotFilmListViewAdapter listViewAdapter;
    private ImageAdapter imageAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root==null){
            root=inflater.inflate(R.layout.fragment_film,container,false);
        }
        okHttpClient=new OkHttpClient();
        //注册事件订阅者
        EventBus.getDefault().register(this);
        ScrollView scrollView=root.findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);//将ScrollView滚动到最顶端
        findView();//获取控件
        findHotFilmList();//获取热门电影的数据
        findBannerFilm();//获取轮播图电影的数据
        findNewFilmList();//获取最新电影数据
        initBanner();//初始化轮播图
        initHotMovie();//初始化热门电影
        initNewFilmData();//初始化最新电影数据


        //点击跳转
        gvHotFilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jumpToDetail(hotFilmList.get(i+1));
            }
        });
        lvHotFilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                jumpToDetail(hotFilmList.get(5+i));
            }
        });
        ivHotMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToDetail(hotFilmList.get(0));

            }
        });
        return root;
    }

    //注销订阅者
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //处理事件的方法 强制要求在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(String msg){
        if (msg.equals("hotfilmupdate")){
            //刷新adapter
            //初始化第一个
            if(hotFilmList.size()>0){
                Film film=hotFilmList.get(0);
                Glide.with(getContext())
                        .load(ConfigUtil.SERVER_ADDR+film.getFilmImg())
                        .fitCenter()
                        .into(ivHotMovie);
                tvHotMovieName.setText(film.getFilmName());
                tvHotMovieFromCountry.setText(film.getFilmProducercountry());
                tvHotMovieType.setText(film.getFilmType());
                tvHotMovieoutTime.setText(film.getFilmReleasetime());

                if (hotFilmList.size()>5 && gvHotFilmList.size()==0 && lvHotFilmList.size()==0){
                    Log.e("update",hotFilmList.size()+"");
                    gvHotFilmList.addAll(hotFilmList.subList(1,5));
                    lvHotFilmList.addAll(hotFilmList.subList(5,hotFilmList.size()));
                }
                gridViewAdapter.notifyDataSetChanged();
                listViewAdapter.notifyDataSetChanged();
            }

            if(msg.equals("newfilmupdate")){
                newFilmAdapter.notifyDataSetChanged();
            }
           if (msg.equals("bannerupdate")){
               imageAdapter.notifyDataSetChanged();
           }

        }
    }

    //从数据库获取热门电影数据
    private void findHotFilmList() {
        if (hotFilmList.size()==0){
            Request request=new Request.Builder()
                    .url(ConfigUtil.SERVER_ADDR+"ClientGetHotFilms")
                    .build();
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("onFailure","hotFilm发生错误");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //获取轮播图列表数据
                    String filmJson=response.body().string();
                    Type type=new TypeToken<List<Film>>(){}.getType();//获取实际类型
                    List<Film> filmList=fromToJson(filmJson,type);
                    //修改数据源
                    hotFilmList.addAll(filmList);

                    //使用EventBus 发布事件更新adapter
                    EventBus.getDefault().post("hotfilmupdate");
                }
            });
        }

    }
    //根据泛型返回解析制定的类型
    public  <T> T fromToJson(String json,Type listType){
        Gson gson = new Gson();
        T t = null;
        t = gson.fromJson(json,listType);
        return t;
    }
    //初始化热门电影
    private void initHotMovie() {
        gridViewAdapter=new HotFilmGridViewAdapter(getContext(),gvHotFilmList,R.layout.item_gv_hotfilm);
        listViewAdapter=new HotFilmListViewAdapter(getContext(),lvHotFilmList,R.layout.item_left_hotfilm,R.layout.item_right_hotfilm);
        gvHotFilm.setAdapter(gridViewAdapter);
        lvHotFilm.setAdapter(listViewAdapter);
    }

    //获取控件
    private void findView() {
        Button btnSearch=root.findViewById(R.id.btn_search);
        banner=root.findViewById(R.id.movie_banner);
        LinearLayout linearType1=root.findViewById(R.id.linear_type1);
        LinearLayout linearType2=root.findViewById(R.id.linear_type2);
        LinearLayout linearType3=root.findViewById(R.id.linear_type3);
        LinearLayout linearType4=root.findViewById(R.id.linear_type4);
        LinearLayout linearType5=root.findViewById(R.id.linear_type5);
        LinearLayout linearType6=root.findViewById(R.id.linear_type6);
        LinearLayout linearType7=root.findViewById(R.id.linear_type7);
        MyOnClickListener listener=new MyOnClickListener();
        linearType1.setOnClickListener(listener);
        linearType2.setOnClickListener(listener);
        linearType3.setOnClickListener(listener);
        linearType4.setOnClickListener(listener);
        linearType5.setOnClickListener(listener);
        linearType6.setOnClickListener(listener);
        linearType7.setOnClickListener(listener);
        btnSearch.setOnClickListener(listener);

        ivHotMovie=root.findViewById(R.id.iv_hotmovie_img);
        tvHotMovieName=root.findViewById(R.id.tv_hotmovie_name);
        tvHotMovieFromCountry=root.findViewById(R.id.tv_hotmovie_fromcountry);
        tvHotMovieoutTime=root.findViewById(R.id.tv_hotmovie_outtime);
        tvHotMovieType=root.findViewById(R.id.tv_hotmovie_type);

        gvHotFilm=root.findViewById(R.id.gv_hotmovie);
        lvHotFilm=root.findViewById(R.id.lv_hotmovie);
    }

    //获取轮播图内电影内容
    private void findBannerFilm() {
        if(bannerList.size()==0){
            RequestBody requestBody = RequestBody.create(MediaType.parse(
                    "text/plain;charset=utf-8"),"requestBannerFilm");
            Request request=new Request.Builder()
                    .post(requestBody)
                    .url(ConfigUtil.SERVER_ADDR+"ClientGetBannerFilms")
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
                    String filmJson=response.body().string();
                    Type type=new TypeToken<List<Film>>(){}.getType();//获取实际类型
                    List<Film> filmList=fromToJson(filmJson,type);
                    //修改数据源
                    bannerList.addAll(filmList);
                    //使用EventBus 发布事件更新adapter
                    EventBus.getDefault().post("bannerupdate");
                }
            });
        }
    }

    //获取最新更新电影的列表
    private void findNewFilmList() {
        if(newFilmList.size()==0){
            Request request=new Request.Builder()
                    .url(ConfigUtil.SERVER_ADDR+"ClientGetNewFilms")
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
                    String filmJson=response.body().string();
                    Type type=new TypeToken<List<Film>>(){}.getType();//获取实际类型
                    List<Film> filmList1=fromToJson(filmJson,type);
                    //修改数据源
                    newFilmList.addAll(filmList1);
                    //使用EventBus 发布事件更新adapter
                    EventBus.getDefault().post("newfilmupdate");
                }
            });
        }
    }

    //初始化RecyclerView 最新电影
    private void initNewFilmData() {
        rvNewFilmList=root.findViewById(R.id.rv_horizontal_list);
        LinearLayoutManager linearLayoutManager;
        //设置布局管理器，垂直设置LinearLayoutManager.VERTICAL
        linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        //创建适配器，将数据传递给适配器
        newFilmAdapter=new NewFilmAdapter(getContext(),newFilmList,R.layout.item_newfilm);
        //固定RecycleView大小,当知道adapter内item的改变不会影响RecycleView宽高的时候设置
        rvNewFilmList.setHasFixedSize(true);
        //设置布局管理器
        rvNewFilmList.setLayoutManager(linearLayoutManager);
        //设置适配器adapter
        rvNewFilmList.setAdapter(newFilmAdapter);
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_search:
                    //点击搜索按钮跳转到搜索页面
                    Intent intent=new Intent(getContext(), SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.linear_type1:
                    Intent intent1=new Intent(getContext(), MovieTypeActivity.class);
                    intent1.putExtra("type","爱情");
                    startActivity(intent1);
                    break;
                case R.id.linear_type2:
                    Intent intent2=new Intent(getContext(), MovieTypeActivity.class);
                    intent2.putExtra("type","喜剧");
                    startActivity(intent2);
                    break;
                case R.id.linear_type3:
                    Intent intent3=new Intent(getContext(), MovieTypeActivity.class);
                    intent3.putExtra("type","科幻");
                    startActivity(intent3);
                    break;
                case R.id.linear_type4:
                    Intent intent4=new Intent(getContext(), MovieTypeActivity.class);
                    intent4.putExtra("type","动漫");
                    startActivity(intent4);
                    break;
                case R.id.linear_type5:
                    Intent intent5=new Intent(getContext(), MovieTypeActivity.class);
                    intent5.putExtra("type","惊悚");
                    startActivity(intent5);
                    break;
                case R.id.linear_type6:
                    Intent intent6=new Intent(getContext(), MovieTypeActivity.class);
                    intent6.putExtra("type","青春");
                    startActivity(intent6);
                    break;
                case R.id.linear_type7:
                    Intent intent7=new Intent(getContext(), MovieTypeActivity.class);
                    intent7.putExtra("type","犯罪");
                    startActivity(intent7);
                    break;
            }
        }
    }

    //轮播图初始化
    private void initBanner() {
        imageAdapter = new ImageAdapter(bannerList,getContext());
        banner.setAdapter(imageAdapter)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getContext()))
                .setBannerRound(BannerUtils.dp2px(2))//圆角
                .setPageTransformer(new ScaleInTransformer())//变换模式
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(Object data, int position) {
                        jumpToDetail(bannerList.get(position));
                    }
                });
    }
    private void jumpToDetail(Film film){
        Intent intent=new Intent(getContext(),MovieDetailActivity.class);
        intent.putExtra("film",new Gson().toJson(film));
        startActivity(intent);
    }
}
