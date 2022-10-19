package com.example.beachtrip;

public class Restaurant {
    String name;
    //LocationObject location;
    String[][] hours;
    String[] Menu;

    public Restaurant(String name, String[][] hours, String[] menu) {
        this.name = name;
        this.hours = hours;
        Menu = menu;
    }

    public String getName() {
        return name;
    }

//    public Location getLocation(){
//        TODO
//    }

    public String[][] getHours() {
        return hours;
    }

    public String[] getMenu() {
        return Menu;
    }
}
