package com.example.train_shadowlinedemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.DrawerConsumer;
import com.billy.android.swipe.listener.SwipeListener;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.BulletChat;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.entity.PlaceAndFilm;
import com.example.train_shadowlinedemo.view.LiveRoom.BulletChatRecyclerViewAdapter;
import com.example.train_shadowlinedemo.view.LiveRoom.LiveRoomRecyclerViewAdapter;
import com.example.train_shadowlinedemo.view.LiveRoom.NearPlaceRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.smssdk.gui.SmartVerifyPage;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoDestroyCompletionCallback;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.callback.IZegoRoomSetRoomExtraInfoCallback;
import im.zego.zegoexpress.constants.ZegoBeautifyFeature;
import im.zego.zegoexpress.constants.ZegoRoomState;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoBarrageMessageInfo;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoRoomConfig;
import im.zego.zegoexpress.entity.ZegoRoomExtraInfo;
import im.zego.zegoexpress.entity.ZegoStream;
import im.zego.zegoexpress.entity.ZegoUser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StartLivingActivity extends AppCompatActivity{
    private ZegoExpressEngine zegoExpressEngine;
    private SurfaceView preview_view;
    private ImageView imageView;
    private int roomId;
    private RecyclerView recyclerView;
    private BulletChatRecyclerViewAdapter myAdapter;
    private ArrayList<BulletChat> bulletChats;
    private LinearLayout cameraLayout;
    private LinearLayout voiceLayout;
    private LinearLayout beautyLayout;
    private ImageView  cameraIV;
    private ImageView   voiceIV;
    private ImageView  beautyIV;
    private TextView cameraTV;
    private TextView voiceTV;
    private TextView beautyTV;
    private boolean isFront=true;
    private boolean isVioce=false;
    private boolean isBeauty=false;
    private LocationClient locationClient;
    private TextView locationTV;
    private ImageView locationIV;
    private LinearLayout linearLayout;
    private String loactionCity;
    private RecyclerView placeRecyclerView;
    private NearPlaceRecyclerViewAdapter nearPlaceRecyclerViewAdapter;
    private List<PlaceAndFilm> placeAndFilms;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    myAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(myAdapter.getItemCount()-1);
                    break;
                case 2:
                    String string= (String) msg.obj;
                    zegoExpressEngine.setRoomExtraInfo(LoginActivity.user.getUser_id()+"", "location", string, new IZegoRoomSetRoomExtraInfoCallback() {
                        @Override
                        public void onRoomSetRoomExtraInfoResult(int i) {
                            Log.e("good",string);

                        }
                    });
                    break;
                case 3:

                    LinearLayoutManager layout = new LinearLayoutManager(StartLivingActivity.this);
                    layout.setOrientation(LinearLayoutManager.HORIZONTAL);
                    placeRecyclerView.setLayoutManager(layout);
                    nearPlaceRecyclerViewAdapter =new NearPlaceRecyclerViewAdapter(placeAndFilms,StartLivingActivity.this);
                    Log.e("data",nearPlaceRecyclerViewAdapter.getItemCount()+"");
                    nearPlaceRecyclerViewAdapter.setOnItemClickListener(nearOnItemClickListener);
                    placeRecyclerView.setAdapter(nearPlaceRecyclerViewAdapter);
                    break;


            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_living);

        getView();

        initLiving();
        //初始化bar
        //initBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter =new BulletChatRecyclerViewAdapter(bulletChats,StartLivingActivity.this);
        myAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(myAdapter);
    }

    private void getView() {
        bulletChats=new ArrayList<>();
        recyclerView=findViewById(R.id.rv);
        preview_view=findViewById(R.id.surfaceView);
        imageView=findViewById(R.id.back);
        cameraLayout=findViewById(R.id.camera);
        cameraIV=findViewById(R.id.camera_img);
        cameraTV=findViewById(R.id.camera_txt);
        cameraTV.setTextColor(Color.GRAY);
        voiceLayout=findViewById(R.id.voice);
        voiceIV=findViewById(R.id.voice_img);
        voiceTV=findViewById(R.id.voice_txt);
        voiceTV.setTextColor(Color.GRAY);
        beautyLayout=findViewById(R.id.beauty);
        beautyIV=findViewById(R.id.beauty_img);
        beautyTV=findViewById(R.id.beauty_txt);
        beautyTV.setTextColor(Color.GRAY);
        locationIV=findViewById(R.id.location_img);
        locationTV=findViewById(R.id.location_txt);
        linearLayout=findViewById(R.id.place_layout);
        placeRecyclerView=findViewById(R.id.place_recyclerview);
        SmartSwipe.wrap(this)
                .addConsumer(new DrawerConsumer())    //抽屉效果
                //可以设置横向(左右两侧)的抽屉为同一个view
                //也可以为不同方向分别设置不同的view
                .setBottomDrawerView(linearLayout)
                .addListener(new SwipeListener() {
                    @Override
                    public void onConsumerAttachedToWrapper(SmartSwipeWrapper wrapper, SwipeConsumer consumer) {
                        Log.e("Attached","Attached");
                    }

                    @Override
                    public void onConsumerDetachedFromWrapper(SmartSwipeWrapper wrapper, SwipeConsumer consumer) {
                        Log.e("Detached","Detached");
                    }

                    @Override
                    public void onSwipeStateChanged(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int state, int direction, float progress) {
                        Log.e("State","State");
                    }

                    @Override
                    public void onSwipeStart(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        Log.e("Start","Start");
                    }

                    @Override
                    public void onSwipeProcess(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction, boolean settling, float progress) {
                        Log.e("Process","Process");
                    }

                    @Override
                    public void onSwipeRelease(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction, float progress, float xVelocity, float yVelocity) {
                        Log.e("Release","Release");
                    }

                    @Override
                    public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        Log.e("Opened","Opened");
                        //获取地点
                        if(loactionCity!=null&&!locationClient.equals("")) {
                            getLocationPlace();
                        }


                    }

                    @Override
                    public void onSwipeClosed(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        Log.e("Closed","Closed");

                    }
                });
        //点击返回按钮，结束直播
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFront==true){
                    cameraIV.setImageResource(R.drawable.camera_front);
                    cameraTV.setTextColor(Color.BLACK);
                    isFront=false;
                    zegoExpressEngine.useFrontCamera(isFront);
                }else {
                    cameraIV.setImageResource(R.drawable.camera_behind);
                    cameraTV.setTextColor(Color.GRAY);
                    isFront=true;
                    zegoExpressEngine.useFrontCamera(isFront);
                }

            }
        });
        voiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVioce==true){
                    voiceIV.setImageResource(R.drawable.microphone_close);
                    voiceTV.setTextColor(Color.GRAY);
                    isVioce=false;
                    zegoExpressEngine.enableAudioMixing(isVioce);

                }else {
                    voiceIV.setImageResource(R.drawable.microphone_up);
                    voiceTV.setTextColor(Color.BLACK);
                    isVioce=true;
                    zegoExpressEngine.enableAudioMixing(isVioce);
                }

            }
        });
        beautyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBeauty==true){
                    zegoExpressEngine.enableBeautify(0);
                    beautyIV.setImageResource(R.drawable.beauty_close);
                    beautyTV.setTextColor(Color.GRAY);
                    isBeauty=false;
                }else {
                    zegoExpressEngine.enableBeautify(ZegoBeautifyFeature.POLISH.value()|ZegoBeautifyFeature.SHARPEN.value());
                    beautyIV.setImageResource(R.drawable.beauty_up);
                    beautyTV.setTextColor(Color.BLACK);
                    isBeauty=true;
                }

            }
        });

    }




    private void initLiving() {
        String[] permissionNeeded = {
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionNeeded, 101);
            }
        }

        zegoExpressEngine = ZegoExpressEngine.createEngine(	1365603232,
                "985c35b21cfeede8e9b42b30d0ae79e623a9175493bc5f353e8d74508eff0808",
                true, ZegoScenario.COMMUNICATION,
                getApplication(),
                null);
        zegoExpressEngine.setEventHandler(null);
        zegoExpressEngine.setEventHandler(new IZegoEventHandler() {
            @Override
            public void onRoomStateUpdate(String roomID, ZegoRoomState state, int errorCode, JSONObject extendedData) {
                /** 房间状态更新回调，登陆房间后，当房间连接状态发生变更（如出现房间断开，登陆认证失败等情况），SDK会通过该回调通知 */
                super.onRoomStateUpdate(roomID, state, errorCode, extendedData);
            }

            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                /** 用户状态更新，登陆房间后，当房间内有用户新增或删除时，SDK会通过该回调通知 */
            }

            @Override
            public void onRoomStreamUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoStream> streamList) {
                /** 流状态更新，登陆房间后，当房间内有用户新推送或删除音视频流时，SDK会通过该回调通知 */
            }

            @Override
            public void onIMRecvBarrageMessage(String roomID, ArrayList<ZegoBarrageMessageInfo> messageList) {

                ZegoBarrageMessageInfo zegoBarrageMessageInfo= messageList.get(messageList.size()-1);
                String content=zegoBarrageMessageInfo.message;
                String userName=zegoBarrageMessageInfo.fromUser.userName;
                BulletChat bulletChat=new BulletChat();
                bulletChat.setUserName(userName);
                bulletChat.setContent(":  "+content);
                bulletChats.add(bulletChat);
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        });

        /** 创建用户 */
        ZegoUser user = new ZegoUser(""+LoginActivity.user.getUser_id(),LoginActivity.user.getUser_name());
        /** 开始登陆房间 */
        ZegoRoomConfig zegoRoomConfig=new ZegoRoomConfig();
        zegoRoomConfig.isUserStatusNotify=true;

        zegoExpressEngine.loginRoom(""+LoginActivity.user.getUser_id(), user,zegoRoomConfig);
        //开始推流
        zegoExpressEngine.startPublishingStream(""+LoginActivity.user.getUser_id());

        //开始自我预览
        zegoExpressEngine.startPreview(new ZegoCanvas(preview_view));


        showLocation();

    }

    /**
     * 初始化bar
     */
    private void initBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.custom_bar);  //绑定自定义的布局：actionbar_layout.xml
            imageView=actionBar.getCustomView().findViewById(R.id.back);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            Log.e("actionbar","is null");
        }
    }



    @Override
    protected void onStop() {
        stopLiving();
        zegoExpressEngine.useFrontCamera(true);
        zegoExpressEngine.stopPublishingStream();
        zegoExpressEngine.logoutRoom(""+LoginActivity.user.getUser_id());
        super.onStop();
    }


    BulletChatRecyclerViewAdapter.OnItemClickListener onItemClickListener=new BulletChatRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, BulletChatRecyclerViewAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()){
                default:
//                    Toast.makeText(StartLivingActivity.this,"你点击了item按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        @Override
        public void onItemLongClick(View v) {
        }
    };

    NearPlaceRecyclerViewAdapter.OnItemClickListener nearOnItemClickListener=new NearPlaceRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, NearPlaceRecyclerViewAdapter.ViewName viewName, int position) {

        }

        @Override
        public void onItemLongClick(View v) {

        }
    };


    private void stopLiving(){
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),""+ LoginActivity.user.getUser_id());
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"StopRoomServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        OkHttpClient okHttpClient=new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        //异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
                String str=response.body().string();
            }
        });
    }


    private void  showLocation(){
        locationClient=new LocationClient(getApplicationContext());
        //3.创建LocationClientOption对象
        LocationClientOption option=new LocationClientOption();
        //配置一些定位的参数
        //设置打开GPS
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        //4.设置坐标系类型
        option.setCoorType("bd09ll");
        //设置定位模式(推荐使用低功耗的定位模式)
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //将定位的参数应用到定位客户端
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //定位成功后自动执行，定位成功后位置信息保存在BDLocation对象中
                Double latitude=bdLocation.getLatitude();//纬度
                Double longitude=bdLocation.getLongitude();//精度
                int code=bdLocation.getLocType();//获取定位错误码
                Log.e("定位成功",latitude+""+longitude);
                //移动地图界面显示到当前位置
                //把哪一个坐标点显示到地图中间
                LatLng point=new LatLng(latitude,longitude);
                locationTV.setText(bdLocation.getCity()+"");
                loactionCity=bdLocation.getCity();
                locationLiving();

            }
        });
        locationClient.start();
    }


    private void getLocationPlace() {
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),loactionCity);
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"GetNearPlaceAndFilmServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        OkHttpClient okHttpClient=new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        //异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
                String str=response.body().string();
                Gson gson=new Gson();
                Type type = new TypeToken<ArrayList<PlaceAndFilm>>() {}.getType();
                placeAndFilms=new ArrayList<>();
                placeAndFilms=gson.fromJson(str,type);
                Message message=new Message();
                message.what=3;
                handler.sendMessage(message);
            }
        });
    }


    private void locationLiving(){
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),""+ LoginActivity.user.getUser_id()+"&"+loactionCity);
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"LocationRoomServlet")
                .build();
        //4.创建Call对象，发送请求，并接受响应
        OkHttpClient okHttpClient=new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        //异步网络请求（不需要创建子线程）
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
                String str=response.body().string();
            }
        });

    }



}
