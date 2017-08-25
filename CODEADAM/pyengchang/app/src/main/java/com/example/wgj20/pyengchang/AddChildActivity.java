package com.example.wgj20.pyengchang;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddChildActivity extends AppCompatActivity {


    EditText editTextName; // 아이이름 정보
    EditText editTextAge; // 아이나이 정보
    EditText editTextPhoneNumber; //부모님핸드폰번호 정보
    TextView textViewUUID; // 아이 기기 고유번호
    String parentID;
    LocationManager locationManager; //gps 사용하기위한 manager



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        parentID = getIntent().getExtras().getString("parentID");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        editTextName = (EditText) findViewById(R.id.editName);
        editTextAge = (EditText) findViewById(R.id.editAge);
        editTextPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        textViewUUID = (TextView) findViewById(R.id.textViewUUID);

        if(getIntent().hasExtra("UUID")) {

            textViewUUID.setText(getIntent().getExtras().getString("UUID"));
        }
    }

    public void onClickSetUUID(View view) {
        Intent intent = new Intent(getApplicationContext(), GetBeaconActivity.class);
        intent.putExtra("parentID", parentID);
        startActivity(intent);
    }

    public void onClickAddChildren (View view) {  // 아이추가 버튼을 눌렀을 때 실행. DB에 아이 정보를 Insert한다.

        Intent intent = new Intent(getApplicationContext(), MyChildActivity.class);
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String UUID = (String)textViewUUID.getText();

        InsertData task = new InsertData();
        task.execute(name,age,phoneNumber,UUID,"0");

//        InsertData1 task1 = new InsertData1();
//        task.execute(UUID,rLatitude, rLongitude, "100");


        intent.putExtra("parentID", parentID);
        startActivity(intent);
    }


    class InsertData extends AsyncTask<String, Void, String> { // 앱 - > PHP -> DB 과정을 통해 아이 정보 Insert class
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AddChildActivity.this,
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
            int isMissing = Integer.valueOf(params[4]);

            String serverURL = "http://13.124.182.10/childInfoInsert.php"; //server URL
            String postParameters = "parentID=" + parentID + "&parentPhoneNumber=" + phoneNumber + "&childUUID=" + UUID + "&childName=" + name + "&childAge=" + age + "&isMissing=" + isMissing; // DB에 저장할 파라미터


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


}