package com.example.train_shadowlinedemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerEditImgAdapter extends BaseAdapter {
    private Context mContext;
    private List<Uri> selected=new ArrayList<>();
    private int itemLayoutRes;

    public CustomerEditImgAdapter(Context mContext, List<Uri> selected, int itemLayoutRes) {
        this.mContext = mContext;
        this.selected = selected;
        this.itemLayoutRes = itemLayoutRes;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Uri> getSelected() {
        return selected;
    }

    public void setSelected(List<Uri> selected) {
        this.selected = selected;
    }

    public int getItemLayoutRes() {
        return itemLayoutRes;
    }

    public void setItemLayoutRes(int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=selected){
            return selected.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null!=selected){
            return selected.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_dynamic_img,null);
        ImageView  ivEditImg=view.findViewById(R.id.iv_item_edit_img);
        Glide.with(mContext).load(selected.get(position)).into(ivEditImg);
      return view;
    }

}
