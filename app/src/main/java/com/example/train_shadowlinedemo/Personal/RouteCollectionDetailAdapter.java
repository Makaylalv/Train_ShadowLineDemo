package com.example.train_shadowlinedemo.Personal;

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
import com.example.train_shadowlinedemo.entity.Place;

import java.util.List;

public class RouteCollectionDetailAdapter extends BaseAdapter {
    private Context context;
    private int itemLayoutRes;
    private List<Place> places;

    public RouteCollectionDetailAdapter(Context context, int itemLayoutRes, List<Place> places) {
        this.context = context;
        this.itemLayoutRes = itemLayoutRes;
        this.places = places;
    }

    @Override
    public int getCount() {
        if (places!=null){
            return places.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (places!=null){
            return places.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("places",places.toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_route_detail, null);
        ImageView route_place_img = view.findViewById(R.id.route_place_img);
        TextView count = view.findViewById(R.id.count);
        TextView route_place_name = view.findViewById(R.id.route_place_name);
        TextView route_place_englishname = view.findViewById(R.id.route_place_englishname);
//        TextView route_place_filmname = view.findViewById(R.id.route_place_filmname);
        TextView route_place_describe = view.findViewById(R.id.route_place_describe);
        String str = getItemId(i)+1+"";
        if (str.length()==1){
            count.setText("0"+((int) getItemId(i)+1)+"");
        }else {
            count.setText((int) getItemId(i) + 1 + "");
        }
        route_place_name.setText(places.get(i).getPlaceName());
        route_place_englishname.setText(places.get(i).getPlaceEnglishname());
        route_place_describe.setText(places.get(i).getPlaceFilmDescribe());
        Glide.with(context).load(ConfigUtil.SERVER_ADDR+places.get(i).getPlaceReallyImg()).into(route_place_img);
        return view;
    }
}
