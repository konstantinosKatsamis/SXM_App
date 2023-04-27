package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    LatLng receivedCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


//        System.out.println("MapActivity===========================================================================================================================================================\n"); del
//        System.out.println("RECEIVED COORDS FOR CURRENT LOCATION:\n"); del

        receivedCurrentLocation = getIntent().getParcelableExtra("currentLocation");
        if (receivedCurrentLocation != null) {
            System.out.println("ReceivingActivity" + "Received location: " + receivedCurrentLocation.toString());
//            System.out.println("LATITUDE: " + receivedCurrentLocation.latitude); del
//            System.out.println("LONGITUDE: " + receivedCurrentLocation.longitude); del
        } else {
            System.out.println("ReceivingActivity" + "Location parameter is null");
        }



//        System.out.println("==========================================================================================================================================================="); del

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // We create and start a new Intent from splash activity to MainActivity
                // MainActivity : Sign in/ Login screen
                map = googleMap;
                googleMap.setIndoorEnabled(false);
                LatLng Athens = new LatLng(receivedCurrentLocation.latitude, receivedCurrentLocation.longitude);
                map.addMarker(new MarkerOptions().position(Athens).title("Athens"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(Athens, 16));

//                System.out.println("LATITUDE: " + receivedCurrentLocation.latitude); del
//                System.out.println("LONGITUDE: " + receivedCurrentLocation.longitude); del

            }
        },5500);
    }
}