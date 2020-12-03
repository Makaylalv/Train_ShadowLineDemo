package com.example.train_shadowlinedemo.Personal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.train_shadowlinedemo.R;

import java.util.ArrayList;
import java.util.List;

public class CityCollectionAdapter extends BaseAdapter {
    private android.content.Context Context;
    private int itemLayoutRes;
    private List<CityCollection> cityCollections = new ArrayList<>();
    private boolean isOP;
    public CityCollectionAdapter(Context context, List<CityCollection> cityCollections, int itemLayoutRes){
        this.Context = context;
        this.itemLayoutRes = itemLayoutRes;
        this.cityCollections = cityCollections;
    }

    public void setOP(boolean OP) {
        isOP = OP;
    }

    @Override
    public int getCount() {
        if (cityCollections!=null){
            return cityCollections.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (cityCollections!=null){
            return cityCollections.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Log.e("cityCollections",cityCollections.toString());
        View view;
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(Context);
        if (convertView == null){
            view = inflater.inflate(R.layout.item_city_collection, null);
            holder = new ViewHolder();
            holder.cb_op = view.findViewById(R.id.cb_op);
            holder.tv_name = view.findViewById(R.id.city_name);
            holder.tv_english = view.findViewById(R.id.city_english);
            holder.city = view.findViewById(R.id.city);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        if (isOP){
            holder.cb_op.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) holder.city.getLayoutParams();
            lps.leftMargin = 60;
//            Button button = new Button(Context);
//            button.setId(1);
//            RelativeLayout relativeLayout = view.findViewById(R.id.rv_city_list);
//            relativeLayout.addView(button);
        }else {
            holder.cb_op.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) holder.city.getLayoutParams();
            lps.leftMargin = 0;
        }
        CityCollection c = cityCollections.get(i);
        if (c.isChecked()){
            holder.cb_op.setChecked(true);
        }else {
            holder.cb_op.setChecked(false);
        }
        holder.tv_name.setText(c.getCity_name());
        holder.tv_english.setText(c.getCity_english());
        holder.cb_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = c.isChecked();
                c.setChecked(!isCheck);
            }
        });


        return view;
    }
    static class ViewHolder{
        CheckBox cb_op;
        RelativeLayout city;
        TextView tv_name;
        TextView tv_english;

    }
}
