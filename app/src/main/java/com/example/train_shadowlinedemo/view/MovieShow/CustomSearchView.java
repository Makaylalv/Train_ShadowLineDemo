package com.example.train_shadowlinedemo.view.MovieShow;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.train_shadowlinedemo.R;

public class CustomSearchView extends LinearLayout implements View.OnClickListener {
    private EditText etInput;//输入框
    private ImageView ivDelete;//删除键
    private Context mContext;//上下文对象
    private TextView textView;//热搜榜文字提示
    private ListView lvTips;//弹出列表
    private ArrayAdapter<String> mHintAdapter;//提示adapter
    private ArrayAdapter<String> mAutoCompleteAdapter;//自动补全
    private SearchAdapter mResultAdapter;//搜索结果的adapter
    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public CustomSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);
        initView();
    }

    private void initView() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        lvTips = (ListView) findViewById(R.id.search_lv_tips);
        textView=(TextView)findViewById(R.id.tv_tip);
        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //set edit text
                String text = lvTips.getAdapter().getItem(i).toString();
                etInput.setText(text);
                etInput.setSelection(text.length());
                //提示列表消失
                lvTips.setVisibility(View.GONE);
                textView.setVisibility(GONE);
                notifyStartSearching(text);
            }
        });
        ivDelete.setOnClickListener(this);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lvTips.setVisibility(GONE);
                    textView.setVisibility(GONE);
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lvTips.getAdapter() == null) {
            lvTips.setAdapter(mHintAdapter);
        }
    }

    //设置搜索结果adapter
    public void setmResultAdapter(SearchAdapter adapter){
        this.mResultAdapter=adapter;
        if(mResultAdapter!=null){
            ivDelete.setVisibility(VISIBLE);
        }

    }
    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {

        this.mAutoCompleteAdapter = adapter;
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {//输入不为空
                ivDelete.setVisibility(VISIBLE);
                lvTips.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {
                    lvTips.setAdapter(mAutoCompleteAdapter);
                }
                //更新autoComplete数据
                if (mListener != null) {
                    mListener.onRefreshAutoComplete(charSequence.toString());
                }
            } else {//输入框为空时
                ivDelete.setVisibility(GONE);
                if (mHintAdapter != null) {
                    lvTips.setAdapter(mHintAdapter);
                }
                lvTips.setVisibility(GONE);
                textView.setVisibility(GONE);
                if (mResultAdapter!=null){
                    mListener.onSearchAgain(charSequence.toString());
                }

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_input:
                lvTips.setVisibility(VISIBLE);
                textView.setVisibility(VISIBLE);
                break;
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
        }
    }


    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 显示搜索结果，输入框重新为空时，回调此方法
//         */
        void onSearchAgain(String text);
    }
}
