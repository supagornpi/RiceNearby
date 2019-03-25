package com.warunya.ricenearby.constant;

import java.util.HashMap;

public enum MealTime {

    Breakfast("มื้อเช้า"),
    Lunch("มื้อกลางวัน"),
    Dinner("มื้อเย็น"),
    LateDinner("มื้อค่ำ");

    String mealTimeText;

    MealTime(String mealTimeText) {
        this.mealTimeText = mealTimeText;
    }

    public String getMealTimeText() {
        return mealTimeText;
    }

    public static MealTime parse(int type) {
        HashMap<Integer, MealTime> creatorMap = new HashMap<>();
        creatorMap.put(Breakfast.ordinal(), Breakfast);
        creatorMap.put(Lunch.ordinal(), Lunch);
        creatorMap.put(Dinner.ordinal(), Dinner);
        creatorMap.put(LateDinner.ordinal(), LateDinner);

        return (creatorMap.get(type) == null) ? Breakfast : creatorMap.get(type);
    }
}
