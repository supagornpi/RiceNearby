package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class Cart {

    public String key;
    public User seller;
    public String uid;
    public Food food;
    public int amount;
    public boolean isConfirmOrder;
    public List<Meal> meals;

    public Cart() {
    }

    public Cart(String uid, Food food, int amount) {
        this.uid = uid;
        this.food = food;
        this.amount = amount;
    }

    public Cart(String uid, Food food, int amount, List<Meal> meals) {
        this.uid = uid;
        this.food = food;
        this.amount = amount;
        this.meals = meals;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("seller", seller);
        result.put("uid", uid);
        result.put("food", food);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("amount", amount);
        result.put("isConfirmOrder", isConfirmOrder);
        result.put("meals", meals);
        return result;
    }
}
