package com.example.train_shadowlinedemo.view.MovieShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Film;

import java.util.List;

public class NewFilmAdapter extends RecyclerView.Adapter<NewFilmAdapter.ItemViewHolder> {

    private Context mContext;
    private int layoutItemId;
    private List<Film> films;//数据源
    public NewFilmAdapter(Context context,List<Film> list,int layoutItemId){
        films=list;
        this.layoutItemId=layoutItemId;
        this.mContext=context;
    }
    //反射出item布局
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //利用反射将item布局加载出来
        View view= LayoutInflater.from(parent.getContext()).inflate(layoutItemId,parent,false);
        //new一个我们的ViewHolder，findViewById操作都在ItemViewHolder的构造方法中进行
        return new ItemViewHolder(view);
    }

    //为布局中的控件设置数据
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Film film=films.get(position);
        //Glide.with(mContext).load(film.getFilmImg()).into(holder.ivImg);
        holder.ivImg.setImageResource(Integer.parseInt(film.getFilmImg()));
        holder.tvName.setText(film.getFilmName());
        holder.tvOutTime.setText(film.getFilmReleasetime());
        holder.tvProductFrom.setText(film.getFilmProducercountry());
    }

    @Override
    public int getItemCount() {
        //返回数据个数
        return films.size();
    }
    //相当于ListView中的ViewHolder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvName;
        TextView tvOutTime;
        TextView tvProductFrom;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg=itemView.findViewById(R.id.iv_newfilm_img);
            tvName=itemView.findViewById(R.id.tv_newfilm_name);
            tvOutTime=itemView.findViewById(R.id.tv_newfilm_outtime);
            tvProductFrom=itemView.findViewById(R.id.tv_newfilm_productfrom);

        }
    }
}

