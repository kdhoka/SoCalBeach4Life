package com.example.beachtrip;

public class User {
    String name;
    String email;
    String password;
    Review[] reviews;

    public User(String name, String email, String password, Review[] reviews) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
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
