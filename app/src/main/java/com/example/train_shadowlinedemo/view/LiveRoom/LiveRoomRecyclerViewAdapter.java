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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.entity.Room;
import com.example.train_shadowlinedemo.view.MovieDetail.PlaceRecyclerViewAdapter;

import java.util.ArrayList;

public class LiveRoomRecyclerViewAdapter extends RecyclerView.Adapter<LiveRoomRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{
    private ArrayList<Room> roomList;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userNameTextView;
        ImageView roomImageView;

        public ViewHolder (View view)
        {
            super(view);
            userNameTextView=view.findViewById(R.id.user_name);
            roomImageView=view.findViewById(R.id.room_img);
            itemView.setOnClickListener(LiveRoomRecyclerViewAdapter.this);
        }

    }
    public LiveRoomRecyclerViewAdapter(ArrayList<Room> roomList,Context context){
        this.roomList = roomList;
        this.context=context;
    }
    @Override

    public LiveRoomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false);
        LiveRoomRecyclerViewAdapter.ViewHolder holder = new LiveRoomRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(LiveRoomRecyclerViewAdapter.ViewHolder holder, int position){

        Room room=roomList.get(position);


        holder.userNameTextView.setText(room.getUserName());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
//                .bitmapTransform(new RoundedCorners(20))
                .error(R.drawable.glide_error)//请求失败时显示
                .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示

        int n = (int)(1+Math.random()*3);
        Log.e("11111",n+"");
        if(n==1){
            Glide.with(context)
                    .load(R.drawable.room_1)
                    .apply(options)//应用请求选项
                    .into(holder.roomImageView);
        }else if(n==2){
            Glide.with(context)
                    .load(R.drawable.room_2)
                    .apply(options)//应用请求选项
                    .into(holder.roomImageView);
        }else if(n==3){
            Glide.with(context)
                    .load(R.drawable.room_3)
                    .apply(options)//应用请求选项
                    .into(holder.roomImageView);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount(){
        return roomList.size();
    }


    //=======================以下为item中的button控件点击事件处理===================================

    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, LiveRoomRecyclerViewAdapter.ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private LiveRoomRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(LiveRoomRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();//getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.recyclerview:
                    mOnItemClickListener.onItemClick(v, LiveRoomRecyclerViewAdapter.ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, LiveRoomRecyclerViewAdapter.ViewName.ITEM, position);
                    break;
            }
        }
    }
}
