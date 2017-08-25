package com.example.wgj20.pyengchang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class child_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_info);

        Intent intent = getIntent();
        TextView tv_name = (TextView) findViewById(R.id.child_name_); //아이 이름 텍스트뷰
        TextView tv_age = (TextView) findViewById(R.id.child_age_); // 아이 나이 텍스트뷰
        TextView tv_phonenumber = (TextView) findViewById(R.id.parent_phonenumber_); // 부모님 핸드폰번호 텍스트뷰


        tv_name.setText(intent.getStringExtra("name").toString());
        tv_age.setText(intent.getStringExtra("age").toString());
        tv_phonenumber.setText(intent.getStringExtra("phonenumber").toString());

    }
}
