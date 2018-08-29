package com.example.wwwconcepts.firebase.POJOs;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Cart { //TODO: For logging history purchases and points maybe?

    public String userId;
    public HashMap<String, HashMap<String, String>> items;
    private String paymentStatus;
//    public float subtotal;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Cart.class)
    public Cart() {
    }

    public Cart(String userId, HashMap<String, HashMap<String, String>> items) {
        this.userId = userId;
        this.items = items;
//        this.subtotal=0;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public float getSubtotal() {
//        //TODO: point to all item price, add and return!
//        return subtotal;
//    }

    public HashMap<String, HashMap<String, String>> getItems() {
        return items;
    }

    public void setItems(HashMap<String, HashMap<String, String>> items) {
        this.items = items;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}