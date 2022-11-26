package com.example.beachtrip;


import static org.junit.Assert.assertEquals;

import android.net.Uri;

import org.junit.Assert;
import org.junit.Test;

public class ModelUnitTest {
    @Test
    public void ModelNullConstructorTest(){
        Model model = new Model();
        assertEquals(null, model.getImageUri());
        assertEquals(null, model.getImagePath());
    }

    @Test
    public void ModelConstructorTest(){
        String uri = "testUri";
        String imagePath = "imagePath1";

        Model model = new Model(uri, imagePath);
        assertEquals(uri, model.getImageUri());
        assertEquals(imagePath, model.getImagePath());
    }

    @Test
    public void ModelGettersTest(){
        String uri = "testUriGetter";
        String imagePath = "testImagePathGetter";

        Model model = new Model(uri, imagePath);
        assertEquals(uri, model.getImageUri());
        assertEquals(imagePath, model.getImagePath());
    }

    @Test
    public void ModelSettersTest(){
        Model model = new Model();
        model.setImagePath("setPath");
        assertEquals("setPath", model.getImagePath());
    }
}
