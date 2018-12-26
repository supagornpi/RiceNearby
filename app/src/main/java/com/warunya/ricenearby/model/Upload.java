package com.warunya.ricenearby.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

@Parcel
@IgnoreExtraProperties
public class Upload {

    public String author;
    public String name;
    public String url;
    public Uri uri;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String author, String name, String url) {
        this.author = author;
        this.name = name;
        this.url = url;
    }

    public Upload(Uri uri) {
        this.uri = uri;
    }
}