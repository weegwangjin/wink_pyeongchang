package com.example.leesangwook.exerbluetooth;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;

    public TextView textView;

    private List<Beacon> beaconList = new ArrayList<>();

    /** 위도, 경도 **/
    static Double latitude = 0.0 ;
    static Double longitude = 0.0 ;

    List<Double> distance = new ArrayList<>();
    List<String> uuid = new ArrayList<>();

    /** 위치정보를 보여주는 뷰 **/
    TextView textEventLatitude ;
    TextView textEventLongitude ;

    TextView textBeaconDistance ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** 위치정보를 보여주는 뷰 **/
        textEventLatitude = (TextView) findViewById(R.id.text_EventLatitude) ;
        textEventLongitude = (TextView) findViewById(R.id.text_EventLongitude) ;

        textBeaconDistance = (TextView) findViewById(R.id.text_BeaconDistance);

        textView = (TextView) findViewById(R.id.textview);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    public void onClick(View view) {

        handler.sendEmptyMessage(0);
        startLocationService();
    }

    public void onClickMap(View view) {

        Intent intent= new Intent(getApplicationContext(), MapsActivity.class);

        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude",longitude);
        intent.putExtra("Distance", distance.get(0));
        intent.putExtra("UUID", uuid.get(0));

        startActivity(intent);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                beaconList.clear();


                if (beacons.size() > 0) {
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

            textView.setText("");

            for (Beacon beacon : beaconList) {
                textView.append(beacon.getId1() + " ");
            }

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private void startLocationService() {
        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();

        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        latitude = gpsListener.latitude ;
        longitude = gpsListener.longitude ;

        textEventLatitude.setText("Latitude: " + latitude);
        textEventLongitude.setText("Longitude: " + longitude);

        if (beaconList.size() > 0) {

            textBeaconDistance.setText("");

            uuid.clear();
            distance.clear();

            for (Beacon beacon : beaconList) {
                textBeaconDistance.append("ID: " + beacon.getId1() + "\n");
                textBeaconDistance.append("Distance: " + beacon.getDistance() + "\n");
                uuid.add(beacon.getId1().toString());
                distance.add(beacon.getDistance());
            }
        }
    }
}
