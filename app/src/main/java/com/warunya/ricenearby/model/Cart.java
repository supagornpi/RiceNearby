package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

@Parcel
public class Cart {

    public String key;
    public User seller;
    public String uid;
    public Food food;
    public int amount;

    public Cart() {
    }

    public Cart(String uid, Food food, int amount) {
        this.uid = uid;
        this.food = food;
        this.amount = amount;
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
        return result;
    }
}
