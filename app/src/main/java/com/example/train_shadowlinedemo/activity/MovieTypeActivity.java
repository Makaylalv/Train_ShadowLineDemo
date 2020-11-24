package com.example.train_shadowlinedemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.train_shadowlinedemo.R;

public class MovieTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_type);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        Log.e("MovieTypeActivity",type);
    }
}