package com.example.train_shadowlinedemo.view.MovieShow;

import android.content.Context;
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

public class HotFilmListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Film> films=new ArrayList<>();
    private int rightLayoutItemId;
    private int leftLayoutItemId;

    public HotFilmListViewAdapter(Context mContext, List<Film> films, int layoutItemId,int leftLayoutItemId) {
        this.mContext = mContext;
        this.films = films;
        this.rightLayoutItemId = layoutItemId;
        this.leftLayoutItemId=leftLayoutItemId;
    }

    @Override
    public int getCount() {
        if (null!=films){
            return  films.size();
        }
        return 0;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view == null) {
            if (position % 2 == 0) {
                view = LayoutInflater.from(mContext).inflate(rightLayoutItemId, null);
            } else {
                view = LayoutInflater.from(mContext).inflate(leftLayoutItemId, null);
            }

            holder = new ViewHolder();
            holder.ivImg = view.findViewById(R.id.iv_img);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvInfo=view.findViewById(R.id.tv_filminfo);
            //缓存ViewHolder对象
            view.setTag(holder);
        } else {
            //获取缓存的ViewHolder对象
            holder = (ViewHolder) view.getTag();
        }
        Film film = films.get(position);
        //给布局赋值
        Glide.with(mContext)
                .load(ConfigUtil.SERVER_ADDR+film.getFilmImg())
                .centerCrop()
                .into(holder.ivImg);
        holder.tvName.setText(film.getFilmName());
        holder.tvInfo.setText(film.getFilmInfo());
        return view;
    }
    private class ViewHolder{
        ImageView ivImg;
        TextView tvName;
        TextView tvInfo;
    }
}
