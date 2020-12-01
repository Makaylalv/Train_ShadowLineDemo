package com.example.train_shadowlinedemo.fragment.movieDetailsFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.Place;
import com.example.train_shadowlinedemo.view.MovieDetail.PlaceRecyclerViewAdapter;

import java.util.ArrayList;

public class FragmenPlace extends androidx.fragment.app.Fragment {
    private ArrayList<Place> places= new ArrayList<>();
    View view;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, android.os.Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_place, container, false);
        return view;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setData(ArrayList<Place> places){
        this.places=places;
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PlaceRecyclerViewAdapter myAdapter =new PlaceRecyclerViewAdapter(places,getContext());
        myAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(myAdapter);
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
