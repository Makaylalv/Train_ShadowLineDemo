package com.example.train_shadowlinedemo.Personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.train_shadowlinedemo.R;
import com.example.train_shadowlinedemo.fragment.PersonalFragment;

public class SetActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_name;
    private EditText et_phone;
    private ImageView back;
    private RelativeLayout set_edit;
    private RelativeLayout username_set;
    private LinearLayout personal_title;
    private TextView cancel;
    private TextView save;
    public static String nameupdate;
    public static boolean update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);
        update = true;
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String sign = intent.getStringExtra("signature");

        set_edit = findViewById(R.id.set_edit);
        personal_title = findViewById(R.id.personal_title);
        username_set = findViewById(R.id.username_set);
        cancel = findViewById(R.id.edit_cancel);
        save = findViewById(R.id.edit_save);

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_name.setText(name);
        et_phone.setText(sign);
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    personal_title.setVisibility(View.GONE);
                    set_edit.setVisibility(View.VISIBLE);
                }
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        personal_title.setVisibility(View.VISIBLE);
                        set_edit.setVisibility(View.GONE);
                        et_name.setFocusable(false);
                        nameupdate = et_name.getText().toString();
                        update = false;
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        personal_title.setVisibility(View.VISIBLE);
                        set_edit.setVisibility(View.GONE);
                        et_name.setFocusable(false);
                    }
                });
            }
        });
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    personal_title.setVisibility(View.GONE);
                    set_edit.setVisibility(View.VISIBLE);
                }
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        personal_title.setVisibility(View.VISIBLE);
                        set_edit.setVisibility(View.GONE);
                        et_phone.setFocusable(false);
                        nameupdate = et_name.getText().toString();
                        update = false;
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        personal_title.setVisibility(View.VISIBLE);
                        set_edit.setVisibility(View.GONE);
                        et_phone.setFocusable(false);
                    }
                });
            }
        });
        back = findViewById(R.id.set_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_name:
                Log.e("et_name",et_name.toString());
                personal_title.setVisibility(View.GONE);
                set_edit.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        personal_title.setVisibility(View.VISIBLE);
                        set_edit.setVisibility(View.GONE);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        personal_title.setVisibility(View.VISIBLE);
                        set_edit.setVisibility(View.GONE);
                    }
                });
                break;
        }
    }
}