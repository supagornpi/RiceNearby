package com.warunya.ricenearby.utils;

import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {

    public static void sortMeals(List<Meal> meals) {
        Collections.sort(meals, new Comparator<Meal>() {
            @Override
            public int compare(Meal meal1, Meal meal2) {
                // ## Ascending order
//                return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values

                Calendar calendar1 = ConvertDateUtils.convertToCalendar(meal1.date);
                Calendar calendar2 = ConvertDateUtils.convertToCalendar(meal2.date);

                return calendar1.compareTo(calendar2); // To compare integer values
            }
        });

    }

    public static void sortDistance(List<Food> foods) {
        Collections.sort(foods, new Comparator<Food>() {
            @Override
            public int compare(Food food1, Food food2) {
                // ## Ascending order
//                return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                if (food1.distance != null && food2.distance != null) {
                    return Integer.valueOf(food1.distance.intValue()).compareTo(food2.distance.intValue()); // To compare integer values
                } else {
                    return -1;
                }
            }
        });
    }
}
