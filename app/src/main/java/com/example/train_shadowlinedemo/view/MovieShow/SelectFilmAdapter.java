package com.example.train_shadowlinedemo.view.MovieShow;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_shadowlinedemo.R;

import java.util.ArrayList;
import java.util.List;

public class SelectFilmAdapter extends RecyclerView.Adapter<SelectFilmAdapter.MyHolder>{
    private Context mContext;
    private List<String> mDatas;
    private int itemLayoutId;
    private OnItemClickLitener onItemClickLitener;
    private List<Boolean> isClicks;
    public List<TextView> views=new ArrayList<>();

    public SelectFilmAdapter(Context mContext, List<String> mDatas, int itemLayoutId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.itemLayoutId = itemLayoutId;
        isClicks=new ArrayList<>();
        for(int i = 0;i<mDatas.size();i++){
            isClicks.add(false);
        }

    }
    public interface OnItemClickLitener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener){
        this.onItemClickLitener=onItemClickLitener;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(itemLayoutId,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));
        views.add(holder.tv);
        if(isClicks.get(position)){
            holder.tv.setTextColor(Color.WHITE);
            holder.tv.setBackgroundColor(Color.BLACK);
        }else{
            holder.tv.setTextColor(Color.BLACK);
            holder.tv.setBackgroundColor(Color.WHITE);
        }
        if (onItemClickLitener!=null){
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i=0;i<isClicks.size();i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
                    onItemClickLitener.onItemClick(holder.tv,position);
                }
            });
        }

    }

    public List<String> getmDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_select_film);
        }
    }

}
