package com.example.mysignupapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity
{
    private DatePickerDialog datePicker;
    private Button dateOfBirthButton;
    Button verification;
    Button login;

    String firstName;
    String lastName;
    String userName;
    String email;
    String birthDate;
    int year;
    String password;
    String re_enter_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initDatePicker();
        dateOfBirthButton = findViewById(R.id.datePickerButton);

        verification = findViewById(R.id.createAccount);
        login = findViewById(R.id.login_button);

        TextInputLayout PASSWORD_1_textInputLayout = (TextInputLayout) findViewById(R.id.password_textfield);
        password = String.valueOf(PASSWORD_1_textInputLayout.getEditText().getText());

        TextInputLayout PASSWORD_2_textInputLayout = (TextInputLayout) findViewById(R.id.password_confirm);
        re_enter_password = String.valueOf(PASSWORD_2_textInputLayout.getEditText().getText());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent register_to_login = new Intent(Register.this, MainActivity.class);
                startActivity(register_to_login);
            }
        });

        verification.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                TextInputLayout FIRST_NAME_textInputLayout = (TextInputLayout) findViewById(R.id.first_name_textfield);
                firstName = FIRST_NAME_textInputLayout.getEditText().getText().toString().trim();

                TextInputLayout LAST_NAME_textInputLayout = (TextInputLayout) findViewById(R.id.last_name_textfield);
                lastName = LAST_NAME_textInputLayout.getEditText().getText().toString().trim();

                TextInputLayout USERNAME_textInputLayout = (TextInputLayout) findViewById(R.id.username_textfield);
                userName = USERNAME_textInputLayout.getEditText().getText().toString().trim();

                TextInputLayout EMAIL_textInputLayout = (TextInputLayout) findViewById(R.id.email_address_textfield);
                String email = EMAIL_textInputLayout.getEditText().getText().toString().trim();

                TextInputLayout PASSWORD_1_textInputLayout = (TextInputLayout) findViewById(R.id.password_textfield);
                password = PASSWORD_1_textInputLayout.getEditText().getText().toString().trim();

                year = datePicker.getDatePicker().getYear();

                TextInputLayout PASSWORD_2_textInputLayout = (TextInputLayout) findViewById(R.id.password_confirm);
                re_enter_password = PASSWORD_2_textInputLayout.getEditText().getText().toString().trim();

                if(!checkFirstName(firstName))
                {
                    showPop(v, "First Name input incorrect!");
                    Log.d("Reg", "First Name written: " + firstName);
                }
                else if(!checkLastName(lastName))
                {
                    showPop(v, "Last Name input incorrect!");
                    Log.d("Reg", "Last Name written: " + lastName);
                }

                else if(!checkUserName(userName))
                {
                    showPop(v, "Username input incorrect!");
                    Log.d("Reg", "Username written: " + userName);
                }
                else if(!checkEmail(email))
                {
                    showPop(v, "Email input incorrect!");
                    Log.d("Reg", "Email written: " + email);
                }
                else if(!checkAge(year))
                {
                    showPop(v, "You must be over 16 to create account!");
                    Log.d("Reg", "Age calculated: " + (LocalDate.now().getYear() - year));
                }
                else if(!checkPasswords(password, re_enter_password))
                {
                    showPop(v, "Password inputs incorrect!");
                    Log.d("Reg", "Password1: " + password);
                    Log.d("Reg", "Password2: " + re_enter_password);
                }
            }
        });
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

    private boolean checkFirstName(String val)
    {
        if(val.isEmpty() || !val.matches("[a-zA-Z]+"))
        {
            return false;
        }
        return true;
    }

    private boolean checkLastName(String val)
    {
        if(val.isEmpty() || !Pattern.matches("[a-zA-Z]+", val))
        {
            return false;
        }
        return true;
    }

    private boolean checkUserName(String val)
    {
        String checkSpaces = "[a-zA-Z0-9]*";
        if(val.isEmpty() || val.length() > 20 && val.length() < 5 || !val.matches(checkSpaces))
            return false;
        return true;
    }

    public boolean checkEmail(String val)
    {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(val);
        boolean matchFound = m.matches();
        return matchFound;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkAge(int val)
    {
        int now = LocalDate.now().getYear();
        return now - val >= 16;
    }

    public boolean checkPasswords(String password1, String password2)
    {
        String checkSpaces = "\\A\\w{1,20}\\z";

        if(password1.isEmpty() || password1.length() > 20 || password1.length() < 5 || !password1.matches(checkSpaces))
        {
            return false;
        }

        if(password2.isEmpty() || password2.length() > 20 || password2.length() < 5 || !password2.matches(checkSpaces))
        {
            return false;
        }

        return password1.equals(password2);

    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateOfBirthButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " "+ year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1) {return "Jan";}
        if(month == 2) {return "Feb";}
        if(month == 3) {return "Mar";}
        if(month == 4) {return "Apr";}
        if(month == 5) {return "May";}
        if(month == 6) {return "Jun";}
        if(month == 7) {return "Jul";}
        if(month == 8) {return "Aug";}
        if(month == 9) {return "Sep";}
        if(month == 10) {return "Oct";}
        if(month == 11) {return "Nov";}
        if(month == 12) {return "Dec";}

        return "Jan";
    }

    public void openBirthDatePicker(View view)
    {
        datePicker.show();
    }
}