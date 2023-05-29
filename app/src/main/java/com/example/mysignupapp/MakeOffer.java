package com.example.mysignupapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mysignupapp.Utility.NetworkChangeListener;
import com.example.mysignupapp.databinding.ActivityMakeOfferBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MakeOffer extends DrawerBaseActivity
{
    ActivityMakeOfferBinding activityMakeOfferBinding;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    boolean offer_money_choice;
    boolean offer_ad_choice;
    String offer_sender; //String which carries the id of the sender(current user)
    String offer_receiver;//String which carries the id of the ad's publisher
    String offer_about_ad_id;//String which carries the id of the ad
    private static final int PICK_REQUEST_IMAGES_CODE = 1000; //code for pick image intent
    int position = 0; //int to keep track of the images' indexes
    TextInputLayout SELECT_CATEGORY_TEXT_INPUT; //"Select Category" for offer's category
    TextInputLayout TITLE_TEXT_INPUT;//Title of Ad Offer
    TextInputLayout PRICE_TEXT_INPUT;//Price to offer
    TextInputLayout DESCRIPTION_TEXT_INPUT;//Description of offer
    RelativeLayout RELATIVE_LAYOUT_REQUEST;
    TextView image_number_text;
    ImageSwitcher image_switcher_offer;
    private ArrayList<Uri> imageUris;
    private ArrayList<String> image_url_paths;
    Button previous_image_button;
    Button next_image_button;
    ImageButton delete_image_button;
    Button pick_image_button;
    Button send_request_button;
    Button smart_check;
    String[] categories_to_choose = {"Vehicles", "Men Clothing", "Women Clothing", "Music",
            "Sports", "Office", "Books", "Electronics", "Toys", "Movies", "Collectibles"};
    AutoCompleteTextView text_of_chosen_category;
    ArrayAdapter<String> adapter_for_categories;
    String chosen_category_input;
    FirebaseDatabase db;
    HashMap<String, Object> which_ad_i_am_interested;
    String sender_username;
    String receiver_username;
    StorageTask uploadTask2;
    StorageReference storageReference;

    String offer_title_input;
    String offer_category_input;
    String offer_description_input;

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
        offer_sender = getIntent().getStringExtra("OFFER_FROM");
        offer_receiver = getIntent().getStringExtra("OFFER_TO");
        offer_about_ad_id = getIntent().getStringExtra("OFFER_ABOUT");

        previous_image_button = (Button) findViewById(R.id.request_previousBtn);
        next_image_button = (Button) findViewById(R.id.request_nextBtn);
        delete_image_button = (ImageButton) findViewById(R.id.request_deleteBtn);
        pick_image_button = (Button) findViewById(R.id.request_pickImagesBtn);
        send_request_button = (Button) findViewById(R.id.create_request);
        smart_check = (Button) findViewById(R.id.request_smart_check);

        SELECT_CATEGORY_TEXT_INPUT = findViewById(R.id.request_category_box);
        text_of_chosen_category = findViewById(R.id.request_category);
        TITLE_TEXT_INPUT = findViewById(R.id.request_title_textfield);
        PRICE_TEXT_INPUT = findViewById(R.id.request_price_textfield);
        DESCRIPTION_TEXT_INPUT = findViewById(R.id.request_description_textfield);
        image_number_text = findViewById(R.id.request_image_number);
        RELATIVE_LAYOUT_REQUEST = findViewById(R.id.relative_for_request_image);
        image_switcher_offer = findViewById(R.id.request_imagesIs);
        adapter_for_categories = new ArrayAdapter<String>(this, R.layout.list_item, categories_to_choose);
        text_of_chosen_category.setAdapter(adapter_for_categories);

        if (offer_money_choice) {
            SELECT_CATEGORY_TEXT_INPUT.setVisibility(View.GONE);
            TITLE_TEXT_INPUT.setVisibility(View.GONE);
            DESCRIPTION_TEXT_INPUT.setVisibility(View.GONE);
            image_number_text.setVisibility(View.GONE);
            pick_image_button.setVisibility(View.GONE);
            previous_image_button.setVisibility(View.GONE);
            delete_image_button.setVisibility(View.GONE);
            next_image_button.setVisibility(View.GONE);
            image_switcher_offer.setVisibility(View.GONE);
            RELATIVE_LAYOUT_REQUEST.setVisibility(View.GONE);
        } else if (offer_ad_choice) {
            PRICE_TEXT_INPUT.setVisibility(View.GONE);
        }

        db = FirebaseDatabase.getInstance();
        DatabaseReference ads_ref = db.getReference("Ads");
        DatabaseReference user_ref = db.getReference("Users");

        storageReference = FirebaseStorage.getInstance().getReference("Requests");

        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user_child : snapshot.getChildren()) {
                    User user_now = user_child.getValue(User.class);

                    if (user_now.getId().equals(offer_sender)) {
                        sender_username = user_now.getUsername();
                    }

                    if (user_now.getId().equals(offer_receiver)) {
                        receiver_username = user_now.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ads_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    HashMap<String, Object> child_hashmap = (HashMap<String, Object>) child.getValue();
                    if (child_hashmap.get("ID").equals(offer_about_ad_id)) {
                        which_ad_i_am_interested = child_hashmap;
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Requests");
        text_of_chosen_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                chosen_category_input = item;
            }
        });

        imageUris = new ArrayList<>();
        image_url_paths = new ArrayList<>();

        String image_number_so_far = "Images: " + imageUris.size() + "/5";
        image_number_text.setText(image_number_so_far);
        image_switcher_offer.setFactory(new ViewSwitcher.ViewFactory() {
            @Override

            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });
        previous_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position--;
                    image_switcher_offer.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(MakeOffer.this, "No Previous images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imageUris.size() - 1) {
                    position++;
                    image_switcher_offer.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(MakeOffer.this, "No More images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris.size() == 1) {
                    image_switcher_offer.setImageURI(null);
                    imageUris.remove(position);
                    String image_number_now = "Images: " + imageUris.size() + "/5";
                    image_number_text.setText(image_number_now);
                } else if (imageUris.size() >= 2) {
                    imageUris.remove(position);
                    image_switcher_offer.setImageURI(imageUris.get(0));
                    String image_number_now = "Images: " + imageUris.size() + "/5";
                    image_number_text.setText(image_number_now);
                } else if (imageUris.isEmpty()) {
                    Toast.makeText(MakeOffer.this, "No images to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pick_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUris.size() >= 5) {
                    Toast.makeText(MakeOffer.this, "Max image input reached!", Toast.LENGTH_SHORT).show();
                } else {
                    pickImagesIntent2();
                }
            }
        });


        send_request_button.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                sendRequestToUser();
            }
        });

    }

    private void sendRequestToUser()
    {
        if (offer_money_choice)
        {
            String price_input = PRICE_TEXT_INPUT.getEditText().getText().toString();

            boolean isDouble = false;
            boolean isDoublePositive = false;

            try {
                double number = Double.parseDouble(price_input);
                if (number > 0) {
                    isDouble = true;
                    isDoublePositive = true;
                } else {
                    isDouble = true;
                }
            } catch (NumberFormatException e) {
                showPop(getWindow().getDecorView().getRootView(), "Not a number");
            }

            if (isDouble && isDoublePositive) {
                ProgressDialog progressDialog = new ProgressDialog(MakeOffer.this);
                progressDialog.setMessage("Sending Request...");
                progressDialog.show();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
                String request_id = reference.push().getKey();

                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                String date_now = dateFormat.format(now);

                Request send_request = new Request(request_id, offer_sender, offer_receiver, date_now,
                        which_ad_i_am_interested, price_input + "$", null, false);

                String title_for_request = which_ad_i_am_interested.get("Title") + ": " + sender_username + "to " + receiver_username;
                reference.child(title_for_request).setValue(send_request);

                progressDialog.dismiss();

            } else if (isDouble) {
                showPop(getWindow().getDecorView().getRootView(), "You must put positive number for money");
            }
        }
        else if (offer_ad_choice)
        {
            ProgressDialog progressDialog = new ProgressDialog(MakeOffer.this);
            progressDialog.setMessage("Sending request...");
            progressDialog.show();

            offer_title_input = TITLE_TEXT_INPUT.getEditText().getText().toString();
            offer_category_input = chosen_category_input;
            offer_description_input = DESCRIPTION_TEXT_INPUT.getEditText().getText().toString();

            if (imageUris.size() > 0) {
                ArrayList<String> my_urls = new ArrayList<>();

                for (int i = 0; i < imageUris.size(); i++) {
                    StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                            + "." + getFileExtension(imageUris.get(i)));

                    uploadTask2 = fileReference.putFile(imageUris.get(i));
                    uploadTask2.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener((OnCompleteListener<Uri>) task ->
                    {
                        if (task.isSuccessful()) {
                            Uri image = task.getResult();
                            String myurl = image.toString();
                            my_urls.add(myurl);

                            if (my_urls.size() == imageUris.size()) {
                                try {
                                    HashMap<String, Object> request_offer = new HashMap<>();
                                    db = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
                                    String requestId = reference.push().getKey();

                                    request_offer.put("Title", offer_title_input);
                                    request_offer.put("Description", offer_description_input);
                                    request_offer.put("Category", offer_category_input);
                                    request_offer.put("Images", my_urls);

                                    Date now = new Date();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                                    String date_now = dateFormat.format(now);

                                    Request send_request = new Request(requestId, offer_sender, offer_receiver, date_now,
                                            which_ad_i_am_interested, null, request_offer, false);

                                    String title_for_request = which_ad_i_am_interested.get("Title") + ": " + sender_username + "to " + receiver_username;
                                    reference.child(title_for_request).setValue(send_request);

                                    progressDialog.dismiss();

                                } catch (NullPointerException e) {
                                    Toast.makeText(MakeOffer.this, "!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(MakeOffer.this, "Failed to post!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MakeOffer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(MakeOffer.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void pickImagesIntent2()
    {
        Intent intent_for_images = new Intent();
        intent_for_images.setType("image/*");
        intent_for_images.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent_for_images.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent_for_images, "Select Image(s)"), PICK_REQUEST_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_REQUEST_IMAGES_CODE)
        {

            if (resultCode == Activity.RESULT_OK)
            {

                if(data.getClipData() != null)
                {

                    int count = data.getClipData().getItemCount();

                    if(count > 5)
                    {
                        count = 5;
                    }
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }

                    image_switcher_offer.setImageURI(imageUris.get(0));
                    position = 0;
                }
                else
                {
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    image_switcher_offer.setImageURI(imageUris.get(0));
                    position = 0;
                }
                String image_number_so_far = "Images: " + imageUris.size() + "/5";
                image_number_text.setText(image_number_so_far);
            }
        }
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

    public void showPop(View view, String error_message)
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.wrong_input_popup, null);
        builder.setView(customLayout);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        Button OK = customLayout.findViewById(R.id.OK);
        TextView errorMsg = customLayout.findViewById(R.id.error_message);
        errorMsg.setText(error_message);
        OK.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
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