package com.example.beachtrip;

public class Beach {
    String name;
    //LocationObject location;
    String[][] hours;
    ParkingLot[] parkingLots;
    Review[] reviews;

    public Beach(){
        this.name = "Null Beach";
        hours = new String[2][7];
        parkingLots = new ParkingLot[5];
        reviews = new Review[10];
    }

    public Beach(String name, String[][] hours, ParkingLot[] parkingLots, Review[] reviews) {
        this.name = name;
        this.hours = hours;
        this.parkingLots = parkingLots;
        this.reviews = reviews;
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

    public ParkingLot[] getParkingLots() {
        return parkingLots;
    }

    public Review[] getReviews() {
        return reviews;
    }


}
