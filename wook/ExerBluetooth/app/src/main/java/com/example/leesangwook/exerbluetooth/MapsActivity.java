package com.example.leesangwook.exerbluetooth;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    /** 위도, 경도 **/
    Double latitude = 0.0;
    Double longitude = 0.0;
    Double distance = 0.0;

    String UUID = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        latitude = getIntent().getExtras().getDouble("Latitude");
        longitude = getIntent().getExtras().getDouble("Longitude");
        distance =  getIntent().getExtras().getDouble("Distance");
        UUID = getIntent().getExtras().getString("UUID");

        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(loc).title(UUID + ""));

        CircleOptions circleOptions = new CircleOptions()
                .center(loc)
                .radius(distance); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 20));
    }
}
