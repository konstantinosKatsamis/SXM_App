package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

import com.example.mysignupapp.databinding.ActivityMyRequestsBinding;

public class MyRequests extends DrawerBaseActivity {

    ActivityMyRequestsBinding activityMyRequestsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyRequestsBinding = ActivityMyRequestsBinding.inflate(getLayoutInflater());
        setContentView(activityMyRequestsBinding.getRoot());
        allocateActivityTitle("My Requests");
    }

    @Override
    public void onLocationChanged(Location location) {

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
}