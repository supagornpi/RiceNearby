package com.warunya.ricenearby.model;

import com.warunya.ricenearby.constant.MealTime;

import java.util.List;

public class MealByDate {

    public MealTime mealTime;
    public Meal meal;
    public List<Food> foodList;


    public MealByDate(MealTime mealTime, Meal meal) {
        this.mealTime = mealTime;
        this.meal = meal;
    }
}
