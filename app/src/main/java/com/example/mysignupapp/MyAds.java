package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mysignupapp.databinding.ActivityMapBinding;
import com.example.mysignupapp.databinding.ActivityMyAdsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAds extends DrawerBaseActivity {

    ActivityMyAdsBinding activityMyAdsBinding;

    private RecyclerView my_ad_list;
    private List<HashMap<String, Object>> all_ads;
    private AdAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyAdsBinding = ActivityMyAdsBinding.inflate(getLayoutInflater());
        setContentView(activityMyAdsBinding.getRoot());
        allocateActivityTitle("My Ads");

        mAuth = FirebaseAuth.getInstance();
        me = mAuth.getCurrentUser();

        my_ad_list = findViewById(R.id.my_ad_list);

        ProgressDialog progressDialog = new ProgressDialog(MyAds.this);
        progressDialog.setMessage("Loading ads...");
        progressDialog.show();

        DatabaseReference ads_ref = FirebaseDatabase.getInstance().getReference("Ads");

        all_ads = new ArrayList<>();

        adapter = new AdAdapter(this, all_ads);
        ads_ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int count = 1;
                all_ads.clear();
                boolean ad = false;
                for(DataSnapshot adSnapshot : snapshot.getChildren())
                {
                    HashMap<String, Object> ad_from_Ads = (HashMap<String, Object>) adSnapshot.getValue();
                    Log.d("A", "Ad number " + count);
                    count++;

                    if((ad_from_Ads != null && ad_from_Ads.get("Publisher").equals(me.getUid())))
                    {
                        all_ads.add(ad_from_Ads);
                        ad = true;
                    }
                }
                GridLayoutManager gridLayoutManager = new GridLayoutManager(MyAds.this, 2, GridLayoutManager.VERTICAL, false);
                my_ad_list.setLayoutManager(gridLayoutManager);
                my_ad_list.setAdapter(adapter);
                progressDialog.dismiss();

                if (!ad){
                    TextView textView_my_ads = findViewById(R.id.textView_my_ads);
                    textView_my_ads.setText("No items");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

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