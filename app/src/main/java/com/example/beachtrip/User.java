package com.example.beachtrip;

public class User {
    String first_name;
    String last_name;
    String email;
    String password;
    Review[] reviews;

    public User(String first_name, String last_name, String email, String password, Review[] reviews) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.reviews = reviews;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void addReview(Review toAdd){
        //TODO
    }

    public void deleteReview(Review toDelete){
        //TODO
    }
}
