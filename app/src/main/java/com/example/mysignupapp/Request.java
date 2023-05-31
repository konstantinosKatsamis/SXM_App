package com.example.mysignupapp;

import java.util.HashMap;

public class Request
{
    String request_id;
    String sender_id;
    String receiver_id;
    String when;
    HashMap<String, Object> about;
    String price_offer;
    HashMap<String, Object> trade;
    boolean accepted;

    public Request(String request_id, String sender_id, String receiver_id, String when,
                   HashMap<String, Object> about, String price_offer, HashMap<String, Object> trade, boolean accepted)
    {
        this.request_id = request_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.when = when;
        this.about = about;
        this.price_offer = price_offer;
        this.trade = trade;
        this.accepted = accepted;
    }

    public Request()
    {

    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getSender_id()
    {
        return sender_id;
    }

    public void setSender_id(String sender_id)
    {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id)
    {
        this.receiver_id = receiver_id;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when)
    {
        this.when = when;
    }

    public HashMap<String, Object> getAbout()
    {
        return about;
    }

    public void setAbout(HashMap<String, Object> about)
    {
        this.about = about;
    }

    public String getPrice_offer()
    {
        return price_offer;
    }

    public void setPrice_offer(String price_offer)
    {
        this.price_offer = price_offer;
    }

    public HashMap<String, Object> getTrade()
    {
        return trade;
    }

    public void setTrade(HashMap<String, Object> trade) {
        this.trade = trade;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public void setAccepted(boolean accepted)
    {
        this.accepted = accepted;
    }
}


