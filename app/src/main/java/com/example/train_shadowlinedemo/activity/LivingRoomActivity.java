package com.example.train_shadowlinedemo.activity;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.model.LatLng;
import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.DrawerConsumer;
import com.billy.android.swipe.listener.SwipeListener;
import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.BulletChat;
import com.example.train_shadowlinedemo.entity.PlaceAndFilm;
import com.example.train_shadowlinedemo.view.LiveRoom.BulletChatRecyclerViewAdapter;
import com.example.train_shadowlinedemo.view.LiveRoom.NearPlaceRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoDestroyCompletionCallback;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.callback.IZegoIMSendBarrageMessageCallback;
import im.zego.zegoexpress.callback.IZegoRoomSetRoomExtraInfoCallback;
import im.zego.zegoexpress.constants.ZegoRoomState;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoBarrageMessageInfo;
import im.zego.zegoexpress.entity.ZegoBroadcastMessageInfo;
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

public class LivingRoomActivity extends AppCompatActivity {
    private ZegoExpressEngine zegoExpressEngine;
    private SurfaceView preview_view;
    private ImageView imageView;
    private int roomId;
    private RecyclerView recyclerView;
    private BulletChatRecyclerViewAdapter myAdapter;
    private ArrayList<BulletChat> bulletChats;
    private ImageView sendButton;
    private EditText messageEditText;
    private ImageView goOutIV;
    private TextView goOutTV;
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
                    goOutIV.setVisibility(View.VISIBLE);
                    goOutTV.setVisibility(View.VISIBLE);
                    preview_view.setVisibility(View.GONE);
                    Glide.with(LivingRoomActivity.this)
                            .asGif()
                            .load(R.drawable.go_out)
                            .into(goOutIV);
                    break;
                case 3:
                    LinearLayoutManager layout = new LinearLayoutManager(LivingRoomActivity.this);
                    layout.setOrientation(LinearLayoutManager.HORIZONTAL);
                    placeRecyclerView.setLayoutManager(layout);
                    nearPlaceRecyclerViewAdapter =new NearPlaceRecyclerViewAdapter(placeAndFilms,LivingRoomActivity.this);
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
        setContentView(R.layout.activity_living_room);
        getView();

        initLiving();

        initRecyclerView();


        sendMsg();

        getLivingNearPlace();


    }

    private void getLivingNearPlace() {
        //2.创建RequestBody（请求体）对象
        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),roomId+"");
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"GetLivingCityServlet")
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
                loactionCity=str;
            }
        });


    }


    private void sendMsg() {
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                zegoExpressEngine.sendBarrageMessage("" + roomId,"来到直播间" , new IZegoIMSendBarrageMessageCallback() {
                    /**
                     * 发送弹幕消息结果回调处理
                     */
                    @Override
                    public void onIMSendBarrageMessageResult(int errorCode, String messageID) {
                        //发送消息结果成功或失败的处理
                    }
                });
            }
        };
        timer.schedule(timerTask,3000);

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter =new BulletChatRecyclerViewAdapter(bulletChats,LivingRoomActivity.this);
        myAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(myAdapter);

    }


    private void initLiving() {
        Intent intent=getIntent();
        roomId=intent.getIntExtra("roomId",1);
        String[] permissionNeeded = {
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO"};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
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
            public void onIMRecvBarrageMessage(String roomID, ArrayList<ZegoBarrageMessageInfo> messageList) {
                ZegoBarrageMessageInfo zegoBarrageMessageInfo= messageList.get(messageList.size()-1);
                String content=zegoBarrageMessageInfo.message;
                String userName=zegoBarrageMessageInfo.fromUser.userName;
                BulletChat bulletChat=new BulletChat();
                bulletChat.setUserName(userName);
                bulletChat.setContent(content);
                bulletChats.add(bulletChat);
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }

            @Override
            public void onRoomStateUpdate(String roomID, ZegoRoomState state, int errorCode, JSONObject extendedData) {
                super.onRoomStateUpdate(roomID, state, errorCode, extendedData);
            }

            @Override
            public void onRoomUserUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoUser> userList) {
                /** 用户状态更新，登陆房间后，当房间内有用户新增或删除时，SDK会通过该回调通知 */
            }

            @Override
            public void onRoomStreamUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoStream> streamList) {
                if (updateType.value()==1){
                    Message message = new Message();
                    message.what=2;
                    handler.sendMessage(message);
                }else if(updateType.value()==0){
                    preview_view.setVisibility(View.VISIBLE);
                    goOutTV.setVisibility(View.GONE);
                    goOutIV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRoomExtraInfoUpdate(String roomID, ArrayList<ZegoRoomExtraInfo> roomExtraInfoList) {
                Log.e("88888","888888");
                ZegoRoomExtraInfo zegoRoomExtraInfo=roomExtraInfoList.get(roomExtraInfoList.size()-1);
                String loction=zegoRoomExtraInfo.value;
                String str[]=loction.split("&");
                String address=str[0];
               // LatLng latLng=new LatLng(Double.parseDouble(str[1]),Double.parseDouble(str[2]));
                locationTV.setText(address);
            }
        });
      ;
        /** 创建用户 */
        ZegoUser user = new ZegoUser(""+LoginActivity.user.getUser_id(),LoginActivity.user.getUser_name());
        /** 开始登陆房间 */
        ZegoRoomConfig zegoRoomConfig=new ZegoRoomConfig();
        zegoRoomConfig.isUserStatusNotify=true;
        zegoExpressEngine.loginRoom(""+roomId, user,zegoRoomConfig);
        zegoExpressEngine.startPlayingStream(""+roomId, new ZegoCanvas(preview_view));


    }

    private void getView() {
        bulletChats=new ArrayList<>();
        goOutIV=findViewById(R.id.go_out);
        recyclerView=findViewById(R.id.rv);
        preview_view=findViewById(R.id.surfaceView);
        sendButton=findViewById(R.id.send);
        messageEditText=findViewById(R.id.content);
        imageView=findViewById(R.id.back);
        goOutTV=findViewById(R.id.go_out_text);
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
                        getLocationPlace();

                    }

                    @Override
                    public void onSwipeClosed(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        Log.e("Closed","Closed");

                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=messageEditText.getText().toString();
                String userName=LoginActivity.user.getUser_name();
                if(!str.equals("")&&str!=null) {
                    messageEditText.setText("");
                    BulletChat bulletChat=new BulletChat();
                    bulletChat.setUserName(userName);
                    bulletChat.setContent(":  "+str);
                    bulletChats.add(bulletChat);
                    myAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(myAdapter.getItemCount()-1);
                    zegoExpressEngine.sendBarrageMessage("" + roomId, str, new IZegoIMSendBarrageMessageCallback() {
                        /**
                         * 发送弹幕消息结果回调处理
                         */
                        @Override
                        public void onIMSendBarrageMessageResult(int errorCode, String messageID) {
                            //发送消息结果成功或失败的处理
                        }
                    });
                }
            }
        });

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
        zegoExpressEngine.stopPlayingStream(""+roomId);
        zegoExpressEngine.logoutRoom(""+roomId);
        super.onStop();
    }


    BulletChatRecyclerViewAdapter.OnItemClickListener onItemClickListener=new BulletChatRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, BulletChatRecyclerViewAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()){
                default:
                    Toast.makeText(LivingRoomActivity.this,"你点击了item按钮"+(position+1),Toast.LENGTH_SHORT).show();
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
            switch (v.getId()){
                default:
                    Intent intent=new Intent(LivingRoomActivity.this,PlaceDetailActivity.class);
                    intent.putExtra("place",new Gson().toJson(placeAndFilms.get(position).getPlace()));
                    startActivity(intent);
                    break;
            }
        }
        @Override
        public void onItemLongClick(View v) {

        }
    };

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
}
