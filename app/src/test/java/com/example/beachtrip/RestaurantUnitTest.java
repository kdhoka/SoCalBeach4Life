package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

public class RestaurantUnitTest {

    @Test
    public void RestaurantObjectTest(){
        Restaurant rt = new Restaurant("name",null, null, new LatLng(0.0, 0.0));
        Assert.assertEquals(rt.getName(), "name");
        Assert.assertEquals(rt.getHours(), null);
        Assert.assertEquals(rt.getMenu(), null);
        LatLng target = new LatLng(0.0, 0.0);
        Assert.assertEquals(rt.getLocation(), target);
    }
}
