package com.example.leesangwook.beacontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MyChildrenInfoActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewAge;
    TextView textViewPhoneNumber;
    TextView textViewUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children_info);

        String name = getIntent().getExtras().getString("name");
        String age = getIntent().getExtras().getString("age");
        String phonenumber = getIntent().getExtras().getString("phonenumber");
        String UUID = getIntent().getExtras().getString("UUID");

        textViewName = (TextView) findViewById(R.id.textName);
        textViewAge = (TextView) findViewById(R.id.textAge);
        textViewPhoneNumber = (TextView) findViewById(R.id.textPhoneNumber);
        textViewUUID = (TextView) findViewById(R.id.textUUID);

        textViewName.setText(name);
        textViewAge.setText(age);
        textViewPhoneNumber.setText(phonenumber);
        textViewUUID.setText(UUID);
    }

//    public void onClickAskHelp() {
//        /*
//            아이 찾아주세요! 요청
//         */
//    }
}


