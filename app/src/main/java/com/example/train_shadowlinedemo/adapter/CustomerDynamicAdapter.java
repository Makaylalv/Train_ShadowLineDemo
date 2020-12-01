package com.example.train_shadowlinedemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Dynamic;

import java.util.ArrayList;
import java.util.List;

public class CustomerDynamicAdapter  extends BaseAdapter {
    private Context mContext;
    private List<Dynamic> dynamics=new ArrayList<>();
    private int itemLayoutRes;
    private DynamicViewHolder holder=null;

    public CustomerDynamicAdapter(Context mContext, List<Dynamic> dynamics, int itemLayoutRes) {
        this.mContext = mContext;
        this.dynamics = dynamics;
        this.itemLayoutRes = itemLayoutRes;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Dynamic> getDynamics() {
        return dynamics;
    }

    public void setDynamics(List<Dynamic> dynamics) {
        this.dynamics = dynamics;
    }

    public int getItemLayoutRes() {
        return itemLayoutRes;
    }

    public void setItemLayoutRes(int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=dynamics){
            return dynamics.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null!=dynamics){
            return dynamics.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ImageView[] btn = new ImageView[1];
        if(null==view){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_dynamic,null);
            holder=new DynamicViewHolder();
            btn[0] =view.findViewById(R.id.btn_dynamic_like);
            holder.ivDynamicUserImg=view.findViewById(R.id.iv_dynamic_userimg);
            holder.tvDynamicUserName=view.findViewById(R.id.tv_dynamic_username);
            holder.tvDynamicDynamicTime=view.findViewById(R.id.tv_dynamic_dynamictime);
            holder.tvDynmaicDynamicContent=view.findViewById(R.id.tv_dynamic_dynamiccontent);
            holder.gvDynamicDynamicImgs=view.findViewById(R.id.gv_dynamic_dynamicimgs);
            holder.btnDynamicLike=view.findViewById(R.id.btn_dynamic_like);
            holder.btnDynamicComment=view.findViewById(R.id.btn_dynamic_comment);
            holder.btnDynamicForward=view.findViewById(R.id.btn_dynamic_forward);
            holder.tvDynamicLikeUsers=view.findViewById(R.id.tv_dynamic_likeusers);
            holder.lvDynamicComments=view.findViewById(R.id.lv_dynamic_comments);
            holder.tvUserLocation=view.findViewById(R.id.tv_user_location);
            view.setTag(holder);
        }else{
            holder= (DynamicViewHolder) view.getTag();
            btn[0] =view.findViewById(R.id.btn_dynamic_like);
        }
        Glide.with(mContext).load(dynamics.get(i).getUserImg()).circleCrop().into(holder.ivDynamicUserImg);
        holder.tvDynamicUserName.setText(dynamics.get(i).getUserName());
        holder.tvDynamicDynamicTime.setText(dynamics.get(i).getDynamicTime());
        holder.tvDynmaicDynamicContent.setText(dynamics.get(i).getDynamicContent());
        holder.tvDynamicLikeUsers.setText(dynamics.get(i).getLikeUser().toString()+"觉得很赞");
        holder.tvUserLocation.setText(dynamics.get(i).getDynamicPlace()+" ");
        holder.btnDynamicLike.setImageResource(R.drawable.share_like_true);
        CustomerDynamicImgAdapter customerDynamicImgAdapter=new CustomerDynamicImgAdapter(mContext,dynamics.get(i).getDynamicImgs(),R.layout.item_dynamic_img);
        holder.gvDynamicDynamicImgs.setAdapter(customerDynamicImgAdapter);
        //为点赞按钮设置点击事件
        final int[] likeflag = {0};


        holder.btnDynamicLike.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                //设置点赞事件
                if(likeflag[0] ==0){
                   btn[0].setImageResource(R.drawable.share_like_true);
                    likeflag[0] =1;
                }else if(likeflag[0] ==1){ //设置取消点赞事件
                    holder.btnDynamicLike.setImageResource(R.drawable.share_like_false);
                    btn[0].setImageResource(R.drawable.share_like_false);
                    likeflag[0]=0;
                }
            }
        });



        return view;
    }
    static class DynamicViewHolder{
        ImageView ivDynamicUserImg;
        TextView tvDynamicUserName;
        TextView tvDynamicDynamicTime;
        TextView tvDynmaicDynamicContent;
        GridView gvDynamicDynamicImgs;
        ImageView btnDynamicLike;
        Button btnDynamicComment;
        Button btnDynamicForward;
        TextView tvDynamicLikeUsers;
        ListView lvDynamicComments;
        TextView tvUserLocation;
    }
}
