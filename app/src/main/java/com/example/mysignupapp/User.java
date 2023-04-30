package com.example.mysignupapp;

import android.annotation.SuppressLint;

public class User {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String username;
    private String password;

    public User(String firstName, String lastName, String birthDate, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = birthDate;
        this.email = email;
        this.username = userName;
        this.password = password;
    }

    public User(){
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
