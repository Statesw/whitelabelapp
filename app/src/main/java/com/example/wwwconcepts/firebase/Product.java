package com.example.wwwconcepts.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    public String title;
    public String image;
    public String price;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Product() {
    }

    public Product(String name, String email, String price) {
        this.title = name;
        this.image = email;
        this.price = price;
    }
}
