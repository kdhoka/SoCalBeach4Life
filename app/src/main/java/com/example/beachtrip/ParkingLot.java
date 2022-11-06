package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;

public class ParkingLot {
    String id;
    String name;
    LatLng location;

    public ParkingLot(){
        this.id = "";
        this.name = "null parking lot";
        this.location = null;
    }

    public ParkingLot(String id, String name, LatLng location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public LatLng getLocation() {
        return location;
    }
}
