package com.example.train_shadowlinedemo.SharedFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DynamicFragment extends Fragment {
    private View view;
    private FloatingActionButton fabAddDynamic;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_shared_dynamic_fragment,container,false);
        initView();
        setOnClickListener();
        return view;
    }
    //初始化布局控件
    private void initView(){
        fabAddDynamic=view.findViewById(R.id.fab_add_dynamic);
    }
    //为布局控件设置点击事件
    private void setOnClickListener(){
        fabAddDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到编辑动态界面
                Intent intent=new Intent();
                intent.setClass(getContext(),EditDynamicActivity.class);
                startActivity(intent);
            }
        });
    }
}
