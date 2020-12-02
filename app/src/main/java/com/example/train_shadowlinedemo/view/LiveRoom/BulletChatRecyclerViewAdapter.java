package com.example.train_shadowlinedemo.view.LiveRoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.BulletChat;

import java.util.ArrayList;

public class BulletChatRecyclerViewAdapter extends RecyclerView.Adapter<BulletChatRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{
    private ArrayList<BulletChat> bulletChats;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userNameTV;
        TextView contentTV;


        public ViewHolder (View view)
        {
            super(view);
            userNameTV=view.findViewById(R.id.user_name);
            contentTV=view.findViewById(R.id.content);
            itemView.setOnClickListener(com.example.train_shadowlinedemo.view.LiveRoom.BulletChatRecyclerViewAdapter.this);
        }

    }
    public BulletChatRecyclerViewAdapter(ArrayList<BulletChat> bulletChats, Context context){
        this.bulletChats = bulletChats;
        this.context=context;
    }
    @Override

    public BulletChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
        BulletChatRecyclerViewAdapter.ViewHolder holder = new BulletChatRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(BulletChatRecyclerViewAdapter.ViewHolder holder, int position){

        BulletChat bulletChat=bulletChats.get(position);
        holder.userNameTV.setText(bulletChat.getUserName());
        holder.contentTV.setText(bulletChat.getContent());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount(){
        return bulletChats.size();
    }


    //=======================以下为item中的button控件点击事件处理===================================

    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, BulletChatRecyclerViewAdapter.ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private BulletChatRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(BulletChatRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();//getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.rv:
                    mOnItemClickListener.onItemClick(v, BulletChatRecyclerViewAdapter.ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, BulletChatRecyclerViewAdapter.ViewName.ITEM, position);
                    break;
            }
        }
    }
}
