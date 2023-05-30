package com.example.mysignupapp;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mysignupapp.databinding.ActivityRequestDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestDetailsActivity extends DrawerBaseActivity
{
    ActivityRequestDetailsBinding activityRequestDetailsBinding;
    String sender_id;
    String receiver_id;
    String key_for_request;
    String name_of_ad;
    String sender_username;
    String receiver_username;

    FirebaseAuth mAuth;
    FirebaseUser me;

    DatabaseReference users;
    DatabaseReference sender_user;
    DatabaseReference receiver_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestDetailsBinding = ActivityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityRequestDetailsBinding.getRoot());
        allocateActivityTitle("Request Details");

        mAuth = FirebaseAuth.getInstance();
        me = mAuth.getCurrentUser();

        sender_id = getIntent().getStringExtra("S_ID");
        receiver_id = getIntent().getStringExtra("R_ID");
        name_of_ad = getIntent().getStringExtra("A_ID");

        users = FirebaseDatabase.getInstance().getReference("Users");
        sender_user = users.child(sender_id);
        receiver_user = users.child(receiver_id);

        key_for_request = name_of_ad + ": ";

        ProgressDialog progressDialog = new ProgressDialog(RequestDetailsActivity.this);
        progressDialog.setMessage("Loading request...");
        progressDialog.show();

        sender_user.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                User sender = snapshot.getValue(User.class);
                sender_username = sender.getUsername();

                if(sender != null && sender_username != null)
                {
                    progressDialog.dismiss();

                    if (receiver_username != null)
                    {
                        key_for_request = key_for_request + sender_username + "to " + receiver_username;
                        useCompleteKey();
                    }

                    Toast.makeText(RequestDetailsActivity.this, sender_username, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        receiver_user.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                User receiver = snapshot.getValue(User.class);
                receiver_username = receiver.getUsername();

                if(receiver != null && receiver_username != null)
                {
                    progressDialog.dismiss();

                    if (sender_username != null)
                    {
                        key_for_request = key_for_request + sender_username + "to " + receiver_username;
                        useCompleteKey();
                    }
                    Toast.makeText(RequestDetailsActivity.this, receiver_username, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }

    private void useCompleteKey()
    {
        Toast.makeText(RequestDetailsActivity.this, key_for_request, Toast.LENGTH_LONG).show();
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