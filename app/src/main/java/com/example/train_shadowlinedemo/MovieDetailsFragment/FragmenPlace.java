package com.example.train_shadowlinedemo.MovieDetailsFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.train_shadowlinedemo.MovieDetail.view.PlaceRecyclerViewAdapter;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;

import java.util.ArrayList;

public class FragmenPlace extends androidx.fragment.app.Fragment {
    ArrayList<Place> places= new ArrayList<>();
    RecyclerView recyclerView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_place, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
        PlaceRecyclerViewAdapter myAdapter =new PlaceRecyclerViewAdapter(places);
        myAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    private void initData() {
        Place place1=new Place();
        Place place2=new Place();
        place1.setPlaceName("重庆德普外国语学校");
        place2.setPlaceName("白象居");
        place1.setPlaceFalseImg(R.drawable.false_place_img1);
        place2.setPlaceFalseImg(R.drawable.flase_place_img2);
        place1.setPlacePosition("中国重庆市");
        place2.setPlacePosition("中国重庆市");
        place1.setPlaceFilmDescribe("陈念教英语的学校");
        place2.setPlaceFilmDescribe("陈念上学的路");
        place1.setPlaceTime("02:00");
        place2.setPlaceTime("06:58");
        places.add(place1);
        places.add(place2);
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    PlaceRecyclerViewAdapter.OnItemClickListener onItemClickListener=new PlaceRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, PlaceRecyclerViewAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()){
                default:
                    Toast.makeText(getContext(),"你点击了item按钮"+(position+1),Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        @Override
        public void onItemLongClick(View v) {

        }
    };


}
