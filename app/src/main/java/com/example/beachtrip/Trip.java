package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;

public class Trip {
    private String user;
    private String origin_name;
    private String dest_name;
    private String ETA;
    private String mode;


    public Trip(String user, String origin_name, String dest_name, String ETA, String mode) {
        this.user = user;
        this.origin_name = origin_name;
        this.dest_name = dest_name;
        this.ETA = ETA;
        this.mode = mode;
    }

    public String getUser() {
        return user;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public String getDest_name() {
        return dest_name;
    }

    public String getETA() {
        return ETA;
    }

    public String getMode(){
        return mode;
    }
}
