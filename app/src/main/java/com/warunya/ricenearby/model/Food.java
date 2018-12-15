package com.warunya.ricenearby.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class Food {

    public String uid;
    public List<String> urls;
    public String foodName;
    public String detail;
    public int amount;
    public int price;
    public List<FoodImage> foodImages;
    public List<Upload> uploads;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("foodName", foodName);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("detail", detail);
        result.put("amount", amount);
        result.put("price", price);
        result.put("urls", urls);
        result.put("uploads", uploads);
        return result;
    }

    public Food() {
    }

    public Food(String uid, String foodName, int amount, int price, String detail, List<FoodImage> foodImages) {
        this.uid = uid;
        this.foodName = foodName;
        this.detail = detail;
        this.amount = amount;
        this.price = price;
        this.foodImages = foodImages;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
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
