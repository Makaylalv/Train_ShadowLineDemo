package com.example.train_shadowlinedemo.fragment.ShareChildrenFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.adapter.CustomerDynamicAdapter;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DynamicFragment extends Fragment {
    private View view;
    private FloatingActionButton fabAddDynamic;
    private List<Dynamic> dynamics=new ArrayList<Dynamic>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_shared_dynamic_fragment,container,false);
        initView();
        setOnClickListener();
        initData();
        CustomerDynamicAdapter customerDynamicAdapter=new CustomerDynamicAdapter(getContext(),dynamics,R.layout.item_dynamic);
        ListView lvDynamics=view.findViewById(R.id.lv_dynamics);
        lvDynamics.setAdapter(customerDynamicAdapter);
        return view;
    }
    //初始化布局控件
    private void initView(){
        fabAddDynamic=view.findViewById(R.id.fab_add_dynamic);
    }
    //为布局控件设置点击事件
    private void setOnClickListener(){
        //添加动态设置点击事件
        fabAddDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到编辑动态界面
                Intent intent=new Intent();
                intent.putExtra("skipdynamic","skipdynamic");
                intent.setClass(getContext(), EditDynamicActivity.class);
                startActivity(intent);
            }
        });
    }
    //初始化数据
    private void initData(){
      Dynamic dynamic=new Dynamic();
      dynamic.setUserImg("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      dynamic.setUserName("马大爷");
      dynamic.setDynamicTime("今天15:36");
      dynamic.setDynamicContent("今天有点小难过");
      List<String> users=new ArrayList<>();
      users.add("懒羊羊");
      users.add("西门庆");
      users.add("迪迦奥特曼");
      dynamic.setLikeUser(users);
      List<String> imgs=new ArrayList<>();
      imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
        imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
        imgs.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684536465,3063644397&fm=26&gp=0.jpg");
      dynamic.setDynamicImgs(imgs);
      dynamics.add(dynamic);


    }
}
