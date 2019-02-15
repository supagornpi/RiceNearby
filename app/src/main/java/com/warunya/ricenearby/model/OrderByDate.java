package com.warunya.ricenearby.model;

import java.util.List;

public class OrderByDate {

    public String date;
    public List<MealByDate> mealByDateList;

    public OrderByDate() {
    }

    public OrderByDate(String date) {
        this.date = date;
    }
}
