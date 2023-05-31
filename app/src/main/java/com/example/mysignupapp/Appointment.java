package com.example.mysignupapp;

public class Appointment
{
    Request request;
    String sender_id;
    String sender_username;
    String receiver_id;
    String receiver_username;

    public Appointment()
    {

    }

    public Appointment(Request request, String sender_id, String sender_username, String receiver_id, String receiver_username)
    {
        this.request = request;
        this.sender_id = sender_id;
        this.sender_username = sender_username;
        this.receiver_id = receiver_id;
        this.receiver_username = receiver_username;
    }

    public Request getRequest()
    {
        return request;
    }

    public void setRequest(Request request)
    {
        this.request = request;
    }

    public String getSender_id()
    {
        return sender_id;
    }

    public void setSender_id(String sender_id)
    {
        this.sender_id = sender_id;
    }

    public String getSender_username()
    {
        return sender_username;
    }

    public void setSender_username(String sender_username)
    {
        this.sender_username = sender_username;
    }

    public String getReceiver_id()
    {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id)
    {
        this.receiver_id = receiver_id;
    }

    public String getReceiver_username()
    {
        return receiver_username;
    }

    public void setReceiver_username(String receiver_username)
    {
        this.receiver_username = receiver_username;
    }
}
