package com.example.train_shadowlinedemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;

import java.util.ArrayList;
import java.util.List;

public class SpotChooseAdapter extends BaseAdapter {
    private Context context;
    private List<Place> places = new ArrayList<>();
    private List<Integer> spotHasChosen = new ArrayList<>();
    private int layoutId;
    private List<Integer> is = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private CheckBox allin;
//    int i=0;

    @Override
    public int getCount() {
        if (places != null) {
            return places.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (places != null) {
            return places.get(position);
        } else {
            return null;
        }
    }

    public void changeCheckState() {
        for (View view : views) {
            TextView id = view.findViewById(R.id.choose_id);
            String pid = id.getText().toString();
            for (int i = 0; i < spotHasChosen.size(); i++) {
                if (spotHasChosen.get(i) == Integer.parseInt(pid)) {
                    Log.e("相同的s", spotHasChosen.get(i) + "");
                    Log.e("相同的v", Integer.parseInt(pid) + "");
                    CheckBox checkBox = view.findViewById(R.id.check);
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        break;
                    }
                }
            }
        }

    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public SpotChooseAdapter(List<Place> places, Context context, int layoutId, List<Integer> spotHasChosen, CheckBox allIn) {
        this.places = places;
        this.context = context;
        this.layoutId = layoutId;
        this.spotHasChosen = spotHasChosen;
        this.allin=allIn;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.id = convertView.findViewById(R.id.choose_id);
            viewHolder.pic = convertView.findViewById(R.id.choose_photo);
            viewHolder.english = convertView.findViewById(R.id.choose_titleE);
            viewHolder.chinese = convertView.findViewById(R.id.choose_titleC);
            viewHolder.checkBox = convertView.findViewById(R.id.check);
            //缓存viewHolder对象
            convertView.setTag(viewHolder);//这个方法可以缓存任意类型对象
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.id.setText(places.get(position).getPlaceId() + "");
        viewHolder.chinese.setText(places.get(position).getPlaceName());
        viewHolder.english.setText(places.get(position).getPlaceEnglishname() + "");
        String pic = places.get(position).getPlaceReallyImg();
        Glide.with(context)
                .load(ConfigUtil.SERVER_ADDR + pic)
                .fitCenter()
                .into(viewHolder.pic);

        Log.e("图片地址0", ConfigUtil.SERVER_ADDR + pic);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if (!spotHasChosen.contains(places.get(position).getPlaceId())) {
                        spotHasChosen.add(places.get(position).getPlaceId());
                        Log.e("添加内容+索引", "" + places.get(position).getPlaceId() + spotHasChosen.indexOf(places.get(position).getPlaceId()));
                    }
                } else {
                    if (spotHasChosen.contains(places.get(position).getPlaceId())) {
                        int i = spotHasChosen.indexOf(places.get(position).getPlaceId());
                        Log.e("i的值", i + "");
                        Log.e("点击事件操作前spotHasChosen", spotHasChosen.toString());
                        Log.e("删除内容+索引", "" + places.get(position).getPlaceId() + spotHasChosen.indexOf(places.get(position).getPlaceId()));
                        spotHasChosen.remove(i);
                        Log.e("点击事件操作后spotHasChosen", spotHasChosen.toString());
                        if(spotHasChosen.size()==0){
                            allin.setText("全选");
                            allin.setChecked(false);
                        }
                    }
                }
            }
        });

        views.add(convertView);
        return convertView;
    }

    public void allCheckTrue() {
        int i=0;
        Log.e("allCheckTrue",views.toString()+"**"+views.size());
        for (View view : views) {
            i++;
            CheckBox checkBox = view.findViewById(R.id.check);
            if(checkBox.isChecked()==false){
                checkBox.setChecked(true);
            }
            Log.e("allCheckTrue中的i",i+"");
        }

    }

    private class ViewHolder {
        //定义每个item布局中
        ImageView pic;
        TextView id;
        TextView chinese;
        TextView english;
        CheckBox checkBox;
    }
}
