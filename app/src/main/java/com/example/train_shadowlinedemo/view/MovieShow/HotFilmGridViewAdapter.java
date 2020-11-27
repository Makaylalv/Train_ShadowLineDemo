package com.example.train_shadowlinedemo.view.MovieShow;

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

public class HotFilmGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Film> films;
    private int layoutItemId;

    public HotFilmGridViewAdapter(Context mContext, List<Film> films, int layoutItemId) {
        this.mContext = mContext;
        this.films = films;
        this.layoutItemId = layoutItemId;
        Log.e("HotFilmGridViewAdapter",films.size()+"");
    }

    @Override
    public int getCount() {
        return  films.size();
    }

    @Override
    public Object getItem(int i) {
        if(null!=films){
            return  films.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder=null;
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(mContext).inflate(layoutItemId,null);
            holder.ivNewFilmImg=view.findViewById(R.id.iv_hotfilm_img);
            holder.tvNewFilmName=view.findViewById(R.id.tv_hotfilm_name);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        Film film=films.get(i);
        Glide.with(mContext)
                .load(ConfigUtil.SERVER_ADDR+film.getFilmImg())
                .fitCenter()
                .into(holder.ivNewFilmImg);
        holder.tvNewFilmName.setText(film.getFilmName());
        return view;
    }
    private class ViewHolder{
        ImageView ivNewFilmImg;
        TextView tvNewFilmName;

    }
}
