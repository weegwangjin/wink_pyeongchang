package com.example.wgj20.pyengchang;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class GetBeaconActivity extends AppCompatActivity implements BeaconConsumer {

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager; // beacon 신호를 수신하기 위한 manager

    public TextView textView;
    EditText editTextUUID;

    private List<Beacon> beaconList = new ArrayList<>(); //수신된 비콘신호를 저장하기위한 리스트

    String parentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_beacon);

        textView = (TextView) findViewById(R.id.textview);
        editTextUUID = (EditText) findViewById(R.id.editUUID);

        parentID = getIntent().getStringExtra("parentID");

        beaconManager = BeaconManager.getInstanceForApplication(this); // BeaconManager 클래스로부터 인스턴스를 가져온다.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); // 수신된 비콘 정보를 자리에 맞게 '-'를 붙여서 파싱해준다.
        beaconManager.bind(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    public void onClick(View view) {

        handler.sendEmptyMessage(0);
    }

    public void onClickUUID(View view) {

        Intent intent = new Intent(getApplicationContext(), AddChildActivity.class);

        intent.putExtra("UUID", editTextUUID.getText().toString());
        intent.putExtra("parentID", parentID);
        startActivity(intent);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) { // beacon이 주변에 있을 경우 신호를 수신받는다.

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

            textView.setText("");

            for (Beacon beacon : beaconList) {
                textView.append(beacon.getId1() + " ");
            }

            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
}