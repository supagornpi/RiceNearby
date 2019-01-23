package com.warunya.ricenearby.model;

import org.parceler.Parcel;

@Parcel
public class Meal {
    public String key;
    public String date;
    public int amount;

    public Meal() {
    }

    public Meal(String date, int amount) {
        this.date = date;
        this.amount = amount;
    }
}
