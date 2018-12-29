package com.warunya.ricenearby.model;

import android.net.Uri;

import org.parceler.Parcel;

@Parcel
public class FoodImage {

    public String author;
    public String name;
    public Uri uri;
    public String url;
    public boolean isRemoved;

    public FoodImage() {
    }

    public FoodImage(Uri uri) {
        this.uri = uri;
    }

    public FoodImage(String author, String name, String url) {
        this.author = author;
        this.name = name;
        this.url = url;
    }
}