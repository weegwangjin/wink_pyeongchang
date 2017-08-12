package com.example.lenovo.pyeongchang;

import android.content.ClipData;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.ListView;



public class Asking_help extends AppCompatActivity {

    public class Listviewitem{
        public String info;
        public String getInfo() {return info;}
        public Listviewitem(String info){
            this.info = info;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asking_help);

        Button Help_Btn = (Button) findViewById(R.id.Help_Btn);
        final TextView Name = (TextView) findViewById(R.id.input_Name);
        final TextView Age = (TextView) findViewById(R.id.input_Age);
        final TextView Pphone = (TextView) findViewById(R.id.input_Pphone);
        final TextView Time = (TextView) findViewById(R.id.input_Time);
        final TextView Note1 = (TextView) findViewById(R.id.input_Note);
        final TextView Note2= (TextView) findViewById(R.id.input_Note2);
        ImageView CImage = (ImageView) findViewById(R.id.input_Image);

        ArrayList<Listviewitem> data = new ArrayList<>();
         Listviewitem Cinfo = new Listviewitem("info");

        data.add(Cinfo);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Help_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asking_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
