package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mysignupapp.databinding.ActivityMapBinding;
import com.example.mysignupapp.databinding.ActivityMyAdsBinding;

public class MyAds extends DrawerBaseActivity {

    ActivityMyAdsBinding activityMyAdsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyAdsBinding = ActivityMyAdsBinding.inflate(getLayoutInflater());
        setContentView(activityMyAdsBinding.getRoot());
        allocateActivityTitle("My Ads");
    }
}