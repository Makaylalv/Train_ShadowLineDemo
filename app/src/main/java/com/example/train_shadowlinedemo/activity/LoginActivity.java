package com.example.train_shadowlinedemo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bkk.library.QQLoginManager;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.MainActivity;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.RegisterActivity;
import com.example.train_shadowlinedemo.entity.User;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private String reg = null;
    private String phone;
    private String name;
    private String pwd;
    public static User user;

    private EditText etPhone;
    private EditText etPwd;
    private boolean i = false;

    private static final String TAG = "MainActivity";
    //QQ包名
    private static final String PACKAGE_QQ = "com.tencent.mobileqq";

    //展示个人信息
    private TextView infoText, infoName;
    private ImageView infoIcon;
    //初始化腾讯服务
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.et_login_phone);
        etPwd = findViewById(R.id.et_login_password);

        //腾讯AppId(替换你自己App Id)、上下文
        mTencent = Tencent.createInstance("1111171437", this);

        infoText = this.findViewById(R.id.text_info);
        infoIcon = this.findViewById(R.id.info_icon);
        infoName = this.findViewById(R.id.info_name);
        //QQ登录点击事件
        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意：此段非必要，如果手机未安装应用则会跳转网页进行授权
                if (!hasApp(LoginActivity.this, PACKAGE_QQ)) {
                    Toast.makeText(LoginActivity.this, "未安装QQ应用",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果session无效，就开始做登录操作
                if (!mTencent.isSessionValid()) {
                    loginQQ();
                }
            }
        });

        //新用户？注册（点击跳转到注册页面
        TextView new_user = findViewById(R.id.tv_new_user);
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        reg = intent.getStringExtra("Register");

        //注册，插入添加用户
        if (reg != null){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Log.e("111","111");
                    try{
                        phone = intent.getStringExtra("Phone");
                        name = intent.getStringExtra("Name");
                        pwd = intent.getStringExtra("Pwd");
                        URL url = new URL("http://192.168.43.175:8080/ShadowLine/AddUserServlet");
                        Log.e("111","333");
                        HttpURLConnection connection = (HttpURLConnection)
                                url.openConnection();
                        //设置Http的请求方式：get\post\put\delete...,默认使get请求
                        connection.setRequestMethod("POST");
                        OutputStream outputStream = connection.getOutputStream();
                        JSONObject object = new JSONObject();
                        object.put("Phone",phone);
                        object.put("Name",name);
                        object.put("Pwd",pwd);
                        outputStream.write(object.toString().getBytes());
                        Log.e("111","222");
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                        if(reader.readLine().equals("true")) {
                            Log.e("insert","插入成功");
                        }
                        inputStream.close();
                        outputStream.close();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    //把用户对象的数据转成JSON格式字符串
    public String object2JSON(User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Phone",user.getUser_phone());
        jsonObject.put("Pwd",user.getUser_password());
        return jsonObject.toString();
    }

    //点击登录按钮进入主页面
    public void onClicked(View view) {
        //访问服务器端
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(ConfigUtil.SERVER_ADDR+"LoginUserServlet");
                    HttpURLConnection connection = (HttpURLConnection)
                            url.openConnection();
                    //设置Http的请求方式：get\post\put\delete...,默认使get请求
                    connection.setRequestMethod("POST");
                    OutputStream outputStream = connection.getOutputStream();
                    String uPhone = etPhone.getText().toString();
                    String uPwd = etPwd.getText().toString();
                    String str=uPhone+"&"+uPwd;
                    outputStream.write(str.getBytes());
                    InputStream inputStream = connection.getInputStream();
                    byte[] buffer = new byte[256];
                    int len = inputStream.read(buffer);
                    String content = new String(buffer,0,len);
                    outputStream.close();
                    inputStream.close();
                    Log.e("cccccccc",content);
                    if (!content.equals("登陆失败")){
                        Log.e("登陆结果","登录成功");
                        Gson gson=new Gson();
                        user=gson.fromJson(content,User.class);
                        Log.e("userId",user.getUser_id()+"");
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Log.e("登陆结果","登录失败");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * QQ登录
     */
    private IUiListener listener;

    private void loginQQ() {
        listener = new IUiListener() {
            @Override
            public void onComplete(Object object) {

                Log.e(TAG, "登录成功: " + object.toString() );

                JSONObject jsonObject = (JSONObject) object;
                try {
                    //得到token、expires、openId等参数
                    String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                    String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                    String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);
                    Log.e(TAG, "token: " + token);
                    Log.e(TAG, "expires: " + expires);
                    Log.e(TAG, "openId: " + openId);

                    //获取个人信息
                    getQQInfo();
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(UiError uiError) {
                //登录失败
                Log.e(TAG, "登录失败" + uiError.errorDetail);
                Log.e(TAG, "登录失败" + uiError.errorMessage);
                Log.e(TAG, "登录失败" + uiError.errorCode + "");

            }

            @Override
            public void onCancel() {
                //登录取消
                Log.e(TAG, "登录取消");

            }
        };
        //context上下文、第二个参数SCOPO 是一个String类型的字符串，表示一些权限
        //应用需要获得权限，由“,”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
        //第三个参数事件监听器
        mTencent.login(this, "all", listener);
        //注销登录
        //mTencent.logout(this);
    }

    /**
     * 获取QQ个人信息
     */
    private void getQQInfo() {
        //获取基本信息
        QQToken qqToken = mTencent.getQQToken();
        UserInfo info = new UserInfo(this, qqToken);
        info.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                try {
                    Log.e(TAG, "个人信息：" + object.toString());
                    //infoText.setText(object.toString());
                    //头像
                    String avatar = ((JSONObject) object).getString("figureurl_2");
                    ////GlideUtils.showGlide(MainActivity.this, avatar, infoIcon);
                    String nickName = ((JSONObject) object).getString("nickname");
                    //infoName.setText(nickName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    /**
     * 回调必不可少,官方文档未说明
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //腾讯QQ回调
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, listener);
            }
        }
        //通过QQ登录跳转到主页面
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(this,"登录成功",Toast.LENGTH_LONG).show();
    }

    /**
     * true 安装了相应包名的app
     */
    private boolean hasApp(Context context, String packName) {
        boolean is = false;
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String packageName = packageInfo.packageName;
            if (packageName.equals(packName)) {
                is = true;
            }
        }
        return is;
    }

}










