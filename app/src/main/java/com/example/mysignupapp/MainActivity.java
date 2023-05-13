package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    Button button_for_login; // the button "Enter". Used to enter from login to home screen
    Button button_for_register; // the button "Sign up". Used to register a new account
    String exist_username;
    String exist_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {

        }

        //we give our buttons ids from the activity_main.xml file
        button_for_login = findViewById(R.id.completeLogin);
        button_for_register = findViewById(R.id.register_button);

        //When we press the "Sign up" button
        button_for_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ownerView) {

                //We enter a new Intent which takes us to the Register activity
                Intent intent_for_register = new Intent(MainActivity.this, Register.class);
                startActivity(intent_for_register);

            }
        });

        /*When we press the "Enter button"
            1.  If both textfields (username, password) are filled
            2. The Firebase database checks if the authentication has a correct form (Username and Password)
            3. If the syntax is correct, the database searches if user with given credentials exists
            4. If not, we show the proper message
            5. If credentials are correct, we enter as the given user to the HomeActivity
         */
        button_for_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ownerView) {

                TextInputLayout USERNAME_textInputLayout = (TextInputLayout) findViewById(R.id.username_textfield);
                exist_username = USERNAME_textInputLayout.getEditText().getText().toString().trim();

                TextInputLayout PASSWORD_textInputLayout = (TextInputLayout) findViewById(R.id.password_textfield);
                exist_password = PASSWORD_textInputLayout.getEditText().getText().toString().trim();


                Log.d("Logi", "Username empty: " + exist_username.isEmpty());
                Log.d("Logi", "Password empty: " + exist_password.isEmpty());
                Log.d("Logi", "Username: " + exist_username);
                Log.d("Logi", "Password: " + exist_password);

                authenticateUser();

            }
        });
    }

    private void authenticateUser()
    {
        String username1 = exist_username;
        String password1 = exist_password;

        if(username1.isEmpty() || password1.isEmpty())
        {
            Toast.makeText(this, "Please fill username and password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(username1 + "@mydomain.com", password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            LoginToHome();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Authentication failed due to: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    private void LoginToHome()
    {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //In case we press the back button we have a choice to exit and close the app
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
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
}