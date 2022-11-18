package com.example.beachtrip;

import android.net.Uri;

public class Model {
    private String imageUri;
    private String imagePath;
    public Model(){

    }

    public Model(String uri, String imagePath){
        this.imageUri = uri.toString();
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }
}
