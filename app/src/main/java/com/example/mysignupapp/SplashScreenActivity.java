package com.example.mysignupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/*SplashScreenActivity is used to play an animated intro of our app
Code: When the activity is created the intro
will be displayed for 4 seconds (line 29 shows time in milliseconds)
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // We create and start a new Intent from splash activity to MainActivity
                // MainActivity : Sign in/ Login screen
                Intent login_intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(login_intent);
                finish();

            }
        },4000);




    }
}