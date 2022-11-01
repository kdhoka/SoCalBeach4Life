package com.example.beachtrip;
import com.google.android.gms.maps.model.LatLng;

public class Beach {
    String id;
    String name;
    LatLng location;
    String[][] hours;
    ParkingLot[] parkingLots;
    Review[] reviews;

    public Beach(){
        id = "";
        this.name = "Null Beach";
        hours = new String[2][7];
        parkingLots = new ParkingLot[5];
        reviews = new Review[10];
    }

    public Beach(String id, String name, LatLng location, String[][] hours, ParkingLot[] parkingLots, Review[] reviews) {
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


    public String[][] getHours() {
        return hours;
    }

    public ParkingLot[] getParkingLots() {
        return parkingLots;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public String toString(){
        return "{ID: "+id+", NAME: "+name+", LOCATION: "+location.toString()+"}";
    }
}
