package com.example.train_shadowlinedemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.Personal.DeleteCommentPopupWindow;
import com.example.train_shadowlinedemo.Personal.DeleteDynamicPopupWindow;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.activity.LoginActivity;
import com.example.train_shadowlinedemo.entity.Comment;
import com.example.train_shadowlinedemo.entity.Dynamic;
import com.example.train_shadowlinedemo.entity.DynamicLikeUser;
import com.example.train_shadowlinedemo.fragment.ShareChildrenFragment.DynamicFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private DeleteDynamicPopupWindow deleteDynamicPopupWindow;
    private ImageView ivUserImg;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Drawable drawable= (Drawable) msg.obj;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap head = bitmapDrawable.getBitmap();
                    Bitmap bitmap =  toRoundBitmap(head);
                    Glide.with(mContext).load(bitmap).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).
                    into(ivUserImg);//跳过内存缓存;
                    break;
                case 2:
                    Toast toast1=Toast.makeText(mContext,"评论失败",Toast.LENGTH_SHORT);
                    toast1.show();
                    break;
                case 3:
                    dynamicFragment.notifyDataSetChanged();
                    Toast toast2=Toast.makeText(mContext,"评论成功",Toast.LENGTH_SHORT);
                    toast2.show();
                    break;
                case 4:
                    dynamicFragment.notifyDataSetChanged();
                    Toast toast3=Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT);
                    toast3.show();
                    break;
                case 5:
                    Toast toast4=Toast.makeText(mContext,"删除失败",Toast.LENGTH_SHORT);
                    toast4.show();
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
            ivUserImg=view.findViewById(R.id.iv_dynamic_userimg);
            holder.tvDynamicUserName=view.findViewById(R.id.tv_dynamic_username);
            holder.tvDynamicDynamicTime=view.findViewById(R.id.tv_dynamic_dynamictime);
            holder.tvDynmaicDynamicContent=view.findViewById(R.id.tv_dynamic_dynamiccontent);
            holder.gvDynamicDynamicImgs=view.findViewById(R.id.gv_dynamic_dynamicimgs);
            GridView gvDynamicDynamicImgs =view.findViewById(R.id.gv_dynamic_dynamicimgs);
            ListView lvDynamicComments=view.findViewById(R.id.lv_dynamic_comments);
            ImageView ivDynamicManage=view.findViewById(R.id.iv_dynamicmanage);
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
        //diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).
        Glide.with(mContext).load(ConfigUtil.SERVER_ADDR+"imgs/user/userimgs/"+dynamics.get(i).getUserId()+".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).circleCrop().into(holder.ivDynamicUserImg);
      //  loadImageFromNetwork(ConfigUtil.SERVER_ADDR+"imgs/user/userimgs/"+ dynamics.get(i).getUserId()+".jpg");
        Log.e("动态中用户的头像是",ConfigUtil.SERVER_ADDR+"imgs/user/userimgs/"+ dynamics.get(i).getUserId()+".jpg");

        holder.tvDynamicUserName.setText(dynamics.get(i).getUserName());
        holder.tvDynamicDynamicTime.setText(dynamics.get(i).getDynamicTime());
        holder.tvDynmaicDynamicContent.setText(dynamics.get(i).getDynamicContent());
        AssetManager mgr = mContext.getResources().getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "Regular.ttf");//仿宋
        holder.tvDynmaicDynamicContent.setTypeface(tf);
        holder.tvDynamicLikeUsers.setText(dynamics.get(i).getLikeUser().toString()+"觉得很赞");
        holder.tvUserLocation.setText(dynamics.get(i).getDynamicPlace()+" ");
        if(dynamics.get(i).getLikeUser().contains(userName)){
            holder.btnDynamicLike.setImageResource(R.drawable.share_like_true);
            likeflag[0]=1;
        }else{
            holder.btnDynamicLike.setImageResource(R.drawable.share_like_false);
            likeflag[0]=0;
        }


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
                comment_content.requestFocus();


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
                    notifyDataSetChanged();
                }
            }
        });
        //为管理动态按钮设置点击事件
        ivDynamicManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dynamics.get(i).getUserId()==LoginActivity.user.getUser_id()&&dynamics.get(i).getUserName().equals(LoginActivity.user.getUser_name())){
                    Log.e("点击的动态是",dynamics.get(i).toString());
                    deleteDynamicPopupWindow=new DeleteDynamicPopupWindow(dynamicFragment.getActivity(), new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            deleteDynamicPopupWindow.dismiss();


                            deleteDynamicById(dynamics.get(i));

                        }
                    },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteDynamicPopupWindow.dismiss();
                                }
                            });
                    View rootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_shared_dynamic_fragment,null);
                    deleteDynamicPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
                    Log.e("啊啊","a");
                }else{

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
    private void loadImageFromNetwork(String imageUrl) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url=new URL(imageUrl);
                    Log.e("aaaaa",imageUrl);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    InputStream in=url.openStream();
                    Drawable drawable=new BitmapDrawable(BitmapFactory.decodeStream(in));
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=drawable;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    //将图片设置为圆形
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        //圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //正方形的边长
        int r = 0;
        //取最短边做边长
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        //构建一个bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true);
        //宽高相等，即正方形
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);
        //返回已经绘画好的backgroundBmp
        return backgroundBmp;
    }
    //删除动态
    //删除评论
    public void deleteDynamicById(Dynamic dynamic) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), new Gson().toJson(dynamic));
        Log.e("删除动态请求的接口是", ConfigUtil.SERVER_ADDR + "DeleteDynamicByIdServlet");
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ConfigUtil.SERVER_ADDR + "DeleteDynamicByIdServlet")
                .build();
        //创建Call对象,发送请求，并接受响应

        Call call = okHttpClient.newCall(request);
        //异步网络请求(不需要创建子线程)
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时回调
                e.printStackTrace();
                Message message = handler.obtainMessage();
                message.what = 5;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("异步请求的结果", response.body().toString());
                Log.e("取消点赞的的结果", "取消点赞成功");
                Message message = handler.obtainMessage();
                message.what = 4;
                handler.sendMessage(message);
            }
        });
    }

}
