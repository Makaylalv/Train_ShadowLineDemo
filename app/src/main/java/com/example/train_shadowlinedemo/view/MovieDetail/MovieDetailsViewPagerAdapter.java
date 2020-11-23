package com.example.train_shadowlinedemo.view.MovieDetail;



import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MovieDetailsViewPagerAdapter extends FragmentPagerAdapter {
    
private ArrayList<androidx.fragment.app.Fragment> fragments;
    private ArrayList<String> list;

    public MovieDetailsViewPagerAdapter(FragmentManager fm, ArrayList<androidx.fragment.app.Fragment> fragments, ArrayList<String> list) {
        super(fm);
        this.fragments = fragments;
        this.list = list;
    }
    @Override
    public androidx.fragment.app.Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }


}
