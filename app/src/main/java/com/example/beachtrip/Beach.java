package com.example.beachtrip;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Beach {
    String id;
    String name;
    LatLng location;
    String hours;
    ArrayList<ParkingLot> parkingLots;
    ArrayList<Review> reviews;

    public Beach(){
        id = "";
        this.name = "Null Beach";
        hours = "0-0";
        parkingLots = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public Beach(String id, String name, LatLng location, String hours, ArrayList<ParkingLot> parkingLots, ArrayList<Review> reviews) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.hours = hours;
        this.parkingLots = parkingLots;
        this.reviews = reviews;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation(){
        return location;
    }


    public String getHours() {
        return hours;
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public String toString(){
        return "{ID: "+id+", NAME: "+name+", LOCATION: "+location.toString()+"}";
    }
}
