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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    Button button_for_login; // the button "Enter". Used to enter from login to home screen
    Button button_for_register; // the button "Sign up". Used to register a new account

//   FirebaseFirestore fireStore;

    FirebaseAuth mAuth;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //we give our buttons ids from the activity_main.xml file
        button_for_login = findViewById(R.id.completeLogin);
        button_for_register = findViewById(R.id.register_button);

//        fireStore = FirebaseFirestore.getInstance();

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









                ArrayList<HashMap<String, User>> userList = new ArrayList<>();

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("users")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    String userId = document.getId();
                                    User user = document.toObject(User.class);

                                    HashMap<String, User> userMap = new HashMap<>();
                                    userMap.put(userId, user);

                                    userList.add(userMap);
                                }





                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAA");

                                // Do something with the userList...
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Reg", String.valueOf(e));
                            }
                        });




                for(HashMap<String, User> usr: userList){
                    for(User u: usr.values()){
                        Log.d("NEW USER", usr.keySet().toString());
                        Log.d("Reg", u.getFirstName());
                        Log.d("Reg", u.getLastName());
                        Log.d("Reg", u.getEmail());
                        Log.d("Reg", u.getDateOfBirth());
                        Log.d("Reg", u.getPassword());
                        Log.d("Reg", u.getUsername());
                        Log.d("Reg", "====================================");
                    }
                }







                /*
                mAuth = FirebaseAuth.getInstance();
                TextInputLayout USERNAME_textInputLayout = (TextInputLayout) findViewById(R.id.username_textfield);
                username = USERNAME_textInputLayout.getEditText().getText().toString().trim();
                TextInputLayout PASSWORD_textInputLayout = (TextInputLayout) findViewById(R.id.password_textfield);
                password = PASSWORD_textInputLayout.getEditText().getText().toString().trim();

                mAuth.signInWithEmailAndPassword("konstantinos1300@gmail.com", password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                                    Intent intent_for_home = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent_for_home);
                                } else {
                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }
                            }
                        });

*/


            }
        });

//        Map<String, Object> users = new HashMap<>();
//        users.put("firstName", "EASY");
//        users.put("lastName", "TUTO");
//        users.put("description", "Sub");

/*        fireStore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
            }
        });*/

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