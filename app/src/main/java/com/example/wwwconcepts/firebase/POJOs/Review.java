package com.example.wwwconcepts.firebase.POJOs;

import com.google.firebase.database.IgnoreExtraProperties;




@IgnoreExtraProperties
public class Review {
    private String reviewId;
    private String reviewAuthorName;
    private String userId;
    private String reviewPost;
    private String productId;

    public Review(){
        //this constructor is required
    }

    public Review(String reviewId, String reviewAuthorName, String userId, String reviewPost, String productId) {
        this.reviewId = reviewId;
        this.reviewAuthorName = reviewAuthorName;
        this.userId = userId;
        this.reviewPost = reviewPost;
        this.productId = productId;
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
}