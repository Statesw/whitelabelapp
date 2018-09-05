package com.example.wwwconcepts.firebase.POJOs;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Promo {
    public String description;
    public int pointsNeeded;
    public String promoId;

    public Promo(){}

    public Promo(String description, int pointsNeeded, String promoId){
        this.description = description;
        this.pointsNeeded = pointsNeeded;
        this.promoId = promoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointsNeeded() {
        return pointsNeeded;
    }

    public void setPointsNeeded(int pointsNeeded) {
        this.pointsNeeded = pointsNeeded;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }
}
