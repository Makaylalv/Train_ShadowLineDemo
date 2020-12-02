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
import com.example.train_shadowlinedemo.entity.SearchResult;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private List<SearchResult> results;
    private Context mContext;
    private int itemLayoutId;

    public SearchAdapter(Context context, List<SearchResult> resultData, int item_search_result) {
        mContext=context;
        results=resultData;
        itemLayoutId=item_search_result;

    }

    @Override
    public int getCount() {
        if (results!=null){
            return results.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (results!=null){
            return results.get(i);
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
            view= LayoutInflater.from(mContext).inflate(itemLayoutId,null);
            holder=new ViewHolder();
            holder.ivImg=view.findViewById(R.id.iv_item_img);
            holder.tvName=view.findViewById(R.id.tv_item_name);
            holder.tvEnglishName=view.findViewById(R.id.tv_item_englishname);
            holder.tvInfo=view.findViewById(R.id.tv_item_info);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        SearchResult result=results.get(i);
        Glide.with(mContext)
                .load(ConfigUtil.SERVER_ADDR+result.getImgUrl())
                .into(holder.ivImg);

        holder.tvName.setText(result.getName());
        holder.tvEnglishName.setText(result.getEnglishName());
        holder.tvInfo.setText(result.getInfo());
        return view;
    }
    private class ViewHolder{
        ImageView ivImg;
        TextView tvName;
        TextView tvEnglishName;
        TextView tvInfo;
    }
}
