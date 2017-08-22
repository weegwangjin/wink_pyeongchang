package com.example.leesangwook.beacontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyChildrenActivity extends AppCompatActivity {

    private static final String TAG_NAME = "name";
    private static final String TAG_AGE ="age";
    private static final String TAG_phonenumber="phonenumber";

    ListAdapter adapter;
    ListView mlistView;
    public final static ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children);

        String name = "이상욱";
        String age = "24";
        String phonenumber = "01030088478";
        String UUID = " ";

        if (getIntent().hasExtra("name")) {

            name = getIntent().getExtras().getString("name");// = "이상욱";
            age = getIntent().getExtras().getString("age");// = "24";
            phonenumber = getIntent().getExtras().getString("phonenumber");// = "01030088478";
            UUID = getIntent().getExtras().getString("UUID");
        }

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(TAG_NAME, name);
        hashMap.put(TAG_AGE, age);
        hashMap.put(TAG_phonenumber, phonenumber);
        hashMap.put("UUID", UUID);

        mArrayList.add(hashMap);

        mlistView = (ListView) findViewById(R.id.list);

        adapter = new SimpleAdapter(
                MyChildrenActivity.this, mArrayList, R.layout.item_list,
                new String[]{TAG_NAME,TAG_AGE, TAG_phonenumber, "UUID"},
                new int[]{R.id.textView_list_name, R.id.textView_list_age, R.id.textView_list_phonenumber, R.id.textView_list_uuid}
        );

        mlistView.setAdapter(adapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), MyChildrenInfoActivity.class);

                intent.putExtra("name", mArrayList.get(position).get("name"));
                intent.putExtra("age",mArrayList.get(position).get("age"));
                intent.putExtra("phonenumber", mArrayList.get(position).get("phonenumber"));
                intent.putExtra("UUID", mArrayList.get(position).get("UUID"));

                startActivity(intent);
            }
        });
    }

    public void onClick(View view) {

        Intent intent = new Intent(getApplicationContext(), AddChildrenActivity.class);
        startActivity(intent);
    }
}