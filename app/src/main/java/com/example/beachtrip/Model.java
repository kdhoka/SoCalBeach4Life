package com.example.beachtrip;

import android.net.Uri;

public class Model {
    private String imageUri;

    public Model(){

    }

    public Model(String uri){
        this.imageUri = uri.toString();
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }
}
