package com.example.beachtrip;

public class Review {
    String user_name;
    String beach_name;
    Boolean is_anonymous;
    double rating;
    String content;
    //img[] pictures;

    public Review(){
        this.user_name = "user";
        this.beach_name = "Null Beach";
        is_anonymous = false;
        rating = 0.0;
        content = "this is a review";
    }

    public Review(String user_name, String beach_name, Boolean is_anonymous, double rating, String content) {
        this.user_name = user_name;
        this.beach_name = beach_name;
        this.is_anonymous = is_anonymous;
        this.rating = rating;
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getBeach_name() {
        return beach_name;
    }

    public Boolean getIs_anonymous() {
        return is_anonymous;
    }

    public double getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public void setIs_anonymous(Boolean is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addPicture(){
        //TODO
    }

    public void deletePicture(){
        //TODO
    }
}
