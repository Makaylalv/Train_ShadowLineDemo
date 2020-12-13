package com.example.train_shadowlinedemo.Personal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceCollectionAdapter extends BaseAdapter {
    private Context context;
    private List<PlaceCollection> collections = new ArrayList<>();
    private int itemLayoutRes;
    private boolean isOP;
    public PlaceCollectionAdapter(Context context, List<PlaceCollection> collections, int itemLayoutRes) {
        this.context = context;
        this.collections = collections;
        this.itemLayoutRes = itemLayoutRes;
    }

    public void setOP(boolean OP) {
        isOP = OP;
    }

    @Override
    public int getCount() {
        if (collections!=null){
            return collections.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (collections!=null){
            return collections.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("placecollections",collections.toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_place_collection, null);

        TextView place_name=view.findViewById(R.id.place_name);
        TextView place_english=view.findViewById(R.id.place_english);
        TextView place_city=view.findViewById(R.id.place_city);
        TextView place_film=view.findViewById(R.id.place_film);
        ImageView place_img = view.findViewById(R.id.place_img);
        CheckBox cb_op = view.findViewById(R.id.place_op);

        place_name.setText(collections.get(i).getPlace_name());
        place_english.setText(collections.get(i).getPlace_english());
        place_city.setText(collections.get(i).getPlace_position());
        place_film.setText(collections.get(i).getFilm());
        Glide.with(context).load(ConfigUtil.SERVER_ADDR+collections.get(i).getImg()).into(place_img);
        if (isOP){
            cb_op.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.findViewById(R.id.place_head).getLayoutParams();
            lps.leftMargin = 80;
//            Button button = new Button(Context);
//            button.setId(1);
//            RelativeLayout relativeLayout = view.findViewById(R.id.rv_city_list);
//            relativeLayout.addView(button);
        }else {
            cb_op.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.findViewById(R.id.place_head).getLayoutParams();
            lps.leftMargin = 0;
        }
        PlaceCollection c = collections.get(i);
        if (c.isChecked()){
            cb_op.setChecked(true);
        }else {
            cb_op.setChecked(false);
        }
        cb_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = c.isChecked();
                c.setChecked(!isCheck);
            }
        });
        return view;
    }
}
