package com.example.train_shadowlinedemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerDynamicImgAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> imgs=new ArrayList<String>();
    private int itemLayoutRes;


    public CustomerDynamicImgAdapter(Context mContext, List<String> imgs, int itemLayoutRes) {
        this.mContext = mContext;
        this.imgs = imgs;
        this.itemLayoutRes = itemLayoutRes;

    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getItemLayoutRes() {
        return itemLayoutRes;
    }

    public void setItemLayoutRes(int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=imgs){
            return imgs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null!=imgs){
            return imgs.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DynamicImgViewHolder dynamicImgViewHolder=null;
        if(null==view){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_img,null);
            dynamicImgViewHolder=new DynamicImgViewHolder();
            dynamicImgViewHolder.ivDynamicImg=view.findViewById(R.id.iv_dynamic_img);
            view.setTag(dynamicImgViewHolder);
        }else{
            dynamicImgViewHolder= (DynamicImgViewHolder) view.getTag();
        }
        Glide.with(mContext).load(ConfigUtil.SERVER_ADDR +"imgs/dynamic/dynamicImgs/"+imgs.get(i)+".jpg").override(300,300).into(dynamicImgViewHolder.ivDynamicImg);
        Log.e("图片的地址是",ConfigUtil.SERVER_ADDR +"imgs/dynamic/dynamicImgs/"+imgs.get(i)+".jpg"+imgs.get(i)+".jpg");
        return view;
    }
    static class  DynamicImgViewHolder{
        private ImageView ivDynamicImg;
    }
}
