package com.example.train_shadowlinedemo.view.MovieDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.train_shadowlinedemo.ConfigUtil;
import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.MyRoute;
import com.example.train_shadowlinedemo.entity.RouteSpot;

import java.util.ArrayList;
import java.util.List;

public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<MyRoute> myRoutes;
    private Context context;
    private ImageView[] imgs=new  ImageView[5];

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgBig;
        TextView textView_stance;
        ImageView imgStar5;
        ImageView imgStar1;
        ImageView imgStar2;
        ImageView imgStar3;
        ImageView imgStar4;
        public ViewHolder (View view)
        {
            super(view);
            imgBig = view.findViewById(R.id.route_img);
            imgStar5=view.findViewById(R.id.star5);
            imgStar4=view.findViewById(R.id.star4);
            imgStar3=view.findViewById(R.id.star3);
            imgStar2=view.findViewById(R.id.star2);
            imgStar1=view.findViewById(R.id.star1);
            imgs[0]=imgStar1;
            imgs[1]=imgStar2;
            imgs[2]=imgStar3;
            imgs[3]=imgStar4;
            imgs[4]=imgStar5;
            textView_stance=view.findViewById(R.id.romance_stance);
            itemView.setOnClickListener(RouteRecyclerViewAdapter.this);

        }

    }

    public RouteRecyclerViewAdapter(ArrayList<MyRoute> list,Context context){
        myRoutes = list;
        this.context=context;
    }
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        MyRoute myRoute= myRoutes.get(position);
        holder.textView_stance.setText(myRoute.getWord());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.glide_loading)//加载图片的过程中显示
                .error(R.drawable.glide_error)//请求失败时显示
                .fallback(R.drawable.glide_defaultimg);//当请求URL是null时显示
        Glide.with(context)
                .load(ConfigUtil.SERVER_ADDR+"imgs/film/routeImg/"+myRoute.getImg()+".jpg")
                .apply(options)//应用请求选项
                .into(holder.imgBig);
        for(int i=1;i<=myRoutes.get(position).getStar();i++){
            ImageView m=imgs[i-1];
            Glide.with(context)
                    .load(R.drawable.yellow_star)
                    .apply(options)//应用请求选项
                    .into(m);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount(){
        return myRoutes.size();
    }


    //=======================以下为item中的button控件点击事件处理===================================

    //item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener  {
        void onItemClick(View v, ViewName viewName, int position);
        void onItemLongClick(View v);
    }

    private OnItemClickListener mOnItemClickListener;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();//getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.recyclerview:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }

}
