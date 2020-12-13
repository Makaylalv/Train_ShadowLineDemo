package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.JetPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.entity.Film;
import com.example.train_shadowlinedemo.fragment.PersonalFragment;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_name;
    private EditText et_phone;
    private RelativeLayout set_edit;
    private LinearLayout personal_title;
    private RelativeLayout relativeLayout;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String sign = intent.getStringExtra("signature");

        okHttpClient=new OkHttpClient();
        relativeLayout=findViewById(R.id.username_set);
        set_edit = findViewById(R.id.set_edit);
        personal_title = findViewById(R.id.personal_title);
        et_phone=findViewById(R.id.et_phone);
        et_name=findViewById(R.id.et_name);
        et_name.setText(name);
        et_phone.setText(sign);
        et_name.setOnClickListener(this);
        et_phone.setOnClickListener(this);
        ImageView back=findViewById(R.id.set_back);
        back.setOnClickListener(this);
        TextView tvCancel=findViewById(R.id.edit_cancel);
        tvCancel.setOnClickListener(this);
        TextView tvSave=findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);
        TextView tvPassword=findViewById(R.id.tv_password_set);
        tvPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_back://返回界面
                finish();
                break;
            case R.id.et_name://点击编辑
                personal_title.setVisibility(View.GONE);
                set_edit.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                params.addRule(RelativeLayout.BELOW,R.id.set_edit);
                relativeLayout.setLayoutParams(params);
                break;
            case R.id.et_phone:
                personal_title.setVisibility(View.GONE);
                set_edit.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                param.addRule(RelativeLayout.BELOW,R.id.set_edit);
                relativeLayout.setLayoutParams(param);
                break;
            case R.id.edit_cancel://取消修改
                set_edit.setVisibility(View.GONE);
                personal_title.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams param2= (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                param2.addRule(RelativeLayout.BELOW,R.id.personal_title);
                relativeLayout.setLayoutParams(param2);
                break;
            case R.id.tv_save://保存修改信息
                String name=et_name.getText().toString();
                String sign=et_phone.getText().toString();
                saveInfo(name,sign);
                Toast.makeText(this,"修改成功",Toast.LENGTH_LONG).show();
                LoginActivity.user.setUser_name(name);
                LoginActivity.user.setUser_sign(sign);
                EventBus.getDefault().post("updateUser");
                break;
            case R.id.tv_password_set://跳转修改密码界面
                break;
        }
    }


    private void saveInfo(String name, String sign) {
        FormBody formBody=new FormBody.Builder()
                .add("name",name)
                .add("sign",sign)
                .add("id",LoginActivity.user.getUser_id()+"")
                .build();
        Request request=new Request.Builder()
                .post(formBody)
                .url(ConfigUtil.SERVER_ADDR+"ClientUpdateInfo")
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
            }
        });
    }
}