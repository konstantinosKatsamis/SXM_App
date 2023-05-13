package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CreateAd extends AppCompatActivity
{
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String[] items = {"Collectors", "Vehicles", "Books", "Men Clothing", "Women Clothing", "Music", "Sports"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    private ImageSwitcher imageIs;
    private Button previousBtn, nextBtn, pickImagesBtn;
    private Button create_ad_button;

    private ArrayList<Uri> imageUris;
    private ArrayList<String> myurls;

    StorageTask uploadTask;
    StorageReference storageReference;

    String title_input;
    String category_input;
    String price_input;
    ArrayList<String> switch_inputs;

    TextView multiple_selections;
    boolean[] selectedDay;

    ArrayList<Integer> daylist = new ArrayList<>();
    String[] dayArray = {"Collectors", "Vehicles", "Books", "Men Clothing", "Women Clothing", "Music", "Sports"};


    private static final int PICK_IMAGES_CODE = 0;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Toast.makeText(CreateAd.this, "YOU EXIST", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(CreateAd.this, "WHO ARE YOU", Toast.LENGTH_LONG).show();
        }
        switch_inputs = new ArrayList<>();

        storageReference = FirebaseStorage.getInstance().getReference("Ads");

        autoCompleteTxt = findViewById(R.id.select_category);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        imageIs = findViewById(R.id.imagesIs);
        previousBtn = findViewById(R.id.previousBtn);
        nextBtn = findViewById(R.id.nextBtn);
        pickImagesBtn = findViewById(R.id.pickImagesBtn);

        imageUris = new ArrayList<>();
        myurls = new ArrayList<>();

        imageIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

        pickImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position--;
                    imageIs.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(CreateAd.this, "No Previous images...", Toast.LENGTH_SHORT).show();

                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imageUris.size() - 1) {
                    position++;
                    imageIs.setImageURI(imageUris.get(position));
                } else {
                    Toast.makeText(CreateAd.this, "No More images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextInputLayout TITLE_textInputLayout = (TextInputLayout) findViewById(R.id.title_textfield);
        title_input = TITLE_textInputLayout.getEditText().getText().toString().trim();

        AutoCompleteTextView CATEGORY_textInputLayout = findViewById(R.id.select_category);
        category_input = CATEGORY_textInputLayout.getText().toString().trim();

        TextInputLayout PRICE_textInputLayout = (TextInputLayout) findViewById(R.id.price_textfield);
        price_input = PRICE_textInputLayout.getEditText().getText().toString().trim();

        multiple_selections = findViewById(R.id.switch_multiple_selector);
        selectedDay = new boolean[dayArray.length];

        create_ad_button = findViewById(R.id.create_button);
        multiple_selections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAd.this);
                builder.setTitle("Category");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            daylist.add(which);
                            Collections.sort(daylist);
                        } else {
                            daylist.remove(Integer.valueOf(which));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int j = 0; j < daylist.size(); j++) {
                            stringBuilder.append(dayArray[daylist.get(j)]);
                            switch_inputs.add(dayArray[daylist.get(j)]);

                            if (j != daylist.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }

                        multiple_selections.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j = 0; j < selectedDay.length; j++) {
                            selectedDay[j] = false;
                            daylist.clear();
                            switch_inputs.clear();
                            multiple_selections.setText("");
                        }

                    }
                });

                builder.show();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("Ads") ;
        create_ad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAd();
            }
        });

    }

    private void makeAd()
    {
        ProgressDialog progressDialog = new ProgressDialog(CreateAd.this);
        progressDialog.setMessage("Posting ad...");
        progressDialog.show();

        if (imageUris.size() > 0) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUris.get(0)));

            uploadTask = fileReference.putFile(imageUris.get(0));
            uploadTask.continueWithTask(new Continuation()
            {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if(task.isSuccessful())
                {
                    Uri image = task.getResult();
                    String myurl = image.toString();
                    myurls.add(myurl);

                    Ad new_ad = new Ad(title_input, category_input, price_input, switch_inputs, myurls);

                    if(new_ad == null)
                    {
                        Toast.makeText(CreateAd.this, "Failed!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                    }

                    String user_id = mAuth.getCurrentUser().getUid();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();

                    DatabaseReference user_ref = db.getReference("Users/" + user_id);
                    user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            User user_now = snapshot.getValue(User.class);
                            user_now.getAds().add(new_ad);
                            user_ref.setValue(user_now);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            Toast.makeText(CreateAd.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ads");
                    String adId = reference.push().getKey();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("adId", adId);
                    hashMap.put("adIimage", myurl);
                    hashMap.put("publisher", currentUser.getUid());
                    hashMap.put("title", title_input);
                    hashMap.put("price", price_input);
                    hashMap.put("category", category_input);
                    hashMap.put("switch", switch_inputs);

                    reference.child(adId).setValue(hashMap);

                    progressDialog.dismiss();

                    startActivity(new Intent(CreateAd.this, HomeActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(CreateAd.this, "Failed to post!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(CreateAd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
        {
            Toast.makeText(CreateAd.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                if (data.getClipData() != null) {

                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }

                    imageIs.setImageURI(imageUris.get(0));
                    position = 0;
                } else {
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    imageIs.setImageURI(imageUris.get(0));
                    position = 0;
                }
            }
        }
    }
}