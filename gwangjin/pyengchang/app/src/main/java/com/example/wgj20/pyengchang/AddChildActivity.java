package com.example.wgj20.pyengchang;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddChildActivity extends AppCompatActivity {


    EditText editTextName;
    EditText editTextAge;
    EditText editTextPhoneNumber;
    TextView textViewUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        editTextName = (EditText) findViewById(R.id.editName);
        editTextAge = (EditText) findViewById(R.id.editAge);
        editTextPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        textViewUUID = (TextView) findViewById(R.id.textViewUUID);

        if(getIntent().hasExtra("UUID")) {

            textViewUUID.setText("UUID: " + getIntent().getExtras().getString("UUID"));
        }
    }

    public void onClickSetUUID(View view) {
        Intent intent = new Intent(getApplicationContext(), GetBeaconActivity.class);

        startActivity(intent);
    }

    public void onClickAddChildren (View view) {

        Intent intent = new Intent(getApplicationContext(), MyChildActivity.class);

        intent.putExtra("name", editTextName.getText().toString());
        intent.putExtra("age", editTextAge.getText().toString());
        intent.putExtra("phonenumber", editTextPhoneNumber.getText().toString());
        intent.putExtra("UUID", getIntent().getExtras().getString("UUID"));

        startActivity(intent);
    }
}