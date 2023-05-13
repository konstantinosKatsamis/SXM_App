package com.example.mysignupapp;

public class User
{
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String emailAddress;
    public String username;
    public String password;

    public User(){}

    public User(String firstName, String lastName, String dateOfBirth, String emailAddress, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.emailAddress = emailAddress;
        this.username = username;
        this.password = password;
    }
}
