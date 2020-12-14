package com.example.train_shadowlinedemo.Personal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.train_shadowlinedemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteCollectionAdapter extends BaseAdapter {
    private Context context;
    private int itemLayoutRes;
    private List<RouteCollection> routeCollections = new ArrayList<>();
    private boolean isOP;

    public RouteCollectionAdapter(Context context, int itemLayoutRes, List<RouteCollection> routeCollections) {
        this.context = context;
        this.itemLayoutRes = itemLayoutRes;
        this.routeCollections = routeCollections;
    }

    public void setOP(boolean OP) {
        isOP = OP;
    }

    @Override
    public int getCount() {
        if (routeCollections!=null){
            return routeCollections.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (routeCollections!=null){
            return routeCollections.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e("routeCollections",routeCollections.toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_route_collection, null);

        TextView route_data = view.findViewById(R.id.route_data);
        TextView tv_route_city = view.findViewById(R.id.tv_route_city);
        CheckBox cb_op = view.findViewById(R.id.route_op);
        Button bt_tag = view.findViewById(R.id.bt_tag);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String t=format.format(new Date());
        Log.e("t",t);
        route_data.setText(t);
        tv_route_city.setText(routeCollections.get(i).getName());
        if (routeCollections.get(i).getTag()==0){
            bt_tag.setText("城市");
        }else if (routeCollections.get(i).getTag()==1){
            bt_tag.setText("电影");
        }
        if (isOP){
            cb_op.setVisibility(View.VISIBLE);
            Log.e("op","op");
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.findViewById(R.id.rv_rout).getLayoutParams();
            lps.leftMargin = 80;

        }else {
            cb_op.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.findViewById(R.id.rv_rout).getLayoutParams();
            lps.leftMargin = 0;
        }
        RouteCollection c = routeCollections.get(i);
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
