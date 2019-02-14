package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class Food {

    public String key;
    public String uid;
    public String sellerName; //get in view
    public String foodName;
    public String detail;
    public String date;
    public String meal;
    public int amount;
    public int price;
    public List<FoodImage> foodImages;
    public List<Upload> uploads;
    public List<FoodType> foodTypes;
    public boolean isSelected;
    public Double latitude;
    public Double longitude;
    public Float distance;
    public boolean isBreakfast;
    public boolean isLaunch;
    public boolean isDinner;
    public List<Meal> breakfasts;
    public List<Meal> lunches;
    public List<Meal> dinners;
    public List<Meal> lateDinners;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uid", uid);
        result.put("foodName", foodName);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("detail", detail);
        result.put("amount", amount);
        result.put("price", price);
        result.put("uploads", uploads);
        result.put("date", date);
        result.put("meal", meal);
        result.put("foodTypes", foodTypes);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }

    public Food() {
    }

    public Food(String uid, String foodName, int amount, int price, String detail, List<FoodType> foodTypes, List<FoodImage> foodImages) {
        this.uid = uid;
        this.key = uid;
        this.foodName = foodName;
        this.detail = detail;
        this.amount = amount;
        this.price = price;
        this.foodImages = foodImages;
        this.foodTypes = foodTypes;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
