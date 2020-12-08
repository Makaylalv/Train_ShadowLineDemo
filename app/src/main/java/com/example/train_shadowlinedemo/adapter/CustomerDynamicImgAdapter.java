package com.example.train_shadowlinedemo.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.MainActivity;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.fragment.ShareChildrenFragment.DynamicFragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomerDynamicImgAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> imgs = new ArrayList<String>();
    private int itemLayoutRes;
    private Dialog dialog;
    private ImageView iiImg;
    private DynamicFragment dynamicFragment;
    @SuppressLint("ResourceType")
    private List<ImageView> images =new ArrayList<>();

    public CustomerDynamicImgAdapter(Context mContext, List<String> imgs, int itemLayoutRes, DynamicFragment dynamicFragment) {
        this.mContext = mContext;
        this.imgs = imgs;
        this.itemLayoutRes = itemLayoutRes;
        this.dynamicFragment = dynamicFragment;
        dialog = new Dialog(mContext, R.style.FullActivity);
        WindowManager.LayoutParams attributes = dynamicFragment.getActivity().getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(attributes);
        for(int i=0;i<imgs.size();i++){
            images.add(getImageView(imgs.get(i)));
        }


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
        if (null != imgs) {
            return imgs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != imgs) {
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

        Log.e("aaaaaaa",getCount()+"");
        DynamicImgViewHolder dynamicImgViewHolder = null;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_img, null);
            dynamicImgViewHolder = new DynamicImgViewHolder();
            dynamicImgViewHolder.ivDynamicImg = view.findViewById(R.id.iv_dynamic_img);
            view.setTag(dynamicImgViewHolder);
        } else {
            dynamicImgViewHolder = (DynamicImgViewHolder) view.getTag();
        }
        iiImg=view.findViewById(R.id.iv_dynamic_img);
        Glide.with(mContext).load(ConfigUtil.SERVER_ADDR + "imgs/dynamic/dynamicImgs/" + imgs.get(i) + ".jpg").override(300, 300).into(dynamicImgViewHolder.ivDynamicImg);
        Log.e("图片的地址是", ConfigUtil.SERVER_ADDR + "imgs/dynamic/dynamicImgs/" + imgs.get(i) + ".jpg" + imgs.get(i) + ".jpg");
        images.add(getImageView(imgs.get(i))) ;
        Log.e("222222222222",images.toString());
        iiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(images.get(i));
                Log.e("点的是",i+"张");
                dialog.show();
            }
        });

        //大图的点击事件（点击让他消失）
        images.get(i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return view;
    }

    static class DynamicImgViewHolder {
        private ImageView ivDynamicImg;
    }

    private ImageView getImageView(String img) {
        ImageView imageView = new ImageView(mContext);
        //宽高
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        new Thread() {
//            @Override
//            public void run() {
//
//                try {
//                    URL url = new URL(ConfigUtil.SERVER_ADDR + "imgs/dynamic/dynamicImgs/" + img + ".jpg");
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    Log.e("biubiub", ConfigUtil.SERVER_ADDR + "imgs/dynamic/dynamicImgs/" + img + ".jpg");
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.connect();
//                    //imageView设置图片
//                    @SuppressLint("ResourceType") InputStream is = urlConnection.getInputStream();
//                    Drawable drawable = BitmapDrawable.createFromStream(is, null);
//                    imageView.setImageDrawable(drawable);
//                } catch (Exception e) {
//                    Log.e("heieheiehi", e.toString());
//                }
//            }
//        }.start();
        Glide.with(mContext).load(ConfigUtil.SERVER_ADDR + "imgs/dynamic/dynamicImgs/" + img + ".jpg").into(imageView);
        Log.e("点击的图片是",ConfigUtil.SERVER_ADDR + "imgs/dynamic/dynamicImgs/" + img + ".jpg"+img);
        return imageView;
    }

    ;
}
