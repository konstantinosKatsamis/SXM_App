package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

import com.example.mysignupapp.databinding.ActivityMyAppointmentsBinding;

public class MyAppointments extends DrawerBaseActivity {

    ActivityMyAppointmentsBinding activityMyAppointmentsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyAppointmentsBinding = ActivityMyAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(activityMyAppointmentsBinding.getRoot());
        allocateActivityTitle("My Appointments");
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