package com.example.train_shadowlinedemo.view.MovieDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;

import java.util.ArrayList;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<Place> places;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_time;
        TextView textView_name;
        TextView textView_pos;
        TextView textView_des;
        ImageView imageView;

        public ViewHolder (View view)
        {
            super(view);
            imageView=view.findViewById(R.id.film_img);
            textView_time=view.findViewById(R.id.film_time);
            textView_name=view.findViewById(R.id.film_name);
            textView_pos=view.findViewById(R.id.film_pos);
            textView_des=view.findViewById(R.id.film_des);
            itemView.setOnClickListener(PlaceRecyclerViewAdapter.this);
        }

    }
    public PlaceRecyclerViewAdapter(ArrayList<Place> list,Context context){
        places = list;
        this.context=context;
    }
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Place place= places.get(position);
        holder.textView_time.setText(place.getPlaceTime());
        holder.textView_pos.setText(place.getPlacePosition());
        holder.textView_des.setText(place.getPlaceFilmDescribe());
        holder.textView_name.setText(place.getPlaceName());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
                .error(R.drawable.glide_error)//请求失败时显示
                .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示
        Glide.with(context)
                .load("http://192.168.43.128:8080/ShadowLine/"+place.getPlaceFalseImg())
                .apply(options)//应用请求选项
                .into(holder.imageView);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount(){
        return places.size();
    }


    //=======================以下为item中的button控件点击事件处理===================================

    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();//getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.recyclerview:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }

}
