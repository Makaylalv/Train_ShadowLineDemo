package com.example.train_shadowlinedemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.fragment.ShareChildrenFragment.DynamicFragment;
import com.example.train_shadowlinedemo.fragment.ShareChildrenFragment.LiveFragment;


/**
 * 这是动态界面
 */
public class ShareFragment extends Fragment {
    private Fragment currentFragment =new Fragment();
    private View view;
    private TextView tvSharedDynamic;
    private TextView tvSharedLive;
    private DynamicFragment dynamicFragment;
    private LiveFragment liveFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_shared,container,false);
        initView();
        changeTab(dynamicFragment);
        changeColor("dynamic");
        currentFragment=dynamicFragment;
        sharedFragmentOnClicked();
        return view;

    }
    //初始化控件
    public void initView(){
        tvSharedDynamic=view.findViewById(R.id.shared_tv_dynamic);
        tvSharedLive=view.findViewById(R.id.shared_tv_live);
        dynamicFragment=new DynamicFragment();
        liveFragment=new LiveFragment();
    }
    //改变选项卡
    public void changeTab(Fragment fragment){
        FragmentManager manager =getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        if(currentFragment!=fragment){
            if(!fragment.isAdded()){
                transaction.add(R.id.shared_tabcontent,fragment);
            }
            transaction.hide(currentFragment);
            transaction.show(fragment);
            transaction.commit();
            currentFragment=fragment;
        }
    }
    //选项卡点击事件
    public void sharedFragmentOnClicked(){

        tvSharedDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(dynamicFragment);
                changeColor("dynamic");

            }
        });
        tvSharedLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(liveFragment);
                changeColor("live");
            }
        });
    }
    private void changeColor(String str){
        switch (str){
            case "dynamic":
                tvSharedDynamic.setTextColor(getResources().getColor(R.color.mainColor));
                tvSharedLive.setTextColor(getResources().getColor(R.color.black));
                break;
            case "live":
                tvSharedDynamic.setTextColor(getResources().getColor(R.color.black));
                tvSharedLive.setTextColor(getResources().getColor(R.color.mainColor));
                break;
        }

    }


}
