package com.example.train_shadowlinedemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;

import java.util.ArrayList;
import java.util.List;

public class SpotListAdapter extends BaseAdapter {
    private List<Place> places = new ArrayList<>();
    private Context context;
    private int layoutId;

    @Override
    public int getCount() {
        if (places != null) {
            return places.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (places != null) {
            return places.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public SpotListAdapter(List<Place> places, Context context, int layoutId) {
        this.places = places;
        this.context = context;
        this.layoutId = layoutId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutId, null);
            viewHolder=new ViewHolder();
            viewHolder.pic= convertView.findViewById(R.id.city_spot_photo);
            viewHolder.english= convertView.findViewById(R.id.city_spot_english);
            viewHolder.chinese= convertView.findViewById(R.id.city_spot_chinese);
            //缓存viewHolder对象
            convertView.setTag(viewHolder);//这个方法可以缓存任意类型对象
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.chinese.setText(places.get(position).getPlaceName());
        viewHolder.english.setText(places.get(position).getPlaceEnglishname() + "");
        String pic=places.get(position).getPlaceReallyImg();
        Glide.with(context).load(ConfigUtil.SERVER_ADDR+pic).into(viewHolder.pic);
        Log.e("图片地址",ConfigUtil.SERVER_ADDR+pic);
//        viewHolder.pic.setImageBitmap(Glide);
        return convertView;
    }

    private class ViewHolder {
        //定义每个item布局中
        ImageView pic;
        TextView chinese;
        TextView english;
    }
}
