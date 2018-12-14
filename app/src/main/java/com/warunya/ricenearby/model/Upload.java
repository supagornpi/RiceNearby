package com.warunya.ricenearby.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Upload {

    public String author;
    public String name;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String author, String name, String url) {
        this.author = author;
        this.name = name;
        this.url = url;
    }
}