package com.example.wwwconcepts.firebase.POJOs;

import com.google.firebase.database.IgnoreExtraProperties;




@IgnoreExtraProperties
public class Review {
    private String reviewId;
    private String reviewAuthorName;
    private String reviewPost;

    public Review(){
        //this constructor is required
    }

    public Review(String reviewId, String reviewAuthorName, String reviewPost) {
        this.reviewId = reviewId;
        this.reviewAuthorName = reviewAuthorName;
        this.reviewPost = reviewPost;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewAuthorName() {
        return reviewAuthorName;
    }

    public String getReviewPost() {
        return reviewPost;
    }
}