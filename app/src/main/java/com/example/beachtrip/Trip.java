package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

public class Trip {
    private String user;
    private String origin_name;
    private String dest_name;
    private long start_time;
    private String ETA;
    private String mode;

    public Trip(String user, String origin_name, String dest_name, long start_time, String ETA, String mode) {
        this.user = user;
        this.origin_name = origin_name;
        this.dest_name = dest_name;
        this.start_time = start_time;
        this.ETA = ETA;
        this.mode = mode;
    }

    public Trip() {
        this.user = "";
        this.origin_name = "";
        this.dest_name = "";
        this.start_time = 0;
        this.ETA = "";
        this.mode = "";
    }

    public String getUser() {
        return user;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
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

    public void setUser(String user) {
        this.user = user;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private String calculateEndTime(){
        int eta = Integer.parseInt(ETA.substring(0,ETA.indexOf(" ")));
        Date endtime = new Date(start_time + (eta * 60 * 1000));
        return endtime.toString();
    }

    public String toString(){
        return "Origin: "+origin_name+"\nDestination: "+dest_name+"\nStart Time: "+(new Date(start_time)).toString()+"\nEnd Time: "+calculateEndTime()+"\nETA: "+ETA+"\nMode: "+mode;
    }
}
