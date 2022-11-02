package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    String name;
    LatLng location;
    String[][] hours;
    String[] Menu;

    public Restaurant(String name, String[][] hours, String[] menu, LatLng location) {
        this.name = name;
        this.hours = hours;
        this.Menu = menu;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation(){
        return this.location;
   }

    public String[][] getHours() {
        return hours;
    }

    public String[] getMenu() {
        return Menu;
    }
}
