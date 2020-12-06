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

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.BulletChat;
import com.example.train_shadowlinedemo.view.LiveRoom.BulletChatRecyclerViewAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoDestroyCompletionCallback;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.callback.IZegoIMSendBarrageMessageCallback;
import im.zego.zegoexpress.constants.ZegoRoomState;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.entity.ZegoBarrageMessageInfo;
import im.zego.zegoexpress.entity.ZegoBroadcastMessageInfo;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoRoomConfig;
import im.zego.zegoexpress.entity.ZegoStream;
import im.zego.zegoexpress.entity.ZegoUser;

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
}
