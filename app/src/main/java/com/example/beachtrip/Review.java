package com.example.beachtrip;

import android.net.Uri;

public class Review {
    String uID;
    String beach;
    Boolean isAnonymous;
    double rate;
    String content;
    String reviewID;
    String imageURL;

    public Review(){
        this.uID = null;
        this.beach = null;
        isAnonymous = false;
        rate = 0.0;
        content = null;
        this.reviewID = null;
        this.imageURL = null;
    }

    public Review(String reviewID, String user_name, String beach_name, Boolean is_anonymous, double rating, String content, String imageURL) {
        this.reviewID = reviewID;
        this.uID = user_name;
        this.beach = beach_name;
        this.isAnonymous = is_anonymous;
        this.rate = rating;
        this.content = content;
        this.imageURL = imageURL;
    }

    public Review(String user_name, String beach_name, Boolean is_anonymous, double rating, String content) {
        this.reviewID = "";
        this.uID = user_name;
        this.beach = beach_name;
        this.isAnonymous = is_anonymous;
        this.rate = rating;
        this.content = content;
    }

    public String getReviewID(){return reviewID;}

    public String getUser_ID() {
        return uID;
    }

    public String getBeach_name() {
        return beach;
    }

    public Boolean getIs_anonymous() {
        return isAnonymous;
    }

    public double getRating() {
        return rate;
    }

    public String getContent() {
        return content;
    }

    public void setIs_anonymous(Boolean is_anonymous) {
        this.isAnonymous = is_anonymous;
    }

    public void setRating(double rating) {
        this.rate = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageURL(String img){
        this.imageURL = img;
    }

    public String getImageURL(){
        return this.imageURL;
    }
}
