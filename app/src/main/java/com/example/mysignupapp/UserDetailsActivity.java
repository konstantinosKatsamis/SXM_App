package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.example.mysignupapp.Utility.NetworkChangeListener;
import com.example.mysignupapp.databinding.ActivityUserDetailsBinding;

public class UserDetailsActivity extends DrawerBaseActivity
{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ActivityUserDetailsBinding activityCreateAdBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityCreateAdBinding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityCreateAdBinding.getRoot());
        allocateActivityTitle("My Account");
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