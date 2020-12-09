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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.Personal.DeleteCommentPopupWindow;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.entity.Comment;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.example.train_shadowlinedemo.entity.DynamicLikeUser;
import com.example.train_shadowlinedemo.fragment.ShareChildrenFragment.DynamicFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private DynamicFragment  dynamicFragment;
    private String userName= LoginActivity.user.getUser_name();
    private int userId=LoginActivity.user.getUser_id();
    private DeleteCommentPopupWindow deleteCommentPopupWindow;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    Toast toast1=Toast.makeText(mContext,"评论失败",Toast.LENGTH_SHORT);
                    toast1.show();
                    break;
                case 3:
                    Toast toast2=Toast.makeText(mContext,"评论成功",Toast.LENGTH_SHORT);
                    toast2.show();
                    break;
            }
        }
    };

    public CustomerDynamicAdapter(Context mContext, List<Dynamic> dynamics, int itemLayoutRes,DynamicFragment dynamicFragment) {
        this.mContext = mContext;
        this.dynamics = dynamics;
        this.itemLayoutRes = itemLayoutRes;
        this.dynamicFragment=dynamicFragment;
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
       // if(null==view){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_dynamic,null);
            holder=new DynamicViewHolder();
            holder.ivDynamicUserImg=view.findViewById(R.id.iv_dynamic_userimg);
            holder.tvDynamicUserName=view.findViewById(R.id.tv_dynamic_username);
            holder.tvDynamicDynamicTime=view.findViewById(R.id.tv_dynamic_dynamictime);
            holder.tvDynmaicDynamicContent=view.findViewById(R.id.tv_dynamic_dynamiccontent);
            holder.gvDynamicDynamicImgs=view.findViewById(R.id.gv_dynamic_dynamicimgs);
            GridView gvDynamicDynamicImgs =view.findViewById(R.id.gv_dynamic_dynamicimgs);
            ListView lvDynamicComments=view.findViewById(R.id.lv_dynamic_comments);
            holder.btnDynamicLike=view.findViewById(R.id.btn_dynamic_like);
            holder.btnDynamicComment=view.findViewById(R.id.btn_dynamic_comment);
            holder.btnDynamicForward=view.findViewById(R.id.btn_dynamic_forward);
            holder.tvDynamicLikeUsers=view.findViewById(R.id.tv_dynamic_likeusers);
            holder.lvDynamicComments=view.findViewById(R.id.lv_dynamic_comments);
            holder.tvUserLocation=view.findViewById(R.id.tv_user_location);
            TextView tv=view.findViewById(R.id.tv_dynamic_likeusers);
            view.setTag(holder);
//        }else{
//            holder= (DynamicViewHolder) view.getTag();
//        }
        final int[] likeflag = {0};
        Glide.with(mContext).load(dynamics.get(i).getUserImg()).circleCrop().into(holder.ivDynamicUserImg);
        holder.tvDynamicUserName.setText(dynamics.get(i).getUserName());
        holder.tvDynamicDynamicTime.setText(dynamics.get(i).getDynamicTime());
        holder.tvDynmaicDynamicContent.setText(dynamics.get(i).getDynamicContent());
        holder.tvDynamicLikeUsers.setText(dynamics.get(i).getLikeUser().toString()+"觉得很赞");
        holder.tvUserLocation.setText(dynamics.get(i).getDynamicPlace()+" ");
        if(dynamics.get(i).getLikeUser().contains(userName)){
            holder.btnDynamicLike.setImageResource(R.drawable.share_like_true);
            likeflag[0]=1;
        }else{
            holder.btnDynamicLike.setImageResource(R.drawable.share_like_false);
            likeflag[0]=0;
        }

        Glide.with(mContext).load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1528689441,659647338&fm=26&gp=0.jpg").circleCrop().into(holder.ivDynamicUserImg);
        CustomerDynamicImgAdapter customerDynamicImgAdapter=new CustomerDynamicImgAdapter(mContext,dynamics.get(i).getDynamicImgs(),R.layout.item_dynamic_img,dynamicFragment);
        List<String> dynamicsimgs=dynamics.get(i).getDynamicImgs();
        Log.e("我的动态1",dynamicsimgs.size()+""+dynamicsimgs.toString());
        if(dynamicsimgs.size()==0){
           gvDynamicDynamicImgs.setVisibility(View.GONE);
        }

        gvDynamicDynamicImgs.setAdapter(customerDynamicImgAdapter);
        //为评论设置监听器
        CustomerDynamicCommentAdapter customerDynamicCommentAdapter=new CustomerDynamicCommentAdapter(mContext,dynamics.get(i).getComments(),R.layout.item_comment,dynamicFragment);
        Log.e("评论的内容是",dynamics.get(i).getComments().toString());
        lvDynamicComments.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,70*dynamics.get(i).getComments().size()));

        lvDynamicComments.setAdapter(customerDynamicCommentAdapter);

        TextView hide_down = view.findViewById(R.id.hide_down);
        EditText comment_content = view.findViewById(R.id.comment_content);
        Button comment_send = view.findViewById(R.id.comment_send);
        RelativeLayout rlComment=view.findViewById(R.id.rl_comment);
        //为点赞按钮设置点击事件
        holder.btnDynamicLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView=view.findViewById(R.id.btn_dynamic_like);
                if(likeflag[0]==0){
                    imageView.setImageResource(R.drawable.share_like_true);
                  //  holder.btnDynamicLike.setImageResource(R.drawable.share_like_true);
                    likeflag[0]=1;
                    DynamicLikeUser dynamicLikeUser=new DynamicLikeUser(dynamics.get(i).getDynamicId(),userId,userName);
                    dynamics.get(i).getLikeUser().add(userName);
                    insertDynamicLikeUser(dynamicLikeUser,i);
                    tv.setText(dynamics.get(i).getLikeUser().toString()+"觉得很赞");
                }else if(likeflag[0]==1){
                    imageView.setImageResource(R.drawable.share_like_false);
                  //  holder.btnDynamicLike.setImageResource(R.drawable.share_like_false);
                    DynamicLikeUser dynamicLikeUser=new DynamicLikeUser(dynamics.get(i).getDynamicId(),userId,userName);
                    dynamics.get(i).getLikeUser().remove(userName);
                    likeflag[0]=0;
                    deleteDynamicLikeUser(dynamicLikeUser,i);
                    tv.setText(dynamics.get(i).getLikeUser().toString()+"觉得很赞");
                }
            }
        });
        //为评论按钮设置点击事件
        holder.btnDynamicComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹出输入法
                InputMethodManager imm = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                rlComment.setVisibility(View.VISIBLE);

            }
        });
        //为取消按钮设置点击事件
        hide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlComment.setVisibility(View.GONE);
                // 隐藏输入法，然后暂存当前输入框的内容，方便下次使用
                InputMethodManager im = (InputMethodManager)mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);

            }
        });
        //为发送按钮设置点击事件
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment_content.getText().toString().equals("")){
                    Toast toast=Toast.makeText(mContext,"不能评论空的内容",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    Comment comment =new Comment(dynamics.get(i).getDynamicId(),userId,userName,comment_content.getText().toString(),df.format(new Date()));
                    insertDynamicComment(comment);
                    comment_content.setText("");
                    InputMethodManager im = (InputMethodManager)mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
                    dynamicFragment.notifyDataSetChanged();
                }
            }
        });
        return view;
    }
    class DynamicViewHolder{
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
    public void insertDynamicLikeUser(DynamicLikeUser dynamicLikeUser,int i){
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
                Log.e("点赞失败","点赞失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果",response.body().toString());
                Log.e("点赞的的结果","点赞成功成功");
                //

            }
        });

    }
    public void deleteDynamicLikeUser(DynamicLikeUser dynamicLikeUser,int i){
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),new Gson().toJson(dynamicLikeUser));
        Log.e("点赞请求的接口是",ConfigUtil.SERVER_ADDR+"DeleteLikeUserServlet");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"DeleteLikeUserServlet")
                .build();
        //创建Call对象,发送请求，并接受响应

        Call call=okHttpClient.newCall(request);
        //异步网络请求(不需要创建子线程)
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Log.e("取消点赞失败","取消点赞失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果",response.body().toString());
                Log.e("取消点赞的的结果","取消点赞成功");
            }
        });
    }
    public void insertDynamicComment(Comment comment){
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),new Gson().toJson(comment));
        Log.e("评论请求的接口是",ConfigUtil.SERVER_ADDR+"InsertDynamicCommentServlet");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"InsertDynamicCommentServlet")
                .build();
        //创建Call对象,发送请求，并接受响应
        Call call=okHttpClient.newCall(request);
        //异步网络请求(不需要创建子线程)
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Log.e("评论失败","评论失败");
                Message message=handler.obtainMessage();
                message.what=2;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果",response.body().toString());
                Log.e("评论成功","评论成功");
                Message message=handler.obtainMessage();
                message.what=3;
                handler.sendMessage(message);
            }
        });
    }


    public int DpToPx(Context context,float number){
        float scale =context.getResources().getDisplayMetrics().density;
        return Math.round(scale);
    }


}
