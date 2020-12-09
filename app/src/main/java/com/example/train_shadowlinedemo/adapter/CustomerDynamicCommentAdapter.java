package com.example.train_shadowlinedemo.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.MainActivity;
import com.example.train_shadowlinedemo.Personal.DeleteCommentPopupWindow;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.entity.Comment;
import com.example.train_shadowlinedemo.fragment.PersonalFragment;
import com.example.train_shadowlinedemo.fragment.ShareChildrenFragment.DynamicFragment;
import com.example.train_shadowlinedemo.fragment.ShareFragment;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

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

public class CustomerDynamicCommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comment> comments=new ArrayList<>();
    private int itemLayoutRes;
    private DynamicFragment dynamicFragment;
    private DeleteCommentPopupWindow deleteCommentPopupWindow;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast toast0=Toast.makeText(mContext,"删除失败",Toast.LENGTH_SHORT);
                    toast0.show();
                    break;
                case 1:
                    dynamicFragment.notifyDataSetChanged();
                    Toast toast1=Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT);
                    toast1.show();
                    break;
            }
        }
    };




    public CustomerDynamicCommentAdapter(Context mContext, List<Comment> comments, int itemLayoutRes, DynamicFragment dynamicFragment) {
        this.mContext = mContext;
        this.comments = comments;
        this.itemLayoutRes = itemLayoutRes;
        this.dynamicFragment=dynamicFragment;

    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getItemLayoutRes() {
        return itemLayoutRes;
    }

    public void setItemLayoutRes(int itemLayoutRes) {
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=comments){
            return comments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null!=comments){
            return comments.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DynamicCommentViewHolder dynamicCommentViewHolder=null;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, null);
            dynamicCommentViewHolder=new DynamicCommentViewHolder();
            dynamicCommentViewHolder.tvCommentUserName=view.findViewById(R.id.tv_commentusername);
            dynamicCommentViewHolder.tvCommentContent=view.findViewById(R.id.tv_commentcontent);
            dynamicCommentViewHolder.llComment=view.findViewById(R.id.ll_comment);
            view.setTag(dynamicCommentViewHolder);
        } else {
            dynamicCommentViewHolder = (DynamicCommentViewHolder) view.getTag();
        }
        dynamicCommentViewHolder.tvCommentUserName.setText(comments.get(i).getUsername());
        dynamicCommentViewHolder.tvCommentContent.setText(comments.get(i).getCommentContent());
        dynamicCommentViewHolder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("点击了用户民","a");
                if(comments.get(i).getUserId()==LoginActivity.user.getUser_id()&&comments.get(i).getUsername().equals(LoginActivity.user.getUser_name())){
                      deleteCommentPopupWindow=new DeleteCommentPopupWindow(dynamicFragment.getActivity(), new View.OnClickListener(){
                          @Override
                          public void onClick(View view) {
                              deleteCommentPopupWindow.dismiss();
                               deleteCommentById(comments.get(i));
                          }
                      },
                              new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      deleteCommentPopupWindow.dismiss();
                                  }
                              });
                    View rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_shared_dynamic_fragment,null);
                    deleteCommentPopupWindow.showAtLocation((View)view.getParent().getParent(), Gravity.BOTTOM,0,0);
                    Log.e("啊啊","a");
                }else{

                }


            }

        });




       return view;
    }

    class DynamicCommentViewHolder{
        TextView tvCommentUserName;
        TextView tvCommentContent;
        LinearLayout llComment;
    }
    //删除评论
    public void deleteCommentById(Comment comment){
        RequestBody requestBody=RequestBody.create(MediaType.parse("text/plain;charset=utf-8"),new Gson().toJson(comment));
        Log.e("删除评论请求的接口是", ConfigUtil.SERVER_ADDR+"DeleteCommentByIdServlet");
        Request request=new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR+"DeleteCommentByIdServlet")
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
                Message message=handler.obtainMessage();
                message.what=0;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果",response.body().toString());
                Log.e("取消点赞的的结果","取消点赞成功");
                Message message=handler.obtainMessage();
                message.what=1;
                handler.sendMessage(message);
            }
        });
    }
}
