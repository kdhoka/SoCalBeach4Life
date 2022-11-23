package com.example.beachtrip;


import org.junit.Assert;
import org.junit.Test;

public class ReviewUnitTest {

    @Test
    public void ReviewObjectGettersTest(){
        Review rv = new Review("dumpId", "user", "beach", true, 3.0, "content", "imageUrl", "imagePath");
        Assert.assertEquals(rv.getReviewID(), "dumpId");
        Assert.assertEquals(rv.getUser_ID(), "user");
        Assert.assertEquals(rv.getBeach_name(), "beach");
        Assert.assertEquals(rv.getIs_anonymous(), true);
        double delta = 0.0;
        Assert.assertEquals(rv.getRating(), 3.0, delta);
        Assert.assertEquals(rv.getContent(), "content");
        Assert.assertEquals(rv.getImageURL(), "imageUrl");
        Assert.assertEquals(rv.getImagePath(), "imagePath");
    }

    @Test
    public void ReviewObjectSettersTest() {
        Review rv = new Review("dumpId", "user", "beach", true, 3.0, "content", "imageUrl", "imagePath");
        rv.setContent("newContent");
        Assert.assertEquals(rv.getContent(), "newContent");
        rv.setRating(2.0);
        double newRate = 2.0;
        double delta = 0.0;
        Assert.assertEquals(rv.getRating(), newRate, delta);
        rv.setIs_anonymous(!rv.getIs_anonymous());
        Assert.assertEquals(rv.getIs_anonymous(), false);
        rv.setImagePath("newImagePath");
        Assert.assertEquals(rv.getImagePath(), "newImagePath");
        rv.setImageURL("newImageUrl");
        Assert.assertEquals(rv.getImageURL(), "newImageUrl");
    }

    @Test
    public void ReviewObjectNullTest() {
        Review rv = new Review();
        Assert.assertEquals(rv.getReviewID(), null);
        Assert.assertEquals(rv.getUser_ID(), null);
        Assert.assertEquals(rv.getBeach_name(), null);
        Assert.assertEquals(rv.getIs_anonymous(), false);
        double delta = 0.0;
        Assert.assertEquals(rv.getRating(), 0.0, delta);
        Assert.assertEquals(rv.getContent(), null);
        Assert.assertEquals(rv.getImageURL(), null);
        Assert.assertEquals(rv.getImagePath(), null);
    }

    @Test
    public void ReviewObjectConstructWithoutReviewIdTest() {
        Review rv = new Review("user", "beach", true, 3.0, "content", "imagePath");
        Assert.assertEquals(rv.getReviewID(), "");
        Assert.assertEquals(rv.getUser_ID(), "user");
        Assert.assertEquals(rv.getBeach_name(), "beach");
        Assert.assertEquals(rv.getIs_anonymous(), true);
        double delta = 0.0;
        Assert.assertEquals(rv.getRating(), 3.0, delta);
        Assert.assertEquals(rv.getContent(), "content");
        Assert.assertEquals(rv.getImagePath(), "imagePath");
    }

}
