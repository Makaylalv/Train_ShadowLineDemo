package com.example.train_shadowlinedemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.train_shadowlinedemo.MovieDetail.view.MovieDetailsViewPager;
import com.example.train_shadowlinedemo.MovieDetail.view.MovieDetailsViewPagerAdapter;
import com.example.train_shadowlinedemo.MovieDetailsFragment.Fragment1;
import com.example.train_shadowlinedemo.MovieDetailsFragment.Fragment2;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MovieDetailActivity extends AppCompatActivity {
    TabLayout tabLayout;
    MovieDetailsViewPager viewPager;
    ArrayList<Fragment> fragments = new  ArrayList<>();
    ArrayList<String>  listTitle = new ArrayList<>();
    public static TextView textView;
    public static TextView textView1;
    Button button;
    Timer timer = new Timer();
    private ArrayList<ActivityTouchListener> myActivityTouchListeners = new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case 1:
                    float a=textView.getAlpha();
                    if(a>=1){
                        timer.cancel();
                        timer.purge();
                    }else {
                        a= (float) (a+0.02);
                        textView.setAlpha(a);
                    }
                    break;
                case 2:
                    TimerTask task1 = new TimerTask() {
                        @Override
                        public void run() {
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    };
                    timer = new Timer();
                    textView.setText("少年的你");
                    textView.setAlpha(0);
                    timer.schedule(task1, 0, 30);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        textView1=findViewById(R.id.film_English_name);
        tabLayout = findViewById(R.id.tab_main);
        viewPager = findViewById(R.id.viewpager);
        button=findViewById(R.id.more);
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        listTitle.add("片场"); //标题
        listTitle.add("片场");//标题
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new MovieDetailsViewPagerAdapter(getSupportFragmentManager(),fragments,listTitle));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setCustomView(R.layout.custom_bar);  //绑定自定义的布局：actionbar_layout.xml
            textView=actionBar.getCustomView().findViewById(R.id.film_name);

        }else {
            Log.e("actionbar","is null");
        }

        (this).registerFragmentTouchListener(touchListener);

    }
    MovieDetailActivity.ActivityTouchListener touchListener = new MovieDetailActivity.ActivityTouchListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    int[] location = new int[2];
                    textView1.getLocationOnScreen(location);
                    if(location[1]<=0&&!textView.getText().toString().equals("少年的你")){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                    if(location[1]>=0&&!textView.getText().toString().equals("")){
                        textView.setText("");
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {
                    int[] location = new int[2];
                    textView1.getLocationOnScreen(location);
                    if(location[1]<=0&&!textView.getText().toString().equals("少年的你")){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                    if(location[1]>=0&&!textView.getText().toString().equals("")){
                        textView.setText("");
                    }


                }
                break;
                case MotionEvent.ACTION_MOVE:
                {
                    int[] location = new int[2];
                    textView1.getLocationOnScreen(location);
                    if(location[1]<=0&&!textView.getText().toString().equals("少年的你")){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }
                    if(location[1]>=0&&!textView.getText().toString().equals("")){
                        textView.setText("");
                    }

                }
                break;

            }
            return false;
        }
    };

    public void registerFragmentTouchListener(ActivityTouchListener listener) {
        myActivityTouchListeners.add(listener);
    }

    public void unRegisterFragmentTouchListener(ActivityTouchListener listener) {
        myActivityTouchListeners.remove(listener);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        for (ActivityTouchListener listener : myActivityTouchListeners) {
            listener.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    public interface ActivityTouchListener {

        boolean onTouchEvent(MotionEvent event);
    }

}
