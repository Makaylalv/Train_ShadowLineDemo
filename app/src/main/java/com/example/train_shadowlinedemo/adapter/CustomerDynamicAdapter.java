package com.example.train_shadowlinedemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.example.train_shadowlinedemo.entity.DynamicLikeUser;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomerDynamicAdapter  extends BaseAdapter {
    private Context mContext;
    private List<Dynamic> dynamics=new ArrayList<>();
    private int itemLayoutRes;
    private DynamicViewHolder holder=null;
    private OkHttpClient okHttpClient=new OkHttpClient();
  //  private List<View>  viewList=new ArrayList<>();


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

      //  if(null==view){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_dynamic,null);
            holder=new DynamicViewHolder();
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
//        }else{
//            holder= (DynamicViewHolder) view.getTag();
//
//        }
        Glide.with(mContext).load(dynamics.get(i).getUserImg()).circleCrop().into(holder.ivDynamicUserImg);
        holder.tvDynamicUserName.setText(dynamics.get(i).getUserName());
        holder.tvDynamicDynamicTime.setText(dynamics.get(i).getDynamicTime());
        holder.tvDynmaicDynamicContent.setText(dynamics.get(i).getDynamicContent());
        holder.tvDynamicLikeUsers.setText(dynamics.get(i).getLikeUser().toString()+"觉得很赞");
        holder.tvUserLocation.setText(dynamics.get(i).getDynamicPlace()+" ");

        Glide.with(mContext).load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1528689441,659647338&fm=26&gp=0.jpg").circleCrop().into(holder.ivDynamicUserImg);
        CustomerDynamicImgAdapter customerDynamicImgAdapter=new CustomerDynamicImgAdapter(mContext,dynamics.get(i).getDynamicImgs(),R.layout.item_dynamic_img);
        holder.gvDynamicDynamicImgs.setAdapter(customerDynamicImgAdapter);
        //为点赞按钮设置点击事件
        final int[] likeflag = {0};
        holder.btnDynamicLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       //         View view1=viewList.get(i);
                ImageView imageView=view.findViewById(R.id.btn_dynamic_like);
                if(likeflag[0]==0){
                    holder.btnDynamicLike.setImageResource(R.drawable.share_like_true);
                    likeflag[0]=1;
                }else{
                    imageView.setImageResource(R.drawable.share_like_false);
                    likeflag[0]=0;
                }


                Log.e("333333333333333333",view.getId()+"");
//                Drawable drawable=mContext.getResources().getDrawable(R.drawable.share_like_false);
//                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
//
//                holder.btnDynamicLike.setImageResource(R.drawable.share_like_true);
//                Log.e("position",i+"");
//                DynamicLikeUser dynamicLikeUser=new DynamicLikeUser(dynamics.get(i).getDynamicId(),1,"张加民");
//                insertDynamicLikeUser(dynamicLikeUser);
//                holder.tvDynamicLikeUsers.setText("aaaa");


            }
        });



   //     viewList.add(view);
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
    public void insertDynamicLikeUser(DynamicLikeUser dynamicLikeUser){
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),new Gson().toJson(dynamicLikeUser));
        Log.e("点赞请求的接口是",ConfigUtil.SERVER_ADDR+"InsertDynamicLikeUserServlet");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"InsertDynamicLikeUserServlet")
                .build();
        //创建Call对象,发送请求，并接受响应

        Call call=okHttpClient.newCall(request);
        //异步网络请求(不需要创建子线程)
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Log.e("登录失败","发布失败");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果",response.body().toString());
                Log.e("点赞的的结果","点赞成功成功");

            }
        });

    }
}
