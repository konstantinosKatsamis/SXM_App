package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.mysignupapp.Utility.NetworkChangeListener;
import com.example.mysignupapp.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MapActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    GoogleMap map;
    LatLng receivedCurrentLocation;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    ActivityMapBinding activityMapBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(activityMapBinding.getRoot());
        allocateActivityTitle("Find Ads");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Toast.makeText(MapActivity.this, "YOU EXIST", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MapActivity.this, "WHO ARE YOU", Toast.LENGTH_LONG).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
//        builder.setTitle("Confirmation"); del
        builder.setMessage("We are already here");

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

            }
        }, 4000); // 3000 milliseconds = 3 seconds


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
                map = googleMap;
                googleMap.setIndoorEnabled(false);
                LatLng Athens = new LatLng(receivedCurrentLocation.latitude, receivedCurrentLocation.longitude);
                map.addMarker(new MarkerOptions().position(Athens).title("Athens"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(Athens, 16));

            }
        },9000);
    }

    @Override
    protected void onStart()
    {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}