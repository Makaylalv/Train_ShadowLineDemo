package com.example.train_shadowlinedemo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class ShadowLineApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
