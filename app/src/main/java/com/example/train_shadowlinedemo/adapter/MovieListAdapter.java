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
import com.example.train_shadowlinedemo.entity.Film;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends BaseAdapter {
    private List<Film> films = new ArrayList<>();
    private Context context;
    private int layoutId;

    @Override
    public int getCount() {
        if (films != null) {
            return films.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (films != null) {
            return films.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public MovieListAdapter(List<Film> films, Context context, int layoutId) {
        this.films = films;
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
            viewHolder.pic= convertView.findViewById(R.id.city_movie_photo);
            viewHolder.english= convertView.findViewById(R.id.city_movie_english);
            viewHolder.chinese= convertView.findViewById(R.id.city_movie_chinese);
            //缓存viewHolder对象
            convertView.setTag(viewHolder);//这个方法可以缓存任意类型对象
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.chinese.setText(films.get(position).getFilmName());
        viewHolder.english.setText(films.get(position).getFilmEnglishname() + "");
        String pic=films.get(position).getFilmImg();
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
