package com.example.wwwconcepts.firebase.POJOs;

import com.google.firebase.database.IgnoreExtraProperties;




@IgnoreExtraProperties
public class Review {
    private String reviewId;
    private String reviewAuthorName;
    private String userId;
    private String reviewPost;
    private String productId;
    private Boolean owner;
    private String reviewAuthorEmail;
    private String rating;

    public Review(){
        //this constructor is required
    }

    public Review(String reviewId, String reviewAuthorName, String reviewAuthorEmail, String userId, String reviewPost, String productId, String rating) {
        this.reviewId = reviewId;
        this.reviewAuthorName = reviewAuthorName;
        this.userId = userId;
        this.reviewPost = reviewPost;
        this.productId = productId;
        this.reviewAuthorEmail = reviewAuthorEmail;
        this.rating = rating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewAuthorName() {
        return reviewAuthorName;
    }

    public void setReviewAuthorName(String reviewAuthorName) {
        this.reviewAuthorName = reviewAuthorName;
    }

    public String getReviewPost() {
        return reviewPost;
    }

    public void setReviewPost(String reviewPost) {
        this.reviewPost = reviewPost;
    }

    public String getProductId(){return productId;}

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public String getReviewAuthorEmail() {
        return reviewAuthorEmail;
    }

    public void setReviewAuthorEmail(String reviewAuthorEmail) {
        this.reviewAuthorEmail = reviewAuthorEmail;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }
}