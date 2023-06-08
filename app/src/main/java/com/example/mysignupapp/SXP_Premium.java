package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import com.example.mysignupapp.databinding.ActivitySxpPremiumBinding;

public class SXP_Premium extends DrawerBaseActivity {

    ActivitySxpPremiumBinding activitySxpPremiumBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySxpPremiumBinding = ActivitySxpPremiumBinding.inflate(getLayoutInflater());
        setContentView(activitySxpPremiumBinding.getRoot());
        allocateActivityTitle("SXM Premium");
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