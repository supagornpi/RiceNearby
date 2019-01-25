package com.warunya.ricenearby.model;

import com.warunya.ricenearby.constant.MealTime;

import org.parceler.Parcel;

@Parcel
public class Meal {
    public String key;
    public String date;
    public int amount;
    public MealTime mealTime;

    public Meal() {
    }

    public Meal(String date, int amount, MealTime mealTime) {
        this.date = date;
        this.amount = amount;
        this.mealTime = mealTime;
    }
}
