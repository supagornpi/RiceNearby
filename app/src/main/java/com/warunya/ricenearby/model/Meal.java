package com.warunya.ricenearby.model;

import org.parceler.Parcel;

@Parcel
public class Meal {
    public String key;
    public String date;
    public String amount;

    public Meal() {
    }

    public Meal(String date) {
        this.date = date;
    }
}
