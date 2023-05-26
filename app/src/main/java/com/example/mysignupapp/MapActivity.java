package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysignupapp.Utility.NetworkChangeListener;
import com.example.mysignupapp.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.Random;

public class MapActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    GoogleMap map;
    LatLng receivedCurrentLocation;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    ActivityMapBinding activityMapBinding;
    private LatLng latLng = new LatLng(38.00237292320933, 23.735086083815727);
    private List<HashMap<String, Object>> all_ads;
    private Button marker_btn;
    private View infoWindowView;
//    private AdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(activityMapBinding.getRoot());
        allocateActivityTitle("Find Ads");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Toast.makeText(MapActivity.this, "YOU EXIST", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MapActivity.this, "WHO ARE YOU", Toast.LENGTH_LONG).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
        builder.setMessage("We are already here");

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();

            }
        }, 4000); // 3000 milliseconds = 3 seconds

        receivedCurrentLocation = getIntent().getParcelableExtra("currentLocation");
        if (receivedCurrentLocation != null) {
            System.out.println("ReceivingActivity" + "Received location: " + receivedCurrentLocation.toString());
        } else {
            System.out.println("ReceivingActivity" + "Location parameter is null");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                map = googleMap;
                googleMap.setIndoorEnabled(false);
//                LatLng athens = new LatLng(receivedCurrentLocation.latitude + getRandom(), receivedCurrentLocation.longitude + getRandom());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                map.getUiSettings().setMyLocationButtonEnabled(false);

                // Remove the default location icon
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(false);

                CircleOptions circleOptions = new CircleOptions()
                        .center(latLng)
                        .radius(750) // Set the radius of the circle in meters
                        .strokeWidth(2)
                        .strokeColor(Color.YELLOW)
                        .fillColor(Color.argb(70, 255, 255, 0)); // Transparent red fill color
                map.addCircle(circleOptions);

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        marker_btn.setVisibility(View.GONE);
                    }
                });


                DatabaseReference ads_ref = FirebaseDatabase.getInstance().getReference("Ads");

                all_ads = new ArrayList<>();

//                adapter = new AdAdapter(this, all_ads);
                ads_ref.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        int count = 1;
                        all_ads.clear();
                        /*
                        * TODO skopos einai na ta valw ola ta Ad tou Firebase se ena Collection
                        * TODO kai se ena parallilo Collection id me kapoia extra outos wste
                        * TODO otan eimai mesa stor marker, na kserw to id ths aggelias k na
                        * TODO phgainw sto katallilo Ad sto megalo Collection gia na vrw tis
                        * TODO extra plirofories
                        * */
                        for(DataSnapshot adSnapshot : snapshot.getChildren())
                        {
                            HashMap<String, Object> ad_from_Ads = (HashMap<String, Object>) adSnapshot.getValue();
                            System.out.println(ad_from_Ads);
                            for(String o: ad_from_Ads.keySet()){ // TODO vlepe ti tiponei dame touto, en kltro pou to *1
                                System.out.println(o + ": " + ad_from_Ads.get(o));
                            }

                            /*int i=0; // vlepe ti tiponei dame *1
                            for(Object o: ad_from_Ads.values()){
                                System.out.println("\t" + o);
                                if(i==1){
                                    ArrayList something = (ArrayList) o;
//                                    String[] splited = something.split(",");
                                    for(Object s: something){
                                        System.out.println("\t\t" + s);
                                    }
                                }
                                i++;
                            }
                            i=0;*/ // os dame *1 gia na katalaveis pws na ta kameis Ad Objects

                            Log.d("A", "Ad number " + count);
                            count++;

                            if(!(ad_from_Ads != null && ad_from_Ads.get("Publisher").equals(currentUser.getUid())))
                            {
                                all_ads.add(ad_from_Ads);
                            }
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MapActivity.this, 2, GridLayoutManager.VERTICAL, false);
//                        adList.setLayoutManager(gridLayoutManager);
//                        adList.setAdapter(adapter);
//                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });

//                TODO mallon tha emfanizw: titlo, katigoria, switch items, price(free), publisher
                addMarker(latLng, "Your Ad Title", "Your Ad Description");
            }
        },9000);
    }

    private void openOtherActivity() {
        Intent intent = new Intent(MapActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void addMarker(LatLng latLng, String title, String snippet) {

        marker_btn = findViewById(R.id.button_perki);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(createCustomMarkerIcon(75, 75));
        Marker marker = map.addMarker(markerOptions);

        // Set a click listener for the marker
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clickedMarker) {
                if (clickedMarker.equals(marker)) {
//                    TODO isos na emfanizi apo edw kapos, kapoies leptomeries gia thn aggelia
//                    openOtherActivity();
                }
                // Return 'false' to allow the default marker click behavior (info window display, etc.)
                return false;
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                marker_btn.setVisibility(View.VISIBLE);

                // Inflate your custom info window layout
                infoWindowView = getLayoutInflater().inflate(R.layout.activity_marker_layout, null);

                // Customize the content of your info window
                TextView titleTextView = infoWindowView.findViewById(R.id.titleTextView);
                TextView snippetTextView = infoWindowView.findViewById(R.id.snippetTextView);

                titleTextView.setText(marker.getTitle());
                snippetTextView.setText(marker.getSnippet());

                infoWindowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapActivity.this, "ATEEEEEE", Toast.LENGTH_LONG).show();
                        openOtherActivity();
                    }
                });
                return infoWindowView;
            }
        });
    }

    private BitmapDescriptor createCustomMarkerIcon(int width, int height) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.logo);
        Bitmap originalBitmap = bitmapDrawable.getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
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

    private double getRandom(){
        double rangeMin = 0.001, rangeMax = 0.003;
        Random r = new Random();
        int num = getInt();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

        if(num==1){
            return randomValue * -1;
        }
        return randomValue;
    }

    private int getInt(){
        Random rand = new Random();
        int min = 0, max = 1;
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
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