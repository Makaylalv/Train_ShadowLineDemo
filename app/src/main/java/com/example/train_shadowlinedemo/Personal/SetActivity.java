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
    private LinearLayout personal_title;
    private TextView cancel;
    private TextView save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String sign = intent.getStringExtra("signature");

        set_edit = findViewById(R.id.set_edit);
        personal_title = findViewById(R.id.personal_title);
        cancel = findViewById(R.id.edit_cancel);
        save = findViewById(R.id.edit_save);

        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_name.setText(name);
        et_phone.setText(sign);
        et_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personal_title.setVisibility(View.GONE);
                set_edit.setVisibility(View.VISIBLE);
            }
        });
//        et_name.setOnClickListener(this);
//        et_phone.setOnClickListener(this);
        back = findViewById(R.id.set_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent();
                backIntent.setClass(SetActivity.this, PersonalFragment.class);
                backIntent.putExtra("name",et_name.getText());
                startActivity(backIntent);
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