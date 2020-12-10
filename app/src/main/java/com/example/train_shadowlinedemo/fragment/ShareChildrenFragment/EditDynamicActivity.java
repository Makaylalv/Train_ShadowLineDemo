package com.example.train_shadowlinedemo.fragment.ShareChildrenFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.MainActivity;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.adapter.CustomerEditImgAdapter;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditDynamicActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE=10;
    private Button btnBackDynamic;
    private ImageView ivAddPicture;
    private EditText etShareText;
    private DisplayMetrics dm;
    private LinearLayout llUserLocation;
    private Button btnConfirmPublish;
    private OkHttpClient okHttpClient;
    private List<String> imagePaths=new ArrayList<>();
    //百度地图定位功能
    private TextView tvPersonalLocation;
    private GridView gvEditImgs;
    public LocationClient mLocationClient=null;
    private CustomerEditImgAdapter customerEditImgAdapter;
    //初始化定位的监听器

    private MyLocationListener myListener=new MyLocationListener();
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast toast1=Toast.makeText(EditDynamicActivity.this,"发布成功",Toast.LENGTH_SHORT);
                    toast1.show();
                    break;
                case 2:
                    Toast toast2=Toast.makeText(EditDynamicActivity.this,"发布失败",Toast.LENGTH_SHORT);
                    toast2.show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_dynamic);
        initView();
        setOnclickListener();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                100);
        mLocationClient=new LocationClient(getApplicationContext());
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditDynamicActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            location();
        }

    }
    public void location(){
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true);
        locationClientOption.setIsNeedAddress(true);//设置是否需要获取地址
        locationClientOption.setCoorType("bd0911");
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mLocationClient.setLocOption(locationClientOption);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String location = bdLocation.getCity();
                String addr=bdLocation.getAddrStr();
                Log.e("当前城市为",addr+"");
                tvPersonalLocation.setText(addr);
            }
        });
        mLocationClient.start();
    }
    //初始化布局控件
    public void initView(){
        btnBackDynamic=findViewById(R.id.btn_back_dynamic);
        ivAddPicture=findViewById(R.id.iv_add_picture);
        etShareText=findViewById(R.id.et_share_text);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        llUserLocation=findViewById(R.id.ll_user_location);
        btnConfirmPublish=findViewById(R.id.btn_share_confirm_publish);
        okHttpClient=new OkHttpClient();
        //百度地图
        tvPersonalLocation=findViewById(R.id.tv_personal_location);
        gvEditImgs=findViewById(R.id.gv_edit_imgs);
    }
    //为控件设置点击事件
    private void setOnclickListener(){
        //为返回按钮设置点击事件
        btnBackDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("skipDynamic","skipDynamic");
                intent.setClass(EditDynamicActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //为照片图片设置点击事件
        ivAddPicture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermission();
                        Matisse.from(EditDynamicActivity.this)
                        .choose(MimeType.ofAll())
                        .countable(true)//是否有序
                        .maxSelectable(9)//最大图片数量
                        .gridExpectedSize(dm.widthPixels/3)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .theme(R.style.Matisse_Zhihu) //主题
                        .capture(true)
                                .captureStrategy(new CaptureStrategy(true,"com.example.train_shadowlinedemo.SharedFragment.MyFileProvider"))
                        .forResult(REQUEST_CODE_CHOOSE);
                    }
                }
        );
        //为确定发布按钮设置点击事件
        btnConfirmPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将目前布局中的图片保存到数据库
                ContentResolver contentResolver = getContentResolver();
                List<Uri> selected2=customerEditImgAdapter.getSelected();
                if(selected2==null){
                    //跳转到动态页面
                    Intent intent=new Intent();
                    intent.putExtra("skipDynamic","skipDynamic");
                    intent.setClass(EditDynamicActivity.this, MainActivity.class);
                    addDynamic();
                }else{
                    Log.e("剩下的图片有","数量有"+selected2.size());
                    imagePaths.clear();
                    for(int i=0;i<selected2.size();i++){
                        Cursor cursor=contentResolver.query(selected2.get(i),null,null,null,null);
                        if(cursor.moveToFirst()){
                            String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                            imagePaths.add(imagePath);
                        }
                    }
                    //跳转到动态页面
                    Intent intent=new Intent();
                    intent.putExtra("skipDynamic","skipDynamic");
                    intent.setClass(EditDynamicActivity.this, MainActivity.class);
                    addDynamic();
                    startActivity(intent);

                }

            }
        });
        //为定位按钮获取点击事件
        llUserLocation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }
        );

    }

protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 10 && resultCode == RESULT_OK) {
        List<Uri> selected = Matisse.obtainResult(data);
        ContentResolver contentResolver = getContentResolver();
         customerEditImgAdapter=new CustomerEditImgAdapter(EditDynamicActivity.this,selected,R.layout.item_edit_dynamic_img);
        gvEditImgs.setAdapter(customerEditImgAdapter);

    }
}
//动态申请权限
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA
            ,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PRECISE_PHONE_STATE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }
    //将动态信息上传到数据库
    private void addDynamic(){
        Dynamic dynamic =new Dynamic();
        dynamic.setUserName(LoginActivity.user.getUser_name());
        dynamic.setUserId(LoginActivity.user.getUser_id());
        dynamic.setLikeUser(null);
        dynamic.setDynamicPlace(tvPersonalLocation.getText().toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        dynamic.setDynamicTime(df.format(new Date()));
        dynamic.setDynamicContent(etShareText.getText().toString());
        List<String> imgs=new ArrayList<>();
        long time=System.currentTimeMillis();
        for(int i=0;i<imagePaths.size();i++){
            imgs.add(time+"img"+i);
            uploadFile(imagePaths.get(i),imgs.get(i));
        }
        Log.e("选择的图片路径为",imagePaths.toString());
        dynamic.setDynamicImgs(imgs);
        //创建RequestBody（请求体对象）
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),new Gson().toJson(dynamic));
        Log.e("dddddddddd",dynamic.toString());
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"AddDynamicServlet")
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
                        Message msg=handler.obtainMessage();
                        msg.what=2;
                        handler.sendMessage(msg);
                    }
                }.start();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 Log.e("异步请求的结果",response.body().toString());
                 Log.e("登录的结果","请求成功");
                 new Thread(){
                     @Override
                     public void run() {
                         super.run();
                         Message msg=handler.obtainMessage();
                         msg.what=1;
                         handler.sendMessage(msg);
                     }
                 }.start();
            }
        });
    }
    public void uploadFile(String filePath,String fileName){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                100);
        File file = new File(filePath);
        Log.e("照片的路径为",filePath);
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/octet-stream"),file);
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"UploadImgServlet?filename="+fileName)
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

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果",response.body().toString());
                Log.e("登录的结果","请求成功");
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == 0){
            location();
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("addr",tvPersonalLocation.getText().toString());
    }
    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

}
 class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
       double  latitude= location.getLatitude();
       double longitude=  location.getLongitude();
       Log.e("当前位置的经纬度信息为",latitude+",,"+longitude);
       Log.e("当前位置的详细信息为",location.getAddrStr()+"");
       LatLng point=new LatLng(latitude,longitude);
       GeoCoder mCoder= GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener=new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    return;
                } else {
                    //详细地址
                    String address = reverseGeoCodeResult.getAddress();
                    Log.e("当前位置的详细地址为",address);
                    //行政区号
                    int adCode = reverseGeoCodeResult. getCityCode();
                }
            }
        };
        mCoder.setOnGetGeoCodeResultListener(listener);
        mCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(point)
                // 设置是否返回新数据 默认值0不返回，1返回
                .newVersion(1)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(500));
        mCoder.destroy();
    }

 }
