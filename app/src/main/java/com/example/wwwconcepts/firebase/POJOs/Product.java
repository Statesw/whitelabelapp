package com.example.wwwconcepts.firebase.POJOs;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Product {

    public String title;
    public String image;
    public String price;
    public String productId;
    public HashMap<String, HashMap<String,String>> reviews;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Product() {
    }

    public Product(String name, String image, String price, String productId, HashMap<String, HashMap<String,String>> reviews) {
        this.title = name;
        this.image = image;
        this.price = price;
        this.productId = productId;
        this.reviews = reviews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public HashMap<String, HashMap<String,String>> getReviews() {
        return reviews;
    }

    public void setReviews(HashMap<String, HashMap<String,String>> reviews) {
        this.reviews = reviews;
    }

}
