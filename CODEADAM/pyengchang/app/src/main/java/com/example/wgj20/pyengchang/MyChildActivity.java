package com.example.wgj20.pyengchang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MyChildActivity extends AppCompatActivity {

    private static final String TAG_NAME = "childName";
    private static final String TAG_AGE ="childAge";
    private static final String TAG_phonenumber="parentPhoneNumber";
    private static final String TAG_JSON="childInfo";

    ListAdapter adapter;
    ListView mlistView;
    String parentID;
    String mJsonString;

    ArrayList<HashMap<String, String>> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_child);

        parentID = getIntent().getStringExtra("parentID");

        mArrayList = new ArrayList<>();
        mlistView = (ListView) findViewById(R.id.myChildList);
        Log.v("mychildACtivityCHECK",parentID);

        GetData task = new GetData();
        task.execute("http://13.124.182.10/childInfo.php");

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), MyChildInfoActivity.class);

                intent.putExtra("name", mArrayList.get(position).get("childName"));
                intent.putExtra("age",mArrayList.get(position).get("childAge"));
                intent.putExtra("phonenumber", mArrayList.get(position).get("parentPhoneNumber"));
                intent.putExtra("UUID", mArrayList.get(position).get("UUID"));
                intent.putExtra("isMissing",mArrayList.get(position).get("isMissing"));
                Log.v("sendCHECK",mArrayList.get(position).get("UUID"));


                startActivity(intent);
            }
        });


    }

    public void onClick(View view) {

        Intent intent = new Intent(getApplicationContext(), AddChildActivity.class);
        intent.putExtra("parentID", parentID);
        startActivity(intent);
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyChildActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d("onPostExeTAG", "response  - " + result);

            if (result == null){
                Log.v("childnullTAG","0");
                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("httpTAG", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();
                Log.v("sbTAG",sb.toString());

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("errorTAG", "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            Log.v("jsonObjectTAG",jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String parentID_ = item.getString("parentID");

                if(!parentID_.equals(parentID)) continue;

                String name = item.getString(TAG_NAME);
                String phonenumber = item.getString(TAG_phonenumber);
                String age = item.getString(TAG_AGE);
                String UUID = item.getString("childUUID");
                String isMissing = item.getString("isMissing");

                HashMap<String,String> hashMap = new HashMap<>();


                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_AGE, age);
                hashMap.put(TAG_phonenumber, phonenumber);
                hashMap.put("UUID",UUID);
                hashMap.put("isMissing",isMissing);
                mArrayList.add(hashMap);
            }

            adapter = new SimpleAdapter(
                    MyChildActivity.this, mArrayList, R.layout.mychild_item_list,
                    new String[]{TAG_NAME,TAG_AGE, TAG_phonenumber},
                    new int[]{R.id.textView_list_name, R.id.textView_list_age, R.id.textView_list_phonenumber}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d("ExceptionTAG", "showResult : ", e);
        }

    }


}