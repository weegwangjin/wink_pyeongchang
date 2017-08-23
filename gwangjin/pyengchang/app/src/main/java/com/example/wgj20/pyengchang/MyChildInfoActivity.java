package com.example.wgj20.pyengchang;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyChildInfoActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewAge;
    TextView textViewPhoneNumber;
    TextView textViewUUID;
    String name;
    String age;
    String phonenumber;
    String UUID;
    String isMissing;
    Button helping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_child_info);

        name = getIntent().getExtras().getString("name");
        age = getIntent().getExtras().getString("age");
        phonenumber = getIntent().getExtras().getString("phonenumber");
        UUID = getIntent().getExtras().getString("UUID");
        isMissing = getIntent().getExtras().getString("isMissing");

        Log.v("UUIDCHECK",UUID);

        textViewName = (TextView) findViewById(R.id.textName);
        textViewAge = (TextView) findViewById(R.id.textAge);
        textViewPhoneNumber = (TextView) findViewById(R.id.textPhoneNumber);
        textViewUUID = (TextView) findViewById(R.id.textUUID);
        helping = (Button) findViewById(R.id.help);

        if(isMissing == "1") {
            helping.setText("요청해제");
        }
        helping.setOnClickListener(mClickListener);


        textViewName.setText(name);
        textViewAge.setText(age);
        textViewPhoneNumber.setText(phonenumber);
        textViewUUID.setText(UUID);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            InsertData task = new InsertData();
            task.execute(name,age,phonenumber,UUID);
            UpdateData task1 = new UpdateData();
            task1.execute(name,age,phonenumber,UUID);
        }
    };

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyChildInfoActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d("onPostExeTAG", "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
            String name = (String)params[0];
            String age = (String)params[1];
            String phoneNumber = (String)params[2];
            String UUID = (String)params[3];

            String serverURL = "http://13.124.182.10/missingChildInsert.php";
            String postParameters = "parentPhoneNumber=" + phoneNumber + "&childUUID=" + UUID + "&childName=" + name + "&childAge=" + age;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("POSTTAG", "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d("ERRORTAG", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    class UpdateData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyChildInfoActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d("onPostExeTAG", "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String UUID = (String)params[0];

            String serverURL = "http://13.124.182.10/childInfoUpdate.php";
            String postParameters = "childUUID=" + UUID;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("POSTTAG", "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d("UpdateERRORTAG", "UpdateData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
