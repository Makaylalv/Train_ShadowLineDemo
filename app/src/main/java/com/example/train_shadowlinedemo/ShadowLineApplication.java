package com.example.train_shadowlinedemo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.mob.MobSDK;
//import com.mob.MobApplication;


public class ShadowLineApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        MobSDK.init(this, this.getAppkey(), this.getAppSecret());
        MobSDK.submitPolicyGrantResult(true, null);
//        MobSDK.init(this);

    }
    protected String getAppkey() {
        return null;
    }

    protected String getAppSecret() {
        return null;
    }
}
