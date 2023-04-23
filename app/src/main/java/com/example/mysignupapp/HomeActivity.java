package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    ImageButton to_map_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_menu_main);

        to_map_button = findViewById(R.id.map_mode);

        to_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Intent to_map = new Intent(HomeActivity.this, Map_Activity.class);
                //startActivity(to_map);
            }
        });
    }
}