package com.example.beachtrip;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;



public class TripUnitTest {

    @Test
    public void BeachObjectGettersTest(){
        LatLng dummyLL = new LatLng(0,0);
        ArrayList<ParkingLot> dummyPList = new ArrayList<>();
        ArrayList<Review> dummyRList = new ArrayList<>();
        Beach b = new Beach("dummyId","dummy",dummyLL,"10-10",dummyPList,dummyRList);
        Assert.assertEquals(b.getID(),"dummyId");
        Assert.assertEquals(b.getName(),"dummy");
        Assert.assertEquals(b.getLocation(),dummyLL);
        Assert.assertEquals(b.getHours(), "10-10");
        Assert.assertEquals(b.getParkingLots(), dummyPList);
        Assert.assertEquals(b.getReviews(), dummyRList);
    }

    @Test
    public void ParkingLotObjectGettersTest(){
        ParkingLot p = new ParkingLot("dummyId","name",null);
        Assert.assertEquals(p.getId(),"dummyId");
        Assert.assertEquals(p.getName(),"name");
        Assert.assertEquals(p.getLocation(),null);
    }

    @Test
    public void TripObjectGettersTest(){
        Trip t = new Trip("user","origin","dest",0,"0 mins","walking");
        Assert.assertEquals(t.getUser(), "user");
        Assert.assertEquals(t.getOrigin_name(), "origin");
        Assert.assertEquals(t.getDest_name(), "dest");
        Assert.assertEquals(t.getStart_time(), 0);
        Assert.assertEquals(t.getETA(), "0 mins");
        Assert.assertEquals(t.getMode(), "walking");
    }

    @Test
    public void BeachObjectEmptyConstructionTest() {
        Beach b = new Beach();
        Assert.assertEquals(b.getID(),"");
        Assert.assertEquals(b.getName(),"Null Beach");
        Assert.assertEquals(b.getLocation(),null);
        Assert.assertEquals(b.getHours(), "0-0");
        LatLng dummyLL = new LatLng(0,0);
    }

    @Test
    public void ParkingLotObjectEmptyConstructionTest() {
        ParkingLot p = new ParkingLot();
        Assert.assertEquals(p.getId(),"");
        Assert.assertEquals(p.getName(),"null parking lot");
        Assert.assertEquals(p.getLocation(),null);
    }

    @Test
    public void TripObjectEmptyConstructionTest() {
        Trip t = new Trip();
        Assert.assertEquals(t.getUser(), "");
        Assert.assertEquals(t.getOrigin_name(), "");
        Assert.assertEquals(t.getDest_name(), "");
        Assert.assertEquals(t.getStart_time(), 0);
        Assert.assertEquals(t.getETA(), "");
        Assert.assertEquals(t.getMode(), "");
    }

    @Test
    public void TripObjectSettersTest() {
        Trip t = new Trip();
        t.setUser("user");
        t.setOrigin_name("origin");
        t.setDest_name("dest");
        t.setStart_time(10);
        t.setETA("0 mins");
        t.setMode("walking");
        Assert.assertEquals(t.getUser(), "user");
        Assert.assertEquals(t.getOrigin_name(), "origin");
        Assert.assertEquals(t.getDest_name(), "dest");
        Assert.assertEquals(t.getStart_time(), 10);
        Assert.assertEquals(t.getETA(), "0 mins");
        Assert.assertEquals(t.getMode(), "walking");
    }

    @Test
    public void BeachObjectToStringTest() {
        LatLng dummyLL = new LatLng(0,0);
        Beach b = new Beach("dummyId","dummy",dummyLL,"10-10",null,null);
        Assert.assertEquals(b.toString(),"{ID: dummyId, NAME: dummy, LOCATION: lat/lng: (0.0,0.0)}");
    }

    @Test
    public void TripObjectToStringTest() {
        Trip t = new Trip("user","origin","dest",0,"0 mins","walking");
        Assert.assertEquals(t.toString(),"Origin: origin\nDestination: dest\nStart Time: Wed Dec 31 16:00:00 PST 1969\nEnd Time: Wed Dec 31 16:00:00 PST 1969\nETA: 0 mins\nMode: walking");
    }
}
