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

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.MovieDetailActivity;
import com.example.train_shadowlinedemo.activity.MovieTypeActivity;
import com.example.train_shadowlinedemo.activity.SearchActivity;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.view.MovieShow.HotFilmGridViewAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.HotFilmListViewAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.ImageAdapter;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FilmFragment  extends Fragment {
    private View root;
    private Banner banner;
    private RecyclerView rvNewFilmList;
    private List<Film> bannerList=new ArrayList<>();//轮播图
    private List<Film> newFilmList=new ArrayList<>();//最新电影
    private List<Film> hotFilmList=new ArrayList<>();//热门电影
    private ImageView ivHotMovie;
    private TextView tvHotMovieName;
    private TextView tvHotMovieFromCountry;
    private TextView tvHotMovieoutTime;
    private TextView tvHotMovieType;

    private GridView gvHotFilm;
    private ListView lvHotFilm;

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
//        getBannerFilm();//获取轮播图电影的数据
//      findNewFilmList();//获取最新电影数据
//       /// findHotFilmList();//获取热门电影的数据
//        InitBanner();//初始化轮播图
//        initNewFilmData();//初始化最新电影数据
//        initHotMovie();//初始化热门电影

        //点击跳转
        gvHotFilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(), MovieDetailActivity.class);
                startActivity(intent);
            }
        });
        lvHotFilm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),MovieDetailActivity.class);
                startActivity(intent);
            }
        });
        ivHotMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MovieDetailActivity.class);
                startActivity(intent);
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
        if (msg.equals("update")){
            //刷新adapter
            newFilmAdapter.notifyDataSetChanged();
            gridViewAdapter.notifyDataSetChanged();
            listViewAdapter.notifyDataSetChanged();
            imageAdapter.notifyDataSetChanged();

        }
    }

    //从数据库获取热门电影数据
    private void findHotFilmList() {
        Request request=new Request.Builder()
                .url(ConfigUtil.SERVER_ADDR+"")
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
                List<Film> filmList=new Gson().fromJson(filmJson,type);
                //修改数据源
                hotFilmList.addAll(filmList);
                //使用EventBus 发布事件更新adapter
                EventBus.getDefault().post("update");
            }
        });


    }

    //初始化热门电影
    private void initHotMovie() {
        //初始化第一个
        ivHotMovie.setImageResource(R.drawable.newfilm_p1);
        tvHotMovieName.setText("少年的你");
        tvHotMovieFromCountry.setText("中国大陆");
        tvHotMovieType.setText("爱情/喜剧");
        tvHotMovieoutTime.setText("2020");

        gridViewAdapter=new HotFilmGridViewAdapter(getContext(),newFilmList,R.layout.item_gv_hotfilm);
        listViewAdapter=new HotFilmListViewAdapter(getContext(),newFilmList,R.layout.item_left_hotfilm,R.layout.item_right_hotfilm);
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
    private void getBannerFilm() {
        Request request=new Request.Builder()
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
                List<Film> filmList=new Gson().fromJson(filmJson,type);
                //修改数据源
                bannerList.addAll(filmList);
                //使用EventBus 发布事件更新adapter
                EventBus.getDefault().post("update");
            }
        });

    }

    //获取最新更新电影的列表
    private void findNewFilmList() {

        Film film1=new Film();
        film1.setFilmId(1);
        film1.setFilmDirector("曾国祥");
        film1.setFilmEnglishname("Young you");
        film1.setFilmName("少年的你1");
        film1.setFilmInfo("一场高考前夕的校园意外，改变了两个少年的命运。陈念(周冬雨)是学校里的优等生，考上好大学是她唯一的念头。同班同学的意外坠楼牵扯出的故事，陈念也被一点点卷入其中…在她最孤独的时刻，一个叫小北（易烊千玺）的少年闯入了她的世界。");
        film1.setFilmTostar("周冬雨&易烊千玺");
        film1.setFilmReleasetime("2019");
        film1.setFilmProducercountry("中国");
        film1.setFilmType("爱情/犯罪/剧情");
        film1.setFilmImg("R.drawable.newfilm_p1");
        film1.setFlimMapImg("film/map/img1.jpg");

        Film film2=new Film();
        film2.setFilmId(1);
        film2.setFilmDirector("曾国祥");
        film2.setFilmEnglishname("Young you");
        film2.setFilmName("少年的你2");
        film2.setFilmInfo("一场高考前夕的校园意外，改变了两个少年的命运。陈念(周冬雨)是学校里的优等生，考上好大学是她唯一的念头。同班同学的意外坠楼牵扯出的故事，陈念也被一点点卷入其中…在她最孤独的时刻，一个叫小北（易烊千玺）的少年闯入了她的世界。");
        film2.setFilmTostar("周冬雨&易烊千玺");
        film2.setFilmReleasetime("2019");
        film2.setFilmProducercountry("中国");
        film2.setFilmType("爱情/犯罪/剧情");
        film2.setFilmImg("R.drawable.newfilm_p1");
        film2.setFlimMapImg("film/map/img1.jpg");

        Film film3=new Film();
        film3.setFilmId(1);
        film3.setFilmDirector("曾国祥");
        film3.setFilmEnglishname("Young you");
        film3.setFilmName("少年的你3");
        film3.setFilmInfo("一场高考前夕的校园意外，改变了两个少年的命运。陈念(周冬雨)是学校里的优等生，考上好大学是她唯一的念头。同班同学的意外坠楼牵扯出的故事，陈念也被一点点卷入其中…在她最孤独的时刻，一个叫小北（易烊千玺）的少年闯入了她的世界。");
        film3.setFilmTostar("周冬雨&易烊千玺");
        film3.setFilmReleasetime("2019");
        film3.setFilmProducercountry("中国");
        film3.setFilmType("爱情/犯罪/剧情");
        film3.setFilmImg("R.drawable.newfilm_p1");
        film3.setFlimMapImg("film/map/img1.jpg");

        Film film4=new Film();
        film4.setFilmId(1);
        film4.setFilmDirector("曾国祥");
        film4.setFilmEnglishname("Young you");
        film4.setFilmName("少年的你4");
        film4.setFilmInfo("一场高考前夕的校园意外，改变了两个少年的命运。陈念(周冬雨)是学校里的优等生，考上好大学是她唯一的念头。同班同学的意外坠楼牵扯出的故事，陈念也被一点点卷入其中…在她最孤独的时刻，一个叫小北（易烊千玺）的少年闯入了她的世界。");
        film4.setFilmTostar("周冬雨&易烊千玺");
        film4.setFilmReleasetime("2019");
        film4.setFilmProducercountry("中国");
        film4.setFilmType("爱情/犯罪/剧情");
        film4.setFilmImg("R.drawable.newfilm_p1");
        film4.setFlimMapImg("film/map/img1.jpg");

        newFilmList.add(film1);
        newFilmList.add(film2);
        newFilmList.add(film3);
        newFilmList.add(film4);

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
    private void InitBanner() {
        imageAdapter = new ImageAdapter(bannerList,getContext());
        banner.setAdapter(imageAdapter)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getContext()))
                .setBannerRound(BannerUtils.dp2px(2))//圆角
                .setPageTransformer(new ScaleInTransformer())//变换模式
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(Object data, int position) {
                        Log.e("banner", "点击第" + position + "个");
                    }
                });
    }
}
