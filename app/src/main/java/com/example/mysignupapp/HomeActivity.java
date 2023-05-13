package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    ImageButton to_map_button;
    ImageButton to_create_Ad_button;

    ImageButton to_filters;

//    Button button_location; del
//    TextView textView_location; del
    LocationManager locationManager;
    LatLng currentLocation;

    RecyclerView adList;
    AdAdapter adapter;
    List<String> titles;
    List<Integer> images;

    FirebaseAuth mAuth;
    FirebaseUser me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_menu_main);

        mAuth = FirebaseAuth.getInstance();
        me = mAuth.getCurrentUser();

        if(me != null)
        {
            Toast.makeText(HomeActivity.this, "YOU EXIST", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(HomeActivity.this, "WHO ARE YOU", Toast.LENGTH_LONG).show();
        }

        adList = findViewById(R.id.adList);
        titles = new ArrayList<>();
        images = new ArrayList<>();

        titles.add("Ad 1");
        titles.add("Ad 2");
        titles.add("Ad 3");
        titles.add("Ad 4");
        titles.add("Ad 5");
        titles.add("Ad 6");
        titles.add("Ad 7");
        titles.add("Ad 8");
        titles.add("Ad 9");
        titles.add("Ad 10");
        titles.add("Ad 11");
        titles.add("Ad 12");
        titles.add("Ad 13");
        titles.add("Ad 14");
        titles.add("Ad 15");


        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);
        images.add(R.drawable.twitter);

        adapter = new AdAdapter(this, titles, images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        adList.setLayoutManager(gridLayoutManager);
        adList.setAdapter(adapter);

        to_map_button = findViewById(R.id.map_mode);
        to_create_Ad_button = findViewById(R.id.create_ad_mode);

//        textView_location = findViewById(R.id.text_location); del
//        button_location = findViewById(R.id.button_location); del

        if(ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        /* button_location.setOnClickListener(new View.OnClickListener() { del
            @Override
            public void onClick(View v) {
//                getLocation();
            }
        });*/

        to_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getLocation();

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setTitle("");
                builder.setMessage("Google Maps is Loading...");

                AlertDialog dialog = builder.create();
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                    }
                }, 4500); // 3000 milliseconds = 3 seconds



                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Intent to_map = new Intent(HomeActivity.this, MapActivity.class);
                        to_map.putExtra("currentLocation", currentLocation);
                        startActivity(to_map);

                    }
                },4500);



            }
        });

        to_filters = findViewById(R.id.filter_mode);

        to_filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_filter = new Intent(HomeActivity.this, FilterActivity.class);
                startActivity(to_filter);
            }
        });

        to_create_Ad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent to_map = new Intent(HomeActivity.this, CreateAd.class);
                startActivity(to_map);
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
//        System.out.println("getLocation() ----------------------------------------------------------------------------------------------------------------------------------------"); del

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,HomeActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
//        System.out.println("onLocationChanged() ----------------------------------------------------------------------------------------------------------------------------------------"); del
//        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show(); del

//        System.out.println(location.getLatitude() + "   " + location.getLongitude()); // Result: 37.99991190433502   23.73816668987274 del
        setCurrentLocation(location.getLatitude(), location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

//            textView_location.setText(address); del

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setCurrentLocation(double lat, double lon){
//        System.out.println("SETTER IS TAKING ACTION ----------------------------------------------------------------------------------------------------------------------------------------"); del
        this.currentLocation = new LatLng(lat, lon);
    }
}