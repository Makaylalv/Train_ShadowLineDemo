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

public class MovieCollectionAdapter extends BaseAdapter {
    private Context context;
    private int itemLayoutRes;
    private List<FilmCollection> filmCollections = new ArrayList<>();
    private boolean isOP;
    public MovieCollectionAdapter(Context context, List<FilmCollection> filmCollections, int itemLayoutRes){
        this.context = context;
        this.itemLayoutRes = itemLayoutRes;
        this.filmCollections = filmCollections;
    }

    public void setOP(boolean OP) {
        isOP = OP;
    }

    @Override
    public int getCount() {
        if (filmCollections!=null){
            return filmCollections.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (filmCollections!=null){
            return filmCollections.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("filmCollections",filmCollections.toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_movie_collection, null);

        ImageView movie_img = view.findViewById(R.id.movie_img);
        TextView tv_name = view.findViewById(R.id.movie_name);
        TextView tv_englishName = view.findViewById(R.id.movie_english);
        TextView tv_tostar = view.findViewById(R.id.movie_type);
        TextView tv_city = view.findViewById(R.id.movie_city);
        TextView tv_cut = view.findViewById(R.id.movie_cutnum);
        CheckBox cb_op = view.findViewById(R.id.movie_op);
        tv_name.setText(filmCollections.get(i).getFilm_name());
        tv_englishName.setText(filmCollections.get(i).getFilm_english());
        tv_tostar.setText(filmCollections.get(i).getFilm_type());
        tv_city.setText(filmCollections.get(i).getFilm_city());
        tv_cut.setText(filmCollections.get(i).getFilm_place());
        Glide.with(context).load(ConfigUtil.SERVER_ADDR+"imgs/film/filmImg/"+filmCollections.get(i).getImg()).into(movie_img);
        if (isOP){
            cb_op.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.findViewById(R.id.movie).getLayoutParams();
            lps.leftMargin = 100;
//            Button button = new Button(Context);
//            button.setId(1);
//            RelativeLayout relativeLayout = view.findViewById(R.id.rv_city_list);
//            relativeLayout.addView(button);
        }else {
            cb_op.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.findViewById(R.id.movie).getLayoutParams();
            lps.leftMargin = 0;
        }
        FilmCollection c = filmCollections.get(i);
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
