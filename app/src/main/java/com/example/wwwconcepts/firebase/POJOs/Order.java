package com.example.wwwconcepts.firebase.POJOs;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Order {
    public HashMap<String, HashMap<String, String>> products;
    public String date;
    public String payStatus;
    public String subtotal;
    public String orderId;


    public Order(){}


    public Order(String date, String payStatus, String subtotal, String orderId){
        this.date = date;
        this.payStatus = payStatus;
        this.subtotal = subtotal;
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
