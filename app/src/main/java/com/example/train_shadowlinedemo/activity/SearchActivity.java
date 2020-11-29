package com.example.train_shadowlinedemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.entity.SearchResult;
import com.example.train_shadowlinedemo.view.MovieShow.CustomSearchView;
import com.example.train_shadowlinedemo.view.MovieShow.SearchAdapter;
import com.example.train_shadowlinedemo.view.MovieShow.SearchFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements CustomSearchView.SearchViewListener {

    private Spinner searchType;
    private Button btnDeletehistory;
    private CustomSearchView customSearchView;
    private SearchFlowLayout flowLayout;
    private String mNames[] = {
            "welcome","android","TextView",
            "apple","jamy","kobe bryant",
            "jordan","layout","viewgroup",
            "margin","padding","text",
            "name","type","search","logcat"
    };
    private ListView lvResults; //搜索结果列表
    private ArrayAdapter<String> hintAdapter;//热搜框列表adapter
    private ArrayAdapter<String> autoCompleteAdapter;//自动补全
    private List<SearchResult> resultData; //搜索结果数据
    private List<SearchResult> dbData;//数据库的结果


    //搜索结果列表adapter
    private SearchAdapter resultAdapter;
    //热搜版数据
    private List<String> hintData;
    //搜索过程中自动补全数据
    private List<String> autoCompleteData;
    //默认提示框显示项的个数
    private static int DEFAULT_HINT_SIZE = 5;

    //提示框显示项的个数
    private static int hintSize = DEFAULT_HINT_SIZE;
     //设置提示框显示项的个数
    public static void setHintSize(int hintSize) {
        SearchActivity.hintSize = hintSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchType=findViewById(R.id.spinner_search);
        btnDeletehistory=findViewById(R.id.btn_delete_history);
        customSearchView=findViewById(R.id.custom_searchview);
        getHistoryData();//获取历史记录
        initChildViews();//初始化历史搜索记录
        
        initData();//初始化数据
        initViews();
        
        

    }

    private void initViews() {
        lvResults = (ListView) findViewById(R.id.search_results);
        //设置监听
        customSearchView.setSearchViewListener(this);
        //设置adapter
        customSearchView.setTipsHintAdapter(hintAdapter);
        customSearchView.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(SearchActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化搜索数据
    private void initData() {
        //从数据库获取数据  
        getDbData();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据  
        getAutoCompleteData(null);
        //初始化搜索结果数据  
        getResultData(null);
    }

    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if(!text.equals("")&&text!=null){
                    if(dbData.get(i).getName()==null){
                        Log.e("33333333333","333333333333333");
                    }
                    if (dbData.get(i).getName().contains(text.trim())) {
                        resultData.add(dbData.get(i));
                    }
                }

            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_search_result);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            autoCompleteData = new ArrayList<>(hintSize);

//             根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
//                if(text!=null&&!text.equals("")){
                    if (dbData.get(i).getName().contains(text.trim())) {
                        autoCompleteData.add(dbData.get(i).getName());
                        count++;
                    }
//                }

            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    private void getHintData() {
        hintData = new ArrayList<>(hintSize);
        for (int i = 1; i <= hintSize; i++) {
            hintData.add("热搜版" + i + "：Android自定义View");
        }
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    private void getDbData() {
        int size = 10;
        dbData = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SearchResult result=new SearchResult();
            result.setImgUrl("fef");
            result.setName("花木兰"+i);
            result.setEnglishName("Mulan");
            result.setInfo("2020/美国/动作/奇幻");
            dbData.add(result);
        }
    }

    private void getHistoryData() {

    }

    //初始化历史搜索记录数据view
    private void initChildViews() {
        flowLayout= (SearchFlowLayout) findViewById(R.id.flowlayout);
        MarginLayoutParams lp = new MarginLayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;

        for(int i = 0; i < mNames.length; i ++){
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.BLACK);
            view.setPadding(10,10,10,10);
            view.setBackgroundResource(R.drawable.history_textview);
            flowLayout.addView(view,lp);
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     * @param text
     */
    @Override
    public void onSearch(String text) {
        Log.e("onSearch ","222222");
        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
        Toast.makeText(this, "完成搜素", Toast.LENGTH_SHORT).show();
    }
}