package com.example.wgj20.pyengchang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class map_activity extends AppCompatActivity implements OnMapReadyCallback, BeaconConsumer {



    private LocationManager locationManager;
    private LocationListener locationListener;


    private GoogleMap googleMap;
    LatLng ME;


    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();

    private static String TAG = "map_activity_TAG";
    private static final String TAG_JSON="missingChild";
    private static final String TAG_UUID = "UUID";
    private static final String TAG_NAME = "name";
    private static final String TAG_AGE ="age";
    private static final String TAG_phonenumber="phonenumber";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";

    String parentID;
    ListAdapter adapter;
    ListView mlistView;
    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ArrayList<HashMap<String, String>> mArrayList_Location;
    String mJsonString;

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

    }

    public void AddMarker(){

        googleMap.clear();
        settingGPS();

        // 사용자의 현재 위치 //
        Location userLocation = getMyLocation();


        if( userLocation != null ) {
            ME = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(ME).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ME, 17.0f));
        }else {
            ME = new LatLng(0,0);
            googleMap.addMarker(new MarkerOptions().position(ME).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ME, 17.0f));
        }


        Float lat;
        Float lng;

               for (int i = 0; i < mArrayList_Location.size() ; i++ ) {
                   try{
                       lat = Float.valueOf(mArrayList_Location.get(i).get("rLatitude"));
                       lng = Float.valueOf(mArrayList_Location.get(i).get("rLongitude"));
                   }catch (Exception e){
                       lat = Float.valueOf(0);
                       lng = Float.valueOf(0);
                   }


                   LatLng A = new LatLng(lat,lng);
                   googleMap.addMarker(new MarkerOptions().position(A).title(mArrayList.get(i).get("name")));

               }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity);

        Intent intent = getIntent();

        parentID = intent.getStringExtra("ID");


        Log.v("parentIDCHECK",parentID);
        Button myChildInfo = (Button) findViewById(R.id.myChildInfo);
        myChildInfo.setOnClickListener(mClickListener);
        mArrayList = new ArrayList<>();
        mArrayList_Location = new ArrayList<>();
        mlistView = (ListView) findViewById(R.id.list);
        Log.d("map_activity","Init GoogleMap");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);


        callAsynchronousTask();

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                  Intent intent = new Intent(map_activity.this,child_info.class);

                intent.putExtra("name", mArrayList.get(position).get("name"));
                intent.putExtra("age",mArrayList.get(position).get("age"));
                intent.putExtra("phonenumber", mArrayList.get(position).get("phonenumber"));
                startActivity(intent);

            }
        });

    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(map_activity.this, MyChildActivity.class);
            intent.putExtra("parentID", parentID);
            startActivity(intent);
        }
    };

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

                    beaconList.clear();

                    for (Beacon beacon : beacons) {

                        beaconList.add(beacon);
                    }

                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                }
            }
        });

        try {

            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

        } catch (RemoteException e) {    }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            for (Beacon beacon : beaconList) {
                for(int i=0;i<mArrayList.size();i++){
                    if(mArrayList.get(i).get("UUID") == beacon.getId1().toString()){

                    }
                }
            }

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(map_activity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
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
                Log.d(TAG, "response code - " + responseStatusCode);

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


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);

            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            mArrayList = new ArrayList<>();
            mArrayList_Location = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                String name = item.getString(TAG_NAME);
                String phonenumber = item.getString(TAG_phonenumber);
                String age = item.getString(TAG_AGE);

                String rLatitude = item.getString("rLatitude");
                String rLongitude = item.getString("rLongitude");
                String rDistance = item.getString("rDistance");

                HashMap<String,String> hashMap = new HashMap<>();
                HashMap<String,String> hashMap1 = new HashMap<>();

                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_AGE, age);
                hashMap.put(TAG_phonenumber, phonenumber);

                hashMap1.put("rLatitude", rLatitude);
                hashMap1.put("rLongitude", rLongitude);
                hashMap1.put("rDistance", rDistance);

                mArrayList_Location.add(hashMap1);
                mArrayList.add(hashMap);
                AddMarker();
            }

            adapter = new SimpleAdapter(
                    map_activity.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_NAME,TAG_AGE, TAG_phonenumber},
                    new int[]{R.id.textView_list_name, R.id.textView_list_age, R.id.textView_list_phonenumber}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new GetData().execute("http://13.124.182.10/missingChild.php");

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
    }


    class UpdateData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(map_activity.this,
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

            String rLatitude = (String)params[0];
            String rLongitude = (String)params[1];

            String serverURL = "http://13.124.182.10/childLocationUpdate.php";
            String postParameters = "rLatitude=" + rLatitude + "&rLongitude=" + rLongitude;


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

    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 사용자 권한 요청
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        else {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
            }else{
                Log.d("받기","실패");
            }

            if(currentLocation == null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (currentLocation != null) {
                    double lng = currentLocation.getLongitude();
                    double lat = currentLocation.getLatitude();
                    Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
                }else{
                    Log.d("2차받기","실패");
                }
            }
        }
        return currentLocation;
    }

    private void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // TODO 위도, 경도로 하고 싶은 것
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

}
