package com.example.mysignupapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mysignupapp.databinding.ActivityAdDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdDetailsActivity extends DrawerBaseActivity
{
    ActivityAdDetailsBinding activityAdDetailsBinding;
    String ad_id;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdDetailsBinding = ActivityAdDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAdDetailsBinding.getRoot());
        ad_id = getIntent().getStringExtra("Ad_id");

        ProgressDialog progressDialog = new ProgressDialog(AdDetailsActivity.this);
        progressDialog.setMessage("Loading ad...");
        progressDialog.show();

        db = FirebaseDatabase.getInstance();
        DatabaseReference ad_ref = db.getReference("Ads/" + ad_id);

        ad_ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                HashMap<String, Object> map_of_ad = (HashMap<String, Object>) snapshot.getValue();
                String title = (String) map_of_ad.get("Title");
                String category = (String) map_of_ad.get("Category");
                String id = (String) map_of_ad.get("ID");
                ArrayList<String> images = (ArrayList<String>) map_of_ad.get("Images");
                ArrayList<String> switches = (ArrayList<String>) map_of_ad.get("Switch");
                String price = (String) map_of_ad.get("Price");

                allocateActivityTitle(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(AdDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        progressDialog.dismiss();
    }
}