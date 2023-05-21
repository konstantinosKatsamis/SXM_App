package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

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
}