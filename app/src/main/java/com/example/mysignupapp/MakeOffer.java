package com.example.mysignupapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mysignupapp.Utility.NetworkChangeListener;
import com.example.mysignupapp.databinding.ActivityMakeOfferBinding;

public class MakeOffer extends DrawerBaseActivity
{
    ActivityMakeOfferBinding activityMakeOfferBinding;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    boolean offer_money_choice;
    boolean offer_ad_choice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);
        activityMakeOfferBinding = ActivityMakeOfferBinding.inflate(getLayoutInflater());
        setContentView(activityMakeOfferBinding.getRoot());
        allocateActivityTitle("Make Offer");

        offer_money_choice = getIntent().getBooleanExtra("OFFER_MONEY", false);
        offer_ad_choice = getIntent().getBooleanExtra("OFFER_AD", false);

        String msg_offer = "Offer chosen: ";
        if(offer_money_choice)
        {
            msg_offer = msg_offer + "Money";
        }
        else if(offer_ad_choice)
        {
            msg_offer = msg_offer + "Trade";
        }
        
        Toast.makeText(MakeOffer.this, msg_offer, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() //if we press back in LoginActivity we can choose to close the app
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.logo_for_appeal);
        builder.setMessage("Cancel offer?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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