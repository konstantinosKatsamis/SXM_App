package com.example.mysignupapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mysignupapp.Utility.NetworkChangeListener;
import com.example.mysignupapp.databinding.ActivityAdDetailsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdDetailsActivity extends DrawerBaseActivity
{
    ActivityAdDetailsBinding activityAdDetailsBinding;
    String ad_id;
    FirebaseDatabase db;

    Button publisher_button;
    Button map_button;
    Button request_button;

    String title;
    String category;
    String id;
    String publisher;
    ArrayList<String> images;
    ArrayList<String> switches;
    String price;
    String description;
    
    TextView category_ad;
//    TextView price_ad;
    TextView switch_ad;
    TextView description_ad;

    HashMap<String, Object> map_of_ad;

    private ViewPager2 viewPager2;

    private Handler handler_for_images = new Handler();
    private HashMap<String, Object> coords;
    private double la, lo;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdDetailsBinding = ActivityAdDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityAdDetailsBinding.getRoot());
        ad_id = getIntent().getStringExtra("Ad_id");

        publisher_button = (Button) findViewById(R.id.publisher_button);
        map_button = (Button) findViewById(R.id.map_button);
        request_button = (Button) findViewById(R.id.request_button);

        category_ad = (TextView) findViewById(R.id.ad_category);
//        price_ad = (TextView) findViewById(R.id.ad_price);
        switch_ad = (TextView) findViewById(R.id.ad_preffered_items);
        description_ad = (TextView) findViewById(R.id.ad_description);

        viewPager2 = findViewById(R.id.ViewPagerForImages);

        List<Image_For_Slider> slider_items = new ArrayList<>();

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
                map_of_ad = (HashMap<String, Object>) snapshot.getValue();
                title = (String) map_of_ad.get("Title");
                category = (String) map_of_ad.get("Category");
                id = (String) map_of_ad.get("ID");
                publisher = (String) map_of_ad.get("Publisher");
                images = (ArrayList<String>) map_of_ad.get("Images");
                switches = (ArrayList<String>) map_of_ad.get("Switch");
                description = (String) map_of_ad.get("Description");
                price = (String) map_of_ad.get("Price");

                coords = (HashMap<String, Object>) map_of_ad.get("Coordinates");

                String str_la = (String) coords.get("latitude").toString();
                String str_lo = (String) coords.get("longitude").toString();
                if(str_la.equals("0") || str_lo.equals("0")){
                    la = 0.0;
                    lo = 0.0;
                }else{
                    la = (double) coords.get("latitude");
                    lo = (double) coords.get("longitude");
                }


                allocateActivityTitle(title);

                for(String path: images)
                {
                    slider_items.add(new Image_For_Slider(path));
                }

                viewPager2.setAdapter(new ImageSliderAdapter(slider_items, viewPager2));
                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer()
                {
                    @Override
                    public void transformPage(@NonNull View page, float position)
                    {
                        float r = 1 - Math.abs(position);
                        page.setScaleY(0.85f + r * 0.15f);
                    }
                });
                viewPager2.setPageTransformer(compositePageTransformer);

                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
                {
                    @Override
                    public void onPageSelected(int position)
                    {
                        super.onPageSelected(position);
                        handler_for_images.removeCallbacks(slider_runnable);
                        handler_for_images.postDelayed(slider_runnable, 3000);
                    }
                });

                String what_category = "Category: " + category;
                category_ad.setText(what_category);

                String what_price = "Price: " + price;
//                price_ad.setText(what_price);

                String what_switches = "";

                if(switches.isEmpty())
                {
                    what_switches = "Preferred categories for switching: " + "Nothing";
                }
                else
                {
                    what_switches = "Preferred categories for switching: " + String.join(",", switches);
                }

                switch_ad.setText(what_switches);

                String what_description = "";

                if(description == null)
                {
                    what_description = "Description: None";
                }
                else
                {
                    what_description = "Description: " + description;
                }

                description_ad.setText(what_description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(AdDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        progressDialog.dismiss();

        publisher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent my_account = new Intent(AdDetailsActivity.this, UserDetailsActivity.class);
                my_account.putExtra("ID", publisher);
                startActivity(my_account);
            }
        });

        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(la == 0){
                    Toast.makeText(AdDetailsActivity.this, "This ad is not available in Maps", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent my_account = new Intent(AdDetailsActivity.this, MapActivity.class);
                    my_account.putExtra("Ad_id", category + " " + title);
                    startActivity(my_account);
                }
            }
        });

        request_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent request_intent = new Intent(AdDetailsActivity.this, ChooseOfferActivity.class);
                request_intent.putExtra("RECEIVER", publisher);
                request_intent.putExtra("SENDER", FirebaseAuth.getInstance().getCurrentUser().getUid());
                request_intent.putExtra("AD_ID", id);

                startActivity(request_intent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    private Runnable slider_runnable = new Runnable()
    {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onStop()
    {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler_for_images.removeCallbacks(slider_runnable);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        handler_for_images.postDelayed(slider_runnable, 3000);
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