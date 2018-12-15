package com.warunya.ricenearby.model;

import android.net.Uri;

import org.parceler.Parcel;

@Parcel
public class FoodImage {

    public Uri uri;
    public String url;

    public FoodImage() {
    }

    public FoodImage(Uri uri) {
        this.uri = uri;
    }
}