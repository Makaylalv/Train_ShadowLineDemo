package com.example.train_shadowlinedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.fragment.WelcomePageFragment.FirstFragment;
import com.example.train_shadowlinedemo.fragment.WelcomePageFragment.ForthFragment;
import com.example.train_shadowlinedemo.fragment.WelcomePageFragment.SecondFragment;
import com.example.train_shadowlinedemo.fragment.WelcomePageFragment.ThirdFragment;
import com.github.appintro.AppIntro;

public class AppIntroActivity extends AppIntro {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addSlide(new FirstFragment());
        addSlide(new SecondFragment());
        addSlide(new ThirdFragment());
        addSlide(new ForthFragment());
        setVibrateDuration(3000);
        setVibrate(true);


    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        //当执行跳过动作时触发
        Intent intent = new Intent(AppIntroActivity.this, LoginActivity.class);
        startActivity(intent);

        SharedPreferences activityPreferences = this.getSharedPreferences(
                "app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean("first_open", true);
        editor.commit();

        finish();
    }

    @Override
    public void onNextPressed(Fragment currentFragment) {
        //当执行下一步动作时触发
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        //当执行完成动作时触发
        Intent intent = new Intent(AppIntroActivity.this, LoginActivity.class);
        startActivity(intent);

        SharedPreferences activityPreferences = this.getSharedPreferences(
                "app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean("first_open", true);
        editor.commit();
        finish();
    }

    @Override
    protected void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        //当执行变化动作时触发
    }
}

