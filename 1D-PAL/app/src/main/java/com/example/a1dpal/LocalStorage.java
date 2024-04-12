package com.example.a1dpal;

public class LocalStorage {
    String stage;
    int image;


    public LocalStorage(String stage, int image) {
        this.stage = stage;
        this.image = image;
    }

    public String getLocation() {
        return stage;
    }
    public int getImage() {
        return image;
    }


}
