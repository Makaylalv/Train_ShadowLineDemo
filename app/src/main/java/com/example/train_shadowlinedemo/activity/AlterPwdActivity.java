package com.example.train_shadowlinedemo.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlterPwdActivity extends AppCompatActivity {
    private TextView goBackTV;
    private TextView saveTV;
    private EditText previouslyET;
    private EditText newPwdET;
    private EditText confirmPpwdET;
    private String newPwds;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_pwd);
        getView();
    }

    private void getView() {
        goBackTV=findViewById(R.id.edit_cancel);
        saveTV=findViewById(R.id.edit_save);
        previouslyET=findViewById(R.id.previously_pwd);
        newPwdET=findViewById(R.id.new_pwd);
        confirmPpwdET=findViewById(R.id.confirm_new_pwd);
        goBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previouslyPwd=previouslyET.getText().toString().trim();
                String newPwd=newPwdET.getText().toString().trim();
                String confirmPpwd=confirmPpwdET.getText().toString().trim();
                if(previouslyPwd.equals("")||previouslyPwd==null||newPwd.equals("")||newPwd==null||confirmPpwd.equals("")||confirmPpwd==null){
                    Toast.makeText(AlterPwdActivity.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                }else {
                    if(newPwd.equals(confirmPpwd)){
                        newPwds=confirmPpwd;
                        alterPwd();
                    }else {
                        Toast.makeText(AlterPwdActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void alterPwd() {
        //2.创建RequestBody（请求体）对象

        RequestBody requestBody = RequestBody.create(MediaType.parse(
                "text/plain;charset=utf-8"),LoginActivity.user.getUser_id()+"&"+newPwds);
        //3.创建请求对象
        Request request = new Request.Builder()
                .post(requestBody)//请求方式为POST
                .url(ConfigUtil.SERVER_ADDR+"AlterPwdServlet")
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
                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();


            }
        });

    }
}
