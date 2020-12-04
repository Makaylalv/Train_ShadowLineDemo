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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ImageView ivAddImg1;
    private ImageView ivAddImg2;
    private ImageView ivAddImg3;
    private LinearLayout llUserLocation;
    private Button btnConfirmPublish;
    private OkHttpClient okHttpClient;
    private List<String> imagePaths=new ArrayList<>();
    //百度地图定位功能
    private TextView tvPersonalLocation;
    public LocationClient mLocationClient=null;
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
        ivAddImg1=findViewById(R.id.iv_add_img1);
        ivAddImg2=findViewById(R.id.iv_add_img2);
        ivAddImg3=findViewById(R.id.iv_add_img3);
        btnConfirmPublish=findViewById(R.id.btn_share_confirm_publish);
        okHttpClient=new OkHttpClient();
        //百度地图
        tvPersonalLocation=findViewById(R.id.tv_personal_location);
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
                        .maxSelectable(3)//最大图片数量
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
                //跳转到动态页面
                Intent intent=new Intent();
                intent.putExtra("skipDynamic","skipDynamic");
                intent.setClass(EditDynamicActivity.this, MainActivity.class);
                addDynamic();
                startActivity(intent);
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
    protected void setnull(){
        ivAddImg1.setImageDrawable(null);
        ivAddImg2.setImageDrawable(null);
        ivAddImg3.setImageDrawable(null);
    }
    //选择图片后的响应操作

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            List<Uri> selected = Matisse.obtainResult(data);
            ContentResolver contentResolver = getContentResolver();
            if(selected.size() == 1) {

                ContentResolver contentResolver1 = getContentResolver();
                imagePaths.clear();
                setnull();
                Glide.with(this).load(selected.get(0)).override(600,200).into(ivAddImg1);
                Cursor cursor = contentResolver1.query(selected.get(0),null,
                        null,null,null);
                if (cursor.moveToFirst()){
                    String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                    imagePaths.add(imagePath);
                }


            }
            else if(selected.size() == 2) {

                imagePaths.clear();
                setnull();
                ContentResolver contentResolver1 = getContentResolver();
                ContentResolver contentResolver2 = getContentResolver();
                Glide.with(this).load(selected.get(0)).override(600,200).into(ivAddImg1);
                Glide.with(this).load(selected.get(1)).override(600,200).into(ivAddImg2);
                Cursor cursor = contentResolver1.query(selected.get(0),null,
                        null,null,null);

                if (cursor.moveToFirst()){
                    String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                    imagePaths.add(imagePath);

                }
                Cursor cursor2 = contentResolver2.query(selected.get(1),null,
                        null,null,null);
                if (cursor2.moveToFirst()){

                    String imagePath = cursor2.getString(cursor2.getColumnIndex("_data"));
                    imagePaths.add(imagePath);
                }


            }
            else if(selected.size() == 3) {

                imagePaths.clear();
                setnull();
                Glide.with(this).load(selected.get(0)).override(600,200).into(ivAddImg1);
                Glide.with(this).load(selected.get(1)).override(600,200).into(ivAddImg2);
                Glide.with(this).load(selected.get(2)).override(600,200).into(ivAddImg3);
                Cursor cursor0 = contentResolver.query(selected.get(0),null,
                        null,null,null);
                if (cursor0.moveToFirst()){
                    String imagePath = cursor0.getString(cursor0.getColumnIndex("_data"));
                    imagePaths.add(imagePath);
                }
                Cursor cursor1 = contentResolver.query(selected.get(1),null,
                        null,null,null);
                if (cursor1.moveToFirst()){
                    String imagePath = cursor1.getString(cursor1.getColumnIndex("_data"));
                    imagePaths.add(imagePath);
                }
                Cursor cursor2 = contentResolver.query(selected.get(2),null,
                        null,null,null);
                if (cursor2.moveToFirst()){
                    String imagePath = cursor2.getString(cursor2.getColumnIndex("_data"));
                    imagePaths.add(imagePath);
                }

            }
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
        dynamic.setUserName("张狗民");
        dynamic.setLikeUser(null);
        dynamic.setDynamicPlace(tvPersonalLocation.getText().toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        dynamic.setDynamicTime(df.format(new Date()));
        dynamic.setDynamicContent(etShareText.getText().toString());
        List<String> imgs=new ArrayList<>();
        long time=System.currentTimeMillis();
        if(imagePaths.size()==0){

        }else if(imagePaths.size()==1){
            imgs.add(time+"img1");
            uploadFile(imagePaths.get(0),imgs.get(0));


        }else if(imagePaths.size()==2){
            imgs.add(time+"img1");
            imgs.add(time+"img2");
            uploadFile(imagePaths.get(0),imgs.get(0));
            uploadFile(imagePaths.get(1),imgs.get(1));
            Log.e("图片的路径为",imagePaths.toString());
            location();
        }else if(imagePaths.size()==3){
            imgs.add(time+"img1");
            imgs.add(time+"img2");
            imgs.add(time+"img3");
            uploadFile(imagePaths.get(0),imgs.get(0));
            uploadFile(imagePaths.get(1),imgs.get(1));
            uploadFile(imagePaths.get(2),imgs.get(2));
        }





        dynamic.setDynamicImgs(imgs);
        //创建RequestBody（请求体对象）
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),new Gson().toJson(dynamic));
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
//        UploadImgAsyncTask uploadImgAsyncTask=new UploadImgAsyncTask();
//        uploadImgAsyncTask.execute(fileName,filePath);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == 0){
            location();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (!mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();//停止定位
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
