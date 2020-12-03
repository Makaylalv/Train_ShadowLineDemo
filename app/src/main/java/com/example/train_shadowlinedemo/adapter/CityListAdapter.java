package com.example.train_shadowlinedemo.adapter;

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
import com.example.train_shadowlinedemo.entity.City;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends BaseAdapter {
    private Context context;
    private int itemLayoutRes0;
    private int itemLayoutRes1;
    private List<City> items=new ArrayList<>();

    public CityListAdapter(Context mContext, int itemLayoutRes0, int itemLayoutRes1, List<City> items) {
        this.context = mContext;
        this.itemLayoutRes0 = itemLayoutRes0;
        this.itemLayoutRes1 = itemLayoutRes1;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (null != items){
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != items){
            return items.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder=null;
       // ViewHolder1 viewHolder1=null;
        if (view == null) {
            if (i % 2 == 0) {
                view = LayoutInflater.from(context).inflate(itemLayoutRes0, null);
            } else {
                view = LayoutInflater.from(context).inflate(itemLayoutRes1, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.chinese= view.findViewById(R.id.cityTitleChinese);
            viewHolder.english= view.findViewById(R.id.cityTitleEnglish);
            viewHolder.cityPhoto=view.findViewById(R.id.cityPhoto);
            //缓存ViewHolder对象
            view.setTag(viewHolder);
        } else {
            //获取缓存的ViewHolder对象
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.chinese.setText(items.get(i).getCityTextChinese());
        viewHolder.english.setText(items.get(i).getCityTextEnglish());
//        viewHolder.cityPhoto.setImageBitmap(items.get(i).getCityPic());
        Glide.with(context).load(ConfigUtil.SERVER_ADDR+items.get(i).getCityImg()).into(viewHolder.cityPhoto);
//        if (view == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            view = inflater.inflate(itemLayoutRes0, null);
//            viewHolder0=new ViewHolder();
//            viewHolder0.chinese= view.findViewById(R.id.cityTitleChinese0);
//            viewHolder0.english= view.findViewById(R.id.cityTitleEnglish0);
//            viewHolder0.cityPhoto=view.findViewById(R.id.cityPhoto0);
//            //缓存viewHolder对象
//            //view.setTag(viewHolder0);//这个方法可以缓存任意类型对象
//            viewHolder0.chinese.setText(items.get(i).getCityTextChinese());
//            viewHolder0.english.setText(items.get(i).getCityTextEnglish());
//            viewHolder0.cityPhoto.setImageBitmap(items.get(i).getCityPic());
//        }else if(i==1 && view==null){
//            LayoutInflater inflater = LayoutInflater.from(context);
//            view = inflater.inflate(itemLayoutRes1, null);
//            viewHolder1=new ViewHolder1();
//            viewHolder1.chinese= view.findViewById(R.id.cityTitleChinese1);
//            viewHolder1.english= view.findViewById(R.id.cityTitleEnglish1);
//            viewHolder1.cityPhoto=view.findViewById(R.id.cityPhoto1);
//            //缓存viewHolder对象
//            view.setTag(viewHolder0);//这个方法可以缓存任意类型对象
//            viewHolder1.chinese.setText(items.get(i).getCityTextChinese());
//            viewHolder1.english.setText(items.get(i).getCityTextEnglish());
//            viewHolder1.cityPhoto.setImageBitmap(items.get(i).getCityPic());
//        }else if(i%2==0 && i!=0){
//            viewHolder0= (ViewHolder0) view.getTag();
//            viewHolder0.chinese.setText(items.get(i).getCityTextChinese());
//            viewHolder0.english.setText(items.get(i).getCityTextEnglish());
//            viewHolder0.cityPhoto.setImageBitmap(items.get(i).getCityPic());
//            view.setTag(viewHolder1);
//        }else{
//            viewHolder1= (ViewHolder1) view.getTag();
//            viewHolder1.chinese.setText(items.get(i).getCityTextChinese());
//            viewHolder1.english.setText(items.get(i).getCityTextEnglish());
//            viewHolder1.cityPhoto.setImageBitmap(items.get(i).getCityPic());
//        }

//        viewHolder.name.setText(books.get(i).getBookName());
//        viewHolder.price.setText(books.get(i).getPrice() + "");
        return view;
    }
//    private class ViewHolder0 {
//        //定义每个item布局中
//        TextView chinese;
//        TextView english;
//        ImageView cityPhoto;
//    }
    private class ViewHolder {
        //定义每个item布局中

        TextView chinese;
        TextView english;
        ImageView cityPhoto;
    }
}
