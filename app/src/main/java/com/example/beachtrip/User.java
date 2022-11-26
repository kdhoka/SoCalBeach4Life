package com.example.beachtrip;

public class User {
    String name;
    String email;
    String password;
    Review[] reviews;

    public User(){
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
}
