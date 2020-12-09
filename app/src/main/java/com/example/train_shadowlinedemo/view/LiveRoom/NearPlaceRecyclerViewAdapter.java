package com.example.train_shadowlinedemo.view.LiveRoom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.PlaceAndFilm;
import com.example.train_shadowlinedemo.entity.Room;

import java.util.ArrayList;
import java.util.List;

public class NearPlaceRecyclerViewAdapter extends RecyclerView.Adapter<NearPlaceRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{
    private List<PlaceAndFilm> placeAndFilms;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView placeImg;
        TextView placeNameTV;
        TextView filmNameTV;

        public ViewHolder (View view)
        {
            super(view);
            placeImg=view.findViewById(R.id.near_img);
            placeNameTV=view.findViewById(R.id.near_place_name);
            filmNameTV=view.findViewById(R.id.near_place_film_name);
            itemView.setOnClickListener(NearPlaceRecyclerViewAdapter.this);
        }

    }
    public NearPlaceRecyclerViewAdapter(List<PlaceAndFilm> placeAndFilms, Context context){
        Log.e("name",placeAndFilms.get(placeAndFilms.size()-1).getPlace().getPlaceName());

        this.placeAndFilms = placeAndFilms;
        this.context=context;
    }
    @Override

    public NearPlaceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_near_place,parent,false);

        NearPlaceRecyclerViewAdapter.ViewHolder holder = new NearPlaceRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(NearPlaceRecyclerViewAdapter.ViewHolder holder, int position){


        PlaceAndFilm placeAndFilm=placeAndFilms.get(position);

        holder.placeNameTV.setText(placeAndFilm.getPlace().getPlaceName());
        holder.filmNameTV.setText("《"+placeAndFilm.getFilm().getFilmName()+"》");

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
//                .bitmapTransform(new RoundedCorners(20))
                .error(R.drawable.glide_error)//请求失败时显示
                .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示
        Glide.with(context)
                .load(ConfigUtil.SERVER_ADDR +placeAndFilm.getPlace().getPlaceFalseImg())
                .apply(options)//应用请求选项
                .into(holder.placeImg);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount(){
        return placeAndFilms.size();
    }


    //=======================以下为item中的button控件点击事件处理===================================

    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, NearPlaceRecyclerViewAdapter.ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private NearPlaceRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(NearPlaceRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();//getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.recyclerview:
                    mOnItemClickListener.onItemClick(v, NearPlaceRecyclerViewAdapter.ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, NearPlaceRecyclerViewAdapter.ViewName.ITEM, position);
                    break;
            }
        }
    }
}
