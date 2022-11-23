package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class RestaurantUnitTest {

    @Test
    public void RestaurantObjectTest(){
        Restaurant rt = new Restaurant("name",null, null, new LatLng(0.0, 0.0));
    }
}
